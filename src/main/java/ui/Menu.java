package ui;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;

import data.User;
import data.UserDirectory;

import model.Friend;

import util.BST;
import util.LinkedList;

/**
 * Menu.java
 * Handles the user interface and menu system for the application.
 * Provides options for login, account creation, viewing user friends,
 * and adding new friends.
 *
 * @author Benjamin Liou
 * @author Kenneth Garcia
 * @author Kevin Young
 * @author Rolen Louie
 * @author Yukai Qiu
 * CIS 22C, Course Project
 */
public class Menu {
    private final Scanner scanner = new Scanner(System.in); // Scanner for user input
    private final UserDirectory ud;
    private final Friend friend;
    public User user;
    private String username;

    // Constructors

    /**
     * Creates a new Menu with the data from the given UserDirectory
     *
     * @param userDirectory the UserDirectory storing the data of existing users
     */
    public Menu(UserDirectory userDirectory) {
        this.ud = userDirectory;
        friend = new Friend(userDirectory);

    }

    // Login Methods

    /**
     * Displays the log in menu and handles user input.
     * Logs in user by authenticating their credentials.
     * Creates new account by creating new User with the given credentials.
     */
    public void loginMenu() {
        String choiceStr;
        boolean validInput = false, login = false, createAccount = false;

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

            if (!validInput) {
                System.out.println("Invalid choice. Returning to login menu.\n");
                scanner.nextLine(); // clear scanner before repeating loop
            }
        }

        // get user's username and password
        System.out.print("Please enter your username: ");
        String userName = scanner.next();
        System.out.print("Please enter your password: ");
        String password = scanner.next();

        if (login) {
            // authenticate given credentials
            boolean authenticate = ud.getCredAuthStatus(userName, password);

            if (authenticate) { // if credentials match
                user = ud.findUserByUsername(userName);
                System.out.print("\nWelcome " + user.getUsername() + "!");
                this.username = user.getUsername();
            } else { // if no matching credentials
                System.out.println("Your username or password is incorrect. Returning to login menu.\n");
                scanner.nextLine(); // clear scanner before recursion
                loginMenu();
            }
        }

