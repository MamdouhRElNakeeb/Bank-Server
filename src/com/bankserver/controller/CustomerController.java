package com.bankserver.controller;

import com.bankserver.model.Customer;
import com.bankserver.database.CustomerRepository;
import com.bankserver.util.Encryption;

import java.util.ArrayList;


public class CustomerController {

    private static CustomerController customerCtrl;
    private CustomerRepository customerRepository;
    private ArrayList<Customer> customers;

    private CustomerController() {
        this.customerRepository = new CustomerRepository();
    }

    public void store(Customer customer) {
        customerRepository.store(customer);
    }

    public ArrayList<Customer> getCustomers() {
        return customerRepository.all();
    }

    public Customer searchCustomer(int accountNumber, int bankID) {
        return customerRepository.find(accountNumber, bankID);
    }

    public Customer searchCustomer(int accountNumber) {
        return customerRepository.find(accountNumber);
    }

    public void update(Customer customer) {
        customerRepository.update(customer);
    }

    public void destroy(Customer customer) {
        customerRepository.destroy(customer);
    }

    public static CustomerController getInstance() {
        if (customerCtrl == null) {
            customerCtrl = new CustomerController();
        }
        return customerCtrl;
    }

    /**
     * Makes the authentication by a given account number and a password
     *
     * @param accountNumber The account number of an user
     * @param password The password of an user
     * @return - Whether an user exists in the database
     */
    public boolean login(String accountNumber, String password) {
        customers = customerRepository.all();
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);

            if (customer.getAccountNumber().equals(accountNumber)) {

                if (Encryption.check(password, customer.getPassword())) {
                    return true;
                }
            }
        }
        return false;
    }
}
