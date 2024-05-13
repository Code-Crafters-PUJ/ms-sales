package com.stockwage.commercial.sales.service.email;

import java.io.IOException;
import java.util.Optional;
import java.nio.file.Files;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.nio.file.Path;
import com.stockwage.commercial.sales.entity.Bill;
import com.stockwage.commercial.sales.entity.Client;
import com.stockwage.commercial.sales.repository.BillRepository;
import com.stockwage.commercial.sales.repository.ClientRepository;
import com.stockwage.commercial.sales.service.bill.BillService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService{
    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BillService billService;

@Override
public ResponseEntity<String> sendEmail(Long id) {

    Optional<Bill> optionalBill = billRepository.findById(id);
    if (!optionalBill.isPresent()){
        return new ResponseEntity<>("Bill not found", HttpStatus.NOT_FOUND);
    } 
    Bill bill = optionalBill.get();

    Optional<Client> optionalClient = clientRepository.findById(bill.getClient().getId());
    if (!optionalClient.isPresent()){
        return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
    }
    Client client = optionalClient.get();

    MimeMessage message = emailSender.createMimeMessage();
    try {
        String filename = "Bill No." + bill.getId() + ".pdf";
        String pdfFile = filename;
        String recipient = client.getEmail();
        String subject = "Bill No." + bill.getId() + " from ";
        String body = "";
        billService.generatePDF(bill, client);
        byte[] pdfBytes = convertToByteArray(pdfFile);
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(body);

        helper.addAttachment(filename, new ByteArrayResource(pdfBytes));
        emailSender.send(message);
    } catch (MessagingException | IOException e) {
        e.printStackTrace();
        return new ResponseEntity<>("Failed to send email", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>("Email sent successfully", HttpStatus.OK);
}




    public static byte[] convertToByteArray(String filePath) throws IOException {
        Path path = Path.of(filePath);
        return Files.readAllBytes(path);
    }
}
