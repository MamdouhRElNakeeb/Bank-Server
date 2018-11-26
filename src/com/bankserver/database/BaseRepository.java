package com.bankserver.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class BaseRepository {
    protected ConnectionManager connectionManager;
    protected Statement statement;
    protected PreparedStatement pstmt;
    protected ResultSet rs;
    
    public BaseRepository() {
        this.connectionManager = ConnectionManager.getInstance();
    }
}
