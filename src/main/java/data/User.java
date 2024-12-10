package data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import util.BST;
import util.LinkedList;

/**
 * User.java
 * User class that represents a User in the system with their
 * personal information and friend connections.
 * Users are comparable by their ID and can be searched by name.
 *
 * @author Benjamin Liou
 * @author Kevin Young
 * @author Rolen Louie
 * @author Yukai Qiu
 * @author Kenneth Garcia
 * @author Tu Luong
 * CIS 22C, Course Project
 */
public class User implements Comparable<User> {
    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String city;
    private final LinkedList<String> interests;
    private BST<User> friends;
    private List<Integer> friendIds;

    // Constructors

    /**
     * Creates a new User with empty friends and interests lists.
     *
     * @see util.BST#BST() for friends list implementation
     * @see util.LinkedList#LinkedList() for interests list implementation
     */
    public User() {
        friends = new BST<>();
        interests = new LinkedList<>();
        friendIds = new ArrayList<>();
    }

    /**
     * Creates a new User with the given username, along with an empty
     * friends and interests lists.
     *
     * @param username the User's username
     */
    public User(String username) {
        this();
        this.username = username;
    }

    /**
     * Creates a new User with the given username and password,
     * along with an empty friends and interests lists.
     *
     * @param username the User's username
     * @param password the User's password
     */
    public User(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    /**
     * Search for a friend by their full name.
     *
     * @param firstName first name to search for
     * @param lastName  last name to search for
     * @return a User representing the found friend or null if not found
     * @see util.BST#search(Object, Comparator) for search
     */
    public User searchFriendByName(String firstName, String lastName) {
        User searchUser = new User();
        searchUser.firstName = firstName;
        searchUser.lastName = lastName;
        return friends.search(searchUser, UserDirectory.nameComparator);
    }

    // Accessors

    /**
     * Get the username of the User.
     *
     * @return the username of the User
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the password of the User.
     *
     * @return the password of the User
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get the ID of the User.
     *
     * @return the ID of the user
     */
    public int getId() {
        return id;
    }

    /**
     * Get the first name of the User.
     *
     * @return the first name of the User
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get the last name of the User.
     *
     * @return the last name of the User
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Get the city of the User.
     *
     * @return the city of the User
     */
    public String getCity() {
        return city;
    }

    /**
     * Get the LinkedList containing the User's Interests.
     *
     * @return the LinkedList containing all Interests of the User
     * @see util.LinkedList for the list implementation
     */
    public LinkedList<String> getInterests() {
        return interests;
    }

    /**
     * Get the BST containing all friends of the User.
     *
     * @return the friends BST
     * @see util.BST
     */
    public BST<User> getFriends() {
        return friends;
    }

    /**
     * Get the number of friends the User has.
     *
     * @return number of friends
     */
    public int getFriendCount() {
        return friends.getSize();
    }

    /**
     * Get the IDs of the friends of the User.
     *
     * @return the ArrayList of friend IDs connected to the User
     */
    public ArrayList<Integer> getFriendIds() {
        return new ArrayList<>(friendIds);
    }

    // Mutators

    /**
     * Set the username of the User to the given username.
     *
     * @param username the given username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Set the password of the User to the given password.
     *
     * @param password the given password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Set the ID of the User to the given ID.
     *
     * @param id the given id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set the first name of the User to the given first name.
     *
     * @param firstName the given first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Set the last name of the User to the given last name.
     *
     * @param lastName the given last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Set the city of the User to the given city.
     *
     * @param city the given city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Set the list of friend IDs of the User to the given list.
     *
     * @param friendIds the given list of friend IDs
     */
    public void setFriendIds(List<Integer> friendIds) {
        this.friendIds = friendIds;
    }

    /**
     * Sets the User's BST of friends to the given BST.
     *
     * @param friends the given BST of friends
     */
    public void setFriends(BST<User> friends) {
        this.friends = friends;
    }

    /**
     * Adds a new interest to the user's list of interests.
     *
     * @param interest the interest to add
     * @see util.LinkedList#addLast(Object) for interest storage
     */
    public void addInterest(String interest) {
        if (interest != null && !interest.trim().isEmpty()) {
            interests.addLast(interest.trim());
        }
    }

    /**
     * Adds a friend connection between two Users if the friend connection
     * does not already exist.
     *
     * @param friend the user to add as a friend
     * @see util.BST#insert(Object, Comparator) for friend storage
     */
    public void addFriend(User friend) {
        // Don't add if: friend is null, friend is self
        if (friend == null || friend == this) {
            return;
        }

        // Or if already friends using BST
        if (friends.search(friend, (u1, u2) -> Integer.compare(u1.getId(), u2.getId())) != null) {
            return;
        }

        friends.insert(friend, UserDirectory.nameComparator);
        friendIds.add(friend.getId());

        // Add reverse connection if not already present
        if (friend.friends.search(this, (u1, u2) -> Integer.compare(u1.getId(), u2.getId())) == null) {
            friend.friends.insert(this, UserDirectory.nameComparator);
            friend.friendIds.add(this.getId());
        }

    }

    /**
     * Removes a friend connection.
     *
     * @param friend the user to remove from friends
     * @see util.BST#remove(Object, Comparator) for friend removal
     */
    public void removeFriend(User friend) {
        friends.remove(friend, UserDirectory.nameComparator);
        if (friend != null) {
            friendIds.remove(Integer.valueOf(friend.getId()));
        }
    }

    // Additional Methods

    /**
     * Compare users by their unique IDs.
     *
     * @param other the other user to compare with
     * @return negative if this user's ID is less, positive if greater, 0 if equal
     * @see #getId() for the ID being compared
     */
    @Override
    public int compareTo(User other) {
        return Integer.compare(this.id, other.id);
    }

    /**
     * Returns a String representation of the User containing their first
     * and last name.
     *
     * @return a String containing the first and last name of the User
     * separated by a space.
     */
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

}
