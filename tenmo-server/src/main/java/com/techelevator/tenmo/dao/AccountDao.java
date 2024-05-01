package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {
    Account findAccountByUserId(int id);

    BigDecimal getBalanceByAccountId(int accountId);


    void transfer(int withdrawAccount, int depositAccount, BigDecimal amount);



}
