package com.n26.services;

import com.n26.models.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class StatisticService {

    @Value(value = "${secondsWithin}")
    private int seconds;
    private Map<Long,Transaction> transactionMap;

    @PostConstruct
    private void initialize(){
        this.transactionMap = new ConcurrentHashMap<>();
    }

    public Map<Long, Transaction> getTransactionMap() {
        return transactionMap;
    }

    public Transaction addTransaction(Transaction tx) {
        return this.getTransactionMap().put(tx.getTimestamp(),tx);
    }

    public void setTransactionMap(Map<Long, Transaction> transactionMap) {
        this.transactionMap = transactionMap;
    }
    public HashMap<String,Object> getTransactionsWithinTime(long sc) {
        HashMap<String,Object >response = new HashMap<>();
        final int count =   this.getTransactionMap().size();
        final double sum = this.getTransactionMap().values().stream().mapToDouble(s -> s.getAmount()).sum();
        final double max = this.getTransactionMap().values().stream().mapToDouble(s -> s.getAmount()).max().getAsDouble();
        final double min = this.getTransactionMap().values().stream().mapToDouble(s -> s.getAmount()).min().getAsDouble();
        final double avg = this.getTransactionMap().values().stream().mapToDouble(s -> s.getAmount()).sum() /this.getTransactionMap().size();
        response.put("sum",sum);
        response.put("max",max);
        response.put("min",min);
        response.put("avg",avg);
        response.put("count",count);
        return response;
    }
}

