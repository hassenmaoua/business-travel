package edu.businesstravel.dao.repository;

import edu.businesstravel.dao.entities.User;
import edu.businesstravel.dao.tools.DatabaseConnection;
import javafx.event.ActionEvent;

import java.sql.*;

public class LoginRepository {
     static Connection connection;
    ResultSet resultSet = null;

    private Long idConnectedUser;

    public LoginRepository(){


            this.connection = DatabaseConnection.getInstance().getConnection();


    }


    public boolean loginUser(ActionEvent event, String email, String password) {
        String query = "SELECT idUser,email,pswd FROM users WHERE email = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            resultSet = statement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found in the database");
                return false;
            }

            while (resultSet.next()) {
                String retrievedPassword = resultSet.getString("pswd");
                if (retrievedPassword.equals(password)) {
                    System.out.println("Login successful");
                    idConnectedUser = resultSet.getLong("idUser");
                    return true;
                } else {
                    System.out.println("Password did not match");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }


    public Long getIdConnected() {
        return idConnectedUser;
    }


}
