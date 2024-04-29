package com.stockwage.commercial.sales.service.email;

import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.nio.file.Path;
import java.text.NumberFormat;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.stockwage.commercial.sales.entity.Bill;
import com.stockwage.commercial.sales.entity.BillProduct;
import com.stockwage.commercial.sales.entity.Product;
import com.stockwage.commercial.sales.repository.BillRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService{
    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private BillRepository billRepository;


@Override
public ResponseEntity<String> sendEmail(Long id) {

    Optional<Bill> optionalBill = billRepository.findById(id);
    if (!optionalBill.isPresent()){
        return new ResponseEntity<>("Bill not found", HttpStatus.NOT_FOUND);
    } 

    Bill bill = optionalBill.get();

    MimeMessage message = emailSender.createMimeMessage();
    try {
        String filename = "Bill No." + bill.getId() + ".pdf";
        String pdfFile = filename;
        String recipient = bill.getEmail();
        String subject = "Bill No." + bill.getId() + " from ";
        String body = "";
        generatePDF(bill);
        byte[] pdfBytes = convertToByteArray(pdfFile);
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(body);

        helper.addAttachment(filename, new ByteArrayResource(pdfBytes));
        emailSender.send(message);

        File file = new File(pdfFile);
        file.delete();
    } catch (MessagingException | IOException e) {
        e.printStackTrace();
        return new ResponseEntity<>("Failed to send email", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>("Email sent successfully", HttpStatus.OK);
}


private void generatePDF(Bill bill) {
    try {
        String filename = "Bill No." + bill.getId() + ".pdf";
        String htmlFile = "bill_template.html";
        String pdfFile = filename;

        String templateContent = loadTemplateFromResource(htmlFile);

        templateContent = templateContent.replace("%clientName%", bill.getClient().getName());
        templateContent = templateContent.replace("%clientContact%", bill.getContact());
        templateContent = templateContent.replace("%clientEmail%", bill.getEmail());
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/ MM/ yyyy");
        String strDate = bill.getDate().format(formatoFecha);
        templateContent = templateContent.replaceAll("%date%", strDate);
        templateContent = templateContent.replace("%seller%", bill.getSeller());
        templateContent = templateContent.replace("%branchId%", bill.getBranchId().toString());
        templateContent = templateContent.replace("%paymentMethod%", bill.getPaymentMethod().getMethod());

        List<BillProduct> items = bill.getBillProducts();
        StringBuilder tableRows = new StringBuilder();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

        Double grossTotal = 0.0;
        Double netTotal = 0.0;
        Double discountsTotal = 0.0;
        Double chargeTaxTotal = 0.0;
        Double withholdingTaxTotal = 0.0;
        Double taxes = 0.0;


        for (BillProduct item : items) {
            Product product = item.getProduct();

            String name = product.getName();
            String description = product.getDescription();
            Integer quantity = item.getQuantity();
            Double unitPrice = item.getUnitPrice();
            Integer discountPercentage = item.getDiscountPercentage();

            Double chargeTax = 0.0;
            Double withholdingTax = 0.0;
            Double subtotal = 0.0;
            Double discount = 0.0;

            Double subtotalWithoutTaxesDiscs = 0.0;

            subtotalWithoutTaxesDiscs = unitPrice * quantity;

            discount = subtotalWithoutTaxesDiscs * discountPercentage / 100;

            subtotal = subtotalWithoutTaxesDiscs - discount;

            if(bill.isWithholdingTax()) // 2.5% withholding tax
                withholdingTax = subtotal * 0.025;
            
            if(bill.isChargeTax()) // 19% tax free of charge
                chargeTax = subtotal * 0.19;
            
            Double total = subtotal + chargeTax + withholdingTax;


            grossTotal += subtotalWithoutTaxesDiscs;
            discountsTotal += discount;
            chargeTaxTotal += chargeTax;
            withholdingTaxTotal += withholdingTax;
            taxes += (chargeTax + withholdingTax);
            
            tableRows.append("<tr>");
            tableRows.append("<td>").append(name).append("</td>");
            tableRows.append("<td>").append(description).append("</td>");
            tableRows.append("<td>").append(quantity).append("</td>");
            tableRows.append("<td>").append(currencyFormat.format(unitPrice)).append("</td>");
            tableRows.append("<td>").append(currencyFormat.format(subtotalWithoutTaxesDiscs)).append("</td>");
            tableRows.append("<td>").append(discountPercentage.toString() + " %").append("</td>");
            tableRows.append("<td>").append(currencyFormat.format(discount)).append("</td>");
            tableRows.append("<td>").append(currencyFormat.format(subtotal)).append("</td>");
            tableRows.append("<td>").append(currencyFormat.format(chargeTax)).append("</td>");
            tableRows.append("<td>").append(currencyFormat.format(withholdingTax)).append("</td>");
            tableRows.append("<td>").append(currencyFormat.format(chargeTax + withholdingTax)).append("</td>");
            tableRows.append("<td>").append(currencyFormat.format(total)).append("</td>");
            tableRows.append("</tr>");
        }
        taxes = chargeTaxTotal + withholdingTaxTotal;

        netTotal = (grossTotal + taxes) - discountsTotal;

        templateContent = templateContent.replace("%grossTotal%", currencyFormat.format(grossTotal).toString());
        templateContent = templateContent.replace("%totalDiscount%", currencyFormat.format(discountsTotal).toString());
        templateContent = templateContent.replace("%taxes%", currencyFormat.format(taxes).toString());
        templateContent = templateContent.replace("%chargeTaxTotal%", currencyFormat.format(chargeTaxTotal).toString());
        templateContent = templateContent.replace("%withholdingTaxTotal%", currencyFormat.format(withholdingTaxTotal).toString());
        templateContent = templateContent.replace("%netTotal%", currencyFormat.format(netTotal).toString());
        templateContent = templateContent.replace("%tableRows%", tableRows.toString());

        try (OutputStream outputStream = new FileOutputStream(pdfFile)) {
            PdfWriter writer = new PdfWriter(outputStream);
            PageSize pageSize = PageSize.A4;
            pageSize = pageSize.rotate();  
            PdfDocument pdf = new PdfDocument(writer);
            pdf.setDefaultPageSize(pageSize);
            ConverterProperties properties = new ConverterProperties();
            properties.setBaseUri(".");
            HtmlConverter.convertToPdf(templateContent, pdf, properties);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}


private String loadTemplateFromResource(String resourceName) {
    try {
        ClassPathResource resource = new ClassPathResource(resourceName);
        byte[] content = FileCopyUtils.copyToByteArray(resource.getInputStream());
        return new String(content, StandardCharsets.UTF_8);
    } catch (IOException e) {
        e.printStackTrace();
        return ""; 
    }
}


    public static byte[] convertToByteArray(String filePath) throws IOException {
        Path path = Path.of(filePath);
        return Files.readAllBytes(path);
    }
}
