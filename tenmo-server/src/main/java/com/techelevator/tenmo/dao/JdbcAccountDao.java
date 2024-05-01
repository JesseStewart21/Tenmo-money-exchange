package com.techelevator.tenmo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{

    @Autowired
    private final JdbcTemplate jdbcTemplate;
    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int findAccountByUserId(int id){
        int accountId;
        //pull the account id from database with the userid
        String sql = "SELECT account_id\n" +
                "FROM account\n" +
                "WHERE user_id = ?;";
        try{
            accountId = jdbcTemplate.queryForObject(sql, int.class, id);
        } catch (NullPointerException | EmptyResultDataAccessException ex){

            throw new RuntimeException("Something Went Wrong");
        }
            return accountId;
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

    @Override
    public BigDecimal withdraw(BigDecimal amount, int accountId){
       //what is the current balance of the account
        BigDecimal newBalance = getBalanceByAccountId(accountId);

        //if the amount they want to withdraw is less than or equal to the current balance, keep going
        if(amount.compareTo(newBalance) == -1 || (amount.compareTo(newBalance) == 0)){
            newBalance = amount.subtract(amount);
//update the balance in the database to lesser amount
            String sql = "UPDATE\n" +
                    "SET balance = ?\n" +
                    "WHERE account_id = ?; ";
            try {
                newBalance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
            } catch (Exception ex){
                throw new RuntimeException();
            }//return the new lesser amount balance
            return newBalance;

            //if amount they want to withdraw was mroe than the current balance, then print error message
        } else {
            System.out.println("The transaction could not be completed, account can not be less than $0");
        }//return original balance
        return newBalance;
    }

    @Override
    public BigDecimal deposit(BigDecimal amount, int accountId){
        //add amount to current balance
        BigDecimal newBalance = getBalanceByAccountId(accountId). add(amount);
    //update the balance in the databse to the new amount
        String sql = "UPDATE\n" +
                "SET balance = ?\n" +
                "WHERE account_id = ?; ";
        try {
            newBalance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
        } catch (Exception ex){
            throw new RuntimeException();
        }//return new balance
        return newBalance;
    }

    public Boolean transfer(int withdrawAccount, int depositAccount, BigDecimal amount){
       //read in current balance of the withdrawing account
        BigDecimal startingWithdrawBalance = getBalanceByAccountId(withdrawAccount);
        //attempt to withraw from the account, if balance would be below 0, the withdraw method will return existing balance
        BigDecimal newWithdrawBalance = withdraw(amount, withdrawAccount);

        //if the starting balance is exactly the same as the balance returned from withdraw, the withdraw was
        //unsuccessful, and transaction should be stopped.
        if(startingWithdrawBalance.compareTo(newWithdrawBalance) == 0){
            return  false;
        }
        //withdraw went through, so continue to deposit
        deposit(amount, depositAccount);

        //transfer went through!
    return true;

    }





}
