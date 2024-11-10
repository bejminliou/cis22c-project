package main.java.data;

import main.java.util.*;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password; // Store hashed password? unknown if plain text allowed
    private String city;
    private BST<User> friends; // BST of friends
    private LinkedList<String> interests; // Or LinkedList<Interest>

    // Constructor, getters, and setters
    // ... (Implementation omitted for brevity)

    public void addFriend(User friend) {
        friends.insert(friend);  // Add friend to BST
    }

    public void removeFriend(User friend) {
        friends.delete(friend);
    }

    // ... other methods as needed
}