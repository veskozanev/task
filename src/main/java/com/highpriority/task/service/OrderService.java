package com.highpriority.task.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.highpriority.task.dto.Client;
import com.highpriority.task.dto.Order;
import com.highpriority.task.dto.OrderLine;
import com.highpriority.task.dto.Product;
import com.highpriority.task.repository.ClientRepository;
import com.highpriority.task.repository.ProductRepository;

/**
 * Service class to handle order processing logic, encapsulating pricing and discount application.
 */
@Service
public class OrderService {
    private final PricingService pricingService;
    private final DiscountService discountService;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    /**
     * Constructs an OrderService with the required service and repository implementations.
     * @param pricingService Service to calculate product pricing.
     * @param discountService Service to calculate discounts applied to the orders.
     * @param clientRepository Repository to access client data.
     * @param productRepository Repository to access product data.
     */
    public OrderService(PricingService pricingService, DiscountService discountService,
                        ClientRepository clientRepository, ProductRepository productRepository) {
        this.pricingService = pricingService;
        this.discountService = discountService;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
    }

    /**
     * Processes a textual input representing an order, fetching client and product data,
     * calculating prices and discounts, and creating an order object.
     *
     * @param input A string representing the order in the format "ClientID,ProductID=Quantity,ProductID=Quantity,..."
     * @return Order The processed order with all details and calculations done or null if the client/product is not found.
     */
    public Order processOrder(String input) {
        String[] parts = input.split(",");
        int clientId = Integer.parseInt(parts[0]);
        Client client = clientRepository.findClientById(clientId);

        if (client == null) {
            System.out.println("Client not found, cannot proceed with order.");
            return null;
        }

        List<OrderLine> orderLines = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            String[] productParts = parts[i].split("=");
            int productId = Integer.parseInt(productParts[0]);
            int quantity = Integer.parseInt(productParts[1]);

            Product product = productRepository.findProductById(productId);
            if (product == null) {
                System.out.println("Product ID " + productId + " not found, skipping product.");
                continue;
            }
            BigDecimal standardUnitPrice = pricingService.calculateStandardUnitPrice(product);
            BigDecimal promotionalUnitPrice = pricingService.calculatePromotionalUnitPrice(product, quantity);
            BigDecimal lineTotal = promotionalUnitPrice.multiply(new BigDecimal(quantity));

            OrderLine orderLine = new OrderLine(product, quantity, standardUnitPrice, promotionalUnitPrice, lineTotal);
            orderLines.add(orderLine);
        }

        Order order = new Order(client, orderLines);
        calculateTotalsAndDiscounts(order);
        return order;
    }

    /**
     * Helper method to calculate the total amounts and discounts for the order.
     *
     * @param order The order to compute totals and discounts for.
     */
    private void calculateTotalsAndDiscounts(Order order) {
        BigDecimal totalBeforeDiscounts = order.getOrderLines().stream()
            .map(OrderLine::getLineTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discountAmount = discountService.calculateClientDiscounts(order.getClient(), totalBeforeDiscounts);
        BigDecimal orderTotalAmount = totalBeforeDiscounts.subtract(discountAmount);

        order.setTotalBeforeDiscounts(totalBeforeDiscounts);
        order.setAdditionalDiscount(discountAmount);
        order.setOrderTotalAmount(orderTotalAmount);
    }

    /**
     * Formats an order into a readable string layout, detailing each line and totals.
     *
     * @param order The order to print.
     * @return A string representation of the order in a human-readable format.
     */
    public String printOrder(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append("Client: ").append(order.getClient().getName()).append("\n");
        for (OrderLine line : order.getOrderLines()) {
            Product product = line.getProduct();
            sb.append("Product: ").append(getProductName(product.getProductId())).append("\n");
            sb.append("\tQuantity: ").append(line.getQuantity()).append("\n");
            sb.append("\tStandard Unit Price: ").append(line.getStandardUnitPrice().setScale(2, RoundingMode.HALF_UP)).append("\n");
            sb.append("\tPromotional Unit Price: ").append(line.getPromotionalUnitPrice().setScale(5, RoundingMode.HALF_UP)).append("\n");
            sb.append("\tLine Total: ").append(line.getLineTotal().setScale(2, RoundingMode.HALF_UP)).append("\n");
        }

        sb.append("Total Before Client Discounts: ").append(order.getTotalBeforeDiscounts().setScale(2, RoundingMode.HALF_UP)).append("\n");
        if (order.getAdditionalDiscount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discountPercentage = order.getAdditionalDiscount()
                .divide(order.getTotalBeforeDiscounts(), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
            sb.append("Additional Volume Discount at ").append(discountPercentage).append("%: ").append(order.getAdditionalDiscount().setScale(2, RoundingMode.HALF_UP)).append("\n");
        }
        sb.append("Order Total Amount: ").append(order.getOrderTotalAmount().setScale(2, RoundingMode.HALF_UP)).append("\n");

        return sb.toString();
    }

    /**
     * Retrieves the product name by product ID, handling any missing products gracefully.
     *
     * @param productId The ID of the product to find the name for.
     * @return The name of the product or a placeholder if not found.
     */
    private String getProductName(int productId) {
        return productRepository.findProductNameById(productId);
    }
}
