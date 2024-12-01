package model;

import data.*;

/**
 * Manage user accounts and authentication in the system
 * Uses Auth for login verification and UserDirectory for user storage
 *
 * @author Benjamin Liou
 * @author Kenneth Garcia
 * @author Kevin Young
 * @see data.Auth for authentication logic
 * @see data.UserDirectory for user data storage
 * CIS 22C, Course Project
 */
public class Account {
    private final Auth auth;

    /**
     * Create a new Account manager with Auth and UserDirectory
     *
     * @param ud the populated UserDirectory
     * @see data.Auth#Auth(UserDirectory) for authentication setup
     */
    public Account(UserDirectory ud) {
        this.auth = new Auth(ud);
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
     * @param ud   the UserDirectory containing all users' data
     * @return true if account creation successful, false if username already exists
     * @see data.Auth#registerUser(User) for user registration
     * @see data.User for user data structure
     */
    public boolean createAccount(User user, UserDirectory ud) {
        if (user == null || user.getUserName() == null || user.getPassword() == null) {
            return false;
        }
        return auth.registerUser(user, ud);
    }

    /**
     * Check if a username is already registered
     *
     * @param username The username to check
     * @param ud       the UserDirectory containing all users' data
     * @return true if username exists, false otherwise
     * @see data.Auth#isUserRegistered(String) for username verification
     */
    public boolean isUsernameTaken(String username, UserDirectory ud) {
        return auth.isUserRegistered(username, ud);
    }

    /**
     * Getter for the Auth
     *
     * @return auth the Authenticator Object
     */
    public Auth getAuth() {
        return auth;
    }
}