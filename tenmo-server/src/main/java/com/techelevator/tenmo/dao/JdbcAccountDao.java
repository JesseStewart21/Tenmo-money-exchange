package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.TransferRequest;
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
    public Account findAccountByUserId(int userId){
       Account account = null;
        //pull the account id from database with the userid
        String sql = "SELECT user_id, account_id, balance\n" +
                "FROM account\n" +
                "WHERE user_id = ?;";
        try{
           SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

           while(results.next()){
              account = mapRowToAccount(results);
           }
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
public void transfer(TransferRequest transfer) {
        //what is current withdrawing accounts balance
    BigDecimal startingWithdrawBalance = getBalanceByAccountId(transfer.getWithdrawAccountId());
    //what will be the new balances if the transfer is successful
    BigDecimal newWithdrawBalance = getBalanceByAccountId(transfer.getWithdrawAccountId()).subtract(transfer.getAmount());
    BigDecimal newDepositBalance = getBalanceByAccountId(transfer.getDepositAccountId()).add(transfer.getAmount());


//if the amount is equal or less than the withdrawing account's balance, and the amount is not 0 or negative, set to true
    Boolean results = ((transfer.getAmount()).compareTo(startingWithdrawBalance) <= 0) && ((transfer.getAmount()).compareTo(new BigDecimal("0.0")) > 0);

    if (results) {

        //push the code to the database
        String sql = "UPDATE\n" +
                "SET balance = ?\n" +
                "WHERE account_id = ?;" +
                "UPDATE\n" +
                "SET balance = ?\n" +
                "Where account_id = ?;";

        jdbcTemplate.update(sql, newWithdrawBalance, transfer.getWithdrawAccountId(), newDepositBalance, transfer.getDepositAccountId());
        //if the transfer should not be completed, throw exception so that it does not post to database
    }else{
    throw new RuntimeException("Transfer failed.");
}
}

private Account mapRowToAccount(SqlRowSet results){
        Account account = new Account();

        int accountId = results.getInt("account_id");
        account.setAccountId(accountId);

        int userId = results.getInt("user_id");
        account.setUserId(userId);

        BigDecimal balance = results.getBigDecimal("balance");
        account.setBalance(balance);

        return account;
}


}
