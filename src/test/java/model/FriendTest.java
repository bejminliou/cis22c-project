package model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import data.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Test cases for Friend utility class
 * Tests name-based search and network relationship functionality
 */
public class FriendTest {
    private Friend friendUtil;
    private UserDirectory directory;
    private User user1, user2, user3;

    private void printSearchResults(String testName, String query, ArrayList<User> results) {
        System.out.println("\n=== " + testName + " ===");
        System.out.println("Search query: \"" + (query != null ? query : "[null]") + "\"");
        System.out.println("Results: [");
        for (User user : results) {
            System.out.println("  " + user.getFirstName() + " " + user.getLastName() + " (ID: " + user.getId() + ")");
        }
        System.out.println("]");
    }

    private void printRecommendations(String testName, int userId, ArrayList<User> recommendations) {
        System.out.println("\n=== " + testName + " ===");
        System.out.println("Getting recommendations for User ID: " + userId);
        System.out.println("Recommendations: [");
        for (User user : recommendations) {
            System.out.println("  " + user.getFirstName() + " " + user.getLastName() + " (ID: " + user.getId() + ")");
        }
        System.out.println("]");
    }

    @Before
    public void setUp() {
        directory = new UserDirectory();

        // Create test users
        user1 = new User();
        user1.setId(1);
        user1.setFirstName("John");
        user1.setLastName("Smith");
        user1.setUserName("jsmith");
        user1.addInterest("Reading");
        user1.addInterest("Gaming");

        user2 = new User();
        user2.setId(2);
        user2.setFirstName("John");
        user2.setLastName("Doe");
        user2.setUserName("jdoe");

        user3 = new User();
        user3.setId(3);
        user3.setFirstName("Jane");
        user3.setLastName("Smith");
        user3.setUserName("janesmith");

        // Add users to directory
        directory.addUser(user1);
        directory.addUser(user2);
        directory.addUser(user3);

        // Set up friend relationships
        user1.addFriend(user2);
        user2.addFriend(user1);
        user2.addFriend(user3);
        user3.addFriend(user2);

        friendUtil = new Friend(directory);
    }

    @Test
    public void testFindUsersByName_ExactMatch() {
        String query = "John Smith";
        ArrayList<User> matches = friendUtil.findUsersByName(query);
        printSearchResults("Exact Match Search", query, matches);
        assertEquals("Should find one exact match", 1, matches.size());
        assertEquals("Should find correct user", user1, matches.get(0));
    }

    @Test
    public void testFindUsersByName_PartialMatch() {
        String query = "John";
        ArrayList<User> matches = friendUtil.findUsersByName(query);
        printSearchResults("Partial Match Search (First Name)", query, matches);
        assertEquals("Should find two Johns", 2, matches.size());
    }

    @Test
    public void testFindUsersByName_LastNameOnly() {
        String query = "Smith";
        ArrayList<User> matches = friendUtil.findUsersByName(query);
        printSearchResults("Last Name Only Search", query, matches);
        assertEquals("Should find two Smiths", 2, matches.size());
    }

    @Test
    public void testFindUsersByName_NoMatch() {
        String query = "Bob";
        ArrayList<User> matches = friendUtil.findUsersByName(query);
        printSearchResults("No Match Search", query, matches);
        assertTrue("Should return empty list for no matches", matches.isEmpty());
    }

    @Test
    public void testFindUsersByName_NullOrEmpty() {
        // Test null input
        ArrayList<User> nullMatches = friendUtil.findUsersByName(null);
        printSearchResults("Null Input Search", null, nullMatches);
        assertTrue("Should handle null input", nullMatches.isEmpty());

        // Test empty string
        ArrayList<User> emptyMatches = friendUtil.findUsersByName("");
        printSearchResults("Empty String Search", "", emptyMatches);
        assertTrue("Should handle empty input", emptyMatches.isEmpty());

        // Test whitespace
        ArrayList<User> whitespaceMatches = friendUtil.findUsersByName("   ");
        printSearchResults("Whitespace Search", "   ", whitespaceMatches);
        assertTrue("Should handle whitespace input", whitespaceMatches.isEmpty());
    }

    @Test
    public void testGetConnections() {
        List<Integer> user1Friends = friendUtil.getConnections(1);
        assertEquals("User1 should have one friend", 1, user1Friends.size());
        assertTrue("User1 should be friends with User2", user1Friends.contains(2));

        List<Integer> user2Friends = friendUtil.getConnections(2);
        assertEquals("User2 should have two friends", 2, user2Friends.size());
        assertTrue("User2 should be friends with User1", user2Friends.contains(1));
        assertTrue("User2 should be friends with User3", user2Friends.contains(3));
    }

