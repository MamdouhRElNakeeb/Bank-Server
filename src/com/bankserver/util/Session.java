package com.bankserver.util;

public class Session {

    private static int bankID;
    private static String accountNumber;
    private static String pass;
    private static String name;
    private static String type;
    private static double balance;

    public static void setBankID(int bankID) {
        Session.bankID = bankID;
    }

    public static int getBankID() {
        return bankID;
    }

    public static String name() {
        return name;
    }

    public static String accountNumber() {
        return accountNumber;
    }

    public static String getPass() {
        return pass;
    }

    public static double balance(){
        return balance;
    }

    public static String type() {
        return type;
    }

    public static void setAccountNumber(String accountNumber) {
        Session.accountNumber = accountNumber;
    }

    public static void setPass(String pass) {
        Session.pass = pass;
    }

    public static void setName(String name) {
        Session.name = name;
    }

    public static void setBalance(double balance){
        Session.balance = balance;
    }

    public static void setType(String type) {
        Session.type = type;
    }

}
