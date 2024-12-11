package data;

import java.util.ArrayList;
import java.util.Comparator;

import util.BST;
import util.Graph;
import util.HashTable;
import util.LinkedList;

/**
 * UserDirectory.java
 * UserDirectory class maintains access and storage of all users in the system.
 * Provides access for searching users by name or username.
 *
 * @author Benjamin Liou
 * @author Kevin Young
 * @author Rolen Louie
 * @author Yukai Qiu
 * @author Kenneth Garcia
 * @author Tu Luong
 * CIS 22C, Course Project
 */
public class UserDirectory {
    private final ArrayList<User> usersAL;
    private final BST<User> usersBST;
    private final Graph friendNetwork;
    private final HashTable<String> loginTable;
    private final InterestManager interestManager;
    int numUsers;

    // Comparators

    /**
     * Compares two Users by their first and last names. Returns a negative integer,
     * zero, or a positive integer as the first firstName/lastName is less than,
     * equal to, or greater than the second firstName/lastName.
     */
    public static final Comparator<User> nameComparator = (u1, u2) -> {
        String lastName1 = u1.getLastName();
        String lastName2 = u2.getLastName();
        String firstName1 = u1.getFirstName();
        String firstName2 = u2.getFirstName();

        // Handle null values
        if (lastName1 == null) {
            lastName1 = "";
        }
        if (lastName2 == null) {
            lastName2 = "";
        }
        if (firstName1 == null) {
            firstName1 = "";
        }
        if (firstName2 == null) {
            firstName2 = "";
        }

        int lastNameComparison = lastName1.compareToIgnoreCase(lastName2);
        if (lastNameComparison != 0) {
            return lastNameComparison;
        }
        return firstName1.compareToIgnoreCase(firstName2);
    };

    // Constructors

    /**
     * Initializes UserDirectory with given ArrayList of users, BinarySearchTree
     * of users, and Graph of the friend connections.
     * Authenticates all user credentials by adding them to loginTable.
     *
     * @param usersAL         an ArrayLIst of users
     * @param usersBST        a BinarySearchTree of users
     * @param friendNetwork   a Graph containing the friend connections between Users
     * @param interestManager an InterestManager containing the existing interests and Users who share the interests
     */
    public UserDirectory(ArrayList<User> usersAL, BST<User> usersBST, Graph friendNetwork,
                         InterestManager interestManager) {
        this.usersAL = usersAL;
        this.usersBST = usersBST;
        this.friendNetwork = friendNetwork;
        this.interestManager = interestManager;

        // authenticate all user credentials into the loginTable
        final int NUM_USERS_OFFSET = 10;
        this.numUsers = usersAL.size();
        this.loginTable = new HashTable<>(numUsers + NUM_USERS_OFFSET);
        for (User user : usersAL) {
            loginTable.add(getLoginKey(user.getUsername(), user.getPassword()));
        }
    }

    // Accessors

    /**
     * Get the name comparator used for ordering users.
     *
     * @return the comparator used for comparing users by name
     */
    public Comparator<User> getNameComparator() {
        return nameComparator;
    }

    /**
     * Get the number of users currently in UserDirectory.
     *
     * @return the number of users in UserDirectory
     */
    public int getNumUsers() {
        return usersAL.size();
    }

    /**
     * Get the ArrayList of all Users in UserDirectory.
     *
     * @return the ArrayList of User objects
     */
    public ArrayList<User> getUsersAL() {
        return usersAL;
    }

    /**
     * Get the Graph containing the friend network of all Users in UserDirectory.
     *
     * @return the Graph representing the friend network
     */
    public Graph getFriendNetwork() {
        return friendNetwork;
    }

    /**
     * Get the InterestManager managing the interests of the Users
     * in the UserDirectory.
     *
     * @return the InterestManager in UserDirectory
     */
    public InterestManager getInterestManager() {
        return interestManager;
    }

    /**
     * Returns all users that match the given first and last name (case-insensitive).
     *
     * @param firstName first name to search for
     * @param lastName  last name to search for
     * @return list of users with exactly matching names
     * @throws NullPointerException if firstName or lastName is null
     * @see data.User#getFirstName
     * @see data.User#getLastName
     */
    public ArrayList<User> findUsersByName(String firstName, String lastName) throws NullPointerException {
        // precondition
        if (firstName == null || lastName == null) {
            throw new NullPointerException("UserDirectory.java findUsersByName(): Firstname and/or Lastname " +
                    "cannot be null");
        }

        User userToFind = new User();
        userToFind.setFirstName(firstName);
        userToFind.setLastName(lastName);
        return usersBST.findUsersByName(userToFind, this.getNameComparator());
    }

