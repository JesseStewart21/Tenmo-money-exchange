package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "/transfer")
public class TransferController {

    @Autowired
    private TransferDao transferDao;


@RequestMapping(path = "/find_transfer/{transfer_id}", method = RequestMethod.GET)
    public Transfer findTransferbyId(@PathVariable int transferId){
        return transferDao.findTransferById(transferId);
    }

@RequestMapping(path = "/{accountId}", method = RequestMethod.GET)
    public List<Transfer> findAllById(@PathVariable int accountId){
        return transferDao.findAllById(accountId);
    }

    @RequestMapping(path ="/new", method = RequestMethod.POST)
    public int createTransfer(@RequestBody Transfer transfer, BigDecimal accountFromBalance, BigDecimal accountToBalance){
     return transferDao.createTransfer(transfer, accountFromBalance, accountToBalance);
    }






}
