package com.bankserver.database;

import com.bankserver.model.Operation;

import java.util.ArrayList;

public interface IOperationRepository {

    /**
     * Stores a new deposit in the database
     *
     * @param customerId the customer id
     * @param amount the amount to be deposited
     */
    public void deposit(int customerId, double amount, int type);

    /**
     * Stores a new withdraw in the database
     *
     * @param customerId the customer id
     * @param amount the amount to be withdrawn
     */
    public void withdraw(int customerId, double amount, int type);

    /**
     * Stores a new transfer in the database
     *
     * @param accNo1 the Account No. 1
     * @param accNo2 the Account No. 2
     * @param amount the amount to be withdrawn
     */
    public void transfer(int accNo1, int accNo2, double amount);

    /**
     * Gets all the deposits
     *
     * @return a list with all the deposits made
     */
    public ArrayList<Operation> deposits();

    /**
     * Gets all the withdraws
     *
     * @return a list with all the withdraws made
     */
    public ArrayList<Operation> withdraws();

    /**
     * Gets all the deposits of a customer
     *
     * @param customerId the customer id
     * @return a list with all the deposits made by a specific customer
     */
    public ArrayList<Operation> deposits(int customerId);

    /**
     * Gets all the withdraws of a customer
     *
     * @param customerId the customer id
     * @return a list with all the withdraws made by a specific customer
     */
    public ArrayList<Operation> withdraws(int customerId);

    /**
     * Gets all the operations of a customer
     *
     * @param customerId the customer id
     * @return a list with all the operations made by a specific customer
     */
    public ArrayList<Operation> operations(int customerId);

}
