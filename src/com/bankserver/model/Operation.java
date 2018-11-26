package com.bankserver.model;

import java.util.Date;

public class Operation {
    
    private String accountNumber;
    private double amount;
    private Date date;
    /**
     * 0: deposit
     * 1: withdraw
     * 2: transfer in
     * 3: transfer out
     * */
    private int type;

    public Operation(String accountNumber, double amount, Date date) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.date = date;
    }

    public Operation(String accountNumber, double amount, int type, Date date) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}
