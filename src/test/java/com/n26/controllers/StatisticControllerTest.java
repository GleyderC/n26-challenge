package com.n26.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.N26Application;
import com.n26.models.Transaction;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.context.embedded.LocalServerPort;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = N26Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.port=0"})
//@WebMvcTest(controllers = {TransactionController.class,StatisticController.class})
public class StatisticControllerTest {

    @LocalServerPort
    private int port;
//    @Autowired
//    private  MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void shouldValidateTransactions() throws  Exception{
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10 ; i++) {

            Transaction transaction = new Transaction();
            transaction.setAmount(10.0);
            Thread.sleep(10);
            transaction.setTimestamp(System.currentTimeMillis());
            Runnable worker = new RunnableTest(transaction);
            executor.execute(worker);

        }
        executor.shutdown();

        while (!executor.isTerminated()) {
        }
        if(executor.isTerminated()) {
            ResponseEntity resp = this.restTemplate.getForEntity("http://localhost:" + this.port + "/statistics", HashMap.class);
            HashMap<String, Object> data = (HashMap<String, Object>) resp.getBody();
            Assert.assertEquals(100.0,data.get("sum"));
            Assert.assertEquals(10.0,data.get("avg"));
            Assert.assertEquals(10,data.get("count"));
        }
    }
    public class RunnableTest implements Runnable {
        private final Transaction transaction;

        RunnableTest(Transaction transaction) {
            this.transaction = transaction;
        }

        @Override
        public void run() {
            try {
                restTemplate.postForEntity("http://localhost:"+port+"/transaction",transaction,Transaction.class);
            }catch (Exception ex){

            }

        }
    }


}

