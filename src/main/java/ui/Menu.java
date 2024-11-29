package ui;

import data.Interest;
import model.Account;

import java.util.Scanner;

import data.User;
import util.LinkedList;

/**
 * Handles the user interface and menu system for the application.
 * Provides options for login, account creation, and other user interactions.
 */
public class Menu {
    private Scanner scanner;
    private Account account;
    public User user;
    private String userName;

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
        userName = "";
    }


    /**
     * Displays the log in menu and handles user input.
     *
     * @see model.Account#login(String, String) for login processing
     * @see model.Account#createAccount(User) for account creation
     */
    public void displayLogIn() {
        String choiceStr;
        int choiceInt;

        // print user options
        System.out.println("Please select from one of the following:\n");
        System.out.println("1. Login");
        System.out.println("2. Create Account");

        // get user choice
        System.out.print("Enter your choice: ");
        choiceStr = scanner.next();

        // error checking to ensure input was "1" or "2"
        if (!choiceStr.equals("1") && !choiceStr.equals("2")) {
            System.out.println("Invalid choice. Please Try again\n\n");
            displayLogIn(); // re-call method
        }
        choiceInt = Integer.parseInt(choiceStr);

        // print option user choose
        if (choiceInt == 1) {
            System.out.println("\nLogin");
        }
        if (choiceInt == 2) {
            System.out.println("\nCreating new account");
        }

        // get user's username and password
        System.out.print("Please enter your username: ");
        String userName = getNext(scanner);
        System.out.print("Please enter your password: ");
        String password = getNext(scanner);

        if (choiceInt == 1) {
            // authenticate given credentials
            boolean authenticate = account.getAuth().authenticate(userName, password);

            if (authenticate) { // if credentials match
                user = account.getUser(userName);
                System.out.println("\nWelcome " + user.getUserName() + "!");
                this.userName = user.getUserName();
            } else { // if no matching credentials
                System.out.println("Your email or password is incorrect. Please try again.\n");
                displayLogIn();
            }
        }

        if (choiceInt == 2) {
            // create new user with given credentials
            user = new User();
            user.setUserName(userName);
            user.setPassword(password);
            boolean accountCreate = account.createAccount(user);

            // if account successfully created
            if (accountCreate) {
                // input user info
                System.out.println("\nLet's finish setting up your account.");
                System.out.print("Enter your first name: ");
                user.setFirstName(getNext(scanner)); // input first name
                System.out.print("Enter your last name: ");
                user.setLastName(getNext(scanner)); // input last name
                System.out.print("Enter your city: ");
                user.setCity(getNext(scanner)); // input city

                // get interests
                scanner.nextLine(); // clear scanner for input
                System.out.println("\nEnter one of your interests followed by the enter key.");
                do {
                    // input interest and add it to user
                    System.out.print("Enter your interest or \"0\" to stop: ");
                    String interests = scanner.nextLine();

                    // check if user quit
                    if (interests.equals("0")) {
                        break;
                    }

                    // add interest to user
                    user.addInterest(interests);
                } while (true);
                System.out.println("Finished entering your interests.\n");

                // set user ID
                String id = "";
                while (id.length() != 2) {
                    System.out.print("Please set an ID of 2 digits: ");
                    id = getNext(scanner);
                }
                user.setId(Integer.parseInt(id));
                System.out.println("Your ID has been set.");

                // print welcome message
                System.out.println("\nYour account has successfully been created, welcome "
                        + user.getUserName() + "!\n");
                this.userName = user.getUserName();
                scanner.nextLine(); // clear scanner

            } else { // failed to create accounts
                System.out.println("\nAn account has been found under this username. Please Log In or choose" +
                        "a different username.\n");
                displayLogIn(); // re-call method
            }
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


    /*
     * private static double getNextDouble(Scanner input) { return Double.parseDouble(getNext(input)); }
     */

    private static char getNextCharUpper(Scanner input) {
        return getNext(input).toUpperCase().charAt(0);
    }


    public void mainMenu() { //maybe add friend suggestion
        System.out.println("Please enter your choice:\n1. View your friends\n2. Make new friends\n3. Quit");
        while (true) {
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                if (choice == 1) {
                    viewMyFriends();
                } else if (choice == 2) {
                    //add friend
                } else if (choice == 3) {
                    System.exit(0);
                } else {
                    System.out.println("Please enter a number 1, 2, or 3");
                }
                break;
            } else {
                System.out.println("Please enter a number 1, 2, or 3");
                scanner.next();
            }
        }
    }

    public void viewMyFriends() {
        System.out.println("Enter choice\n1: View all friends sorted by name\n2: Search for a friend");
        scanner = new Scanner(System.in);
        while (true) {
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                if (choice == 1) {
                    if (account.getUser(this.userName).getFriendCount() == 0) {
                        System.out.println("You currently do not have any friends!");
                        mainMenu();

                    } else {
                        displayFriends();
                    }
                } else if (choice == 2) {
                    searchFriends();

                } else {
                    System.out.println("Please enter a number 1 or 2");
                }
                break;
            } else {
                System.out.println("Please enter a number 1 or 2");
                scanner.next();
            }
        }

    }

    /**
     * Displays the current friends
     *
     * @param userId The user id of the current user.
     */
    private void displayFriends() {

        String result = account.getUser(this.userName).getFriends().inOrderString();
        System.out.println("\nHere are your current friends:");
        System.out.println(result.trim());
        System.out.println();
        mainMenu();
        //jump to friend suggestion


    }

    private void searchFriends() {
        scanner = new Scanner(System.in);
        System.out.println("Enter the first name of the friend");
        String firstNameOfFriend = scanner.next();
        System.out.println("Enter the last name of the friend");
        String lastNameOfFriend = scanner.next();
        User friend = user.searchFriendByName(firstNameOfFriend, lastNameOfFriend);
        if (friend == null) {
            System.out.println("Cannot find friend\nEnter 1 to Retry\nEnter anything else to quit to the previous menu");
            if (scanner.nextInt() == 1) {
                searchFriends();
            } else {
                viewMyFriends();
            }
        } else {
            System.out.println("Enter 1 to view this user's full profile or 2 to remove this friend");
            int choice = scanner.nextInt();
            if (choice == 1) {
                System.out.println("Name: " + friend.toString());
                System.out.println("Id: " + friend.getId());
                System.out.println("City: " + friend.getCity());
                LinkedList<String> friendInterest = friend.getInterests();
                friendInterest.positionIterator();
                System.out.println("Interests" + " (" + friendInterest.getLength() + "):");
                for (int i = 0; i < friendInterest.getLength(); i++) {
                    System.out.println(friendInterest.getIterator());
                    friendInterest.advanceIterator();

                }
                System.out.println();
                viewMyFriends();

            } else if (choice == 2) {
                user.removeFriend(friend);
                System.out.println("Successfully removed, your new friends list is now:");
                displayFriends();
            }
        }

    }

    public void addFriend() {

    }


}
