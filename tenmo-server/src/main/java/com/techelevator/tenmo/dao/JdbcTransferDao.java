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
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> findAllById(int accountId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount\n" +
                "FROM transfer\n" +
                "WHERE account_from = ? OR account_to = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;

    }


    @Override
    public Transfer findTransferById(int transferId) {
        Transfer transfer = null;

        String sql = "SELECT transfer_id, transfer_type_id, " +
                "transfer_status_id, account_from, account_to, amount\n" +
                "FROM transfer\n" +
                "WHERE transfer_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);

            while (results.next()) {
                transfer = mapRowToTransfer(results);
            }
        } catch (NullPointerException | EmptyResultDataAccessException ex) {
            throw new RuntimeException("Something Went Wrong");
        }
        return transfer;
    }

    //when calling from controller, call acct dao and pass it in here...take acct id of from and send to receiver
    public int createTransfer(Transfer transfer, BigDecimal accountFromBalance, BigDecimal accountToBalance) {
        //Making sure you can only send to a different account
        Boolean validTransfer = false;
        BigDecimal amount = transfer.getAmount();

        if (transfer.getAccountFrom() != transfer.getAccountTo()) {

            //==1 is to send money from another acct
            if (transfer.getTransferTypeId() == 1) {
                int accountFrom = transfer.getAccountFrom();

                //checking to make sure transfer amount isn't more than balance amount & a positive number
                 validTransfer = (amount.compareTo(accountFromBalance) <= 0) &&
                        (amount.compareTo(new BigDecimal("0.0")) > 0);
            }else {//send any request amount
                validTransfer = true;
            }


                if (validTransfer) {
                    //if valid run the transfer as follows
                    String sql =
                            "UPDATE account\n" +
                            "SET balance = ?\n" +
                            "WHERE account_id = ?;\n" +
                            "UPDATE account\n" +
                            "SET balance = ?\n" +
                            "WHERE account_id = ?;\n" +
                                    "INSERT INTO transfer(transfer_type_id,\n" +
                                    "transfer_status_id, account_from, account_to, amount)\n" +
                                    "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;\n" +
                                    "\n";
                  //tracking balances
                    BigDecimal newAccountFromBalance = accountFromBalance;
                    BigDecimal newAccountToBalance = accountToBalance;
                    if (transfer.getTransferTypeId() == 1) {
                        //for send transfers, automatically set new balances
                       newAccountFromBalance = accountFromBalance.subtract(amount);
                       newAccountToBalance = accountToBalance.add(amount);
                    } else {
                        //for request transfers, build the transfer but set status to pending, and do not change the amounts.
                        transfer.setTransferStatusId(1);
                    }


                    //create a new transferId
                    try {
                       int results = jdbcTemplate.queryForObject(sql, Integer.class, newAccountFromBalance,
                               transfer.getAccountFrom(), newAccountToBalance, transfer.getAccountTo(), transfer.getTransferTypeId(),
                                transfer.getTransferStatusId(), transfer.getAccountFrom(),
                                transfer.getAccountTo(), transfer.getAmount());

                    } catch (NullPointerException | EmptyResultDataAccessException ex) {
                        throw new RuntimeException("Something Went Wrong");
                    }
                }
            }

            return transfer.getTransferId();
        }


        public void approveRequestTransfer (Transfer transfer, BigDecimal accountFromBalance, BigDecimal accountToBalance) {

            BigDecimal amount = transfer.getAmount();
            //checking to make sure transfer amount isn't more than balance amount & a positive number
            Boolean validTransfer = (amount.compareTo(accountFromBalance) <= 0) &&
                    (amount.compareTo(new BigDecimal("0.0")) > 0);

            if (validTransfer) {
                //if valid run the transfer as follows


            }
        }//end method
        

        private Transfer mapRowToTransfer (SqlRowSet results){
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
