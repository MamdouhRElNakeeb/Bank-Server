package com.bankserver.controller;

import com.bankserver.model.Operation;
import com.bankserver.database.OperationRepository;
import com.bankserver.util.Session;

import java.util.ArrayList;

public class OperationController {

    private static OperationController operationCtrl;
    private OperationRepository operationRepository;
    private ArrayList<Operation> withdraws;
    private ArrayList<Operation> operations;

    private OperationController() {
        this.operationRepository = new OperationRepository();
    }

    public boolean makeDeposit(int accountNumber, double amount, int type) {
        this.operationRepository.deposit(accountNumber, amount, type);
        return true;
    }

    public boolean makeWithdraw(int accountNumber, double amount, int type) {
        if ((float) amount <= getBalance(accountNumber)) {
            this.operationRepository.withdraw(accountNumber, amount, type);
            return true;
        }
        return false;
    }

    public double getBalance(int accountNumber) {
        double balance = 0;
        operations = this.operationRepository.operations(accountNumber);

        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getType() == 0 || operations.get(i).getType() == 2)
                balance += operations.get(i).getAmount();
            else
                balance -= operations.get(i).getAmount();
        }

        System.out.println("balance::: " + balance);

        Session.setBalance(balance);
        return balance;
    }

    public double getPreviousDeposit(int accountNumber) {
        operations = this.operationRepository.deposits(accountNumber);
        if (operations.size() > 0) {
            return operations.get(operations.size() - 1).getAmount();
        }
        return 0;
    }

    public double getPreviousWithdraw(int accountNumber) {
        withdraws = this.operationRepository.withdraws(accountNumber);
        if (withdraws.size() > 0) {
            return withdraws.get(withdraws.size() - 1).getAmount();
        }
        return 0;
    }

    public ArrayList<Operation> getAllWithdraws() {
        withdraws = this.operationRepository.withdraws();
        return withdraws;
    }

    public ArrayList<Operation> getAllOperations(int accountNumber) {
        operations = this.operationRepository.operations(accountNumber);
        return operations;
    }

    public static OperationController getInstance() {
        if (operationCtrl == null) {
            operationCtrl = new OperationController();
        }
        return operationCtrl;
    }
}
