package model;

import javax.crypto.spec.PSource;
import java.io.BufferedReader;
import java.sql.*;

public class SQL_connector implements SQL_InitVari {

    private String user_id;

    private Connection createSQLConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, username, password);
        } catch (ClassNotFoundException classNotFoundException) {
            System.out.println("ClassNotFoundException: " + classNotFoundException);
        } catch (SQLException sqlException) {
            System.out.println("SQLException: " + sqlException);
        }
        return connection;
    }

    private void closeSQLConnection(Connection current_connection) throws SQLException {
        current_connection.close();
    }

    private boolean login(String username, String password, Connection current_connection) throws SQLException {
        String query = "SELECT user_id, user_name, password FROM user WHERE user_name = ?";
        PreparedStatement preparedStatement = current_connection.prepareStatement(query);

        preparedStatement.setString(1, username);

        System.out.println(preparedStatement);

        ResultSet rs = preparedStatement.executeQuery();

        if (!rs.next()) {
            System.out.println("INVALID USERNAME, PLEASE TRY AGAIN!");
        } else {
            do {
                if (rs.getString("password").equals(password)) {
                    System.out.println("Login successfully!");
                    user_id = rs.getString("user_id");
                    return true;
                }
            } while (rs.next());
        }

        return false;
    }


    public boolean authenticator(String username, String password){
        boolean login_flag = false;
        SQL_connector connector = new SQL_connector();
        try {
            Connection initConnection = connector.createSQLConnection();

            if (initConnection == null) {
                System.out.println("Conection to database is failed!");
                return false;
            } else {
                login_flag = connector.login(username, password, initConnection);

                connector.closeSQLConnection(initConnection);
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException);
            System.out.println("INVALID LOGIN FUNCTION, PLEASE PLAY AS GUEST!");
        }
        return login_flag;
    }


    public static void main(String[] args) {
        SQL_connector connector = new SQL_connector();
        try {
            Connection initConnection = connector.createSQLConnection();

            if (initConnection == null) {
                System.out.println("Conection to database is failed!");
            } else {
                System.out.println(connector.login("demo01", "demo01", initConnection));

                connector.closeSQLConnection(initConnection);
            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException);
            System.out.println("INVALID LOGIN FUNCTION, PLEASE PLAY AS GUEST!");
        }

    }
}
