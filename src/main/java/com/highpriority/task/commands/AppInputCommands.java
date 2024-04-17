package com.highpriority.task.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import com.highpriority.task.service.OrderService;
import com.highpriority.task.dto.Order;

import java.io.IOException;
import java.util.List;

/**
 * Shell commands to interact with the OrderService for processing orders.
 */
@ShellComponent
public class AppInputCommands {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;

    /**
     * Constructs an instance of AppInputCommands.
     *
     * @param orderService The service for processing orders.
     * @param objectMapper The ObjectMapper for JSON processing.
     * @param resourceLoader The ResourceLoader for loading external resources.
     */
    public AppInputCommands(OrderService orderService, ObjectMapper objectMapper, ResourceLoader resourceLoader) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
    }

    /**
     * Processes an order based on a provided input string.
     *
     * @param orderInput The order details as a string.
     * @return A string representation of the processed order.
     */
    @ShellMethod(key = "process order", value = "Process an order given input string")
    public String processOrder(String orderInput) {
        Order order = orderService.processOrder(orderInput);
        return orderService.printOrder(order);
    }

    /**
     * Processes orders from a specified JSON file path.
     *
     * @param filePath The path to the JSON file containing order inputs.
     * @return A concatenated string of results for each order processed from the file.
     */
    @ShellMethod(key = "process orders from file", value = "Process orders from a JSON file")
    public String processOrdersFromFile(@ShellOption({"--filePath"}) String filePath) {
        try {
            Resource file = resourceLoader.getResource(filePath);
            // Read and process each order in the JSON file
            List<String> orders = objectMapper.readValue(file.getInputStream(), new TypeReference<List<String>>(){});
            StringBuilder results = new StringBuilder();
            for (String orderInput : orders) {
                Order order = orderService.processOrder(orderInput);
                results.append(orderService.printOrder(order)).append(System.lineSeparator());
            }
            return results.toString();
        } catch (IOException e) {
            return "Failed to read orders from file: " + e.getMessage();
        }
    }
}
