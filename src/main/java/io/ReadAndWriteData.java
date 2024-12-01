package io;

import java.io.*;
import java.util.*;

import data.UserDirectory;
import data.User;
import util.BST;
import util.Graph;
import util.HashTable;
import util.LinkedList;

/**
 * Manage retrieval and saving of all user data.
 *
 * @author Benjamin Liou
 * @author Kevin Young
 * @see data.User for user data structure
 * @see data.UserDirectory for user management operations
 * CIS 22C, Course Project
 */
public class ReadAndWriteData {
    private static final String DEFAULT_DATA_FILE = "src/main/java/io/data.txt";

    /**
     * Read user data from persistent storage.
     *
     * @return a UserDirectory containing data from all current users
     * @throws IOException if there is an error reading the file
     * @see data.User for user data structure
     * @see data.UserDirectory for user management operations
     */
    public static UserDirectory readData() {
        ArrayList<Integer> friendIds = new ArrayList<>();
        HashTable<String> interests = new HashTable<>(1);
        ArrayList<User> usersAL = new ArrayList<>(0);
        BST<User> usersBST = new BST<>();
        usersAL.add(null); // set usersAL[0] to null for 1-based indexing

        int userID, numFriends, friendID, numInterests;
        String fullName, interest;
        Scanner fileInput = null;

        // open file
        try {
            fileInput = new Scanner(new File(DEFAULT_DATA_FILE));
        } catch (Exception e) { // error when opening file
            System.out.println("UserDirectory.java readData(): could not locate data file. Exiting program.");
            System.exit(1);
        }

        int numUsers = Integer.parseInt(fileInput.nextLine()); // get number of usersAL in file
        Graph friendNetwork = new Graph(numUsers + 1); // +1 for additional space for new user

        fileInput.nextLine();
        // read user data in file
        while (fileInput.hasNext()) {
            User currUser = new User();

            userID = Integer.parseInt(fileInput.nextLine()); // get userID
            currUser.setId(userID);

            fullName = fileInput.nextLine(); // read full name
            String[] nameParts = fullName.split(" "); // split full name into first and last
            currUser.setFirstName(nameParts[0]);
            currUser.setLastName(nameParts[1]);

            currUser.setUserName(fileInput.nextLine()); // set username
            currUser.setPassword(fileInput.nextLine()); // set password

            // input friends
            numFriends = Integer.parseInt(fileInput.nextLine()); // read # of friends
            for (int i = 0; i < numFriends; i++) {
                friendID = Integer.parseInt(fileInput.nextLine()); // read friendID
                friendIds.add(friendID);

                // if friendID not already connected to current user
                if (friendNetwork.getAdjacencyList(userID).findIndex(friendID) == -1) { // (-1 if not found in adj list)
                    friendNetwork.addUndirectedEdge(userID, friendID); // connect friendID to current user
                }
            }
            currUser.setFriendIds(friendIds);

            currUser.setCity(fileInput.nextLine()); // set city

            // input interests
            numInterests = Integer.parseInt((fileInput.nextLine()));
            for (int i = 0; i < numInterests; i++) {
                interest = fileInput.nextLine(); // read one interest
                currUser.addInterest(interest); // add to current user
                interests.add(interest); // add to HashTable of interests
            }

            usersAL.add(currUser);
            usersBST.insert(currUser, UserDirectory.nameComparator);
            fileInput.nextLine();
        }

        fileInput.close();
        return new UserDirectory(usersAL, usersBST, friendNetwork);
    }

    /**
     * Save user data to persistent storage
     *
     * @throws IOException if there is an error writing to the file
     * @see data.User for user data structure
     * @see data.UserDirectory for user management operations
     */
    public static void saveData(UserDirectory ud) throws IOException {
        String fileName = "data.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Get all users from directory in sorted order
            ArrayList<User> users = ud.getArrayList();
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
                        User friend = friends.search(ud.parseUser(entry), ud.getNameComparator());
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