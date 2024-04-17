package com.highpriority.task;

import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.highpriority.task.commands.AppInputCommands;
import com.highpriority.task.dto.Order;
import com.highpriority.task.service.OrderService;

import org.springframework.core.io.Resource;

class AppInputCommandsTests {

    @Mock
    private OrderService orderService;
    
    @Mock
    private Resource mockFile;

    @InjectMocks
    private AppInputCommands appInputCommands;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(orderService.processOrder(anyString())).thenReturn(new Order(null, null));
        when(orderService.printOrder(any(Order.class))).thenReturn("Order processed successfully");
    }

    @Test
    void testProcessOrderCommand() {
        String input = "1,2=100,3=150";
        String expectedOutput = "Order processed successfully";
        // Call the command method
        String actualOutput = appInputCommands.processOrder(input);
        // Verify the output
        assertEquals(expectedOutput, actualOutput, "The output should match the expected response.");
        // Verify the interaction with the order service
        verify(orderService, times(1)).processOrder(input);
    }

    @Test
    void testProcessEmptyOrder() {
        String input = "";
        String expectedOutput = "Order processed successfully"; // Adjust based on actual implementation
        String actualOutput = appInputCommands.processOrder(input);
        assertEquals(expectedOutput, actualOutput, "Processing an empty order should return a specific response.");
        verify(orderService, times(1)).processOrder(input);
    }

    @Test
    void testProcessInvalidOrderFormat() {
        String input = "1,xyz";
        when(orderService.processOrder(input)).thenThrow(new IllegalArgumentException("Invalid order format"));
        assertThrows(IllegalArgumentException.class, () -> appInputCommands.processOrder(input), "Should throw an exception for invalid format");
    }

    @Test
    void testProcessOrdersFromFileEmpty() throws IOException {
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream("[]".getBytes()));
        String result = appInputCommands.processOrdersFromFile("dummyPath");
        assertTrue(result.contains("Failed to read orders"), "Should handle empty file gracefully");
    }

    @Test
    void testProcessOrdersFromFileInvalidPath() throws IOException {
        when(mockFile.getInputStream()).thenThrow(new IOException("File not found"));
        String result = appInputCommands.processOrdersFromFile("invalidPath");
        assertTrue(result.startsWith("Failed to read orders"), "Should return an error message when file not found");
    }

    @Test
    void testProcessOrdersFromFileMalformedJson() throws IOException {
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream("{bad json}".getBytes()));
        when(mockFile.exists()).thenReturn(true);
        String result = appInputCommands.processOrdersFromFile("badJson.json");
        assertTrue(result.startsWith("Failed to read orders"), "Should handle JSON parsing errors");
    }
}
