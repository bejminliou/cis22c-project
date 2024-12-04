package data;

import java.util.ArrayList;
import java.util.Comparator;

import util.BST;
import util.Graph;
import util.HashTable;
import util.LinkedList;

/**
 * UserDirectory.java
 * UserDirectory maintains access and storage of all users in the system.
 * Provides access for searching users by name or username.
 *
 * @author Benjamin Liou
 * @author Kenneth Garcia
 * @author Kevin Young
 * CIS 22C, Course Project
 */
public class UserDirectory {
    private final ArrayList<User> usersAL;
    private final BST<User> usersBST;
    private final Graph friendNetwork;
    private final HashTable<String> loginTable;
    public final InterestManager interestManager;

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
     * @param usersAL       an ArrayLIst of users
     * @param usersBST      a BinarySearchTree of users
     * @param friendNetwork a Graph containing the friend connections between Users
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
        int numUsers = usersAL.size();
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

        ArrayList<User> results = new ArrayList<>();
        String orderedStr = usersBST.inOrderString();
        // if no users in BST
        if (orderedStr == null || orderedStr.isEmpty()) {
            return results;
        }

        // adding users with matching name to results
        for (User user : usersAL) {
            if (user.getFirstName().equalsIgnoreCase(firstName) && user.getLastName().equalsIgnoreCase(lastName)) {
                results.add(user);
            }
        }
        return results;
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
     * Add a new User to usersAL, usersBST, and loginTable.
     *
     * @param user the user to add to the directory
     * @see util.BST#insert for insertion
     * @see #nameComparator for ordering
     */
    public void addUser(User user) {
        usersAL.add(user);
        usersBST.insert(user, nameComparator);
        addAuthNewUser(user);
    }

    /**
     * Add a new User into the UserDirectory with their username,
     * password, firstName, and lastName.
     *
     * @param username  Username for the new user
     * @param password  password for the new user
     * @param firstName first name of the user
     * @param lastName  last name of the user
     * @return true if registration successful, false if given username already exists
     */
    public boolean addUser(String username, String password, String firstName, String lastName) {
        if (findUserByUsername(username) != null) {
            return false;
        }

        // creating new user
        User newUser = new User();
        newUser.setusername(username);
        newUser.setPassword(password);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setId(usersAL.size());

        addUser(newUser);
        return true;
    }

    /**
     * Remove a User from usersAL, usersBST, and loginTable.
     *
     * @param user the user to remove from the directory
     * @see util.BST#remove for removal
     * @see #nameComparator for ordering
     */
    public void removeUser(User user) {
        usersAL.remove(user.getId());
        usersBST.remove(user, nameComparator);
        loginTable.delete(getLoginKey(user.getFirstName(), user.getLastName()));
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
     * Authenticate a User by checking if their login key exists in loginTable.
     *
     * @param user the existing user to authenticate
     * @return true if User's login key already exists in loginTable, false otherwise
     * @throws IllegalArgumentException if user, username, or password is null
     * @see #getLoginKey for credential format
     * @see util.HashTable#contains for credential verification
     */
    public boolean getUserAuthStatus(User user) {
        // precondition for user object
        if (user == null) {
            throw new IllegalArgumentException("Auth.java getUserAuthStatus(): User cannot be null");
        }

        String username = user.getUsername();
        String password = user.getPassword();
        // preconditions for user's name and password
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Auth.java getUserAuthStatus(): Username cannot be null or empty");
        }
        if (password == null) {
            throw new IllegalArgumentException("Auth.java getUserAuthStatus(): Password cannot be null");
        }

        String loginKey = getLoginKey(user.getUsername(), user.getPassword());
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
     * Returns a String containing all the Users and their data
     * that is currently in UserDirectory.
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
