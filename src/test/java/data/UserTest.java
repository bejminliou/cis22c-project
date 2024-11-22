package data;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import util.LinkedList;

public class UserTest {
    private User user;

    @Before
    public void setUp() {
        user = new User();
    }

    @Test
    public void testEmptyUser() {
        assertNull("New user should have null first name", user.getFirstName());
        assertNull("New user should have null last name", user.getLastName());
        assertNull("New user should have null username", user.getUserName());
        assertNull("New user should have null password", user.getPassword());
        assertNull("New user should have null city", user.getCity());
        assertEquals("New user should have ID 0", 0, user.getId());
    }

    @Test
    public void testSetAndGetFirstName() {
        user.setFirstName("Jack");
        assertEquals("First name should be Jack", "Jack", user.getFirstName());
        
        user.setFirstName(null);
        assertNull("First name should be null", user.getFirstName());
    }

    @Test
    public void testSetAndGetLastName() {
        user.setLastName("Smith");
        assertEquals("Last name should be Smith", "Smith", user.getLastName());
        
        user.setLastName(null);
        assertNull("Last name should be null", user.getLastName());
    }

    @Test
    public void testSetAndGetUsername() {
        user.setUserName("jsmith");
        assertEquals("Username should be jsmith", "jsmith", user.getUserName());
        
        user.setUserName(null);
        assertNull("Username should be null", user.getUserName());
    }

    @Test
    public void testSetAndGetPassword() {
        user.setPassword("pass123");
        assertEquals("Password should be pass123", "pass123", user.getPassword());
        
        user.setPassword(null);
        assertNull("Password should be null", user.getPassword());
    }

    @Test
    public void testSetAndGetCity() {
        user.setCity("New York");
        assertEquals("City should be New York", "New York", user.getCity());
        
        user.setCity(null);
        assertNull("City should be null", user.getCity());
    }

    @Test
    public void testSetAndGetId() {
        user.setId(42);
        assertEquals("ID should be 42", 42, user.getId());
        
        user.setId(0);
        assertEquals("ID should be 0", 0, user.getId());
        
        user.setId(-1);
        assertEquals("ID should accept negative values", -1, user.getId());
    }

    @Test
    public void testToString() {
        user.setFirstName("Jack");
        user.setLastName("Smith");
        assertEquals("toString should return 'Jack Smith'", "Jack Smith", user.toString());
        
        user.setFirstName(null);
        user.setLastName("Smith");
        assertEquals("toString should handle null first name", "null Smith", user.toString());
        
        user.setFirstName("Jack");
        user.setLastName(null);
        assertEquals("toString should handle null last name", "Jack null", user.toString());
        
        user.setFirstName(null);
        user.setLastName(null);
        assertEquals("toString should handle both names null", "null null", user.toString());
    }

    @Test
    public void testAddInterest() {
        assertEquals("New user should have no interests", 0, user.getInterestCount());
        
        user.addInterest("Reading");
        assertEquals("Should have 1 interest after adding", 1, user.getInterestCount());
        
        LinkedList<String> interests = user.getInterests();
        interests.positionIterator();
        assertEquals("First interest should be Reading", "Reading", interests.getIterator());
        
        // Test adding null interest
        user.addInterest(null);
        assertEquals("Null interest should not be added", 1, user.getInterestCount());
        
        // Test adding empty interest
        user.addInterest("");
        assertEquals("Empty interest should not be added", 1, user.getInterestCount());
        
        // Test adding interest with whitespace
        user.addInterest("  Gaming  ");
        assertEquals("Interest with whitespace should be trimmed and added", 2, user.getInterestCount());
        
        interests.advanceIterator();
        assertEquals("Second interest should be Gaming (trimmed)", "Gaming", interests.getIterator());
    }
    
