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
        if (!user_session.isEmpty()) {
            int sessionCount = user_session.size();
            for (int i = 0; i < sessionCount; i++) {
                System.out.println(
                        user_session.get(i) + " "
                                + user_init_score_by_session.get(i) + " "
                                + user_level_by_sessions.get(i) + " "
                                + user_lives_left_by_sessions.get(i)
                );
            }
        } else {
            System.out.println("NO USER SESSION INFO, PLEASE CHECK AGAIN!");
        }
    }

    public static void usr_logout(){
        user_name = "";
        current_session = "";
        user_session.clear();

        user_init_score_by_session.clear();;
        user_level_by_sessions.clear();
        user_lives_left_by_sessions.clear();
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

    public static void sortSessionsByScore() {
        if (user_session.size() <= 1) return;
        quickSort(0, user_session.size() - 1);
    }

    private static void quickSort(int low, int high) {
        if (low < high) {
            int pivotI = partition(low, high);
            quickSort(low, pivotI - 1);
            quickSort(pivotI + 1, high);
        }
    }

    private static int partition(int low, int high) {
        int pivot = user_init_score_by_session.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (user_init_score_by_session.get(j) > pivot) {
                i++;
                swap(i, j);
            }
        }

        swap(i + 1, high);
        return i + 1;
    }

    private static void swap(int i, int j) {
        int tempScore = user_init_score_by_session.get(i);
        user_init_score_by_session.set(i, user_init_score_by_session.get(j));
        user_init_score_by_session.set(j, tempScore);

        String tempSession = user_session.get(i);
        user_session.set(i, user_session.get(j));
        user_session.set(j, tempSession);

        int tempLevel = user_level_by_sessions.get(i);
        user_level_by_sessions.set(i, user_level_by_sessions.get(j));
        user_level_by_sessions.set(j, tempLevel);

        String tempLives = user_lives_left_by_sessions.get(i);
        user_lives_left_by_sessions.set(i, user_lives_left_by_sessions.get(j));
        user_lives_left_by_sessions.set(j, tempLives);
    }

}
