// import ...
import data.*;
import io.*;
import model.*;
import util.*;
import java.util.*;
import java.io.IOException;


public class App {
    public static UserDirectory ud = new UserDirectory();
    public static DataStorage load = new DataStorage(ud);
    public static Account userAccount;

    public static void main(String[] args) {
        // ...
        try {
            load.loadData();
            userAccount = new Account(ud);
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }



    }

}