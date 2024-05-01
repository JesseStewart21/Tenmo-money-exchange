package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface AccountDao {
    int findAccountByUserId(int id);

    BigDecimal getBalanceByAccountId(int accountId);

    BigDecimal withdraw(BigDecimal amount, int accountId);






}
