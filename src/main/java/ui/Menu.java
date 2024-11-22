package ui;

import java.util.Scanner;

/**
 * Handles the user interface and menu system for the application.
 * Provides options for login, account creation, and other user interactions.
 */
public class Menu {
    private Scanner scanner;

    /**
     * Creates a new Menu with a Scanner for user input.
     * 
     * @see java.util.Scanner#Scanner(System.in) for input handling
     */
    public Menu() {
        scanner = new Scanner(System.in);
    }

    /**
     * Displays the main menu and handles user input.
     * 
     * @see model.Account#login(String, String) for login processing
     * @see model.Account#createAccount(User) for account creation
     */
    public void displayMenu() {
        System.out.println("1. Login");
        System.out.println("2. Create Account");
        // ... other menu options

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline (DO NOT DO THIS)
        // todo ; add scanner helper methods

        switch (choice) {
            case 1:
                // Call login() from Account class
                break;
            // ... other cases
        }
    }
}
