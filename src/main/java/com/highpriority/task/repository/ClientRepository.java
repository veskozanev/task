package com.highpriority.task.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.highpriority.task.dto.Client;
 
/**
 * Repository for managing CRUD operations for clients in the database.
 */
@Repository
public class ClientRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Retrieves a client from the database based on client ID.
     * 
     * @param clientId The ID of the client to retrieve.
     * @return The found Client object, or null if no client is found with the given ID.
     */
    public Client findClientById(int clientId) {
        final String sql = "SELECT c.ClientID, c.ClientName, cd.BasicClientDiscount, " +
                           "cd.AdditionalDiscountAbove10000, cd.AdditionalDiscountAbove30000 " +
                           "FROM Clients c JOIN ClientDiscounts cd ON c.ClientID = cd.ClientID " +
                           "WHERE c.ClientID = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new ClientRowMapper(), clientId);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("Client ID: " + clientId + " not found");
            return null; // Indicate client not found
        } catch (Exception e) {
            e.printStackTrace();
            return null; // General exception handling
        }
    }

    /**
     * RowMapper implementation to convert SQL result sets into Client objects.
     */
    private static class ClientRowMapper implements RowMapper<Client> {
        @Override
        public Client mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            Client client = new Client();
            client.setId(rs.getLong("ClientID"));
            client.setName(rs.getString("ClientName"));
            client.setBasicDiscount(rs.getBigDecimal("BasicClientDiscount"));
            client.setVolumeDiscount10k(rs.getBigDecimal("AdditionalDiscountAbove10000"));
            client.setVolumeDiscount30k(rs.getBigDecimal("AdditionalDiscountAbove30000"));
            return client;
        }
    }
}
