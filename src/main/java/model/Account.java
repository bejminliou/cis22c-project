package model;

import data.*;
import util.HashTable;

/**
 * Manage user accounts and authentication in the system
 * Uses Auth for login verification and UserDirectory for user storage
 * @see data.Auth for authentication logic
 * @see data.UserDirectory for user data storage
 */
public class Account {
    private Auth auth;
    private UserDirectory userDirectory;

    /**
     * Create a new Account manager with Auth and UserDirectory
     * 
     * @see data.Auth#Auth(UserDirectory) for authentication setup
     * @see data.UserDirectory#UserDirectory() for user storage setup
     */
    public Account() {
        this.userDirectory = new UserDirectory();
        this.auth = new Auth(userDirectory);
    }

    /**
     * Authenticate a user with the given credentials
     * 
     * @param username The username to check
     * @param password The password to verify
     * @return true if authentication successful, false otherwise
     * @see data.Auth#authenticate(String, String) for authentication logic
     */
    public boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        return auth.authenticate(username, password);
    }

    /**
     * Create a new user account
     * 
     * @param user The user to create an account for
     * @return true if account creation successful, false if username already exists
     * @see data.Auth#registerUser(User) for user registration
     * @see data.User for user data structure
     */
    public boolean createAccount(User user) {
        if (user == null || user.getUserName() == null || user.getPassword() == null) {
            return false;
        }
        return auth.registerUser(user);
    }

    /**
     * Get a user by their username
     * 
     * @param username The username to look up
     * @return The User object if found, null otherwise
     * @see data.UserDirectory#findUserByUsername(String) for user lookup
     */
    public User getUser(String username) {
        return userDirectory.findUserByUsername(username);
    }

    /**
     * Check if a username is already registered
     * 
     * @param username The username to check
     * @return true if username exists, false otherwise
     * @see data.Auth#isUserRegistered(String) for username verification
     */
    public boolean isUsernameTaken(String username) {
        return auth.isUserRegistered(username);
    }
}