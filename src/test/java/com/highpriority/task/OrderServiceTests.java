package com.highpriority.task;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.highpriority.task.dto.Client;
import com.highpriority.task.dto.Order;
import com.highpriority.task.dto.Product;
import com.highpriority.task.repository.ClientRepository;
import com.highpriority.task.repository.ProductRepository;
import com.highpriority.task.service.DiscountService;
import com.highpriority.task.service.OrderService;
import com.highpriority.task.service.PricingService;

import java.math.BigDecimal;

@SpringBootTest
class OrderServiceTests {

    @Autowired
    private OrderService orderService;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private PricingService pricingService;

    @MockBean
    private DiscountService discountService;

    @BeforeEach
    void setUp() {
        //orderService = new OrderService();
    }
    

    @Test
    void orderWithEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> orderService.processOrder(""),
                     "Processing empty input should throw an exception.");
    }

    @Test
    void clientNotFound() {
        when(clientRepository.findClientById(anyInt())).thenReturn(null);
        assertNull(orderService.processOrder("999,100=1"),
                   "Order processing should return null when client is not found.");
    }

    @Test
    void productNotFound() {
        when(clientRepository.findClientById(anyInt())).thenReturn(new Client());
        when(productRepository.findProductById(anyInt())).thenReturn(null);
        Order result = orderService.processOrder("1,999=10");
        assertTrue(result.getOrderLines().isEmpty(),
                   "Order should have no lines when product is not found.");
    }

    @Test
    void zeroQuantityOrder() {
        setupProductAndClientMocks();
        Order result = orderService.processOrder("1,100=0");
        assertTrue(result.getOrderLines().stream().allMatch(line -> line.getQuantity() == 0),
                   "Order line should process with zero quantity.");
    }

    @Test
    void massiveQuantityOrder() {
        setupProductAndClientMocks();
        Order result = orderService.processOrder("1,100=1000000");
        assertTrue(result.getOrderTotalAmount().compareTo(BigDecimal.ZERO) > 0,
                   "Total should be calculated correctly with massive quantity.");
    }

    private void setupProductAndClientMocks() {
        Client client = new Client();
        client.setBasicDiscount(new BigDecimal("5"));
        when(clientRepository.findClientById(1)).thenReturn(client);

        Product product = new Product();
        product.setUnitCostEUR(new BigDecimal("10.00"));
        product.setMarkup("10%");
        when(productRepository.findProductById(100)).thenReturn(product);

        when(pricingService.calculateStandardUnitPrice(product)).thenReturn(new BigDecimal("11.00"));
        when(pricingService.calculatePromotionalUnitPrice(product, 1000000)).thenReturn(new BigDecimal("11.00"));
        when(discountService.calculateClientDiscounts(any(Client.class), any(BigDecimal.class))).thenReturn(new BigDecimal("50000"));
    }
}