        if (createAccount) {
            // create new user with given credentials
            user = new User(userName, password);
            boolean accountCreate = ud.addAuthNewUser(user);

            // if account successfully created
            if (accountCreate) {
                // input remaining user info
                System.out.println("\nLet's finish setting up your account.");
                System.out.print("Enter your first name: ");
                user.setFirstName(scanner.next()); // input first name
                System.out.print("Enter your last name: ");
                user.setLastName(scanner.next()); // input last name
                System.out.print("Enter your city: ");
                user.setCity(scanner.next()); // input city

                // get interests
                scanner.nextLine(); // clear scanner for input
                System.out.println("\nEnter one of your interests followed by the enter key.");
                do {
                    // input interest and add it to user
                    System.out.print("Enter your interest or \"0\" to stop: ");
                    String interestName = scanner.nextLine();

                    // check if user quit
                    if (interestName.equals("0")) {
                        break;
                    }

                    ud.getInterestManager().addUserToInterest(interestName, user);
                    user.addInterest(interestName);
                } while (true);
                System.out.println("Finished entering your interests.\n");

                // ** Setting the userID like this is part of Issue #11 that we will most likely change **
                // set user ID
                String id = "";
                while (id.length() != 2) {
                    System.out.print("Please set an ID of 2 digits: ");
                    id = scanner.next();
                }
                user.setId(Integer.parseInt(id));
                System.out.println("Your ID has been set.");

                // print welcome message
                System.out.println("\nYour account has successfully been created, welcome "
                        + user.getUsername() + "!\n");
                this.username = user.getUsername();
                scanner.nextLine(); // clear scanner before returning

            } else { // failed to create accounts
                System.out.println("\nAn account has been found under this username. Please Log In or choose" +
                        "a different username.\n");
                scanner.nextLine(); // clear scanner before recursion
                loginMenu();
            }
        }
    }

    // Main Menu Methods

    /**
     * Print the main menu and allow user to choose to view current
     * friends, make new friends, or exit the Menu UI.
     */
    public void mainMenu() {
        while (true) {
            // print user options
            System.out.println("\nMain Menu:\n0. Quit\n1. View Your Friends\n2. Add New Friends");
            System.out.print("Please enter your choice: ");

            // get user input
            try {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 0:
                        System.out.println("\nGoodbye!");
                        return;  // return to App.java
                    case 1:
                        viewMyFriends();
                        break;
                    case 2:
                        addFriendMenu();
                        break;
                    default: // invalid choice
                        System.out.println("Invalid input. Please enter a valid option 0, 1, or 2.");
                        scanner.nextLine(); // clear invalid choice
                }
            } catch (Exception e) { // invalid input
                System.out.println("Invalid input. Please enter a valid option 0, 1, or 2.");
                scanner.nextLine(); // clear invalid input
            }
        }
    }

    /**
     * Prints the View Friends Menu and allows for user to view friends
     * sorted by name or to search for a specific friend by their name.
     */
    public void viewMyFriends() {
        while (true) {
            // display user options
            System.out.println("\nView Friends Menu:\n0. Return to main menu" +
                    "\n1: View all friends sorted by name\n2: Search for a friend");
            System.out.print("Enter your choice: ");

            // get user input
            try {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 0:
                        return;
                    case 1:
                        if (user.getFriendCount() == 0) { // if user's friend list is empty
                            System.out.println("You do not have any friends to display! Returning to main menu.\n");
                            break;
                        }
                        displayFriends();
                        break;
                    case 2:
                        searchFriendByName();
                        break;
                    default: // invalid choice
                        System.out.println("Invalid input. Please enter a valid option (0, 1, or 2).");
                        scanner.nextLine(); // clear invalid choice
                }
            } catch (Exception e) { // invalid input
                System.out.println("Invalid input. Please enter a valid option (0, 1, or 2).");
                scanner.nextLine(); // clear invalid input
            }
        }
    }

    /**
     * Displays the current friends of this User.
     */
    private void displayFriends() {
        String result = ud.findUserByUsername(this.username).getFriends().inOrderString();
        System.out.println("\nHere are your current friends:");
        System.out.println(result.trim());
    }

    /**
     * Prints the details of a given user.
     *
     * @param user the User to print
     */
    private void printUserProfile(User user) {
        System.out.println("\nUser Profile:");

        // print name, ID, and city of friend
        System.out.println("Name: " + user.toString());
        System.out.println("User ID: " + user.getId());
        System.out.println("City: " + user.getCity());

        // print interests of friend as a list separated by commas
        LinkedList<String> friendInterest = user.getInterests();
        friendInterest.positionIterator();
        System.out.print("Interests (" + friendInterest.getLength() + "): ");
        for (int i = 0; i < friendInterest.getLength() - 1; i++) {
            System.out.print(friendInterest.getIterator() + ", ");
            friendInterest.advanceIterator();
        }
        System.out.println(friendInterest.getIterator() + "\n");
    }

    /**
     * Searches for a user's friend based on a given first and last name.
     */
    private void searchFriendByName() {
        String nameOfFriend, firstNameOfFriend = "", lastNameOfFriend = "";

        // get first and last name of user to search
        System.out.println("\nSearching friends by name:");
        System.out.print("Enter the full name of the matchingFriend (first name + last name): ");
        scanner.nextLine(); // clear scanner
        nameOfFriend = scanner.nextLine(); // input full name

        // split full name into first and last
        String[] nameParts = nameOfFriend.split(" ");
        if (nameParts.length >= 2) { // first and last given
            firstNameOfFriend = nameParts[0];
            lastNameOfFriend = nameParts[1];
        }
        if (nameParts.length == 1) { // only first name given
            firstNameOfFriend = nameParts[0];
            System.out.print("Please enter the last name of the user to search: ");
            lastNameOfFriend = scanner.nextLine();
        }

        // search for friend with matching name
        User matchingFriend = user.searchFriendByName(firstNameOfFriend, lastNameOfFriend);

        if (matchingFriend == null) { // matching friend not found
            System.out.print("\nCould not find matching friend.\nEnter 1 to retry search by name or " +
                    "enter any other key to return to the View Friends Menu: ");
            if (scanner.nextInt() == 1) {
                searchFriendByName(); // retry search
            }
        } else { // matching friend found
            printUserProfile(matchingFriend);
            System.out.print("Enter 1 to remove this friend or enter any other key to return " +
                    "to the View Friends Menu: ");
            String choice = scanner.nextLine(); // input user choice

            if (choice.equalsIgnoreCase("1")) {
                // remove matchingFriend
                user.removeFriend(matchingFriend);
                System.out.println("Successfully removed as friend, your new friends list is now:");
                displayFriends();
            }
        }
    }

    // Find and Add Friends Methods

    /**
     * The User Menu used to add friends by name, interest, or get friend recommendations.
     */
    public void addFriendMenu() {
        final int[] validInputs = {0, 1, 2, 3};

        String inputStr;
        int userChoice = -1;

        // prompt user to choose how to find new friends
        while (true) {
            // print options
            System.out.println("\nAdd Friend Menu:");
            System.out.println("0. Return to Main Menu");
            System.out.println("1. Search Users by name");
            System.out.println("2. Search Users by interest");
            System.out.println("3. Get friend recommendations based on your interests and mutual friends");
            System.out.print("Enter your choice: ");

            try {
                // inputStr user choice
                inputStr = scanner.next();

                // check for valid inputStr
                for (int validIn : validInputs) {
                    if (Integer.parseInt(inputStr) == validIn) { // if inputStr matches a valid input
                        userChoice = Integer.parseInt(inputStr); // set userChoice to inputStr
                        break;
                    }
                }

                // exit loop if userChoice is set
                if (userChoice != -1) {
                    break;
                }

                // invalid input if code reaches this point
                System.out.println("Invalid input, please enter a valid choice.");
                scanner.nextLine(); // clear scanner before repeating
            } catch (Exception e) {
                System.out.println("Invalid input, please enter a valid choice.");
                scanner.nextLine(); // clear scanner before repeating
            }
        }

        // continue with user choice
        switch (userChoice) {
            case 1:
                do {
                    User viewedUser = searchUsersByName();

                    if (viewedUser != null) { // user with matching name selected
                        System.out.print("Enter 1 to add this user as your friend, or any other key to continue" +
                                " without adding: ");
                        inputStr = scanner.next();

                        // adding as friend
                        if (inputStr.equals("1")) {
                            user.addFriend(viewedUser);
                            ud.addFriendConnection(user, viewedUser);

                            System.out.print(viewedUser.getFirstName() + " " + viewedUser.getLastName()
                                    + " has been added as your friend!\n");
                            displayFriends();
                        }
                    }

                    // ask if user wants to retry searchUserByName()
                    System.out.print("\nEnter 1 to search another name or any other key to return to main menu: ");
                    inputStr = scanner.next();
                } while (inputStr.equals("1"));
                break;
            case 2:
                System.out.println("\nSearching Users by Interest:");
                System.out.print("Please enter the interest you want to search by: ");
                inputStr = scanner.next();
                searchByInterests(inputStr);
                break;
            case 3:
                getFriendRecs();
                break;
        }
    }

    /**
     * Allows user to search all current Users by name and select a matching user.
     *
     * @return the selected User
     */
    public User searchUsersByName() {
        String nameOfFriend, firstNameOfFriend = "", lastNameOfFriend = "", inputStr;
        ArrayList<User> matchingUsers;
        User returnUser = null;

        // get first and last name of user to search
        System.out.println("\nSearching users by name:");
        System.out.print("Enter the full name of the user (first name + last name): ");
        scanner.nextLine(); // clear scanner
        nameOfFriend = scanner.nextLine(); // input full name

        // split full name into first and last
        String[] nameParts = nameOfFriend.split(" ");
        if (nameParts.length >= 2) { // first and last given
            firstNameOfFriend = nameParts[0];
            lastNameOfFriend = nameParts[1];
        }
        if (nameParts.length == 1) { // only first name given
            firstNameOfFriend = nameParts[0];
            System.out.print("Please enter the last name of the user to search: ");
            lastNameOfFriend = scanner.nextLine();
        }

        // search UserDirectory by name
        matchingUsers = ud.findUsersByName(firstNameOfFriend, lastNameOfFriend);

        if (!matchingUsers.isEmpty()) {
            System.out.println("\nHere are the matching users:");

            // print all users that match the given name
            for (int i = 1; i <= matchingUsers.size(); i++) {
                User currUser = matchingUsers.get(i - 1);
                System.out.println(i + ". " + currUser.getFirstName() + " " + currUser.getLastName());
            }

            // continue loop until valid ID is given
            boolean validInput = false;
            do {
                try {
                    // get index of profile to view
                    System.out.print("Enter the index (1-" + matchingUsers.size() + ") " +
                            "of the User you'd like to view: ");
                    inputStr = scanner.next();

                    // check for valid index
                    int index = Integer.parseInt(inputStr);
                    if (index > 0 && index <= matchingUsers.size()) {
                        // set and print returnUser
                        returnUser = matchingUsers.get(index - 1);
                        printUserProfile(returnUser);

                        validInput = true;
                    }

                    // if input doesn't match validID
                    if (!validInput) {
                        System.out.println("\nPlease ensure the index given is valid.");
                        scanner.nextLine(); // clear scanner after invalid input
                    }
                } catch (Exception e) { // invalid input given
                    System.out.println("\nPlease ensure the index given is valid.");
                    scanner.nextLine(); // clear scanner after invalid input
                }
            } while (!validInput);
        } else { // matchingUsers is empty
            System.out.println("\nThere were no users that match the given name. Returning to friend menu.");
        }

        return returnUser;
    }

    /**
     * Searches Users that share the given Interest and allows this User
     * to add a User who shares the Interest as a friend.
     *
     * @param interestName the name of the given Interest
     */
    private void searchByInterests(String interestName) {
        // return if no existing Users share the Interest
        if (ud.getInterestManager().retrieveInterestBST(interestName) == null) {
            System.out.println("No Users share that interest! Returning to main menu.");
            return;
        }

        // get BST<User> containing Users who share the Interest
        BST<User> interestBST = ud.getInterestManager().retrieveInterestBST(interestName);

        // convert interestBST to ArrayList<String>
        String BSTInOrderStr = interestBST.inOrderString();
        String[] splitArray = BSTInOrderStr.split("\n");
        ArrayList<String> recommendedUsers = new ArrayList<>(Arrays.asList(splitArray));

        do {
            try {
                System.out.println("\nHere's some Users who share the same interests as you:");

                // print recommended Users
                for (int i = 0; i < recommendedUsers.size(); i++) {
                    System.out.println((i + 1) + ". " + recommendedUsers.get(i));
                }

                System.out.print("Enter the index (1-" + recommendedUsers.size() + ") " +
                        "the person whose profile you'd like to view: ");
                int index = scanner.nextInt();
                scanner.nextLine(); // clear line after using nextInt()

                if (index > 0 && index <= recommendedUsers.size()) { // valid ID selected
                    // get first and last name of selected User
                    String selectedUser = recommendedUsers.get(index - 1);
                    String[] splitArray2 = selectedUser.split(" ");
                    String first = splitArray2[0];
                    String last = splitArray2[1];

                    // get User using their name and print their profile
                    User temp = ud.findUserAndReturnUserClass(first, last);
                    printUserProfile(temp);

                    // prompt this User to add friend or return
                    System.out.print("Enter 1 to add as friend or enter any other key to return to the " +
                            "previous menu: ");
                    String choice = scanner.nextLine();

                    if (choice.equalsIgnoreCase("1")) {
                        System.out.println("Successfully added " + temp.getFirstName() + " " + temp.getLastName()
                                + " as a friend!");
                        user.addFriend(temp);
                        displayFriends();
                        break;
                    }
                } else { // invalid ID given
                    System.out.println("Invalid index. Please try again.");
                    scanner.nextLine(); // clear input
                }
            } catch (Exception e) { // invalid input
                System.out.println("Invalid input. Please try again.");
                scanner.nextLine(); // clear input
            }
        } while (true);
    }

    /**
     * Prints out friend recommendations for this User based on other Users'
     * interest and relationships.
     * Allows this User to add a recommended friend.
     *
     * @see Friend#getFriendRecommendations for friend recoomendation system
     */
    public void getFriendRecs() {
        do {
            try {
                ArrayList<User> recommended = friend.getFriendRecommendations(user);

                // print all recommended Users
                System.out.println("\nHere are some recommended friends based on interest and relations: ");
                for (int i = 0; i < recommended.size(); i++) {
                    System.out.println((i + 1) + ". " + recommended.get(i).getFirstName() + " "
                            + recommended.get(i).getLastName());
                }

                // get user choice
                System.out.print("Enter 0 to return to the Main Menu or the index (1-" + recommended.size() + ") " +
                        "of the person whose profile you'd like to view: ");
                int index = scanner.nextInt();

                // return to main menu
                if (index == 0) {
                    break;
                }

                // invalid input given
                if (!(index > 0) || !(index <= recommended.size())) {
                    System.out.println("Invalid index given. Please try again.");
                    scanner.nextLine(); // clear input
                }

                // print selectedUser profile
                User selectedUser = recommended.get(index - 1);
                printUserProfile(selectedUser);

                System.out.print("Enter 1 to add as friend or enter any other key to return " +
                        "to the recommended friends: ");
                scanner.nextLine(); // clear input
                String choice = scanner.nextLine();
                if (choice.equalsIgnoreCase("1")) {
                    System.out.println("Friend added successfully!");

                    // update this user and the overall friend connections
                    user.addFriend(selectedUser);
                    ud.addFriendConnection(this.user, selectedUser);

                    // display this user's updated current friends
                    displayFriends();
                }
            } catch (Exception e) {
                System.out.println("Please ensure the input is a valid index.");
                scanner.nextLine(); // clear input
            }
        } while (true);
    }

}
