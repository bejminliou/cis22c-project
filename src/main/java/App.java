// import ...
import data.*;
import io.*;
import model.*;
import util.*;
import ui.Menu;
import java.util.*;
import java.io.IOException;

/**
 * App.java
 *
 * @author Kenneth Garcia
 * @author Benjamin Garcia
 * CIS 22C, Course Project
 */
public class App {
    public static UserDirectory ud = new UserDirectory();
    public static DataStorage load = new DataStorage(ud);
    public static Account userAccount;
    public static Menu menu;

    public static void main(String[] args) {
        // ...
        try {
            load.loadData();
            userAccount = new Account(ud);
            ArrayList<User> users = ud.getAllUsers();
            for (User aUsers : users) {
                String key = aUsers.getUserName() + ":" + aUsers.getPassword();
                userAccount.getAuth().addExisitingUser(key);
            }
            menu = new Menu(userAccount);
            menu.displayLogIn();


            System.out.println(userAccount.getAuth().getRegisteredUserCount());
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }




    }

}