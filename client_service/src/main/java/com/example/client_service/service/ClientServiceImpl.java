package com.example.client_service.service;

import com.example.client_service.entity.ClientEntity;
import com.example.client_service.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Long getOrCreateClient(final String email) {
        final Optional <ClientEntity> optionalClient = clientRepository.findByEmail(email);
        if (optionalClient.isPresent()){
            return optionalClient.get().getId();
        }
        return createNewClient(email).getId();
    }

    private ClientEntity createNewClient(final String email) {
        final ClientEntity client = new ClientEntity();
        client.setEmail(email);
        clientRepository.saveAndFlush(client);
        return client;
    }
}
