package com.bankserver.database;

import com.bankserver.model.Operation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OperationRepository extends BaseRepository implements IOperationRepository {

    private final String operationsTable = "`operations`";
    private ArrayList<Operation> deposits;
    private ArrayList<Operation> withdraws;
    private ArrayList<Operation> operations;

    @Override
    public void deposit(int accountNumber, double amount, int type) {
        Connection conn = connectionManager.createConnection();
        String sql = "INSERT INTO " + operationsTable + " (`account_number`, `amount`, `type`) VALUES (?, ?, ?)";
        try {
            // Prepare Statement
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, accountNumber);
            pstmt.setDouble(2, amount);
            pstmt.setInt(3, type); // type: deposit or transfer in

            pstmt.execute();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex);
        }
    }

    @Override
    public void withdraw(int accountNumber, double amount, int type) {
        Connection conn = connectionManager.createConnection();
        String sql = "INSERT INTO " + operationsTable + " (`account_number`, `amount`, `type`) VALUES (?, ?, ?)";
        try {
            // Prepare Statement
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, accountNumber);
            pstmt.setDouble(2, amount);
            pstmt.setInt(3, type); // type: withdraw or transfer out

            pstmt.execute();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex);
        }
    }

    @Override
    public void transfer(int accNo1, int accNo2, double amount) {

        withdraw(accNo1, amount, 3);
        deposit(accNo2, amount, 2);

    }

    @Override
    public ArrayList<Operation> deposits() {
        Connection conn = connectionManager.createConnection();
        String sql = "SELECT * FROM " + operationsTable + " WHERE type = 0";
        // Intantiate only once
        if (deposits == null) {
            deposits = new ArrayList<>();
        } else {
            deposits.clear();
        }
        try {
            statement = conn.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                deposits.add(operationMapper(rs));
            }
            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex);
        }
        return deposits;
    }

    @Override
    public ArrayList<Operation> withdraws() {
        Connection conn = connectionManager.createConnection();
        String sql = "SELECT * FROM " + operationsTable + " WHERE type = 1";
        // Intantiate only once
        if (withdraws == null) {
            withdraws = new ArrayList<>();
        } else {
            withdraws.clear();
        }
        try {
            statement = conn.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                withdraws.add(operationMapper(rs));
            }
            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex);
        }
        return withdraws;
    }

    @Override
    public ArrayList<Operation> deposits(int accountNumber) {
        Connection conn = connectionManager.createConnection();
        String sql = "SELECT * FROM " + operationsTable + " WHERE `account_number` = ? AND type = 0";
        // Intantiate only once
        if (deposits == null) {
            deposits = new ArrayList<>();
        } else {
            deposits.clear();
        }
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, accountNumber);
            rs = pstmt.executeQuery();
           
            while (rs.next()) {
                deposits.add(operationMapper(rs));
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex);
        }
        return deposits;
    }

    @Override
    public ArrayList<Operation> withdraws(int accountNumber) {
        Connection conn = connectionManager.createConnection();
        String sql = "SELECT * FROM " + operationsTable + " WHERE `account_number` = ? AND type = 1";
        // Intantiate only once
        if (withdraws == null) {
            withdraws = new ArrayList<>();
        } else {
            withdraws.clear();
        }
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, accountNumber);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                withdraws.add(operationMapper(rs));
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex);
        }
        return withdraws;
    }

    @Override
    public ArrayList<Operation> operations(int accountNumber) {

        Connection conn = connectionManager.createConnection();
        String sql = "SELECT * FROM " + operationsTable + " WHERE `account_number` = ?";
        // Intantiate only once
        if (operations == null) {
            operations = new ArrayList<>();
        } else {
            operations.clear();
        }
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, accountNumber);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                operations.add(operationMapper(rs));
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex);
        }
        return operations;
    }

    /**
     * Constructs a new Operation object
     *
     * @param rs the result set
     * @return a new Operation
     * @throws SQLException
     */
    private Operation operationMapper(ResultSet rs) throws SQLException {
        return new Operation(rs.getString("account_number"), rs.getDouble("amount"), rs.getInt("type"), rs.getDate("created_at"));
    }

    /**
     * Constructs a new Withdraw object
     *
     * @param rs the result set
     * @return a new Withdraw
     * @throws SQLException
     */
    private Operation withdrawMapper(ResultSet rs) throws SQLException {
        return new Operation(rs.getString("account_number"), rs.getDouble("amount"), rs.getDate("created_at"));
    }
}
