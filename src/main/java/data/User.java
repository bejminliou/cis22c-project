package data;

import util.*;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the system with their personal information and friend
 * connections
 * Users are comparable by their ID and can be searched by name
 *
 * @author Benjamin Liou
 * @author Kevin Young
 * CIS 22C, Course Project
 */
public class User implements Comparable<User> {
    private int id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String city;
    private final LinkedList<String> interests;
    private BST<User> friends;
    private List<Integer> friendIds;

    // Constructors

    /**
     * Creates a new User with empty friends list and interests
     *
     * @see util.BST#BST() for friends list implementation
     * @see util.LinkedList#LinkedList() for interests list implementation
     */
    public User() {
        friends = new BST<>();
        interests = new LinkedList<>();
        friendIds = new ArrayList<>();
    }

    // Accessors

    /**
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return
     */
    public String getCity() {
        return city;
    }

    /**
     * Gets the list of user's interests
     *
     * @return The LinkedList containing all interests
     * @see util.LinkedList for the list implementation
     */
    public LinkedList<String> getInterests() {
        return interests;
    }

    /**
     * Gets the number of interests the user has
     *
     * @return The number of interests
     * @see util.LinkedList#getLength() for size calculation
     */
    public int getInterestCount() {
        return interests.getLength();
    }

    /**
     * Get the BST containing all friends
     *
     * @return The friends BST
     * @see util.BST
     */
    public BST<User> getFriends() {
        return friends;
    }

    /**
     * Get the number of friends this user has
     *
     * @return number of friends
     */
    public int getFriendCount() {
        return friends.getSize();
    }

    /**
     * @return
     */
    public List<Integer> getFriendIds() {
        return new ArrayList<>(friendIds);
    }

    // Mutators

    /**
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @param friendIds
     */
    public void setFriendIds(List<Integer> friendIds) {
        this.friendIds = friendIds;
    }

    /**
     * @param friends
     */
    public void setFriends(BST<User> friends) {
        this.friends = friends;
    }

    /**
     * Adds a new interest to the user's list of interests
     *
     * @param interest The interest to add
     * @see util.LinkedList#addLast(Object) for interest storage
     */
    public void addInterest(String interest) {
        if (interest != null && !interest.trim().isEmpty()) {
            interests.addLast(interest.trim());
        }
    }

    /**
     * Remove the [! first occurrence] of an interest from the user's list
     *
     * @param interest The interest to remove (case-sensitive, will be trimmed)
     * @return true if the interest was found and removed, false otherwise
     * @throws NullPointerException if the iterator operations fail
     * @see util.LinkedList#removeIterator() for removal operation
     */
    public boolean removeInterest(String interest) {
        if (interest == null) {
            return false;
        }

        String trimmedInterest = interest.trim();
        interests.positionIterator();

        while (!interests.offEnd()) {
            if (interests.getIterator().equals(trimmedInterest)) {
                interests.removeIterator();
                return true;
            }
            interests.advanceIterator();
        }
        return false;
    }

    /**
     * Adds a new friend connection
     * May want to change later on to return:
     * - an int 0 for success, 1 for already friends, 2 for null input, 3 for is self;
     * <p>
     * (?) once ui is implemented ^
     *
     * @param friend The user to add as a friend
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
     * Removes a friend connection
     *
     * @param friend The user to remove from friends
     * @see util.BST#remove(Object, Comparator) for friend removal
     */
    public void removeFriend(User friend) {
        friends.remove(friend, UserDirectory.nameComparator);
        if (friend != null) {
            friendIds.remove(Integer.valueOf(friend.getId()));
        }
    }

    /**
     * Search for a friend by their name
     *
     * @param firstName First name to search for
     * @param lastName  Last name to search for
     * @return The found friend or null if not found
     * @see util.BST#search(Object, Comparator) for search
     */
    public User searchFriendByName(String firstName, String lastName) {
        User searchUser = new User();
        searchUser.firstName = firstName;
        searchUser.lastName = lastName;
        return friends.search(searchUser, UserDirectory.nameComparator);
    }

    /**
     * Check if another user is a friend
     *
     * @param other user to check friendship with
     * @return true if users are friends, false otherwise
     * @see util.BST#search(Object, Comparator) for search
     */
    public boolean isFriend(User other) {
        if (other == null)
            return false;

        // Use ID comparator since it's unique
        Comparator<User> idComparator = (u1, u2) -> Integer.compare(u1.getId(), u2.getId());
        return friends.search(other, idComparator) != null;
    }

    // Additional Methods

    /**
     * Compare users by their unique IDs
     *
     * @param other The other user to compare with
     * @return negative if this user's ID is less, positive if greater, 0 if equal
     * @see #getId() for the ID being compared
     */
    @Override
    public int compareTo(User other) {
        return Integer.compare(this.id, other.id);
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

}
