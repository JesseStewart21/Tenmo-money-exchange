package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    List<Transfer> findAllBtyId();


    public Transfer findTransferById(int transfer_id) {
        Transfer transfer = null;

        String sql = "SELECT transfer_id, transfer_type_id, " +
                "transfer_status_id, account_from, account_to, amount\n" +
                "FROM transfer\n" +
                "WHERE transfer_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transfer_id);

            while (results.next()) {
                transfer = mapRowToTransfer(results);
            }
        } catch (NullPointerException | EmptyResultDataAccessException ex) {
            throw new RuntimeException("Something Went Wrong");
        }
        return transfer;
    }


/*
    @Transactional
    public void transfer(Transfer transfer) {
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
*/

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();

        int transferId = results.getInt("transfer_id");
        transfer.setTransferId(transferId);


        int transferTypeId = results.getInt("transfer_type_id");
        transfer.setTransferTypeId(transferTypeId);

        int transferStatusId = results.getInt("transfer_status_id");
        transfer.setTransferStatusId(transferStatusId);

        int accountFrom = results.getInt("account_from");
        transfer.setAccountFrom(accountFrom);

        int accountTo = results.getInt("account_to");
        transfer.setAccountTo(accountTo);

        BigDecimal amount = results.getBigDecimal("amount");
        transfer.setAmount(amount);


        return transfer;


    }
}