package com.example.order_service.dao;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ClientDaoImpl implements ClientDao {

    private static final RestTemplate restTemplate = new RestTemplate();

    private final static String CLIENT_SERVICE_URL = "http://localhost:8180/api/client?email=%s";

    @Override
    public Long getUserId(final String email) {
        final ResponseEntity<Long> response = restTemplate.getForEntity(generateUrl(email), Long.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Client-service unavailable");
        }
        return response.getBody();
    }

    private String generateUrl(String email) {
        return String.format(CLIENT_SERVICE_URL, email);
    }
}
