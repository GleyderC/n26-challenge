package com.n26.validators;

import com.n26.models.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component("TransactionValidator")
public class TransactionValidator implements Validator {

    @Value("${secondsWithin}")
    private int seconds;

    @Override
    public boolean supports(Class<?> paramClass) {
        return Transaction.class.equals(paramClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        ValidationUtils.rejectIfEmpty(errors,"amount","amount.required","Amount required");
        ValidationUtils.rejectIfEmpty(errors,"timestamp","timestamp.required","timestamp required");
        Transaction transaction  = (Transaction) o;
        final long secondsWithin =(System.currentTimeMillis()-transaction.getTimestamp())/1000L;
        if(secondsWithin>seconds){
            errors.rejectValue("timestamp","timestamp.old","The timestamp is too old");
        }
        if(secondsWithin<0){
            errors.rejectValue("timestamp","timestamp.invalid","The timestamp is invalid");
        }
        if(transaction.getAmount()<=0){
            errors.rejectValue("amount","amount.lessThanZero","The amount value must be greater than zero");
        }

    }
}
