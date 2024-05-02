package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    public Transfer findTransferById(int transfer_id);
    public List<Transfer> findAllBtyId(int accountId);

}
