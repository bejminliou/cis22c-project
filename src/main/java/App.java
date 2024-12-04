import data.*;
import io.ReadAndWriteData;
import ui.Menu;

/**
 * App.java
 *
 * @author Benjamin Liou
 * @author Kenneth Garcia
 * @author Kevin Young
 * @author Yukai Qiu
 * CIS 22C, Course Project
 */
public class App {
    public static UserDirectory ud = ReadAndWriteData.readData();
    public static Menu menu;

    public static void main(String[] args) {
        try {
            menu = new Menu(ud);
            menu.displayLogIn();
            menu.mainMenu();

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}