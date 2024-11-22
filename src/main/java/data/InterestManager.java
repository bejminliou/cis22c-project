package data;

import util.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Manages the system's user-interest relationships.
 * Provides:
 * 1. A HashTable to look up interests by name
 * 2. An ArrayList of BSTs where each index corresponds to an interest's ID and contains users with that interest
 */
public class InterestManager {
    private HashTable<Interest> interestTable;  // For looking up interests by name
    private ArrayList<BST<User>> usersByInterest;  // BST of users at each interest's ID index
    private ArrayList<Interest> allInterests;
    private int nextInterestId;  // For assigning unique IDs to new interests
    
    private final Comparator<User> nameComparator = (u1, u2) -> {
        String lastName1 = u1.getLastName();
        String lastName2 = u2.getLastName();
        String firstName1 = u1.getFirstName();
        String firstName2 = u2.getFirstName();

        if (lastName1 == null) lastName1 = "";
        if (lastName2 == null) lastName2 = "";
        if (firstName1 == null) firstName1 = "";
        if (firstName2 == null) firstName2 = "";

        int lastNameComparison = lastName1.compareToIgnoreCase(lastName2);
        return lastNameComparison != 0 ? lastNameComparison 
                                     : firstName1.compareToIgnoreCase(firstName2);
    };

    /**
     * Creates a new InterestManager with empty interest table and user BSTs
     */
    public InterestManager() {
        interestTable = new HashTable<>(10);  // resize as needed
        usersByInterest = new ArrayList<>();
        allInterests = new ArrayList<>();
        nextInterestId = 0;
    }

    /**
     * Adds a new interest to the system
     * 
     * @param interestName Name of the interest to add
     * @return The created Interest object, or existing one if already present
     */
    public Interest addInterest(String interestName) {
        if (interestName == null) {
            return null;
        }
        
        // Create Interest object for lookup
        String trimmedName = interestName.trim();
        if (trimmedName.isEmpty()) {
            return null;
        }
        Interest lookupInterest = new Interest(trimmedName, -1);
        
        // Check if interest already exists
        Interest existingInterest = interestTable.get(lookupInterest);
        if (existingInterest != null) {
            return existingInterest;
        }
        
        // Create new interest with next available ID
        Interest newInterest = new Interest(trimmedName, nextInterestId++);
        interestTable.add(newInterest);
        
        // Add to allInterests list
        allInterests.add(newInterest);
        
        // Ensure usersByInterest has space for the new interest's BST
        while (usersByInterest.size() <= newInterest.getId()) {
            usersByInterest.add(new BST<>());
        }
        
        return newInterest;
    }

    /**
     * Associates a user with an interest
     * 
     * @param user The user to add
     * @param interestName Name of the interest (case insensitive)
     * @return true if the user was added to the interest, false if already present
     */
    public boolean addUserToInterest(User user, String interestName) {
        if (user == null || interestName == null) {
            return false;
        }

        Interest interest = interestTable.get(new Interest(interestName, -1));
        if (interest == null) {
            interest = addInterest(interestName);
        }

        BST<User> users = usersByInterest.get(interest.getId());
        User existing = users.search(user, nameComparator);
        
        if (existing != null) {
            return false;
        }

        users.insert(user, nameComparator);
        return true;
    }

    /**
     * Removes a user's association with an interest
     * 
     * @param user The user to remove
     * @param interestName Name of the interest (case insensitive)
     * @return true if the user was removed, false if not found
     */
    public boolean removeUserFromInterest(User user, String interestName) {
        if (user == null || interestName == null) {
            return false;
        }

        Interest interest = interestTable.get(new Interest(interestName, -1));
        if (interest == null || interest.getId() >= usersByInterest.size()) {
            return false;
        }

        BST<User> users = usersByInterest.get(interest.getId());
        User existing = users.search(user, nameComparator);
        
        if (existing == null) {
            return false;
        }

        users.remove(user, nameComparator);
        return true;
    }

    /**
     * Gets all users who share a specific interest
     * 
     * @param interestName Name of the interest to look up (case insensitive)
     * @return ArrayList of users with the interest, empty list if interest not found
     */
    public ArrayList<User> getUsersWithInterest(String interestName) {
        ArrayList<User> results = new ArrayList<>();
        
        Interest interest = interestTable.get(new Interest(interestName, -1));
        if (interest == null || interest.getId() >= usersByInterest.size()) {
            return results;
        }

        BST<User> users = usersByInterest.get(interest.getId());
        
        if (users == null || users.isEmpty()) {
            return results;
        }

        String inOrder = users.inOrderString();
        if (inOrder == null || inOrder.isEmpty()) {
            return results;
        }

        String[] lines = inOrder.split("\n");
        for (String line : lines) {
            if (line == null || line.isEmpty()) {
                continue;
            }
            User dummy = new User();
            String[] parts = line.split(" ");
            if (parts.length >= 2) {
                dummy.setFirstName(parts[0]);
                dummy.setLastName(parts[1]);
                User user = users.search(dummy, nameComparator);
                if (user != null) {
                    results.add(user);
                }
            }
        }

        return results;
    }

    /**
     * Gets all interests in the system
     * 
     * @return ArrayList of all interests
     */
    public ArrayList<Interest> getAllInterests() {
        return new ArrayList<>(allInterests);  // Return copy to prevent mod
    }

    /**
     * Search for potential friends who share a specific interest
     * 
     * @param user The user searching for friends
     * @param interestName Name of the interest to search by (case insensitive)
     * @return ArrayList of users who share the interest, excluding the searching user and existing friends
     */
    public ArrayList<User> searchFriendsByInterest(User user, String interestName) {
        if (user == null || interestName == null) {
            return new ArrayList<>();
        }

        Interest interest = interestTable.get(new Interest(interestName.trim(), -1));
        if (interest == null || interest.getId() >= usersByInterest.size()) {
            return new ArrayList<>();
        }

        BST<User> usersWithInterest = usersByInterest.get(interest.getId());
        ArrayList<User> potentialFriends = new ArrayList<>();

        String userList = usersWithInterest.inOrderString();
        if (userList != null && !userList.isEmpty()) {
            String[] userEntries = userList.split("\n");
            for (String entry : userEntries) {
                if (entry == null || entry.isEmpty()) continue;
                
                User dummy = new User();
                String[] parts = entry.split(" ");
                if (parts.length >= 2) {
                    dummy.setFirstName(parts[0]);
                    dummy.setLastName(parts[1]);
                    
                    // Search for actual user in BST
                    User foundUser = usersWithInterest.search(dummy, nameComparator);
                    if (foundUser != null && foundUser.getId() != user.getId() && !user.isFriend(foundUser)) {
                        potentialFriends.add(foundUser);
                    }
                }
            }
        }

        return potentialFriends;
    }
}
