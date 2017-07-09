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
        final int[] count = {0};
        final double[] avg = {0};
        final double[] sum = {0};
        HashMap<String,Object >response = new HashMap<>();
        this.getTransactionMap().forEach((time,transaction)->{
            long secs  = (System.currentTimeMillis()-transaction.getTimestamp())/1000l;
            if(secs<seconds){
                count[0]++;
                sum[0] = sum[0]+  transaction.getAmount();
                avg[0] = sum[0]/count[0];
                response.put("sum",sum[0]);
                response.put("avg",avg[0]);
                response.put("count",count[0]);
            }
        });
        return response;
    }
}
