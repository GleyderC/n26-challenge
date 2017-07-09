package com.n26.controllers;

import com.n26.models.Transaction;
import com.n26.services.StatisticService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;


@RestController
public class TransactionController {

    @Autowired
    private StatisticService  statisticService;

    @Autowired
    @Qualifier("TransactionValidator")
    private Validator validator;

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(this.validator);
    }

    @RequestMapping(method = RequestMethod.POST,name = "/transaction")
    public ResponseEntity<?> postTransaction(@Validated @RequestBody Transaction transaction, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(statisticService.addTransaction(transaction));
    }


}
