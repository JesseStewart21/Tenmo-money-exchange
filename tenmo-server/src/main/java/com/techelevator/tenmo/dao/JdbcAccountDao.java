package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{

    @Autowired
    private final JdbcTemplate jdbcTemplate;
    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account findAccountByUserId(int id){
       SqlRowSet account = null;
        //pull the account id from database with the userid
        String sql = "SELECT account_id, balance\n" +
                "FROM account\n" +
                "WHERE user_id = ?;";
        try{
            account = jdbcTemplate.queryForRowSet(sql, id);
        } catch (NullPointerException | EmptyResultDataAccessException ex){

            throw new RuntimeException("Something Went Wrong");
        }
            return account;
    }

    @Override
    public BigDecimal getBalanceByAccountId(int accountId){
        BigDecimal balance;
        //pull the balance from the database with the account id
        String sql = "SELECT balance\n" +
                "FROM account\n" +
                "WHERE account_id = ?;";

        try{
            balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
        } catch (Exception ex){
            throw new RuntimeException("Something Went Wrong");
        }
        return balance;
    }


@Transactional
public void transfer(int withdrawAccount, int depositAccount, BigDecimal amount) {
        //what is current withdrawing accounts balance
    BigDecimal startingWithdrawBalance = getBalanceByAccountId(withdrawAccount);
    //what will be the new balances if the transfer is successful
    BigDecimal newWithdrawBalance = getBalanceByAccountId(withdrawAccount).subtract(amount);
    BigDecimal newDepositBalance = getBalanceByAccountId(depositAccount).subtract(amount);


//if the amount is equal or less than the withdrawing account's balance, and the amount is not 0 or negative, set to true
    Boolean results = (amount.compareTo(startingWithdrawBalance) <= 0) && (amount.compareTo(new BigDecimal("0.0")) > 0);


    //push the code to the database
    String sql = "UPDATE\n" +
            "SET balance = ?\n" +
            "WHERE account_id = ?;" +
            "UPDATE\n" +
            "SET balance = ?\n" +
            "Where account_id = ?;";

    jdbcTemplate.update(sql, newWithdrawBalance, withdrawAccount, newDepositBalance, depositAccount);
   //if the transfer cannot be completed, throw exception so that it does not post to database
if (!results){
    throw new RuntimeException("Transfer failed.");
}
}


}
