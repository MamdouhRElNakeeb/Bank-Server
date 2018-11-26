package com.bankserver.controller;

import com.bankserver.database.BankRepository;
import com.bankserver.model.Bank;

import java.util.ArrayList;


public class BankController {

    private static BankController bankController;
    private BankRepository bankRepository;
    private ArrayList<Bank> banks;

    private BankController() {
        this.bankRepository = new BankRepository();
    }

    public ArrayList<Bank> getBanks() {
        return bankRepository.all();
    }

    public Bank getBank(int bankID){
        return bankRepository.find(bankID);
    }


    public static BankController getInstance() {
        if (bankController == null) {
            bankController = new BankController();
        }
        return bankController;
    }

}
