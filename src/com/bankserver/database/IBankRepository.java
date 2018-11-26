package com.bankserver.database;

import com.bankserver.model.Bank;

import java.util.ArrayList;

public interface IBankRepository {

    /**
     * Gets all the banks of the database
     * 
     * @return - A List of Customers
     */
    public ArrayList<Bank> all();


    /**
     * Gets a bank in the database by specifying the id
     *
     * @param bankID the account number of the bank
     * @return - A Bank
     */
    public Bank find(int bankID);

    /**
     * Updates a specific bank in the database
     *
     * @param bank the Bank to update
     */
    public void update(Bank bank);

    /**
     * Stores a new bank in the database
     *
     * @param bank the Bank to be stored
     */
    public void store(Bank bank);

    /**
     * Destroy a bank of the database
     *
     * @param bank the Bank to be destroyed
     */
    public void destroy(Bank bank);

}
