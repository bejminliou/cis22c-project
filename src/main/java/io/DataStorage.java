package io;

import java.io.*;
import java.util.*;
import data.*;
import util.LinkedList;
import util.BST;

/**
 * Handle persistent data storage and retrieval for the application
 * Manage saving and loading of user data, accounts, and relationships
 * 
 * @see data.User for user data structure
 * @see data.UserDirectory for user management operations
 */
public class DataStorage {
    private static final String DEFAULT_DATA_FILE = "src/main/java/io/data.txt";
    private final String dataFile;
    private UserDirectory userDirectory;
    private Map<Integer, User> userMap;

    /**
     * Create a new DataStorage instance with default data file
     * 
     * @param userDirectory directory to load data into and save data from
     * @see data.UserDirectory for user management operations
     */
    public DataStorage(UserDirectory userDirectory) {
        this(userDirectory, DEFAULT_DATA_FILE);
    }

    /**
     * Create a new DataStorage instance with specified data file
     * 
     * @param userDirectory directory to load data into and save data from
     * @param dataFile path to data file
     * @see data.UserDirectory for user management operations
     */
    public DataStorage(UserDirectory userDirectory, String dataFile) {
        this.userDirectory = userDirectory;
        this.userMap = new HashMap<>();
        this.dataFile = dataFile;
    }

    /**
     * Load user data from persistent storage
     * 
     * @throws IOException if there is an error reading the file
     * @see data.User for user data structure
     * @see data.UserDirectory for user management operations
     */
    public void loadData() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            String line;
            User currentUser = null;
            int friendCount = 0;
            int interestCount = 0;
            List<Integer> friendIds = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    if (currentUser != null) {
                        // System.out.println("Adding user: " + currentUser.getFirstName() + " " + currentUser.getLastName() + " (" + currentUser.getUserName() + ")");
                        userMap.put(currentUser.getId(), currentUser);
                        userDirectory.addUser(currentUser);
                        currentUser = null;
                    }
                    continue;
                }

                if (currentUser == null) {
                    currentUser = new User();
                    currentUser.setId(Integer.parseInt(line));
                    continue;
                }

                if (line.startsWith("f ")) {
                    friendCount = Integer.parseInt(line.substring(2));
                    friendIds.clear();
                    continue;
                }
                if (line.startsWith("i ")) {
                    interestCount = Integer.parseInt(line.substring(2));
                    continue;
                }
                if (line.startsWith("city ")) {
                    currentUser.setCity(line.substring(5));
                    continue;
                }

                if (currentUser.getFirstName() == null) {
                    // Name
                    String[] nameParts = line.split(" ");
                    currentUser.setFirstName(nameParts[0]);
                    currentUser.setLastName(nameParts.length > 1 ? String.join(" ", Arrays.copyOfRange(nameParts, 1, nameParts.length)) : "");
                } else if (currentUser.getUserName() == null) {
                    // Username
                    currentUser.setUserName(line);
                } else if (currentUser.getPassword() == null) {
                    // Password
                    currentUser.setPassword(line);
                } else if (friendCount > 0) {
                    // Friend ID
                    friendIds.add(Integer.parseInt(line));
                    friendCount--;

                    if (friendCount == 0) {
                        // Store friend IDs for this user
                        List<Integer> userFriends = new ArrayList<>(friendIds);
                        currentUser.setFriendIds(userFriends);
                    }
                } else if (interestCount > 0) {
                    // Interest
                    currentUser.addInterest(line);
                    interestCount--;
                }
            }

            if (currentUser != null) {
                // System.out.println("Adding final user: " + currentUser.getFirstName() + " " + currentUser.getLastName() + " (" + currentUser.getUserName() + ")");
                userMap.put(currentUser.getId(), currentUser);
                userDirectory.addUser(currentUser);
            }
        }

        // After loading, set up friend connections
        for (User currentUser : userMap.values()) {
            for (int friendId : currentUser.getFriendIds()) {
                User friend = userMap.get(friendId);
                if (friend != null) {
                    currentUser.addFriend(friend);
                }
            }
        }
    }

    /**
     * Check if two users are friends through their user ids
     * 
     * @param userId1 ID of first user to check
     * @param userId2 ID of second user to check
     * @return true if users are friends, false otherwise
     * @see data.User#isFriend(User) for friendship check implementation
     */
    public boolean isFriend(int userId1, int userId2) {
        User user1 = userMap.get(userId1);
        User user2 = userMap.get(userId2);
        return user1 != null && user2 != null && user1.isFriend(user2);
    }

    /**
     * Save user data to persistent storage
     * 
     * @throws IOException if there is an error writing to the file
     * @see data.User for user data structure
     * @see data.UserDirectory for user management operations
     */
    public void saveData() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile))) {
            // Get all users from directory in sorted order
            ArrayList<User> users = userDirectory.getAllUsers();
            boolean firstUser = true;

            for (User user : users) {
                if (!firstUser) {
                    writer.write("\n");
                }
                firstUser = false;

                // Write user ID
                writer.write(String.valueOf(user.getId()));
                writer.newLine();

                // Write name
                writer.write(user.getFirstName() + " " + user.getLastName());
                writer.newLine();

                // Write username
                writer.write(user.getUserName());
                writer.newLine();

                // Write password
                writer.write(user.getPassword());
                writer.newLine();

                // Write friends
                BST<User> friends = user.getFriends();
                ArrayList<User> friendsList = new ArrayList<>();
                String friendsString = friends.inOrderString();
                if (friendsString != null && !friendsString.isEmpty()) {
                    String[] friendEntries = friendsString.split("\n");
                    for (String entry : friendEntries) {
                        if (entry == null || entry.isEmpty()) continue;
                        User friend = friends.search(userDirectory.parseUser(entry), userDirectory.getNameComparator());
                        if (friend != null) {
                            friendsList.add(friend);
                        }
                    }
                }
                writer.write("f " + friendsList.size());
                writer.newLine();
                for (User friend : friendsList) {
                    writer.write(String.valueOf(friend.getId()));
                    writer.newLine();
                }

                // Write city
                writer.write("city " + (user.getCity() != null ? user.getCity() : ""));
                writer.newLine();

                // Write interests
                LinkedList<String> interests = user.getInterests();
                writer.write("i " + interests.getLength());
                writer.newLine();
                interests.positionIterator();
                while (!interests.offEnd()) {
                    writer.write(interests.getIterator());
                    writer.newLine();
                    interests.advanceIterator();
                }
            }
        }
    }
}