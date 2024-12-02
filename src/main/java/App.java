import data.*;
import io.ReadAndWriteData;
import model.*;
import ui.Menu;

import java.util.*;
import java.io.IOException;

/**
 * App.java
 *
 * @author Benjamin Liou
 * @author Kenneth Garcia
 * @author Kevin Young
 * CIS 22C, Course Project
 */
public class App {
    public static UserDirectory ud = ReadAndWriteData.readData();
    public static Account userAccount;
    public static Menu menu;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            userAccount = new Account(ud);
            ArrayList<User> users = ud.getArrayList();
            for (int i = 1; i < users.size(); i++) {
                String key = users.get(i).getUserName() + ":" + users.get(i).getPassword();
                userAccount.getAuth().addExistingUser(key);
            }

            menu = new Menu(userAccount, ud);
            menu.displayLogIn();
            menu.mainMenu();

            System.out.println(userAccount.getAuth().getRegisteredUserCount());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}