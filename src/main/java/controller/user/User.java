package controller.user;

import java.util.ArrayList;

public abstract class User {
    public static String user_name;
    public static String current_session;
    public static ArrayList<String> user_session = new ArrayList<>();

    public static ArrayList<Integer> user_init_score_by_session = new ArrayList<>();
    public static ArrayList<Integer> user_level_by_sessions = new ArrayList<>();
    public static ArrayList<String> user_lives_left_by_sessions = new ArrayList<>();
    public static int user_update_score;

    public static void printOutUserInfo(){
        if (user_session.size() > 0) {
            int user_session_count = user_session.size();

            for (int i = 0; i < user_session_count; ++i){
                System.out.println(user_session.get(i) + " " + user_init_score_by_session.get(i) + " " + user_level_by_sessions.get(i) + " " + user_lives_left_by_sessions.get(i));
            }
        } else {
            System.out.println("NO USER SESSION INFO, PLEASE CHECK AGAIN!");
        }
    }
}
