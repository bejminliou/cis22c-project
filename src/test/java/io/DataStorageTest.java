package io;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import data.*;
import util.LinkedList;

public class DataStorageTest {
    private static final String TEST_DATA_FILE = "src/test/resources/test_data.txt";
    private UserDirectory userDirectory;
    private DataStorage dataStorage;

    @Before
    public void setUp() throws IOException {
        userDirectory = new UserDirectory();
        dataStorage = new DataStorage(userDirectory, TEST_DATA_FILE);

        // Create test data directory if it doesn't exist
        File testDir = new File("src/test/resources");
        testDir.mkdirs();
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_DATA_FILE))) {
            // First user
            writer.println("1"); // ID
            writer.println("George Washington"); // Name
            writer.println("gwashington"); // Username
            writer.println("password123"); // Password
            writer.println("f 2"); // Friend count
            writer.println("2"); // Friend ID
            writer.println("3"); // Friend ID
            writer.println("city Mount Vernon"); // City
            writer.println("i 3"); // Interest count
            writer.println("Leadership"); // Interest
            writer.println("Military"); // Interest
            writer.println("Agriculture"); // Interest
            writer.println(); // Empty line between users

            // Second user
            writer.println("2"); // ID
            writer.println("John Adams"); // Name
            writer.println("jadams"); // Username
            writer.println("pass456"); // Password
            writer.println("f 1"); // Friend count
            writer.println("1"); // Friend ID
            writer.println("city Quincy"); // City
            writer.println("i 2"); // Interest count
            writer.println("Law"); // Interest
            writer.println("Politics"); // Interest
            writer.println(); // Empty line between users

            // Third user
            writer.println("3"); // ID
            writer.println("Thomas Jefferson"); // Name
            writer.println("tjeff"); // Username
            writer.println("pass789"); // Password
            writer.println("f 1"); // Friend count
            writer.println("1"); // Friend ID
            writer.println("city Monticello"); // City
            writer.println("i 2"); // Interest count
            writer.println("Architecture"); // Interest
            writer.println("Writing"); // Interest
            writer.println(); // Empty line at end of file
        }
    }

    @Test
    public void testLoadData() throws IOException {
        dataStorage.loadData();

        // Output directory contents
        System.out.println("Directory after loading test data:");
        System.out.println(userDirectory.toString());

        // Test user count
        assertEquals("Should load 3 users", 3, userDirectory.getUserCount());

        // Test first user (Washington)
        User washington = userDirectory.findUserByUsername("gwashington");
        assertNotNull("Washington should exist", washington);
        assertEquals("ID should be 1", 1, washington.getId());
        assertEquals("First name should be George", "George", washington.getFirstName());
        assertEquals("Last name should be Washington", "Washington", washington.getLastName());
        assertEquals("City should be Mount Vernon", "Mount Vernon", washington.getCity());
        assertEquals("Should have 3 interests", 3, washington.getInterestCount());
        assertEquals("Should have 2 friends", 2, washington.getFriendCount());

        // Test second user (Adams)
        User adams = userDirectory.findUserByUsername("jadams");
        assertNotNull("Adams should exist", adams);
        assertEquals("Should have 1 friend", 1, adams.getFriendCount());
        assertEquals("Should have 2 interests", 2, adams.getInterestCount());

        // Test friendship connections
        assertTrue("Washington and Adams should be friends", washington.isFriend(adams));
        assertTrue("Adams and Washington should be friends", adams.isFriend(washington));
    }

    @Test
    public void testIsFriend() throws IOException {
        dataStorage.loadData();
        assertTrue("User 1 and 2 should be friends", dataStorage.isFriend(1, 2));
        assertTrue("User 2 and 1 should be friends", dataStorage.isFriend(2, 1));
        assertFalse("User 2 and 3 should not be friends", dataStorage.isFriend(2, 3));
    }

    @Test
    public void testSaveData() throws IOException {
        // Create test directory if it doesn't exist
        File testDir = new File("src/test/resources");
        testDir.mkdirs();

        try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_DATA_FILE))) {
            // Empty file
        }

        User user1 = new User();
        user1.setId(1);
        user1.setFirstName("Jane");
        user1.setLastName("Doe");
        user1.setUserName("jdoe");
        user1.setPassword("pass123");
        user1.setCity("New York");
        user1.addInterest("Reading");
        user1.addInterest("Gaming");

        User user2 = new User();
        user2.setId(2);
        user2.setFirstName("John");
        user2.setLastName("Doe");
        user2.setUserName("johndoe");
        user2.setPassword("pass456");
        user2.setCity("Los Angeles");
        user2.addInterest("Gaming");
        user2.addInterest("Sports");
        user2.addInterest("Eating");

        User user3 = new User();
        user3.setId(3);
        user3.setFirstName("John");
        user3.setLastName("Doe");
        user3.setUserName("johndoe2");
        user3.setPassword("pass789");
        user3.setCity("Chicago");
        user3.addInterest("Music");
        user3.addInterest("Movies");

        // Add friendship
        user1.addFriend(user2);
        user2.addFriend(user1);
        user2.addFriend(user3);
        user3.addFriend(user2);

        // Add users to directory
        userDirectory.addUser(user1);
        userDirectory.addUser(user2);
        userDirectory.addUser(user3);

        // ?debug Print directory contents before saving
        System.out.println("\nDirectory contents before saving:");
        System.out.println(userDirectory.toString());

        // ?debug Print all users from getAllUsers
        System.out.println("All users from getAllUsers:");
        ArrayList<User> allUsers = userDirectory.getAllUsers();
        for (User user : allUsers) {
            System.out.println("User: " + user.getFirstName() + " " + user.getLastName() + " (" + user.getUserName() + ")");
        }

        // Save data
        dataStorage.saveData();

        // ?debug Print file contents
        System.out.println("\nSaved file contents:");
        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    @Test
    public void testLoadDataFromSave() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_DATA_FILE))) {
            // Empty file
        }

        User user1 = new User();
        user1.setId(1);
        user1.setFirstName("Jane");
        user1.setLastName("Doe");
        user1.setUserName("jdoe");
        user1.setPassword("pass123");
        user1.setCity("New York");
        user1.addInterest("Reading");
        user1.addInterest("Gaming");

        User user2 = new User();
        user2.setId(2);
        user2.setFirstName("John");
        user2.setLastName("Doe");
        user2.setUserName("johndoe");
        user2.setPassword("pass456");
        user2.setCity("Los Angeles");
        user2.addInterest("Gaming");
        user2.addInterest("Sports");

        User user3 = new User();
        user3.setId(3);
        user3.setFirstName("John");
        user3.setLastName("Doe");
        user3.setUserName("johndoe2");
        user3.setPassword("pass789");
        user3.setCity("Chicago");
        user3.addInterest("Music");
        user3.addInterest("Movies");

        // Add friendship
        user1.addFriend(user2);
        user2.addFriend(user1);
        user2.addFriend(user3);
        user3.addFriend(user2);

        // Add users to directory
        userDirectory.addUser(user1);
        userDirectory.addUser(user2);
        userDirectory.addUser(user3);

        // Save data
        dataStorage.saveData();

        // Create new directory and storage
        UserDirectory newDirectory = new UserDirectory();
        DataStorage newStorage = new DataStorage(newDirectory, TEST_DATA_FILE);

        // Load data
        newStorage.loadData();

        System.out.println("\nLoaded directory contents:");
        System.out.println(newDirectory.toString());

        // Verify users were loaded correctly
        User loadedUser1 = newDirectory.findUserByUsername("jdoe");
        User loadedUser2 = newDirectory.findUserByUsername("johndoe");
        User loadedUser3 = newDirectory.findUserByUsername("johndoe2");

        assertNotNull("User 1 should be loaded", loadedUser1);
        assertNotNull("User 2 should be loaded", loadedUser2);
        assertNotNull("User 3 should be loaded", loadedUser3);

        // Verify user 1 details
        assertEquals("User 1 ID", 1, loadedUser1.getId());
        assertEquals("User 1 first name", "Jane", loadedUser1.getFirstName());
        assertEquals("User 1 last name", "Doe", loadedUser1.getLastName());
        assertEquals("User 1 city", "New York", loadedUser1.getCity());
        
        // Check interests using getInterests()
        LinkedList<String> user1Interests = loadedUser1.getInterests();
        boolean hasReading = false;
        boolean hasGaming = false;
        user1Interests.positionIterator();
        while (!user1Interests.offEnd()) {
            String interest = user1Interests.getIterator();
            if (interest.equals("Reading")) hasReading = true;
            if (interest.equals("Gaming")) hasGaming = true;
            user1Interests.advanceIterator();
        }
        assertTrue("User 1 should have Reading interest", hasReading);
        assertTrue("User 1 should have Gaming interest", hasGaming);

        // Verify user 2 details
        assertEquals("User 2 ID", 2, loadedUser2.getId());
        assertEquals("User 2 first name", "John", loadedUser2.getFirstName());
        assertEquals("User 2 last name", "Doe", loadedUser2.getLastName());
        assertEquals("User 2 city", "Los Angeles", loadedUser2.getCity());
        
        // Check interests using getInterests()
        LinkedList<String> user2Interests = loadedUser2.getInterests();
        boolean hasGaming2 = false;
        boolean hasSports = false;
        user2Interests.positionIterator();
        while (!user2Interests.offEnd()) {
            String interest = user2Interests.getIterator();
            if (interest.equals("Gaming")) hasGaming2 = true;
            if (interest.equals("Sports")) hasSports = true;
            user2Interests.advanceIterator();
        }
        assertTrue("User 2 should have Gaming interest", hasGaming2);
        assertTrue("User 2 should have Sports interest", hasSports);

        // Verify user 3 details
        assertEquals("User 3 ID", 3, loadedUser3.getId());
        assertEquals("User 3 first name", "John", loadedUser3.getFirstName());
        assertEquals("User 3 last name", "Doe", loadedUser3.getLastName());
        assertEquals("User 3 city", "Chicago", loadedUser3.getCity());
        
        // Check interests using getInterests()
        LinkedList<String> user3Interests = loadedUser3.getInterests();
        boolean hasMusic = false;
        boolean hasMovies = false;
        user3Interests.positionIterator();
        while (!user3Interests.offEnd()) {
            String interest = user3Interests.getIterator();
            if (interest.equals("Music")) hasMusic = true;
            if (interest.equals("Movies")) hasMovies = true;
            user3Interests.advanceIterator();
        }
        assertTrue("User 3 should have Music interest", hasMusic);
        assertTrue("User 3 should have Movies interest", hasMovies);

        // Verify friendships
        assertTrue("User 1 and 2 should be friends", loadedUser1.isFriend(loadedUser2));
        assertTrue("User 2 and 1 should be friends", loadedUser2.isFriend(loadedUser1));
        assertTrue("User 2 and 3 should be friends", loadedUser2.isFriend(loadedUser3));
        assertTrue("User 3 and 2 should be friends", loadedUser3.isFriend(loadedUser2));
    }

    @After
    public void tearDown() {
        File testFile = new File(TEST_DATA_FILE);
        if (testFile.exists()) {
            testFile.delete();
        }
    }
}
