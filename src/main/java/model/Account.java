package main.java.model;

// import main.java.util.*; // for HashTable
import java.util.Hashtable; // ?ambiguous allowed?
import main.java.data.*;

public class Account {
    private Hashtable<String, User> accounts; // Username -> User

    public Account() {
        accounts = new Hashtable<>();
    }

    public boolean login(String username, String password) {
        User user = accounts.get(username);
        if (user != null && user.getPassword().equals(hashPassword(password))) {
            return true;
        }
        return false;
    }


    public void createAccount(User user) {
        user.setPassword(hashPassword(user.getPassword())); // Hash the password
        accounts.put(user.getUserName(), user);
    }

    // Hashing function (replace with a more secure algorithm like bcrypt)
    private String hashPassword(String password) {
        return password; // Placeholder - DO NOT USE IN PRODUCTION
    }

}