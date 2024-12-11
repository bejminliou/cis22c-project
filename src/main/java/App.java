import data.UserDirectory;
import io.ReadAndWriteData;
import ui.Menu;

/**
 * App.java
 * Handles initialization of overall program.
 *
 * @author Benjamin Liou
 * @author Kenneth Garcia
 * @author Kevin Young
 * @author Rolen Louie
 * @author Tu Luong
 * @author Yukai Qiu
 * CIS 22C, Course Project
 */
public class App {

    /**
     * Main method that initializes UserDirectory, displays login and main menu,
     * and saves user data before exiting the program.
     *
     * @param args the command line arguments (not used in this program)
     */
    public static void main(String[] args) {
        try {
            UserDirectory userDirectory = ReadAndWriteData.readData(); // get data from data.txt

            // run program UI
            Menu menu = new Menu(userDirectory);
            menu.loginMenu();
            menu.mainMenu();

            ReadAndWriteData.writeData(userDirectory); // save user data into data.txt
        } catch (Exception e) {
            System.out.println("Error caught from main: " + e.getMessage());
        }
    }

}