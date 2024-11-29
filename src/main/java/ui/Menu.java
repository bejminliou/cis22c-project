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
                this.userName = user.getUserName();
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
                //int amount = getNextInt(scanner);
                while (true) {
                    if (scanner.hasNextInt()) {
                        int amount = scanner.nextInt();
                        for (int i = 0; i < amount; ++i) {
                            System.out.println((i + 1) + ". Add Interest: ");
                            user.addInterest(getNext(scanner));
                        }
                        break;
                    } else {
                        System.out.println("Please enter a number greater than 0.\nHow many interests would you like to add?");
                        scanner.next();
                    }
                }

                String id = "";
                while (id.length() != 2) {
                    System.out.println("Please set an id of 2 digits.");
                    id = getNext(scanner);
                }
                user.setId(Integer.parseInt(id));
                System.out.println("You are all set!");
                System.out.println("\nWelcome " + user.getUserName() + "!");
                this.userName = user.getUserName();

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


    public void mainMenu(){ //maybe add friend suggestion
        System.out.println("Please Enter number:\n1. View your friends\n2. Make new friends\n3. Quit");
        while (true) {
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                if (choice == 1) {
                    viewMyFriends();
                } else if (choice == 2) {
                    //add friend
                }else if(choice == 3) {
                    System.exit(0);
                }else {
                    System.out.println("Please enter a number 1, 2, or 3");
                }
                break;
            } else {
                System.out.println("Please enter a number 1, 2, or 3");
                scanner.next();
            }
        }
    }

    public void viewMyFriends(){
        System.out.println("Enter choice\n1: View all friends sorted by name\n2: Search for a friend");
        scanner = new Scanner(System.in);
        while (true) {
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                if (choice == 1) {
                    if (account.getUser(this.userName).getFriendCount() == 0) {
                        System.out.println("You currently do not have any friends!");
                        mainMenu();

                    }else{
                        displayFriends();
                    }
                } else if (choice == 2) {
                    searchFriends();

                }else {
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

    private void searchFriends(){
        scanner = new Scanner(System.in);
        System.out.println("Enter the first name of the friend");
        String firstNameOfFriend = scanner.next();
        System.out.println("Enter the last name of the friend");
        String lastNameOfFriend = scanner.next();
        User friend = user.searchFriendByName(firstNameOfFriend, lastNameOfFriend);
        if(friend == null){
            System.out.println("Cannot find friend\nEnter 1 to Retry\nEnter anything else to quit to the previous menu");
            if(scanner.nextInt() == 1){
                searchFriends();
            }else{
                viewMyFriends();
            }
        }else{
            System.out.println("Enter 1 to view this user's full profile or 2 to remove this friend");
            int choice = scanner.nextInt();
            if(choice == 1){
                System.out.println("Name: " + friend.toString());
                System.out.println("Id: " + friend.getId());
                System.out.println("City: " + friend.getCity());
                LinkedList<String> friendInterest = friend.getInterests();
                friendInterest.positionIterator();
                System.out.println("Interests" + " (" + friendInterest.getLength() +"):");
                for(int i = 0; i < friendInterest.getLength(); i++){
                    System.out.println(friendInterest.getIterator());
                    friendInterest.advanceIterator();

                }
                System.out.println();
                viewMyFriends();

            }else if(choice == 2){
                user.removeFriend(friend);
                System.out.println("Successfully removed, your new friends list is now:");
                displayFriends();
            }
        }

    }

    public void addFriend(){

    }


}
