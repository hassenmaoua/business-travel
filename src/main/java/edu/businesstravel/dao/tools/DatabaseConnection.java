package edu.businesstravel.dao.tools;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/businesstravel";
    private static final String LOGIN = "root";
    private static final String PWD = "";

    public static DatabaseConnection instance;

    private Connection connection;

    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(URL, LOGIN, PWD);

            System.out.println("Connection established.");
            System.out.println("Connected to " + connection.getCatalog() + '.');
        } catch (SQLException e) {
            System.out.println("Connection failed.");
            System.out.println(e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

//    public static DatabaseConnection getInstance() {
//        if (instance == null) {
//            return new DatabaseConnection();
//        }
//        return instance;
//    }


    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }



}
