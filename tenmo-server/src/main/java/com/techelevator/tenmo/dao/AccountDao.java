package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

public interface AccountDao {
    Account findAccountByUserId(int userId);
    BigDecimal getBalanceByAccountId(int accountId);







}
