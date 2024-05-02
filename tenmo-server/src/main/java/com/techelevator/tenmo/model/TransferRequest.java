package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferRequest {

   private BigDecimal amount;
   private int withdrawAccountId;
   private int depositAccountId;

   private String status = "PENDING";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getWithdrawAccountId() {
        return withdrawAccountId;
    }

    public void setWithdrawAccountId(int withdrawAccountId) {
        this.withdrawAccountId = withdrawAccountId;
    }

    public int getDepositAccountId() {
        return depositAccountId;
    }

    public void setDepositAccountId(int depositAccountId) {
        this.depositAccountId = depositAccountId;
    }
}
