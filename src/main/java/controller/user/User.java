package controller.user;

import model.SQL_connector;
import java.util.ArrayList;

public abstract class User {
    public static String user_name;
    public static String current_session;
    public static ArrayList<String> user_session = new ArrayList<>();

    public static ArrayList<Integer> user_init_score_by_session = new ArrayList<>();
    public static ArrayList<Integer> user_level_by_sessions = new ArrayList<>();
    public static ArrayList<String> user_lives_left_by_sessions = new ArrayList<>();
    public static int user_update_score;

    public static void printOutUserInfo() {
        if (user_session.size() > 0) {
            int user_session_count = user_session.size();

            for (int i = 0; i < user_session_count; ++i) {
                System.out.println(user_session.get(i) + " " + user_init_score_by_session.get(i)
                        + " " + user_level_by_sessions.get(i)
                        + " " + user_lives_left_by_sessions.get(i));
            }
        } else {
            System.out.println("NO USER SESSION INFO, PLEASE CHECK AGAIN!");
        }
    }

    public static void loadUserDataFrom(SQL_connector connector) {
        user_name = connector.getUsername();
        user_session = connector.getUserSession();
        user_init_score_by_session = connector.getUserScoreBySessions();
        user_level_by_sessions = connector.getUserLevelBySessions();
        user_lives_left_by_sessions = connector.getUserLivesLeftBySessions();
    }

    public static void selectLatestSession() {
        if (!user_session.isEmpty()) {
            current_session = user_session.get(user_session.size() - 1);
            System.out.println("Selected latest session: " + current_session);
        } else {
            System.out.println("No session available to load!");
        }
    }

    public static boolean canContinueSession(String livesLeft) {
        try {
            int lives = Integer.parseInt(livesLeft);
            return lives > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void selectSession(int index) {
        if (index >= 0 && index < user_session.size()) {
            current_session = user_session.get(index);
            System.out.println("Selected session: " + current_session);
        } else {
            System.out.println("Invalid session index");
        }
    }
}
