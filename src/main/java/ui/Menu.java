package ui;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.InputMismatchException;
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
            choiceStr = scanner.nextLine();

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
            }
        }

        // get user's username and password
        System.out.print("Please enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Please enter your password: ");
        String password = scanner.nextLine();

        if (login) {
            // authenticate given credentials
            boolean authenticate = ud.getCredAuthStatus(username, password);

            if (authenticate) { // if credentials match
                user = ud.findUserByUsername(username);
                System.out.print("\nWelcome " + user.getUsername() + "!");
            } else { // if no matching credentials
                System.out.println("Your username or password is incorrect. Returning to login menu.\n");
                loginMenu();
            }
        }

        if (createAccount) {
            // check to ensure credentials have not been registered
            boolean credentialsStatus = ud.getCredAuthStatus(username, password);

            // if credentials are new
            if (!credentialsStatus) {
                // create new User with username and password
                this.user = new User();
                user.setUsername(username);
                user.setPassword(password);

                // try adding user to UserDirectory
                boolean userAdded = ud.addNewUser(this.user);

                if (!userAdded) { // if failed to add user
                    loginMenu();
                } else {
                    // input remaining user info
                    System.out.println("\nLet's finish setting up your account:");
                    System.out.print("Enter your first name: ");
                    user.setFirstName(scanner.nextLine()); // set first name
                    System.out.print("Enter your last name: ");
                    user.setLastName(scanner.nextLine()); // set last name
                    System.out.print("Enter your city: ");
                    user.setCity(scanner.nextLine()); // set city

                    // get user interests
                    System.out.println("\nEnter one of your interests followed by the enter key.");
                    do {
                        // input Interest
                        System.out.print("Enter your interest or \"0\" to stop: ");
                        String interestName = scanner.nextLine();

                        // check if user quit
                        if (interestName.equals("0")) {
                            break;
                        }

                        // add Interest to user
                        ud.getInterestManager().addUserToInterest(interestName, user);
                        user.addInterest(interestName);
                    } while (true);
                    System.out.println("Finished entering your interests.");

                    // print welcome message
                    System.out.print("\nYour account has successfully been created, welcome "
                            + user.getUsername() + "!");
                    printUserProfile(user);
                }
            } else { // credentials already used
                System.out.println("\nAn account has already been made with your username and/or password. " +
                        "Please login or choose a different username.\n");
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
            System.out.println("\nMain Menu:" +
                    "\n0. Quit" +
                    "\n1. View Your Friends" +
                    "\n2. Add New Friends");
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
            System.out.println("\nView Friends Menu:" +
                    "\n0. Return to Main Menu" +
                    "\n1. View all your friends and their profiles" +
                    "\n2: View a list friends sorted by name" +
                    "\n3: Search for a friend");
            System.out.print("Enter your choice: ");

            // get user input
            try {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 0:
                        return;
                    case 1:
                        if (user.getFriendCount() == 0) { // if user's friend list is empty
                            System.out.println("\nYou do not have any friends to display! " +
                                    "Returning to View Friends Menu.");
                            break;
                        }

                        System.out.println("\nHere are all of your friends' profiles:");

                        // print the profile of all friends
                        ArrayList<Integer> friendIds = user.getFriendIds();
                        for (int friendId : friendIds) {
                            printUserProfile(ud.getUsersAL().get(friendId - 1));
                        }
                        break;
                    case 2:
                        if (user.getFriendCount() == 0) { // if user's friend list is empty
                            System.out.println("\nYou do not have any friends to display! " +
                                    "Returning to View Friends Menu.");
                            break;
                        }
                        displayFriends();
                        break;
                    case 3:
                        searchFriendByName();
                        break;
                    default: // invalid choice
                        System.out.println("Invalid input. Please enter a valid option (0, 1, 2, or 3).");
                        scanner.nextLine(); // clear invalid choice
                }
            } catch (Exception e) { // invalid input
                System.out.println("Invalid input. Please enter a valid option (0, 1, 2, or 3).");
                scanner.nextLine(); // clear invalid input
            }
        }
    }

    /**
     * Displays the current friends of this User.
     */
    private void displayFriends() {
        String result = ud.findUserByUsername(user.getUsername()).getFriends().inOrderString();
        System.out.println("\nHere are your current friends:");
        System.out.println(result.trim());
    }

    /**
     * Prints the details of a given user.
     *
     * @param user the User to print
     */
    private void printUserProfile(User user) {
        System.out.println("\n" + user.getFirstName() + " " + user.getLastName() + "'s Profile:");

        // print name, ID, and city of friend
        System.out.println("Name: " + user);
        System.out.println("User ID: " + user.getId());
        System.out.println("City: " + user.getCity());


        // print interests of friend as a list separated by commas
        LinkedList<String> interests = user.getInterests();
        interests.positionIterator();
        System.out.print("Interests (" + interests.getLength() + "): ");

        if (!(interests.getLength() == 0)) { // if interests are not empty
            interests.positionIterator();
            for (int i = 0; i < interests.getLength() - 1; i++) {
                System.out.print(interests.getIterator() + ", ");
                interests.advanceIterator();
            }
            System.out.print(interests.getIterator());
        }

        System.out.println(); // for newline when there's 0 Interests to print
    }

    /**
     * Searches for a user's friend based on a given first and last name.
     */
    private void searchFriendByName() {
        String nameOfFriend, firstNameOfFriend = "", lastNameOfFriend = "";

        // get first and last name of user to search
        System.out.println("\nSearching friends by name:");
        System.out.print("Enter the full name of the friend (first name + last name): ");
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
            System.out.print("\nCould not find matching friend." +
                    "\nEnter 1 to retry search by name or enter any other key to return to the View Friends Menu: ");
            if (scanner.nextInt() == 1) {
                searchFriendByName(); // retry search
            }
        } else { // matching friend found
            printUserProfile(matchingFriend);
            System.out.print("\nEnter 1 to remove this friend or enter any other key to return " +
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
                        System.out.print("\nEnter 1 to add this user as your friend, or any other key to continue" +
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
                    System.out.print("\nEnter 1 to search another name or any other key to return to Main Menu: ");
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
            System.out.println("\nThere were no users that match the given name.");
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
            System.out.println("\nNo Users share that interest! Returning to Main Menu.");
            return;
        }

        // get BST<User> containing Users who share the Interest
        BST<User> usersWithIterestBST = ud.getInterestManager().retrieveInterestBST(interestName);

        // remove this user from BST to avoid printing themselves
        usersWithIterestBST.remove(user, ud.getNameComparator());

        // convert interestBST to ArrayList<String>
        String BSTInOrderStr = usersWithIterestBST.inOrderString();
        String[] splitArray = BSTInOrderStr.split("\n");
        ArrayList<String> usersWithInterest = new ArrayList<>(Arrays.asList(splitArray));

        do {
            try {
                System.out.println("\nHere are some Users who share the given interests:");
                // print Users who have Interest
                for (int i = 0; i < usersWithInterest.size(); i++) {
                    System.out.println((i + 1) + ". " + usersWithInterest.get(i));
                }

                System.out.print("Enter 0 to return to Main Menu or the index (1-" + usersWithInterest.size() + ") " +
                        "the person whose profile you'd like to view: ");
                int index = Integer.parseInt(scanner.nextLine());

                if (index == 0) {
                    return;
                }

                if (index > 0 && index <= usersWithInterest.size()) { // valid ID selected
                    // get first and lastName name of selected User
                    String selectedUser = usersWithInterest.get(index - 1);
                    String[] splitArray2 = selectedUser.split(" ");
                    String firstName = splitArray2[0];
                    String lastName = splitArray2[1];

                    // get tempUser from interestBST using first and last name
                    User tempUser = new User();
                    tempUser.setFirstName(firstName);
                    tempUser.setLastName(lastName);
                    tempUser = usersWithIterestBST.search(tempUser, ud.getNameComparator());

                    // print tempUser
                    printUserProfile(tempUser);

                    // prompt this User to add friend or return
                    System.out.print("\nEnter 1 to add as friend or enter any other key to return to the " +
                            "previous menu: ");
                    String choice = scanner.nextLine();

                    if (choice.equalsIgnoreCase("1")) {
                        System.out.println("Successfully added " + tempUser.getFirstName() + " "
                                + tempUser.getLastName() + " as a friend!");
                        user.addFriend(tempUser);
                        displayFriends();
                        break;
                    }
                } else { // invalid ID given
                    System.out.println("Invalid index. Please try again.");
                }
            } catch (InputMismatchException e) { // invalid input
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

                // check if there's no recommended friends
                if (recommended.isEmpty()) {
                    System.out.println("\nSorry we don't have any friend recommendations for you at this time." +
                            " Returning to Main Manu.");
                    return;
                }

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
                scanner.nextLine(); // clear input

                // return to main menu
                if (index == 0) {
                    break;
                }

                // invalid input given
                if (!(index > 0) || !(index <= recommended.size())) {
                    System.out.println("Invalid index given. Please try again.");
                    continue;
                }

                // print selectedUser profile
                User selectedUser = recommended.get(index - 1);
                printUserProfile(selectedUser);

                System.out.print("\nEnter 1 to add as friend or enter any other key to return " +
                        "to the recommended friends: ");
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
