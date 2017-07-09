package com.n26.controllers;


import com.n26.services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class StatisticController {
    @Value(value = "${secondsWithin}")
    private int secondsWithin;

    @Autowired
    private StatisticService statisticService;

    @RequestMapping(method = RequestMethod.GET,value = "/statistics")

    public HashMap<String,Object> getStaticstics (){
        return statisticService.getTransactionsWithinTime(secondsWithin);
    }
}
