package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "/transfer")
public class TransferController {

    @Autowired
    private TransferDao transferDao;
    @Autowired
    private AccountDao accountDao;

@RequestMapping(path = "/find_transfer/{transferId}", method = RequestMethod.GET)
    public Transfer findTransferbyId(@PathVariable int transferId){
        return transferDao.findTransferById(transferId);
    }

@RequestMapping(path = "/{accountId}", method = RequestMethod.GET)
    public List<Transfer> findAllById(@PathVariable int accountId){
        return transferDao.findAllById(accountId);
    }

    @RequestMapping(path ="/new", method = RequestMethod.POST)
    public int createTransfer(@RequestBody Transfer transfer){
    BigDecimal accountFromBalance = accountDao.getBalanceByAccountId(transfer.getAccountFrom());
    BigDecimal accountToBalance = accountDao.getBalanceByAccountId(transfer.getAccountTo());
     return transferDao.createTransfer(transfer, accountFromBalance, accountToBalance);
    }

    @RequestMapping(path = "/approve/{transferId}", method = RequestMethod.GET)
public Boolean approveRequestTransfer(@PathVariable int transferId){
    Transfer transfer = transferDao.findTransferById(transferId);
    BigDecimal accountFromBalance = accountDao.getBalanceByAccountId(transfer.getAccountFrom());
    BigDecimal accountToBalance = accountDao.getBalanceByAccountId(transfer.getAccountTo());
    return transferDao.approveRequestTransfer(transfer, accountFromBalance, accountToBalance);
}
@RequestMapping(path = "/reject/{transferId}", method = RequestMethod.GET)
public Boolean rejectRequestTransfer(@PathVariable int transferId){
    Transfer transfer = transferDao.findTransferById(transferId);
    return transferDao.rejectRequestTransfer(transfer);
}


}
