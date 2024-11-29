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

    // Scanner Helper Methods

    /**
     *
     */
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

    /**
     * @param input
     * @return
     */
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

    /**
     * @param input
     * @return
     */
    private static char getNextCharUpper(Scanner input) {
        return getNext(input).toUpperCase().charAt(0);
    }

    // Menu methods

    /**
     * Displays the log in menu and handles user input.
     *
     * @see model.Account#login(String, String) for login processing
     * @see model.Account#createAccount(User) for account creation
     */
    public void displayLogIn() {
        String choiceStr;
        boolean validInput = false, login = false, createAccount = false;

        // have user choose to log in or create a new account
        while (!validInput) {
            // print user options
            System.out.println("Please select from one of the following:");
            System.out.println("1. Login");
            System.out.println("2. Create New Account");

            // get user choice
            System.out.print("Enter your choice: ");
            choiceStr = scanner.next();

            // error checking to ensure input was "1" or "2"
            if (choiceStr.equals("1")) {
                System.out.println("\nLogin");
                login = true;
                validInput = true;
            } else if (choiceStr.equals("2")) {
                System.out.println("\nCreating new account");
                createAccount = true;
                validInput = true;
            }

            if (!validInput) { // if valid input != true
                System.out.println("Invalid choice. Please Try again\n\n");
                scanner.nextLine(); // clear scanner before repeating loop
            }
        }

        // get user's username and password
        System.out.print("Please enter your username: ");
        String userName = getNext(scanner);
        System.out.print("Please enter your password: ");
        String password = getNext(scanner);

        if (login) {
            // authenticate given credentials
            boolean authenticate = account.getAuth().authenticate(userName, password);

            if (authenticate) { // if credentials match
                user = account.getUser(userName);
                System.out.println("\nWelcome " + user.getUserName() + "!");
                this.userName = user.getUserName();
            } else { // if no matching credentials
                System.out.println("Your email or password is incorrect. Please try again.\n");
                scanner.nextLine(); // clear scanner before recursion
                displayLogIn();
            }
        }

        if (createAccount) {
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
                } while (true); // break when user quits
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
                scanner.nextLine(); // clear scanner before returning

            } else { // failed to create accounts
                System.out.println("\nAn account has been found under this username. Please Log In or choose" +
                        "a different username.\n");
                scanner.nextLine(); // clear scanner before recursion
                displayLogIn();
            }
        }
    }

    /**
     * Print the main menu and allow user to choose to view current friends, make new friends, or exit app.
     */
    public void mainMenu() {
        // print user options
        while (true) {
            System.out.println("1. View your friends\n2. Make new friends\n3. Quit");
            System.out.print("Please enter your choice: ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt(); // get user choice
                if (choice == 1) {
                    viewMyFriends();
                } else if (choice == 2) {
                    addFriend();
                } else if (choice == 3) {
                    System.out.println("\nGoodbye!");
                    System.exit(0);
                } else {
                    System.out.println("Please enter a number 1, 2, or 3.\n");
                    scanner.next();
                }
                break;
            } else {
                System.out.println("Please enter a number 1, 2, or 3.\n");
                scanner.next();
            }
        }
    }

    public void viewMyFriends() {
        while (true) {
            // display user options
            System.out.println("\n1: View all friends sorted by name\n2: Search for a friend");
            System.out.print("Enter your choice: ");

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt(); // input user choice
                if (choice == 1) {
                    if (account.getUser(this.userName).getFriendCount() == 0) { // if user's friend list is empty
                        System.out.println("You do not currently have any friends!\n");
                        mainMenu();
                    } else {
                        displayFriends();
                    }
                } else if (choice == 2) {
                    searchFriendByName();
                } else { // if choice is not a valid input
                    System.out.println("Please enter a valid choice (1 or 2)");
                }
                break;
            } else { // if input is not an int
                System.out.println("Please enter a valid choice (1 or 2)");
                scanner.next();
            }
        }
    }

    /**
     * Displays the current friends
     */
    private void displayFriends() {
        String result = account.getUser(this.userName).getFriends().inOrderString();
        System.out.println("\nHere are your current friends:");
        System.out.println(result.trim());
        System.out.println();
        mainMenu();
    }

    /**
     * Searches for a user's friend based on a given first and last name.
     */
    private void searchFriendByName() {
        // get first and last name of friend
        System.out.print("Enter the first name of the friend: ");
        String firstNameOfFriend = scanner.next(); // input first name
        System.out.print("Enter the last name of the friend: ");
        String lastNameOfFriend = scanner.next(); // input last name

        // search for friend
        User friend = user.searchFriendByName(firstNameOfFriend, lastNameOfFriend);
        if (friend == null) { // if friend not found
            System.out.println("Cannot find friend\nEnter 1 to Retry" +
                    "\nEnter anything else to quit to the previous menu");
            if (scanner.nextInt() == 1) {
                searchFriendByName(); // retry search
            } else {
                viewMyFriends(); // quit
            }
        } else { // if matching friend is found
            System.out.println("Enter 1 to view this user's full profile or 2 to remove this friend");
            int choice = scanner.nextInt(); // input user choice

            if (choice == 1) {
                // print name, ID, city, and interests of friend
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
                // remove friend
                user.removeFriend(friend);
                System.out.println("Successfully removed, your new friends list is now:");
                displayFriends();
            }
        }
    }

    /**
     *
     */
    public void addFriend() {
        String input;
        int userChoice = -1;
        int[] validInputs = {1, 2, 3, 4};

        // prompt user to choose how to find new friends
        while (true) {
            // print options
            System.out.println("\nMake New Friends:");
            System.out.println("1. Search for friends by name");
            System.out.println("2. Search for friends by their interests");
            System.out.println("3. Get friend recommendations by interests");
            System.out.println("4. Return to main menu");
            System.out.print("Enter your choice: ");

            try {
                // input user choice
                input = scanner.next();

                // check for valid input
                for (int validIn : validInputs) {
                    if (Integer.parseInt(input) == validIn) { // if input matches a valid input
                        userChoice = Integer.parseInt(input); // set userChoice to input
                        break;
                    }
                }

                // exit loop if userChoice is set
                if (userChoice != -1) {
                    break;
                }

                // throw exception for invalid input if code reaches this point
                throw new IndexOutOfBoundsException();
            } catch (Exception e) {
                System.out.println("Invalid input: please enter a valid choice.\n");
            }
        }

        // continue with user choice
        System.out.println();
        switch (userChoice) {
            case 1:
                searchUserByName();
            case 2:
                searchUserByInterests();
            case 3:
                getFriendRecs();
        }
    }

    /**
     *
     */
    public void searchUserByName() {
        String firstNameOfFriend, lastNameOfFriend, fullFriendName;

        // input first and last name of user to search
        System.out.println("Searching user by name.");
        System.out.print("Enter the first name of the user: ");
        firstNameOfFriend = scanner.next(); // input first name
        System.out.print("Enter the last name of the friend: ");
        lastNameOfFriend = scanner.next(); // input last name

        // create fullFriendName
        fullFriendName = firstNameOfFriend + " " + lastNameOfFriend;

        // **HELP HERE**
    }

    /**
     *
     */
    public void searchUserByInterests() {
        String interest;

        // get interest to search users with
        System.out.println("Searching users by interest.");
        System.out.print("Enter the interest you'd like to search users for: ");
        interest = scanner.next(); // input interest


    }

    /**
     *
     */
    public void getFriendRecs() {

    }
}
