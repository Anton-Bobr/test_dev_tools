package com.example.client_service.controller;

import com.example.client_service.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(ClientController.CLIENT_URL)
@RequiredArgsConstructor
public class ClientController {

    public static final String CLIENT_URL = "/api/client";

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<Long> getAllOrdersByUserId(@RequestParam final String email) {
        return ResponseEntity.ok(clientService.getOrCreateClient(email));
    }
}