    @Test
    public void testRemoveInterest() {
        // Add some interests first
        user.addInterest("Reading");
        user.addInterest("Gaming");
        user.addInterest("Cooking");
        assertEquals("Should have 3 interests", 3, user.getInterestCount());
        
        // Remove middle interest
        assertTrue("Should successfully remove existing interest", user.removeInterest("Gaming"));
        assertEquals("Should have 2 interests after removal", 2, user.getInterestCount());
        
        // Verify remaining interests
        LinkedList<String> interests = user.getInterests();
        interests.positionIterator();
        assertEquals("First interest should still be Reading", "Reading", interests.getIterator());
        interests.advanceIterator();
        assertEquals("Second interest should now be Cooking", "Cooking", interests.getIterator());
        
        // Try to remove non-existent interest
        assertFalse("Should return false when removing non-existent interest", user.removeInterest("Swimming"));
        assertEquals("Interest count should not change", 2, user.getInterestCount());
        
        // Try to remove null interest
        assertFalse("Should return false when removing null interest", user.removeInterest(null));
        assertEquals("Interest count should not change", 2, user.getInterestCount());
        
        // Remove with whitespace
        assertTrue("Should remove interest despite whitespace", user.removeInterest("  Reading  "));
        assertEquals("Should have 1 interest after removal", 1, user.getInterestCount());
    }
    
    @Test
    public void testGetInterests() {
        // Add interests in specific order
        user.addInterest("Reading");
        user.addInterest("Gaming");
        user.addInterest("Cooking");
        
        // Get the list and verify order
        LinkedList<String> interests = user.getInterests();
        interests.positionIterator();
        
        assertEquals("First interest should be Reading", "Reading", interests.getIterator());
        interests.advanceIterator();
        assertEquals("Second interest should be Gaming", "Gaming", interests.getIterator());
        interests.advanceIterator();
        assertEquals("Third interest should be Cooking", "Cooking", interests.getIterator());

        interests.advanceIterator();
        assertTrue("Iterator should be at end", interests.offEnd());
    }
    
    @Test
    public void testGetInterestCount() {
        assertEquals("New user should have 0 interests", 0, user.getInterestCount());
        
        user.addInterest("Reading");
        assertEquals("Should have 1 interest", 1, user.getInterestCount());
        
        user.addInterest("Gaming");
        assertEquals("Should have 2 interests", 2, user.getInterestCount());
        
        user.removeInterest("Reading");
        assertEquals("Should have 1 interest after removal", 1, user.getInterestCount());
        
        user.removeInterest("Gaming");
        assertEquals("Should have 0 interests after removing all", 0, user.getInterestCount());
    }
    
    @Test
    public void testAddFriend() {
        User friend = new User();
        friend.setId(2);
        
        // Test adding a friend
        user.addFriend(friend);
        assertEquals("Should have 1 friend", 1, user.getFriendIds().size());
        assertEquals("Friend list should contain friend's ID", true, user.getFriendIds().contains(2));
        
        // Test adding same friend again
        user.addFriend(friend);
        assertEquals("Friend count should not change", 1, user.getFriendIds().size());
        
        // Test adding self as friend
        user.addFriend(user);
        assertEquals("Friend count should not change", 1, user.getFriendIds().size());
        
        // Test adding null friend
        user.addFriend(null);
        assertEquals("Friend count should not change", 1, user.getFriendIds().size());
    }
    
    @Test
    public void testRemoveFriend() {
        User friend1 = new User();
        friend1.setId(2);
        User friend2 = new User();
        friend2.setId(3);
        
        // Add friends first
        user.addFriend(friend1);
        user.addFriend(friend2);
        assertEquals("Should have 2 friends", 2, user.getFriendIds().size());
        
        // Test removing a friend
        user.removeFriend(friend1);
        assertEquals("Should have 1 friend after removal", 1, user.getFriendIds().size());
        assertEquals("Friend1 should not be in friend list", false, user.getFriendIds().contains(2));
        assertEquals("Friend2 should still be in friend list", true, user.getFriendIds().contains(3));
        
        // Test removing non-existent friend
        user.removeFriend(friend1);
        assertEquals("Friend count should not change", 1, user.getFriendIds().size());
        
        // Test removing null friend
        user.removeFriend(null);
        assertEquals("Friend count should not change", 1, user.getFriendIds().size());
        
        // Try removing self
        user.removeFriend(user);
        assertEquals("Friend count should not change", 1, user.getFriendIds().size());
    }
}
