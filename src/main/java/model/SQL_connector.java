package model;

import javafx.beans.binding.StringBinding;

import java.sql.*;
import java.util.ArrayList;

public class SQL_connector implements SQL_InitVari {

    private String user_id;
    private Connection current_connection;
    public ArrayList<Integer> user_score_by_sessions = new ArrayList<>();

    public SQL_connector() {
        current_connection = this.createSQLConnection();
    }

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

    public void closeSQLConnection() {
        try {
            current_connection.close();
        } catch (SQLException sqlException) {
            System.out.println(sqlException);
        }
    }

    private boolean login(String username, String password) throws SQLException {
        String query = "SELECT user_id, user_name, password FROM user WHERE user_name = ?";
        PreparedStatement preparedStatement = current_connection.prepareStatement(query);

        preparedStatement.setString(1, username);

//        System.out.println(preparedStatement);

        ResultSet rs = preparedStatement.executeQuery();

        if (!rs.next()) {
            System.out.println("INVALID USERNAME, PLEASE TRY AGAIN!");
        } else {
            do {
                if (rs.getString("password").equals(password)) {
                    System.out.println("Password is correct!");
                    this.user_id = rs.getString("user_id");
//                    System.out.println(this.user_id);
                    return true;
                }
            } while (rs.next());
        }

        return false;
    }

    private void user_score_query() throws SQLException {
        String query = "SELECT total_score FROM game_sessions WHERE user_id = ?";
        PreparedStatement preparedStatement = current_connection.prepareStatement(query);

        preparedStatement.setString(1, this.user_id);
        System.out.println(preparedStatement);

        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            user_score_by_sessions.add(rs.getInt("total_score"));
//            System.out.println(user_score_by_sessions.getLast());
        }

    }

    public boolean authenticator(String username, String password) {
        boolean login_flag = false;
        try {

            if (current_connection == null) {
                System.out.println("Conection to database is failed!");
                return false;
            } else {
                login_flag = this.login(username, password);
                this.user_score_query();

            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException);
            System.out.println("INVALID LOGIN FUNCTION, PLEASE PLAY AS GUEST!");
        }
        return login_flag;
    }

    public int get_user_score_by_session(int game_session) {

        if (!user_score_by_sessions.isEmpty()) {
            return user_score_by_sessions.get(game_session);
        }

        return -5;
    }
}
