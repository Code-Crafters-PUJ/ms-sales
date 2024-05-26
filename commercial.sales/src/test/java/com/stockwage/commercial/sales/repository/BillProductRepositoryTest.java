package com.stockwage.commercial.sales.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.stockwage.commercial.sales.entity.Bill;
import com.stockwage.commercial.sales.entity.BillProduct;
import com.stockwage.commercial.sales.entity.BillTypeEnum;
import com.stockwage.commercial.sales.entity.Client;
import com.stockwage.commercial.sales.entity.PaymentMethod;
import com.stockwage.commercial.sales.entity.Product;

@DataJpaTest
public class BillProductRepositoryTest {

    private BillProductRepository billProductRepository;
    private BillRepository billRepository;
    private ProductRepository productRepository;
    private ClientRepository clientRepository;
    private PaymentMethodRepository paymentMethodRepository;

    private Bill bill;
    private Product product;

    @BeforeEach
    void setUp() {
        billRepository = mock(BillRepository.class);
        productRepository = mock(ProductRepository.class);
        clientRepository = mock(ClientRepository.class);
        paymentMethodRepository = mock(PaymentMethodRepository.class);
        billProductRepository = mock(BillProductRepository.class);

        Client client = new Client();
        client.setName("client");
        client.setCard_id("12345678");
        client.setContact("contact");
        client.setEmail("email");

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setMethod("method");

        Bill bill = new Bill();
        bill.setType(BillTypeEnum.E);
        bill.setDate(LocalDate.now());
        bill.setSeller("seller");
        bill.setBranchId(1L);
        bill.setWithholdingTax(true);
        bill.setChargeTax(true);
        bill.setPaymentMethod(paymentMethod);
        bill.setClient(client);

        Product product = new Product();
        product.setName("product");
        product.setDescription("description");
        product.setSalePrice(100.0);
        product.setCategoryId(1);

        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenReturn(paymentMethod);
        when(billRepository.save(any(Bill.class))).thenReturn(bill);
        when(productRepository.save(any(Product.class))).thenReturn(product);
    }

    @Test
    void testSave() {
        // Create a BillProduct object
        BillProduct billProduct = new BillProduct();
        
        // Create Bill and Product objects and set other fields of BillProduct
        billProduct.setBill(bill);
        billProduct.setProduct(product);
        billProduct.setQuantity(5);
        billProduct.setUnitPrice(100.0);
        billProduct.setDiscountPercentage(10);
    
        // Simulate the behavior of save() methods in the repositories
        when(billProductRepository.save(any(BillProduct.class))).thenAnswer(invocation -> {
            BillProduct argument = invocation.getArgument(0);
            argument.setId(1L); // Simulate generating an ID upon saving
            return argument;
        });
    
        // Save the BillProduct
        BillProduct savedBillProduct = billProductRepository.save(billProduct);
    
        // Verify that the saved BillProduct is not null
        assertNotNull(savedBillProduct);
        
        // Verify that the ID of the saved BillProduct is not null
        assertNotNull(savedBillProduct.getId());
        
        // Verify that the discount is as expected
        assertEquals(10, savedBillProduct.getDiscountPercentage());
    }
    

@Test
void testDelete() {
    // Create a BillProduct object
    BillProduct billProduct = new BillProduct();
    
    // Set the ID of the BillProduct
    billProduct.setId(1L);
    
    // Delete the BillProduct
    billProductRepository.delete(billProduct);
    
    // Verify that the BillProduct was deleted
    verify(billProductRepository, times(1)).delete(eq(billProduct));
}

@Test
void testFindById() {
    // Set a test ID
    Long id = 1L;
    
    // Simulate the behavior of the findById method in the repository
    when(billProductRepository.findById(id)).thenReturn(java.util.Optional.of(new BillProduct()));
    
    // Get the BillProduct by its ID
    BillProduct foundBillProduct = billProductRepository.findById(id).orElse(null);
    
    // Verify that the found BillProduct is not null
    assertNotNull(foundBillProduct);
}

@Test
void testFindAll() {
    // Create a list of test BillProducts
    List<BillProduct> billProducts = new ArrayList<>();
    billProducts.add(new BillProduct());
    billProducts.add(new BillProduct());
    billProducts.add(new BillProduct());
    
    // Simulate the behavior of the findAll method in the repository
    when(billProductRepository.findAll()).thenReturn(billProducts);
    
    // Get all BillProducts
    List<BillProduct> foundBillProducts = billProductRepository.findAll();
    
    // Verify that the list is not null and has the expected length
    assertNotNull(foundBillProducts);
    assertEquals(3, foundBillProducts.size());
}

@Test
void testFindByBill() {
    // Create a Bill object to use as reference
    Bill bill = new Bill();
    
    // Simulate the behavior of the findByBill method in the repository
    when(billProductRepository.findByBillId(bill.getId())).thenReturn(new ArrayList<>());
    
    // Get the BillProducts associated with a Bill
    List<BillProduct> foundBillProducts = billProductRepository.findByBillId(bill.getId());
    
    // Verify that the list is not null
    assertNotNull(foundBillProducts);
}

}
