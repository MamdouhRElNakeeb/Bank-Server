package com.bankserver.database;

import com.bankserver.model.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class CustomerRepository extends BaseRepository implements ICustomerRepository {

    private final String table = "`customers`";
    private ArrayList<Customer> customers;

    @Override
    public ArrayList<Customer> all() {
        Connection conn = connectionManager.createConnection();
        String sql = "select * from " + table;
        // Intantiate only once
        if (customers == null) {
            customers = new ArrayList<>();
        } else {
            customers.clear();
        }

        try {
            statement = conn.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                customers.add(customerMapper(rs));
            }
            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex);
        }
        return customers;
    }

    @Override
    public Customer find(int accountNumber, int bankID) {
        Connection conn = connectionManager.createConnection();
        String sql = "SELECT * FROM " + table + " WHERE `account_number` = ? AND `bank_id` = ?";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, accountNumber);
            pstmt.setInt(2, bankID);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                return customerMapper(rs);
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQLError: " + e);
        }
        return null;
    }

    @Override
    public Customer find(int accountNumber) {
        Connection conn = connectionManager.createConnection();
        String sql = "SELECT * FROM " + table + " WHERE `account_number` = ?";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, accountNumber);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                return customerMapper(rs);
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQLError: " + e);
        }
        return null;
    }

    @Override
    public void update(Customer customer) {
        Connection conn = connectionManager.createConnection();
        String sql = "UPDATE " + table + " SET `name` = ? WHERE `account_number` = ?";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, customer.getName());
//            pstmt.setString(2, customer.getAddress());
//            pstmt.setString(3, Character.toString(customer.getSex()));
//            pstmt.setString(4, customer.getDob());
//            pstmt.setString(5, customer.getAccountType());
            pstmt.setString(2, customer.getAccountNumber());
            pstmt.execute();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQLError: " + e);
        }
    }

    @Override
    public void store(Customer customer) {
        Connection conn = connectionManager.createConnection();
        String sql = "INSERT INTO " + table + " (`account_number`, `name`, `password`, `bank_id`) VALUES (?, ?, ?, ?)";
        try {
            // Prepare Statement
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, customer.getAccountNumber());
            pstmt.setString(2, customer.getName());
            pstmt.setString(3, customer.getPassword());
            pstmt.setInt(4, customer.getBankID());
            pstmt.execute();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQL Erro: " + ex);
        }
    }

    @Override
    public void destroy(Customer customer) {
        Connection conn = connectionManager.createConnection();
        String sql = "DELETE FROM " + table + " WHERE `account_number` = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, customer.getAccountNumber());
            pstmt.execute();

            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQLError: " + e);
        }

    }

    /**
     * Constructs a new Customer object
     * 
     * @param rs the result set
     * @return a new Customer
     * @throws SQLException 
     */
    private Customer customerMapper(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setAccountNumber(rs.getString("account_number"));
        customer.setName(rs.getString("name"));
        customer.setBankID(rs.getInt("bank_id"));
        customer.setPassword(rs.getString("password"));
        return customer;
    }
}
