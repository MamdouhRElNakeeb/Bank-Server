package com.bankserver.database;

import com.bankserver.model.Bank;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class BankRepository extends BaseRepository implements IBankRepository {

    private final String table = "`banks`";
    private ArrayList<Bank> banks;

    @Override
    public ArrayList<Bank> all() {
        Connection conn = connectionManager.createConnection();
        String sql = "select * from " + table;
        // Intantiate only once
        if (banks == null) {
            banks = new ArrayList<>();
        } else {
            banks.clear();
        }

        try {
            statement = conn.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                banks.add(bankMapper(rs));
            }
            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex);
        }
        return banks;
    }

    @Override
    public Bank find(int bankID) {
        Connection conn = connectionManager.createConnection();
        String sql = "SELECT * FROM " + table + " WHERE `id` = ?";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bankID);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                return bankMapper(rs);
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
    public void update(Bank bank) {
        Connection conn = connectionManager.createConnection();
        String sql = "UPDATE " + table + " SET `name` = ? WHERE `id` = ?";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bank.getId());
            pstmt.execute();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQLError: " + e);
        }
    }

    @Override
    public void store(Bank bank) {
        Connection conn = connectionManager.createConnection();
        String sql = "INSERT INTO " + table + " (`name`, `port`) VALUES (?, ?)";
        try {
            // Prepare Statement
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bank.getName());
            pstmt.setInt(2, bank.getPort());
            pstmt.execute();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQL Erro: " + ex);
        }
    }

    @Override
    public void destroy(Bank bank) {
        Connection conn = connectionManager.createConnection();
        String sql = "DELETE FROM " + table + " WHERE `id` = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bank.getId());
            pstmt.execute();

            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQLError: " + e);
        }

    }

    /**
     * Constructs a new Bank object
     * 
     * @param rs the result set
     * @return a new Bank
     * @throws SQLException 
     */
    private Bank bankMapper(ResultSet rs) throws SQLException {
        Bank bank = new Bank();
        bank.setId(rs.getInt("id"));
        bank.setName(rs.getString("name"));
        bank.setPort(rs.getInt("port"));
        return bank;
    }
}