    @Test
    public void testAreConnected() {
        assertTrue("User1 and User2 should be connected", friendUtil.areConnected(1, 2));
        assertTrue("User2 and User3 should be connected", friendUtil.areConnected(2, 3));
        assertFalse("User1 and User3 should not be connected", friendUtil.areConnected(1, 3));
    }

    @Test
    public void testAreConnected_InvalidIds() {
        assertFalse("Should handle negative IDs", friendUtil.areConnected(-1, 1));
        assertFalse("Should handle out of range IDs", friendUtil.areConnected(1, 999));
    }

    @Test
    public void testRefreshNetwork() {
        user1.addFriend(user3);
        user3.addFriend(user1);

        // Before refresh
        assertFalse("New connection not in graph yet", friendUtil.areConnected(1, 3));

        // After refresh
        friendUtil.refreshNetwork();
        assertTrue("New connection should be in graph", friendUtil.areConnected(1, 3));
    }

    @Test
    public void testGetFriendRecommendations() {
        // Initial setup has:
        // user1 <-> user2 <-> user3
        // So user3 should be recommended to user1, and user1 to user3

        ArrayList<User> recommendationsForUser1 = friendUtil.getBaseRecommendations(1);
        printRecommendations("Friend Recommendations for User1 (John Smith)", 1, recommendationsForUser1);
        assertEquals("User1 should get one recommendation", 1, recommendationsForUser1.size());
        assertEquals("User3 should be recommended to User1", user3, recommendationsForUser1.get(0));

        ArrayList<User> recommendationsForUser2 = friendUtil.getBaseRecommendations(2);
        printRecommendations("Friend Recommendations for User2 (John Doe)", 2, recommendationsForUser2);
        assertEquals("User2 should get no recommendations (already friends with everyone in network)",
                0, recommendationsForUser2.size());

        ArrayList<User> recommendationsForUser3 = friendUtil.getBaseRecommendations(3);
        printRecommendations("Friend Recommendations for User3 (Jane Smith)", 3, recommendationsForUser3);
        assertEquals("User3 should get one recommendation", 1, recommendationsForUser3.size());
        assertEquals("User1 should be recommended to User3", user1, recommendationsForUser3.get(0));
    }

    @Test
    public void testGetFriendRecommendations_InvalidId() {
        ArrayList<User> recommendations = friendUtil.getBaseRecommendations(-1);
        printRecommendations("Friend Recommendations for Invalid ID", -1, recommendations);
        assertTrue("Should handle negative user ID", recommendations.isEmpty());

        recommendations = friendUtil.getBaseRecommendations(999);
        printRecommendations("Friend Recommendations for Non-existent ID", 999, recommendations);
        assertTrue("Should handle out of range user ID", recommendations.isEmpty());
    }

    @Test
    public void testGetFriendRecommendations_ExtendedNetwork() {
        // Create more users for a longer chain
        User user4 = new User();
        user4.setId(4);
        user4.setFirstName("Alice");
        user4.setLastName("Johnson");
        user4.setUserName("ajohnson");

        User user5 = new User();
        user5.setId(5);
        user5.setFirstName("Bob");
        user5.setLastName("Wilson");
        user5.setUserName("bwilson");

        directory.addUser(user4);
        directory.addUser(user5);

        // Create chain: user1 <-> user2 <-> user3 <-> user4 <-> user5
        user3.addFriend(user4);
        user4.addFriend(user3);
        user4.addFriend(user5);
        user5.addFriend(user4);

        // Refresh network to include new connections
        friendUtil.refreshNetwork();

        // Test recommendations for user1
        ArrayList<User> recommendationsForUser1 = friendUtil.getBaseRecommendations(1);
        printRecommendations("Extended Network Recommendations for User1", 1, recommendationsForUser1);
        assertEquals("User1 should get three recommendations", 3, recommendationsForUser1.size());
        assertTrue("User3 should be recommended to User1", recommendationsForUser1.contains(user3));
        assertTrue("User4 should be recommended to User1", recommendationsForUser1.contains(user4));
        assertTrue("User5 should be recommended to User1", recommendationsForUser1.contains(user5));

        // Test recommendations for user5 (at other end of chain)
        ArrayList<User> recommendationsForUser5 = friendUtil.getBaseRecommendations(5);
        printRecommendations("Extended Network Recommendations for User5", 5, recommendationsForUser5);
        assertEquals("User5 should get three recommendations", 3, recommendationsForUser5.size());
        assertTrue("User1 should be recommended to User5", recommendationsForUser5.contains(user1));
        assertTrue("User2 should be recommended to User5", recommendationsForUser5.contains(user2));
        assertTrue("User3 should be recommended to User5", recommendationsForUser5.contains(user3));
    }

