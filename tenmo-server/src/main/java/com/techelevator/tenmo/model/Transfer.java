package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

   private BigDecimal amount;
   private int accountFrom;
   private int accountTo;
   private int transferId;
   private int transferTypeId;
   private int transferStatusId;


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setTransferId(int transferId) {
    }

    public void setTransferTypeId(int transferTypeId) {
    }

    public void setTransferStatusId(int transferStatusId) {
    }

    public void setAccountFrom(int accountFrom) {
    }

    public void setAccountTo(int accountTo) {
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public int getTransferId() {
        return transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }
    public int getTransferStatusId() {
        return transferStatusId;
    }
}
