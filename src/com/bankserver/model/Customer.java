package com.bankserver.model;

public class Customer {

    private String accountNumber;
    private int bankID;
    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBankID(int bankID) {
        this.bankID = bankID;
    }

    public int getBankID() {
        return bankID;
    }
}
