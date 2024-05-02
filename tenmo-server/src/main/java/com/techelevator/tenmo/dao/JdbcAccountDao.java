package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
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

    public BigDecimal getBalanceByAccountId(int accountId){
        BigDecimal balance;

        String sql = " SELECT balance \n" +
                " FROM account\n" +
                " WHERE account_id = ?;";
        try{
            balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
        }catch (NullPointerException | EmptyResultDataAccessException ex){
            throw new RuntimeException("Something went wrong.");
        }
    return balance;
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
