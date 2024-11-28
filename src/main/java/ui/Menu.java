package ui;

import model.Account;

import java.util.Scanner;

import data.User;

/**
 * Handles the user interface and menu system for the application.
 * Provides options for login, account creation, and other user interactions.
 *
 * @author Kenneth Garcia
 * @author Benjamin Liou
 * CIS 22C, Course Project
 */
public class Menu {
    private Scanner scanner;
    private Account account;
    public User user;

    /**
     * Creates a new Menu with a Scanner for user input.
     *
     * @see java.util.Scanner#Scanner(System.in) for input handling
     */
    public Menu() {
        scanner = new Scanner(System.in);
    }

    /**
     * Creates a new Menu with a Scanner for user input.
     *
     * @param account the Account Object used to manage users
     * @see java.util.Scanner#Scanner(System.in) for input handling
     */
    public Menu(Account account) {
        this.account = account;
        scanner = new Scanner(System.in);
    }


    /**
     * Displays the log in menu and handles user input.
     *
     * @see model.Account#login(String, String) for login processing
     * @see model.Account#createAccount(User) for account creation
     */
    public void displayLogIn() {
        System.out.println("Please select from one of the following:\n");
        System.out.println("1. Login");
        System.out.println("2. Create Account");
        // ... other menu options

        int choice = getNextInt(scanner);
        // scanner.nextLine(); // Consume newline (DO NOT DO THIS)

        if (choice == 1) {
            // Call login() from Account class
            System.out.println("Please enter your username:");
            String userName = getNext(scanner);
            System.out.println("Please enter your password:");
            String password = getNext(scanner);
            boolean authenticate = account.getAuth().authenticate(userName, password);
            if (authenticate) {
                user = account.getUser(userName);
                System.out.println("\nWelcome " + user.getUserName() + "!");
            } else {
                System.out.println("\nYour email or password is incorrect. Please try again.\n");
                displayLogIn();

            }

        } else if (choice == 2) { // Call createAccount() from Account class
            System.out.println("Please enter your username:");
            String userName = getNext(scanner);
            System.out.println("Please enter your password:");
            String password = getNext(scanner);
            user = new User();
            user.setUserName(userName);
            user.setPassword(password);
            boolean accountCreate = account.createAccount(user);
            if (accountCreate) {
                System.out.println("\nLet's finish setting up your account\n");
                System.out.println("Enter your first name:");
                user.setFirstName(getNext(scanner));
                System.out.println("Enter your last name:");
                user.setLastName(getNext(scanner));
                System.out.println("Enter your city:");
                user.setCity(getNext(scanner));
                System.out.println("How many interests would you like to add?");
                int amount = getNextInt(scanner);
                for (int i = 0; i < amount; ++i) {
                    System.out.println((i + 1) + ". Add Interest: ");
                    user.addInterest(getNext(scanner));
                }
                String id = "";
                while (id.length() != 2) {
                    System.out.println("Please set an id of 2 digits.");
                    id = getNext(scanner);
                }
                user.setId(Integer.parseInt(id));
                System.out.println("You are all set!");
                System.out.println("\nWelcome " + user.getUserName() + "!");

            } else {
                System.out.println("\nAn account has been found under this username. Please Log In.\n");
                displayLogIn();
            }
        } else {
            System.out.println("Invalid choice. Please Try again\n");
            displayLogIn();
        }
    }

    // Scanner Helper Methods

    private static String getNext(Scanner input) {
        String next = "";

        while (input.hasNext()) {
            next = input.next().trim();

            if (!next.isEmpty()) {
                break;
            }
        }

        return next;
    }

    private static String getLine(Scanner input) {
        String next = "";

        while (input.hasNextLine()) {
            next = input.nextLine().trim();

            if (!next.isEmpty()) {
                break;
            }
        }

        return next;
    }

    private static int getNextInt(Scanner input) {
        return Integer.parseInt(getNext(input));
    }

    /*
     * private static double getNextDouble(Scanner input) { return Double.parseDouble(getNext(input)); }
     */

    private static char getNextCharUpper(Scanner input) {
        return getNext(input).toUpperCase().charAt(0);
    }
}
