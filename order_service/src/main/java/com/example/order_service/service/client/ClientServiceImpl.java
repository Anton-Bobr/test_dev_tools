package com.example.order_service.service.client;

import com.example.order_service.dao.ClientDao;
import com.example.order_service.dao.ClientDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService{

    @Autowired
    private ClientDao clientDao;

    @Override
    public Long getClientId(String email) {
        return clientDao.getUserId(email);
    }
}
