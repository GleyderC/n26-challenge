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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.OptionalDouble;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = N26Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.port=0"})

public class StatisticControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    private final double  amounts[] = {10.0,50.0,55.4,30.0,12.0,1.0,200.0,10.0,33.0,2.0};

    @Test
    public void shouldValidateTransactions() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            Transaction transaction = new Transaction();
            transaction.setAmount(amounts[i]);
            Thread.sleep(10);
            transaction.setTimestamp(System.currentTimeMillis());
            Runnable worker = new RunnableTest(transaction);
            executor.execute(worker);
        }
        executor.shutdown();

        while (!executor.isTerminated()) {
        }
        if (executor.isTerminated()) {
            ResponseEntity resp = this.restTemplate.getForEntity("http://localhost:" + this.port + "/statistics", HashMap.class);
            HashMap<String, Object> data = (HashMap<String, Object>) resp.getBody();
            double max = Arrays.stream(amounts).max().getAsDouble();//200.0
            double min = Arrays.stream(amounts).min().getAsDouble();//1.0
            double sum = Arrays.stream(amounts).sum();//403.4
            double avg = Arrays.stream(amounts).sum()/amounts.length;//10

            Assert.assertEquals(sum, data.get("sum"));
            Assert.assertEquals(min, data.get("min"));
            Assert.assertEquals(max, data.get("max"));
            Assert.assertEquals(avg, data.get("avg"));
            Assert.assertEquals(amounts.length, data.get("count"));
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
                restTemplate.postForEntity("http://localhost:" + port + "/transaction", transaction, Transaction.class);
            } catch (Exception ex) {

            }

        }
    }


}

