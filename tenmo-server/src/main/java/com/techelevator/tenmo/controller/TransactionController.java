package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class TransactionController {
    @Autowired
    private AccountDao accountDao;


    @RequestMapping(path = "/account/{id}", method = RequestMethod.GET)
    public int findAccountByUserId(@PathVariable int userId){
        return accountDao.findAccountByUserId(userId);
    }

    @RequestMapping(path = "/account/balance/{id}", method = RequestMethod.GET)
    public BigDecimal getBalanceByAccountId(@PathVariable int accountId){
        return accountDao.getBalanceByAccountId(accountId);
    }
    // @RequestMapping(path ="/account/send_transfer", method = RequestMethod.POST)
    //public BigDecimal transfer(@RequestBody )






















}


