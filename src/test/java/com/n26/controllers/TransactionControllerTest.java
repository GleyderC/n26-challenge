package com.n26.controllers;

import com.n26.N26Application;
import com.n26.models.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = N26Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.port=0"})
public class TransactionControllerTest {


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldTransactionAndReturnCreated() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setAmount(10.0);
        transaction.setTimestamp(System.currentTimeMillis());
        ResponseEntity<?> response = this.restTemplate.postForEntity("http://localhost:" + this.port + "/transaction", transaction, ResponseEntity.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }


}

