package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class AccountController {
    @Autowired
    private AccountDao accountDao;


    @RequestMapping(path = "/account/{userId}", method = RequestMethod.GET)
    public Account findAccountByUserId(@PathVariable int userId){
        return accountDao.findAccountByUserId(userId);
    }


























}


