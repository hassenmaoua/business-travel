package edu.businesstravel.dao.repository;

import edu.businesstravel.dao.entities.User;
import javafx.event.ActionEvent;

import java.sql.*;

public class LoginRepository {
     static Connection connection;
    ResultSet resultSet = null;

    public LoginRepository(){
        this.connection = connection;
    }

    public boolean loginUser(ActionEvent event, String email, String password){
        String query = "SELECT idUser,email,pswd FROM users WHERE email = ?";


        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,email);
            resultSet = statement.executeQuery();

            if(resultSet.isBeforeFirst()){
                System.out.println("user not found in the database");
            }else{
                while (resultSet.next()){
                    String retrievedPassword = resultSet.getString("pswd");
                    String retrievedEmail = resultSet.getString("email");
                    if ((retrievedPassword.equals(password)) && (retrievedEmail.equals(email)) ){
                        System.out.println("login success");
                        ;
                    }else {
                        System.out.println("password did not match");
                    }
                }


            }

        }catch(SQLException E){
            E.printStackTrace();

        }finally {
            if(resultSet!=null){
                try{
                    resultSet.close();
                }catch (SQLException E){
                    E.printStackTrace();
                }
            }
        }

        return false;
    }

    public Long getIdConnected() throws SQLException {
        return resultSet.getLong("idUser");
    }


}
