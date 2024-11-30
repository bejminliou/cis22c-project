package data;

import util.HashTable;
import util.KeyValuePair; // (?) req 2 is misleading

/**
 * Manage user authentication using a hash table
 * The hash table uses the combined username+password as the key for login
 *
 * @author Benjamin Liou
 * @author Kenneth Garcia
 * @see model.Account for high-level account management
 * @see data.UserDirectory for user data storage
 * @see util.HashTable
 * CIS 22C, Course Project
 */
public class Auth {
    private HashTable<String> loginTable;
    private static final int INITIAL_TABLE_SIZE = 10;
    private UserDirectory userDirectory;

    /**
     * Create a new Auth instance with an empty login table
     *
     * @param userDirectory The user directory to validate against
     * @throws IllegalArgumentException if userDirectory is null
     * @see data.UserDirectory for user validation
     * @see util.HashTable#HashTable(int) for table initialization
     */
    public Auth(UserDirectory userDirectory) {
        if (userDirectory == null) {
            throw new IllegalArgumentException("User directory cannot be null");
        }
        this.loginTable = new HashTable<>(INITIAL_TABLE_SIZE);
        this.userDirectory = userDirectory;
    }

    /**
     * Create a combined key from username and password
     *
     * @param username The username to combine
     * @param password The password to combine
     * @return The combined login key
     * @throws IllegalArgumentException if either parameter is null
     * @see #register for key usage in registration
     * @see #authenticate for key usage in authentication
     */
    private String createLoginKey(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password cannot be null");
        }
        return username + ":" + password;
    }

    /**
     * Register a user's credentials in the authentication hash table
     * The combined username:password string acts as the key
     *
     * @param username The username to register (must be unique)
     * @param password The password to register
     * @return true if registration successful, false if username exists
     * @see data.UserDirectory#findUserByUsername(String) for username validation
     * @see data.UserDirectory#addUser(User) for user storage
     * @see #createLoginKey(String, String) for key generation
     */
    public boolean register(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        if (userDirectory.findUserByUsername(username) != null) {
            return false;
        }

        String loginKey = createLoginKey(username, password);
        if (loginTable.contains(loginKey)) {
            return false;
        }

        User newUser = new User();
        newUser.setUserName(username);
        newUser.setPassword(password);
        userDirectory.addUser(newUser);

        loginTable.add(loginKey);

        return true;
    }

    /**
     * Authenticate a user by checking if the username password combination exists
     *
     * @param username The username to verify
     * @param password The password to verify
     * @return true if credentials are valid, false otherwise
     * @throws IllegalArgumentException if username or password is null
     * @see #createLoginKey for credential format
     * @see util.HashTable#contains for credential verification
     */
    public boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password cannot be null");
        }

        String loginKey = createLoginKey(username, password);
        return loginTable.contains(loginKey);
    }

    /**
     * Register an existing user's credentials in the hash table
     *
     * @param user The user to register (must have username and password set)
     * @return true if registration successful, false if user exists or invalid
     * @throws IllegalArgumentException if user is null or missing credentials
     * @see data.User#getUserName for username retrieval
     * @see data.User#getPassword for password retrieval
     * @see #createLoginKey for credential storage format
     */
    public boolean registerUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return register(user.getUserName(), user.getPassword());
    }

    /**
     * Check if a username is registered
     *
     * @param username The username to check
     * @return true if username exists, false otherwise
     * @see data.UserDirectory#findUserByUsername(String) for user lookup
     */
    public boolean isUserRegistered(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        return userDirectory.findUserByUsername(username) != null;
    }

    /**
     * Get the number of registered login combinations
     *
     * @return The number of registered users
     * @see util.HashTable#getNumElements for count retrieval
     */
    public int getRegisteredUserCount() {
        return loginTable.getNumElements();
    }


    /**
     * Hashs existing users to the HashTable (SHOULD ONLY BE USED AT START OF APP)
     *
     * @param key a string of the username and password separated by a ':'
     * @return if the User was added to the Hashtable, verified by an increase in table size
     */
    public boolean addExisitingUser(String key) {
        int amount = getRegisteredUserCount();
        loginTable.add(key);
        return amount != getRegisteredUserCount();

    }
}
