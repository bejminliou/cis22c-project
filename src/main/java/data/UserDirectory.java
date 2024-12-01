package data;

import util.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * UserDirectory maintains a BST of all users in the system,
 * allowing for searching users by name with support for duplicates
 *
 * @author Benjamin Liou
 * CIS 22C, Course Project
 */
public class UserDirectory {
    private BST<User> users;
    private Map<String, User> usernameMap; // For quick username lookups
    private final Comparator<User> nameComparator = (u1, u2) -> {
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

    public UserDirectory() {
        users = new BST<>();
        usernameMap = new HashMap<>();
    }

    /**
     * Adds a new user to both the BST and username lookup map
     *
     * @param user The user to add to the directory
     * @see util.BST#insert for insertion
     * @see #nameComparator for ordering
     * @see data.User#getUserName for username uniqueness
     */
    public void addUser(User user) {
        users.insert(user, nameComparator);
        if (user.getUserName() != null) {
            usernameMap.put(user.getUserName(), user);
        }
    }

    /**
     * Removes a user from both the BST and username lookup map
     *
     * @param user The user to remove from the directory
     * @see util.BST#remove for removal
     * @see #nameComparator for ordering
     * @see data.User#getUserName for lookup
     */
    public void removeUser(User user) {
        users.remove(user, nameComparator);
        if (user.getUserName() != null) {
            usernameMap.remove(user.getUserName());
        }
    }

    /**
     * Returns all users that match the given name
     *
     * @param firstName First name to search for (case insensitive)
     * @param lastName  Last name to search for (case insensitive)
     * @return List of users with exactly matching names
     * @see data.User#getFirstName
     * @see data.User#getLastName
     */
    public ArrayList<User> findUsersByName(String firstName, String lastName) {
        ArrayList<User> results = new ArrayList<>();
        String inOrder = users.inOrderString();
        if (inOrder == null || inOrder.isEmpty()) {
            return results;
        }

        String[] userEntries = inOrder.split("\n");
        for (String entry : userEntries) {
            if (entry == null || entry.isEmpty())
                continue;

            User user = users.search(parseUser(entry), nameComparator);
            if (user != null &&
                    user.getFirstName().equalsIgnoreCase(firstName) &&
                    user.getLastName().equalsIgnoreCase(lastName)) {
                results.add(user);
            }
        }
        return results;
    }

    /**
     * Parses a user from the BST's string representation
     *
     * @param entry The string entry to parse
     * @return A User object with the parsed first and last name
     * @see util.BST#inOrderString for the format of the string representation
     * @see data.User#setFirstName for setting parsed first name
     * @see data.User#setLastName for setting parsed last name
     */
    public User parseUser(String entry) {
        User temp = new User();
        String[] parts = entry.trim().split(" ", 2);
        if (parts.length >= 2) {
            temp.setFirstName(parts[0]);
            temp.setLastName(parts[1]);
        } else if (parts.length == 1) {
            temp.setFirstName(parts[0]);
            temp.setLastName("");
        }
        return temp;
    }

    /**
     * Finds a user by their unique username in the system
     * Uses optimized username map for O(1) lookup instead of searching the BST
     *
     * @param username The username to search for ((?) case-sensitive)
     * @return The user with the given username, or null if not found
     * @see data.User#getUserName
     */
    public User findUserByUsername(String username) {
        if (username == null) return null;
        return usernameMap.get(username);
    }

    public int getUserCount() {
        return users.getSize();
    }

    /**
     * Get all users in the directory
     *
     * @return ArrayList of all users in the directory
     */
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(usernameMap.values());
    }

    /**
     * Get the name comparator used for ordering users
     *
     * @return the comparator used for comparing users by name
     */
    public Comparator<User> getNameComparator() {
        return nameComparator;
    }

    /**
     * Register a new user with the directory
     *
     * @param username  Username for the new user
     * @param password  Password for the new user
     * @param firstName First name of the user
     * @param lastName  Last name of the user
     * @return true if registration successful, false if username already exists
     */
    public boolean registerUser(String username, String password, String firstName, String lastName) {
        if (findUserByUsername(username) != null) {
            return false;
        }

        User newUser = new User();
        newUser.setUserName(username);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setId(usernameMap.size() + 1);

        addUser(newUser);

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n===========================\n");// lol
        sb.append("  User Directory Contents\n");
        sb.append("===========================\n\n");
        sb.append(String.format("Total Users: %d\n\n", getUserCount()));

        if (getUserCount() == 0) {
            sb.append("No users in directory.");
            return sb.toString();
        }

        for (User user : usernameMap.values()) {
            sb.append(String.format("User ID: %d\n", user.getId()));
            sb.append(String.format("Name: %s %s\n",
                    user.getFirstName() != null ? user.getFirstName() : "(no first name)",
                    user.getLastName() != null ? user.getLastName() : "(no last name)"));
            sb.append(String.format("Username: %s\n",
                    user.getUserName() != null ? user.getUserName() : "(no username)"));
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
