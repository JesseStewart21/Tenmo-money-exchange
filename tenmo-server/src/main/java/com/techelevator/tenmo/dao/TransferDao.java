package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
     Transfer findTransferById(int transferId);
     List<Transfer> findAllById(int accountId);

     int createTransfer(Transfer transfer, BigDecimal accountFromBalance, BigDecimal accountToBalance);

}
