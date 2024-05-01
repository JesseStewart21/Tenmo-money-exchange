package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.TransferRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@RestController
public class TransactionController {
    @Autowired
    private AccountDao accountDao;


    @RequestMapping(path = "/account/{userId}", method = RequestMethod.GET)
    public Account findAccountByUserId(@PathVariable int userId){
        return accountDao.findAccountByUserId(userId);
    }

    @RequestMapping(path = "/account/balance/{accountId}", method = RequestMethod.GET)
    public BigDecimal getBalanceByAccountId(@PathVariable int accountId){
        return accountDao.getBalanceByAccountId(accountId);
    }
     @RequestMapping(path ="/account/send_transfer", method = RequestMethod.PUT)
     public void transfer(@RequestBody TransferRequest transfer){
      try {
          accountDao.transfer(transfer);
      }catch (Exception ex){
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong here.");
      }
     }
//created a class for a transfer object cuz couldn't figure out how else to read the json body request, in debug
    //this transfer method seems to work right up unitl it sends to the database
    //maybe its the double loaded sql request?  will look into more tomorrow
    //but hooray for progress!!






















}


