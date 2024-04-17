package com.highpriority.task;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.highpriority.task.dto.Client;
import com.highpriority.task.repository.ClientRepository;

class ClientRepositoryTests {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("unchecked")
    @Test
    void testFindClientById() {
        Client expectedClient = new Client();
        expectedClient.setId(1);
        expectedClient.setName("ABC Distribution");

        // Setup mock behavior
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyInt()))
                .thenReturn(expectedClient);

        // Perform the test
        Client actualClient = clientRepository.findClientById(1);

        // Validate the results
        assertNotNull(actualClient);
        assertEquals(expectedClient.getId(), actualClient.getId());
        assertEquals(expectedClient.getName(), actualClient.getName());

        // Verify correct methods were called
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(RowMapper.class), eq(1));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testClientNotFound() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyInt()))
                .thenThrow(new EmptyResultDataAccessException(1));
        assertNull(clientRepository.findClientById(999), "Should return null when client is not found.");
    }
    
    @SuppressWarnings("unchecked")
    @Test
    void testInvalidClientData() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyInt()))
                .thenReturn(new Client());  // Returning an incomplete client
        Client client = clientRepository.findClientById(1);
        assertNotNull(client, "Should not return null even if data is incomplete.");
    }

    @SuppressWarnings("unchecked")
    @Test
    void testBoundaryClientID() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(Integer.MAX_VALUE)))
                .thenReturn(new Client());
        assertNotNull(clientRepository.findClientById(Integer.MAX_VALUE), "Should handle boundary ID values.");
    }
}