    /**
     * Finds a user by their unique username in the system.
     *
     * @param username the username to search for
     * @return the user with the given username, or null if not found
     * @throws NullPointerException if given username is null
     * @see User#getUsername()
     */
    public User findUserByUsername(String username) throws NullPointerException {
        //precondition
        if (username == null) {
            throw new NullPointerException("UserDirectory.java findUserByUsername(): Username cannot be null");
        }

        for (User user : usersAL) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    // Mutators

    /**
     * Add a new User into the UserDirectory with their given
     * username, password, firstName, lastName, and city.
     *
     * @param user the User to add
     * @return the new User added to the UserDirectory, or null if username and/or credentials already exist
     */
    public boolean addNewUser(User user) {
        // check for existing username
        if (findUserByUsername(user.getUsername()) != null) {
            System.out.println("\nUsername already taken. Please login or choose a different username.\n");
            return false;
        }

        // authenticate credentials
        boolean foundCreds = getCredAuthStatus(user.getUsername(), user.getPassword());

        if (!foundCreds) { // if credentials are not already found in UserDirectory
            // add User to UserDirectory
            numUsers++;
            user.setId(numUsers);
            usersAL.add(user);
            usersBST.insert(user, nameComparator);
            addAuthNewUser(user);
            return true;
        }

        System.out.println("Username and/or Password already used.");
        return false; // if given username and password already exist
    }

    // Additional methods for loginTable (HashTable) and authentication

    /**
     * Return a login key created by combining a User's username and password.
     *
     * @param username the username to combine
     * @param password the password to combine
     * @return the combined login key
     * @throws IllegalArgumentException if either parameter is null
     * @see #addAuthNewUser for key usage in registration
     */
    private String getLoginKey(String username, String password) {
        // precondition
        if (username == null || password == null) {
            throw new IllegalArgumentException("Auth.java getLoginKey(): Username and/or Password cannot be null");
        }

        // format: "username:password"
        return username + ":" + password;
    }

    /**
     * Authenticate a given pair of username and password by checking if the login key
     * already exists in loginTable.
     *
     * @param username the username to verify
     * @param password the password to verify
     * @return true if the given username and password already exist (as a
     * login key) in loginTable, false otherwise
     */
    public boolean getCredAuthStatus(String username, String password) {
        // precondition
        if (username == null || password == null) {
            throw new IllegalArgumentException("Auth.java getCredAuthStatus(): " +
                    "Username and password cannot be null");
        }

        String loginKey = getLoginKey(username, password);
        return loginTable.contains(loginKey);
    }

    /**
     * Authenticate a new user by adding their login key to the loginTable if
     * their login key does not already exist.
     *
     * @param user the new user to authenticate (must have username and password set)
     * @return true if User's login key has been added to loginTable, false otherwise
     * @throws IllegalArgumentException if user, username, or password is null
     * @see #getLoginKey for credential format
     * @see util.HashTable#contains for credential verification
     */
    public boolean addAuthNewUser(User user) {
        // precondition for user object
        if (user == null) {
            throw new IllegalArgumentException("Auth.java addAuthNewUser(): User cannot be null");
        }

        String username = user.getUsername();
        String password = user.getPassword();
        // preconditions for user's name and password
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Auth.java addAuthNewUser(): Username cannot be null or empty");
        }
        if (password == null) {
            throw new IllegalArgumentException("Auth.java addAuthNewUser(): Password cannot be null");
        }

        // check for login key in loginTable
        String loginKey = getLoginKey(username, password);
        if (loginTable.contains(loginKey)) {
            System.out.println("Error: could not register user as the given username and password have already " +
                    "been registered.\n");
            return false;
        }

        // authenticate new user
        loginTable.add(loginKey);
        return true;
    }

    // Additional Methods for friendNetwork (Graph)

    /**
     * Updates the friendNetwork by adding a friend to the connected edges
     * of currUser.
     *
     * @param currUser the current user
     * @param friend   the friend to add to currUser's connections
     */
    public void addFriendConnection(User currUser, User friend) {
        if (friendNetwork.getAdjacencyList(currUser.getId()).findIndex(friend.getId()) == -1) { // (-1 for not found)
            friendNetwork.addUndirectedEdge(currUser.getId(), friend.getId());
        }
    }

    // Additional Methods

    /**
     * Returns a String containing all the Users and their corresponding data
     * currently in UserDirectory.
     *
     * @return a String containing the contents of the UserDirectory
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n===========================\n");// lol
        sb.append("  User Directory Contents\n");
        sb.append("===========================\n\n");
        sb.append(String.format("Total Users: %d\n\n", getNumUsers()));

        if (getNumUsers() == 0) {
            sb.append("No users in directory.");
            return sb.toString();
        }

        for (User user : usersAL) {
            sb.append(String.format("User ID: %d\n", user.getId()));
            sb.append(String.format("Name: %s %s\n",
                    user.getFirstName() != null ? user.getFirstName() : "(no first name)",
                    user.getLastName() != null ? user.getLastName() : "(no last name)"));
            sb.append(String.format("Username: %s\n",
                    user.getUsername() != null ? user.getUsername() : "(no username)"));
            sb.append(String.format("City: %s\n",
                    user.getCity() != null ? user.getCity() : "(no city)"));

            sb.append("Interests: ");
            LinkedList<String> interests = user.getInterests();
            if (interests.getLength() == 0) {
                sb.append("(none)");
            } else {
                interests.positionIterator();
                while (!interests.offEnd()) {
                    sb.append(interests.getIterator());
                    interests.advanceIterator();
                    if (!interests.offEnd()) {
                        sb.append(", ");
                    }
                }
            }

            sb.append(String.format("\nFriend Count: %d\n", user.getFriendCount()));
            sb.append("---------------------\n");
        }

        return sb.toString();
    }
}
