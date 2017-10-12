package com.ef;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by dipak on 10/11/2017.
 */
public class InsertHelper {

    private static final String dbDriver = "com.mysql.jdbc.Driver";
    private static final String dbHost = "jdbc:mysql://localhost:3306/parserdb";
    private static final String dbUser = "root";
    private static final String dbPass = "";

    public static void insertDataIntoDB(String ipAddress, int threshold) {

        Connection connection = null;
        try {
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver is not found.");
            return;
        }
        try {
            connection = DriverManager.getConnection(dbHost, dbUser, dbPass);
            String query = "insert into logged_data (ip_address, comment)" + " values (?, ?)";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, ipAddress);
            preparedStmt.setString(2, "It's blocked for more than " + threshold + " request.");
            preparedStmt.execute();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Connection Failed!");
            return;
        }

    }
}
