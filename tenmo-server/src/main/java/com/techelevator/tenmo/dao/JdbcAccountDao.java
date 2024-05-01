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
        BigDecimal newBalance = getBalanceByAccountId(accountId);

        if(amount.compareTo(newBalance) == -1){
            newBalance = amount.subtract(amount);

            String sql = "UPDATE\n" +
                    "SET balance = ?\n" +
                    "WHERE account_id = ?; ";
            try {
                newBalance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
            } catch (Exception ex){
                throw new RuntimeException();
            }
            return newBalance;
        } else {
            System.out.println("The transaction could not be completed, account can not be less than $0");
        }
        return newBalance;
    }


    






}
