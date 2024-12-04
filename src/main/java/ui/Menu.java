package ui;

import java.util.ArrayList;
import java.util.Scanner;

import data.UserDirectory;
import data.User;

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
 * @author Yukai Qiu
 * CIS 22C, Course Project
 */
public class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private final UserDirectory ud;
    public User user;
    private String userName;

    // Constructors

    /**
     * Creates a new Menu with the data from the given UserDirectory
     *
     * @param ud the UserDirectory storing the data of existing users
     */
    public Menu(UserDirectory ud) {
        this.ud = ud;

    }

    // Menu methods

    /**
     * Displays the log in menu and handles user input.
     * Logs in user by authenticating their credentials.
     * Creates new account by creating new User with the given credentials.
     */
    public void displayLogIn() {
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
                System.out.println("Invalid choice. Please Try again\n");
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
                System.out.println("\nWelcome " + user.getUsername() + "!");
                this.userName = user.getUsername();
            } else { // if no matching credentials
                System.out.println("Your username or password is incorrect. Returning to previous menu.\n");
                scanner.nextLine(); // clear scanner before recursion
                displayLogIn();
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
                this.userName = user.getUsername();
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
        while (true) {
            // print user options
            System.out.println("Main Menu:\n1. View your friends\n2. Make new friends\n3. Quit");
            System.out.print("Please enter your choice: ");

            // get user choice
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();

                if (choice == 1) {
                    viewMyFriends();
                } else if (choice == 2) {
                    addFriendMenu();
                } else if (choice == 3) {
                    System.out.println("\nGoodbye!");
                    scanner.close();
                    System.exit(0);
                } else {
                    System.out.println("Invalid input. Please enter a valid option 1, 2, or 3.\n");
                    scanner.nextLine(); // clear scanner before repeating loop
                }
            } else { // if int was not input
                System.out.println("Invalid input. Please enter a valid option 1, 2, or 3.\n");
                scanner.nextLine(); // clear scanner before repeating loop
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
            System.out.println("\n1: View all friends sorted by name\n2: Search for a friend");
            System.out.print("Enter your choice: ");

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt(); // input user choice
                if (choice == 1) {
                    if (user.getFriendCount() == 0) { // if user's friend list is empty
                        System.out.println("You do not have any friends to display!\n");
                        mainMenu();
                    } else {
                        displayFriends();
                    }
                } else if (choice == 2) {
                    searchFriendByName();
                } else { // if choice is not a valid int
                    System.out.println("Please enter a valid choice (1 or 2).");
                }
                break;
            } else { // if input is not an int
                System.out.println("Please enter a valid choice (1 or 2).");
                scanner.next();
            }
        }
    }

    /**
     * Displays the current friends of the User.
     */
    private void displayFriends() {
        String result = ud.findUserByUsername(this.userName).getFriends().inOrderString();
        System.out.println("\nHere are your current friends:");
        System.out.println(result.trim());
        System.out.println();
        mainMenu();
    }

    /**
     * Prints the details of a given user.
     *
     * @param user the User to print
     */
    private void printProfile(User user) {
        System.out.println("\nUser profile:");

        // print name, ID, and city of friend
        System.out.println("Name: " + user.toString());
        System.out.println("Id: " + user.getId());
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
        System.out.println("Searching friends by name:");
        System.out.print("Enter the full name of the friend (first name + last name): ");
        scanner.nextLine(); // clear scanner
        nameOfFriend = scanner.nextLine(); // input full name

        // split full name into first and last
        String[] nameParts = nameOfFriend.split(" ");

        if (nameParts.length >= 2) { // first and last given
            firstNameOfFriend = nameParts[0];
            lastNameOfFriend = nameParts[1];
        } else if (nameParts.length == 1) { // only first name given
            firstNameOfFriend = nameParts[0];
            System.out.print("Please enter the last name of the user to search: ");
            lastNameOfFriend = scanner.nextLine();
        }

        // search for friend
        User friend = user.searchFriendByName(firstNameOfFriend, lastNameOfFriend);
        if (friend == null) { // if friend not found
            System.out.print("\n\nCould not find friend.\nEnter 1 to retry or " +
                    "enter any other key to return to the previous menu: ");
            if (scanner.nextInt() == 1) {
                searchFriendByName(); // retry search
            } else {
                viewMyFriends(); // quit
            }
        } else { // if matching friend is found
            System.out.print("Enter 1 to view this user's full profile or 2 to remove this friend: ");
            int choice = scanner.nextInt(); // input user choice

            if (choice == 1) {
                // print profile
                printProfile(friend);

                // return to mainMenu
                mainMenu();
            } else if (choice == 2) {
                // remove friend
                user.removeFriend(friend);
                System.out.println("Successfully removed, your new friends list is now:");
                displayFriends();
            }
        }
    }

    /**
     * The User Menu used to add friends by name, interest, or get friend recommendations.
     */
    public void addFriendMenu() {
        String inputStr;
        int userChoice = -1;
        int[] validInputs = {1, 2, 3, 4};
        User viewedUser;

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
                System.out.println("Invalid input, please enter a valid choice.\n");
            } catch (Exception e) {
                System.out.println("Invalid input, please enter a valid choice.\n");
            }
        }

        // continue with user choice
        System.out.println();
        switch (userChoice) {
            case 1:
                do {
                    viewedUser = searchUsersByName();

                    if (viewedUser != null) { // user with matching name selected
                        System.out.print("Enter 1 to add this user as your friend, or any other key to continue" +
                                " without adding: ");
                        inputStr = scanner.next();

                        // adding as friend
                        if (inputStr.equals("1")) {
                            user.addFriend(viewedUser);
                            ud.addFriendConnection(user, viewedUser);

                            System.out.println(viewedUser.getFirstName() + " " + viewedUser.getLastName()
                                    + " has been added as your friend!\n");
                        }
                    }

                    // ask if user wants to retry searchUserByName()
                    System.out.print("Enter 1 to search another name or any other key to return to previous menu: ");
                    inputStr = scanner.next();
                    System.out.println();
                } while (inputStr.equals("1"));
                break;
            case 2:
                // **UNFINISHED**//
                //searchUsersByInterest();
                /* I've taken this to a different approach, different from searchUsersByName, i am not sure if this
                 * is more optimal but I think it is more user friendly
                 */
                break;
            case 3:
                // **UNFINISHED**//
                //viewedUser = getFriendRecs();
                break;
        }

        // return to mainMenu
        mainMenu();
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
        ArrayList<Integer> validIDs = new ArrayList<>();

        // get first and last name of user to search
        System.out.println("Searching users by name:");
        System.out.print("Enter the full name of the user (first name + last name): ");
        scanner.nextLine(); // clear scanner
        nameOfFriend = scanner.nextLine(); // input full name

        // split full name into first and last
        String[] nameParts = nameOfFriend.split(" ");

        if (nameParts.length >= 2) { // first and last given
            firstNameOfFriend = nameParts[0];
            lastNameOfFriend = nameParts[1];
        } else if (nameParts.length == 1) { // only first name given
            firstNameOfFriend = nameParts[0];
            System.out.print("Please enter the last name of the user to search: ");
            lastNameOfFriend = scanner.nextLine();
        }

        // search UserDirectory by name
        matchingUsers = ud.findUsersByName(firstNameOfFriend, lastNameOfFriend);

        if (!matchingUsers.isEmpty()) { // matchingUsers is not empty
            System.out.println("\nHere are the matching users:");

            // print all users that match the given name
            for (User user : matchingUsers) {
                validIDs.add(user.getId()); // add ID to validIDs

                // format: "ID. firstName LastName"
                System.out.println(user.getId() + ". " + user.getFirstName() + " " + user.getLastName());
            }

            // continue loop until valid ID is given
            boolean validInput = false;
            do {
                try {
                    // get ID of profile to view
                    System.out.print("Enter the ID (number to the left of the name) of the person whose profile" +
                            " you'd like to view: ");
                    inputStr = scanner.next();

                    // check if inputStr matches a validID
                    for (int ID : validIDs) {
                        if (Integer.parseInt(inputStr) == ID) { // if inputStr matches validID
                            // set and print returnUser
                            returnUser = matchingUsers.get(validIDs.indexOf(Integer.parseInt(inputStr)));
                            printProfile(returnUser);

                            validInput = true;
                            break;
                        }
                    }

                    // if input doesn't match validID
                    if (!validInput) {
                        System.out.println("\nPlease ensure the ID given is valid.");
                    }
                } catch (Exception e) {
                    System.out.println("\nPlease ensure the ID given is valid.");
                    scanner.nextLine(); // clear scanner after invalid input
                }
            } while (!validInput);
        } else { // matchingUsers is empty
            System.out.println("\nThere were no users that match the given name.");
        }

        return returnUser;
    }

    /*
    public User searchUsersByInterest() {
        String interest, inputStr;
        User returnUser = null;
        ;
        //InterestManager storedInterests =
        ArrayList<Integer> validIDs = new ArrayList<>();

        // get interest to search users with
        System.out.println("Searching users by interest:");
        System.out.print("Enter the interest you'd like to search users for: ");
        interest = scanner.next(); // input interest

        // Use InterestManager class to find the list of users with that interest
        // I could not find a way to load up all the interests into interestManager like what yall did in searchUsersByName


        // matchingUsers = im.getUsersWithInterest(interest);
        BST<User> userList = ud.interestManager.retrieveInterestBST(interest);
        if (userList != null) { // If there are matching users
            System.out.println(userList.inOrderString());

        } else { // If no matching users are found
            System.out.println("\nThere were no users that match the given interest.");
        }

        boolean validInput = false;
        do {
            try {
                // Get ID of profile to view
                System.out.print("Enter the ID (number to the left of the name) of the person whose profile" +
                        " you'd like to view: ");
                inputStr = scanner.next();

                // Check if inputStr matches a valid ID
                for (int ID : validIDs) {
                    if (Integer.parseInt(inputStr) == ID) { // If inputStr matches validID
                        // Set and print returnUser
                        //returnUser = userList.get(validIDs.indexOf(Integer.parseInt(inputStr)));  // Assuming BST.get() method is available
                        printProfile(returnUser);

                        validInput = true;
                        break;
                    }
                }

                // If input doesn't match validID
                if (!validInput) {
                    System.out.println("\nPlease ensure the ID given is valid.");
                }
            } catch (Exception e) {
                System.out.println("\nPlease ensure the ID given is valid.");
                scanner.nextLine(); // Clear scanner after invalid input
            }
        } while (!validInput);


        { // If no matching users are found
            System.out.println("\nThere were no users that match the given interest.");
        }

        return returnUser;
    }
    */

    /**
     * Helper method to handle user selection and actions from a list of matching users.
     *
     * @param matchingUsers The list of users matching the search criteria.
     * @param validIDs      The list of valid IDs corresponding to the matching users.
     */
    private void handleUserSelection(ArrayList<User> matchingUsers, ArrayList<Integer> validIDs) {
        boolean continueSelection = true;

        while (continueSelection && !matchingUsers.isEmpty()) {
            // Display the current list of matching users
            System.out.println("\nHere are the current matching users:");
            for (User user : matchingUsers) {
                System.out.println(user.getId() + ". " + user.getFirstName() + " " + user.getLastName());
            }

            try {
                System.out.print("\nEnter the ID of the person you'd like to view, or enter 0 to go back: ");
                int selectedID = scanner.nextInt();

                if (selectedID == 0) {
                    System.out.println("Going back to the previous menu...");
                    continueSelection = false; // Exit the loop and return to the main menu
                } else if (validIDs.contains(selectedID)) {
                    User selectedUser = matchingUsers.get(validIDs.indexOf(selectedID));

                    // Print profile of selected user
                    printProfile(selectedUser);

                    // Prompt user for action
                    boolean actionValid = false;
                    while (!actionValid) {
                        System.out.println("\nWhat would you like to do?");
                        System.out.println("1. Add this user as a friend");
                        System.out.println("2. Skip this user");
                        System.out.println("3. Go back to the list of users");
                        System.out.print("Enter your choice: ");

                        int choice = scanner.nextInt();

                        switch (choice) {
                            case 1: // Add as a friend
                                user.addFriend(selectedUser);
                                System.out.println(selectedUser.getFirstName() + " " + selectedUser.getLastName() + " has been added as a friend.");

                                // Remove the added friend from the list and valid IDs
                                matchingUsers.remove(selectedUser);
                                validIDs.remove((Integer) selectedUser.getId());

                                actionValid = true; // Exit inner loop
                                break;

                            case 2: // Skip user
                                System.out.println("User not added as a friend.");
                                actionValid = true; // Exit inner loop
                                break;

                            case 3: // Go back to the list
                                System.out.println("\nReturning to the list of users...");
                                actionValid = true; // Exit inner loop
                                break;

                            default:
                                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                                break;
                        }
                    }
                } else {
                    System.out.println("\nPlease ensure the ID given is valid.");
                }
            } catch (Exception e) {
                System.out.println("\nPlease ensure the input is valid.");
                scanner.nextLine(); // Clear scanner after invalid input
            }
        }

        if (matchingUsers.isEmpty()) {
            System.out.println("No more users left to select from.");
        }
    }

    /*

    public User getFriendRecs() {
        ArrayList<String> InitialRecomFriends = new ArrayList<>();
        ArrayList<User> recommendedFriends = new ArrayList<>();
        user.getInterests().positionIterator();
        for (int i = 0; i < user.getInterests().getLength(); ++i) {
            InitialRecomFriends.add((ud.getInterestManager().retrieveInterestBST(user.getInterests().getIterator())).inOrderString());
            user.getInterests().advanceIterator();
        }


        // Purpose:
        // Friends by interest -- > 
        for (String recUsers : InitialRecomFriends) {
            Scanner readUsers = new Scanner(recUsers);
            while (readUsers.hasNext()) {
                String firstName = readUsers.next();
                String lastName = readUsers.next();
                ArrayList<User> tempUser = ud.findUsersByName(firstName, lastName);
                //recommendedFriends.add(tempUser);
                //if (!user.equals(tempUser) && !recommendedFriends.contains(tempUser) && tempUser != null)
            }
        }

        return null;
    }
    */
}
