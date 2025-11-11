package model;

import javafx.beans.binding.StringBinding;

import java.sql.*;
import java.util.ArrayList;

public class SQL_connector implements SQL_InitVari {

    private String user_id;
    private String user_name;
    private Connection current_connection;
    private ArrayList<Integer> user_score_by_sessions = new ArrayList<>();
    private ArrayList<Integer> user_level_by_sessions = new ArrayList<>();
    private ArrayList<String> user_start_time_by_sessions = new ArrayList<>();
    private ArrayList<String> user_end_time_by_sessions = new ArrayList<>();
    private ArrayList<String> user_lives_left_by_sessions = new ArrayList<>();


    public SQL_connector() {
        current_connection = this.createSQLConnection();
    }

    private Connection createSQLConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, SQL_username, SQL_password);
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
                    this.user_name = rs.getString("user_name");
//                    System.out.println(this.user_id);
                    return true;
                }
            } while (rs.next());
        }

        return false;
    }

    private void user_info_query() throws SQLException {
        String query = "SELECT level, start_time, end_time, total_score, lives_left FROM game_sessions WHERE user_id = ?";
        PreparedStatement preparedStatement = current_connection.prepareStatement(query);

        preparedStatement.setString(1, this.user_id);
        System.out.println(preparedStatement);

        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            user_level_by_sessions.add(rs.getInt("level"));
            user_start_time_by_sessions.add(rs.getString("start_time"));
            user_end_time_by_sessions.add(rs.getString("end_time"));
            user_score_by_sessions.add(rs.getInt("total_score"));
            user_lives_left_by_sessions.add(rs.getString("lives_left"));

//            System.out.println(user_level_by_sessions.getLast() + user_start_time_by_sessions.getLast() + user_end_time_by_sessions.getLast());
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
                this.user_info_query();

            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException);
            System.out.println("INVALID LOGIN FUNCTION, PLEASE PLAY AS GUEST!");
        }
        return login_flag;
    }

    public void createNewSession(String start_time, String end_time) throws SQLException{
        String query = "INSERT INTO game_sessions VALUES (?, ?, ?, ?, ?, 0, 3, 1)";
        PreparedStatement preparedStatement = current_connection.prepareStatement(query);

        preparedStatement.setString(1, Integer.toString(user_score_by_sessions.size() + 1));
        preparedStatement.setString(2, user_id);
        preparedStatement.setString(3, "1");
        preparedStatement.setString(4, start_time);
        preparedStatement.setString(5, end_time);
    }

    public ArrayList<String> getUserSession(){
        ArrayList<String> concatList = new ArrayList<>();

        for (int i = 0; i < user_start_time_by_sessions.size(); ++i){
            concatList.add(user_start_time_by_sessions.get(i).concat(" - ").concat(user_end_time_by_sessions.get(i)));
        }

        return concatList;
    }

    public ArrayList<Integer> getUserScoreBySessions() {
        return new ArrayList<>(user_score_by_sessions);
    }

    public ArrayList<Integer> getUserLevelBySessions() {
        return new ArrayList<>(user_level_by_sessions);
    }

    public ArrayList<String> getUserLivesLeftBySessions() {
        return new ArrayList<>(user_lives_left_by_sessions);
    }

    public String getUsername(){
        return user_name;
    }


}