    @Test
    public void testGetFriendRecommendations_VeryLongChain() {
        // Create super long chain of users: 1-2-3-4-5-6-7-8-9-10
        User[] users = new User[10];
        
        // Store existing 1-3
        users[0] = user1;
        users[1] = user2;
        users[2] = user3;
        
        // Create 4-10
        for (int i = 3; i < 10; i++) {
            users[i] = new User();
            users[i].setId(i + 1);
            users[i].setFirstName("User");
            users[i].setLastName(String.valueOf(i + 1));
            users[i].setUserName("user" + (i + 1));
            directory.addUser(users[i]);
        }

        // Connect into chain
        for (int i = 0; i < 9; i++) {
            users[i].addFriend(users[i + 1]);
            users[i + 1].addFriend(users[i]);
        }

        friendUtil.refreshNetwork();

        // Test recommendations for user1 (should get users 3-10)
        ArrayList<User> recommendationsForUser1 = friendUtil.getBaseRecommendations(1);
        printRecommendations("Very Long Chain Recommendations for User1", 1, recommendationsForUser1);
        assertEquals("User1 should get recommendations for all non-friend users", 8, recommendationsForUser1.size());

        // Test recommendations for user10 (should get users 1-8)
        ArrayList<User> recommendationsForUser10 = friendUtil.getBaseRecommendations(10);
        printRecommendations("Very Long Chain Recommendations for User10", 10, recommendationsForUser10);
        assertEquals("User10 should get recommendations for all non-friend users", 8, recommendationsForUser10.size());
    }

    @Test
    public void testGetInterestBasedRecommendations() {
        // Create users with varying interests and distances
        User user4 = new User();
        user4.setId(4);
        user4.setFirstName("Alice");
        user4.setLastName("Johnson");
        user4.setUserName("ajohnson");
        user4.addInterest("Reading"); // Shares interest with user1
        user4.addInterest("Gaming");  // Shares interest with user1

        User user5 = new User();
        user5.setId(5);
        user5.setFirstName("Bob");
        user5.setLastName("Wilson");
        user5.setUserName("bwilson");
        user5.addInterest("Sports");  // No shared interests

        User user6 = new User();
        user6.setId(6);
        user6.setFirstName("Carol");
        user6.setLastName("Davis");
        user6.setUserName("cdavis");
        user6.addInterest("Reading"); // Shares one interest with user1

        // Add to directory
        directory.addUser(user4);
        directory.addUser(user5);
        directory.addUser(user6);

        // Create network: user1 <-> user2 <-> user4 <-> user5 <-> user6
        user2.addFriend(user4);
        user4.addFriend(user2);
        user4.addFriend(user5);
        user5.addFriend(user4);
        user5.addFriend(user6);
        user6.addFriend(user5);

        // Refresh network
        friendUtil.refreshNetwork();

        // Get regular BFS recommendations (unsorted)
        ArrayList<User> regularRecommendations = friendUtil.getBaseRecommendations(1);
        System.out.println("\nUnsorted BFS Recommendations for User1 (interests: Reading, Gaming):");
        for (User user : regularRecommendations) {
            System.out.println("- " + user.getFirstName() + " " + user.getLastName() + 
                             " (interests: " + user.getInterests() + ")");
        }

        // Get interest-based recommendations (sorted)
        ArrayList<User> recommendations = friendUtil.getFriendRecommendations(1);
        System.out.println("\nSorted Interest-Based Recommendations for User1:");
        for (User user : recommendations) {
            System.out.println("- " + user.getFirstName() + " " + user.getLastName() + 
                             " (interests: " + user.getInterests() + ")");
        }

        // Alice (user4) should be first due to shared interests despite being distance 2
        assertEquals("First recommendation should be Alice (most shared interests)", 
                    user4, recommendations.get(0));
        
        // Carol (user6) should be before Bob (user5) due to shared interest despite being further
        assertTrue("Carol should be recommended before Bob due to shared interest",
                  recommendations.indexOf(user6) < recommendations.indexOf(user5));
    }
}
