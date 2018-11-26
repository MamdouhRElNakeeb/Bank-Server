package com.bankserver;

public class Main {

    static final int BANK1_PORT = BankService.PORT;
    static final int BANK2_PORT = BANK1_PORT + 1;

    public static void main(String args[]) {

        ServerRunnable r1 = new ServerRunnable(BANK1_PORT);
        ServerRunnable r2 = new ServerRunnable(BANK2_PORT);

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);

        t1.start();
        t2.start();

        View view = new View();
    }
}