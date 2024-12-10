package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Scanner;

import data.InterestManager;
import data.User;
import data.UserDirectory;

import util.BST;
import util.Graph;
import util.LinkedList;

/**
 * ReadAndWriteData.java
 * Manage retrieval and saving of all User data from a file.
 *
 * @author Benjamin Liou
 * @author Kevin Young
 * @author Rolen Louie
 * @author Yukai Qiu
 * @author Kenneth Garcia
 * @author Tu Luong
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
     * @see data.User for user data structure
     * @see data.UserDirectory for user management operations
     */
    public static UserDirectory readData() {
        ArrayList<User> usersAL = new ArrayList<>();
        BST<User> allUsersBST = new BST<>();
        InterestManager interestManager = new InterestManager();

        int userID, numFriends, friendID, numInterests;
        String fullName, interest;
        Scanner fileInput = null;

        // open file Scanner
        try {
            fileInput = new Scanner(new File(DEFAULT_DATA_FILE));
        } catch (FileNotFoundException e) { // error when opening file
            System.out.println("UserDirectory.java readData(): could not locate user data file. Exiting program.");
            System.exit(1);
        }

        int numUsers = Integer.parseInt(fileInput.nextLine()); // get number of usersAL in file
        Graph friendNetwork = new Graph(numUsers + 1); // +1 for additional space for new user

        // read user data in file
        while (fileInput.hasNext()) {
            ArrayList<Integer> friendIds = new ArrayList<>();

            fileInput.nextLine(); // skip blank line

            User currUser = new User();
            userID = Integer.parseInt(fileInput.nextLine()); // get userID
            currUser.setId(userID);

            fullName = fileInput.nextLine(); // read full name
            String[] nameParts = fullName.split(" "); // split full name into first and last
            currUser.setFirstName(nameParts[0]);
            currUser.setLastName(nameParts[1]);

            currUser.setUsername(fileInput.nextLine()); // set username
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
                interestManager.addUserToInterest(interest, currUser); // add to interest manager
            }

            usersAL.add(currUser);
            allUsersBST.insert(currUser, UserDirectory.nameComparator);
        }

        // all friends to users
        for (User user : usersAL) { // for each user
            BST<User> currFriends = new BST<>();
            for (int currFriendID : user.getFriendIds()) { // for each friendID
                currFriends.insert(usersAL.get(currFriendID - 1), UserDirectory.nameComparator);
            }
            user.setFriends(currFriends);
        }

        fileInput.close();
        return new UserDirectory(usersAL, allUsersBST, friendNetwork, interestManager);
    }

    /**
     * Save user data to persistent storage in the specified format.
     *
     * @param ud the UserDirectory containing all the data to save
     * @throws IOException if there is an error writing to the file
     */
    public static void writeData(UserDirectory ud) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DEFAULT_DATA_FILE))) {
            // Get all users from directory
            ArrayList<User> usersAL = ud.getUsersAL();

            // Write the total number of users
            writer.write(String.valueOf(usersAL.size()));
            writer.newLine();

            for (User user : usersAL) {
                writer.newLine();
                // Write user ID
                writer.write(String.valueOf(user.getId()));
                writer.newLine();

                // Write full name
                writer.write(user.getFirstName() + " " + user.getLastName());
                writer.newLine();

                // Write username
                writer.write(user.getUsername());
                writer.newLine();

                // Write password
                writer.write(user.getPassword());
                writer.newLine();

                // Write friend IDs
                ArrayList<Integer> friendIds = user.getFriendIds();
                writer.write(String.valueOf(friendIds.size()));
                writer.newLine();
                for (Integer friendId : friendIds) {
                    writer.write(String.valueOf(friendId));
                    writer.newLine();
                }

                // Write city
                writer.write(user.getCity() != null ? user.getCity() : "");
                writer.newLine();

                // Write interests
                LinkedList<String> interests = user.getInterests();
                writer.write(String.valueOf(interests.getLength()));
                writer.newLine();
                interests.positionIterator();
                while (!interests.offEnd()) {
                    writer.write(interests.getIterator());
                    writer.newLine();
                    interests.advanceIterator();
                }
            }
        } catch (IOException e) {
            throw new IOException("ReadAndWriteData.java writeData(): Error writing to file: " + e.getMessage());
        }
    }
}