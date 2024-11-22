package data;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class InterestManagerTest {
    private InterestManager manager;
    private User user1, user2, user3;

    @Before
    public void setUp() {
        manager = new InterestManager();
        
        // Create test users
        user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setId(1);

        user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setId(2);

        user3 = new User();
        user3.setFirstName("Bob");
        user3.setLastName("Johnson");
        user3.setId(3);
    }

    @Test
    public void testAddInterest() {
        Interest interest = manager.addInterest("Programming");
        assertNotNull("Interest should be created", interest);
        assertEquals("Interest name should match", "Programming", interest.getName());
        assertEquals("First interest should have ID 0", 0, interest.getId());

        // Adding same interest should return existing one
        Interest duplicate = manager.addInterest("programming");
        assertEquals("Should return same interest object", interest, duplicate);
    }

    @Test
    public void testAddUserToInterest() {
        assertTrue("Should add user to new interest", 
            manager.addUserToInterest(user1, "Programming"));
        
        ArrayList<User> users = manager.getUsersWithInterest("Programming");
        assertEquals("Should have one user", 1, users.size());
        assertEquals("Should contain user1", user1, users.get(0));

        // Adding same user again should fail
        assertFalse("Should not add same user twice", 
            manager.addUserToInterest(user1, "Programming"));
    }

    @Test
    public void testRemoveUserFromInterest() {
        manager.addUserToInterest(user1, "Programming");
        assertTrue("Should remove existing user", 
            manager.removeUserFromInterest(user1, "Programming"));
        
        ArrayList<User> users = manager.getUsersWithInterest("Programming");
        assertTrue("Interest should have no users", users.isEmpty());

        // Removing non-existent user should fail
        assertFalse("Should fail to remove non-existent user", 
            manager.removeUserFromInterest(user2, "Programming"));
    }

    @Test
    public void testSearchFriendsByInterest() {
        // Add users with shared interest
        manager.addUserToInterest(user1, "Programming");
        manager.addUserToInterest(user2, "Programming");
        manager.addUserToInterest(user3, "Programming");

        // Make user1 and user2 friends
        user1.addFriend(user2);

        ArrayList<User> potentialFriends = manager.searchFriendsByInterest(user1, "Programming");
        assertEquals("Should find one potential friend", 1, potentialFriends.size());
        assertEquals("Should find user3", user3, potentialFriends.get(0));
        assertFalse("Should not include existing friend", potentialFriends.contains(user2));
    }

    @Test
    public void testGetAllInterests() {
        manager.addInterest("Programming");
        manager.addInterest("Reading");
        manager.addInterest("Gaming");

        ArrayList<Interest> interests = manager.getAllInterests();
        assertEquals("Should have three interests", 3, interests.size());
        assertTrue("Should contain Programming", 
            interests.stream().anyMatch(i -> i.getName().equals("Programming")));
        assertTrue("Should contain Reading", 
            interests.stream().anyMatch(i -> i.getName().equals("Reading")));
        assertTrue("Should contain Gaming", 
            interests.stream().anyMatch(i -> i.getName().equals("Gaming")));
    }

    @Test
    public void testGetUsersWithInterest() {
        manager.addUserToInterest(user1, "Programming");
        manager.addUserToInterest(user2, "Programming");
        manager.addUserToInterest(user3, "Reading");

        ArrayList<User> programmers = manager.getUsersWithInterest("Programming");
        assertEquals("Should have two programmers", 2, programmers.size());
        assertTrue("Should contain user1", programmers.contains(user1));
        assertTrue("Should contain user2", programmers.contains(user2));
        assertFalse("Should not contain user3", programmers.contains(user3));

        ArrayList<User> readers = manager.getUsersWithInterest("Reading");
        assertEquals("Should have one reader", 1, readers.size());
        assertTrue("Should contain user3", readers.contains(user3));
    }

    @Test
    public void testEdgeCases() {
        assertFalse("Should handle null user", 
            manager.addUserToInterest(null, "Programming"));
        assertFalse("Should handle null interest", 
            manager.addUserToInterest(user1, null));
        assertTrue("Empty interest list for non-existent interest", 
            manager.getUsersWithInterest("NonExistent").isEmpty());
        assertTrue("Empty potential friends for null user", 
            manager.searchFriendsByInterest(null, "Programming").isEmpty());
    }
}
