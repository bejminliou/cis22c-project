package data;

import util.HashTable;

/**
 * Manage user authentication using a HashTable.
 * The HashTable uses the combined username and password as the key for login.
 *
 * @author Benjamin Liou
 * @author Kevin Young
 * @see model.Account for high-level account management
 * @see data.UserDirectory for user data storage
 * @see util.HashTable
 * CIS 22C, Course Project
 */
public class Auth {
    private final HashTable<String> loginTable;

    // Constructors

    /**
     * Default constructor creates a new Auth instance with loginTable
     * sized based on the number of users + the headroom.
     *
     * @param numUsers the number of Users that currently exist in the program
     * @see data.UserDirectory for user validation
     * @see util.HashTable#HashTable(int) for table initialization
     */
    public Auth(int numUsers) {
        final int NUM_USERS_HEADROOM = 10;

        this.loginTable = new HashTable<>(numUsers + NUM_USERS_HEADROOM);
    }

    // Accessors

    /**
     * Return the number of authenticated login combinations.
     *
     * @return the number of authenticated users
     * @see util.HashTable#getNumElements for count retrieval
     */
    private int getAuthUserCount() {
        return loginTable.getNumElements();
    }

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
     * @return true if the given username and password
     * already exist (as a login key) in loginTable, false otherwise
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

        String username = user.getUserName();
        String password = user.getPassword();
        // preconditions for user's name and password
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Auth.java getUserAuthStatus(): Username cannot be null or empty");
        }
        if (password == null) {
            throw new IllegalArgumentException("Auth.java getUserAuthStatus(): Password cannot be null");
        }

        String loginKey = getLoginKey(user.getUserName(), user.getPassword());
        return loginTable.contains(loginKey);
    }

    // Additional Methods

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

        String username = user.getUserName();
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

}
