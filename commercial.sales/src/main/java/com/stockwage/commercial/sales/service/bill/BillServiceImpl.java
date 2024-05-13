package com.stockwage.commercial.sales.service.bill;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.stockwage.commercial.sales.dto.BillDTO;
import com.stockwage.commercial.sales.entity.Bill;
import com.stockwage.commercial.sales.entity.BillProduct;
import com.stockwage.commercial.sales.entity.BillTypeEnum;
import com.stockwage.commercial.sales.entity.Client;
import com.stockwage.commercial.sales.entity.PaymentMethod;
import com.stockwage.commercial.sales.entity.Product;
import com.stockwage.commercial.sales.repository.BillRepository;
import com.stockwage.commercial.sales.repository.ClientRepository;
import com.stockwage.commercial.sales.repository.PaymentMethodRepository;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public Optional<BillDTO> getById(Long id) {
        Optional<Bill> bill = billRepository.findById(id);
        if (bill.isPresent()) {
            BillDTO billDTO = convertToDTO(bill.get());
            billDTO.setId(bill.get().getId());
            return Optional.of(billDTO);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public BillDTO save(BillDTO billDTO) {
        Bill bill = new Bill();
        Optional<Client> optClient = clientRepository.findById(billDTO.getClientId());
        Optional<PaymentMethod> optionalPaymentMethod = paymentMethodRepository.findById(billDTO.getPaymentMethodId());
        BillTypeEnum billType = null;

        if (!optClient.isPresent() || !optionalPaymentMethod.isPresent()) {
            return null;
        }
        
        try {
            billType = BillTypeEnum.valueOf(billDTO.getType());
        } catch (IllegalArgumentException e) {
            return null;
        }

        Client client = optClient.get();
        PaymentMethod paymentMethod = optionalPaymentMethod.get();

        bill = Bill.builder()
            .type(billType)
            .date(billDTO.getDate())
            .seller(billDTO.getSeller())
            .branchId(billDTO.getBranchId())
            .withholdingTax(billDTO.isWithholdingTax())
            .chargeTax(billDTO.isChargeTax())
            .client(client)
            .paymentMethod(paymentMethod) 
            .build();

        billRepository.save(bill);
        billDTO.setId(bill.getId());
        return billDTO;
    }

    @Override
    public boolean delete(Long id) {
        Optional<Bill> billOptional = billRepository.findById(id);
        if (billOptional.isPresent()) {
            billRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Bill update(Long id, BillDTO billDTO) {
        Optional<Client> optClient = clientRepository.findById(billDTO.getClientId());
        Optional<PaymentMethod> optionalPaymentMethod = paymentMethodRepository.findById(billDTO.getPaymentMethodId());
        BillTypeEnum billType = null;
        try {
            billType = BillTypeEnum.valueOf(billDTO.getType());
        } catch (IllegalArgumentException e) {
            return null;
        }
        if (!optClient.isPresent() || !optionalPaymentMethod.isPresent()) {
            return null;
        }
        Optional<Bill> existingBillOptional = billRepository.findById(id);

        if (existingBillOptional.isPresent()) {
            Bill existingBill = existingBillOptional.get();
            existingBill.setType(billType);
            existingBill.setDate(billDTO.getDate());
            existingBill.setSeller(billDTO.getSeller());
            existingBill.setBranchId(billDTO.getBranchId());
            existingBill.setWithholdingTax(billDTO.isWithholdingTax());
            existingBill.setChargeTax(billDTO.isChargeTax());
            existingBill.setClient(optClient.get());
            existingBill.setPaymentMethod(optionalPaymentMethod.get());
            return billRepository.save(existingBill);
        } else {
            return null;
        }
    }
    
    
    
    @Override
    public List<BillDTO> getAll() {
        List<Bill> bills = billRepository.findAll();
        List<BillDTO> billDTOs = new ArrayList<>();
        for (Bill bill : bills) {
            BillDTO billDTO = convertToDTO(bill);
            billDTO.setId(bill.getId());
            billDTOs.add(billDTO);
        }
        return billDTOs;
    }
    
    @Override
    public List<BillDTO> findByBranchId(Long branchId) {
        List<Bill> bills = billRepository.findByBranchId(branchId);
        List<BillDTO> billDTOs = new ArrayList<>();
        for (Bill bill : bills) {
            BillDTO billDTO = convertToDTO(bill);
            billDTO.setId(bill.getId());
            billDTOs.add(billDTO);
        }
        return billDTOs;
    }

    private BillDTO convertToDTO(Bill bill) {
        BillDTO billDTO = new BillDTO();
        String type = bill.getType().name();
        billDTO.setType(type);
        billDTO.setDate(bill.getDate());
        billDTO.setSeller(bill.getSeller());
        billDTO.setBranchId(bill.getBranchId());
        billDTO.setWithholdingTax(bill.isWithholdingTax());
        billDTO.setChargeTax(bill.isChargeTax());
        billDTO.setClientId(bill.getClient().getId());
        billDTO.setPaymentMethodId(bill.getPaymentMethod().getId());
        return billDTO;
    }


    public void generatePDF(Long billId) {
        Optional<Bill> optionalBill = billRepository.findById(billId);
        if (!optionalBill.isPresent()){
            return ;
        } 
        Bill bill = optionalBill.get();

        Optional<Client> optionalClient = clientRepository.findById(bill.getClient().getId());
        if (!optionalClient.isPresent()){
            return ;
        }
        Client client = optionalClient.get();
        try {
            String filename = "./bills/Bill-" + client.getCard_id() + "-" + bill.getId() + ".pdf";
            String htmlFile = "bill_template.html";
            String pdfFile = filename;

            String templateContent = loadTemplateFromResource(htmlFile);

            templateContent = templateContent.replace("%clientName%", "");
            templateContent = templateContent.replace("%clientContact%", "");
            templateContent = templateContent.replace("%clientEmail%", "");
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

    public void generatePDF(Bill bill, Client client) {
        try {
            String filename = "./bills/Bill-" + client.getCard_id() + "-" + bill.getId() + ".pdf";
            String htmlFile = "bill_template.html";
            String pdfFile = filename;

            String templateContent = loadTemplateFromResource(htmlFile);

            templateContent = templateContent.replace("%clientName%", bill.getClient().getName());
            templateContent = templateContent.replace("%clientContact%", client.getContact());
            templateContent = templateContent.replace("%clientEmail%", client.getEmail());
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
}
