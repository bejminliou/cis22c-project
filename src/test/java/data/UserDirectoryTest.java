package data;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import util.BST;

public class UserDirectoryTest {
    private UserDirectory directory;
    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
        directory = new UserDirectory();
        
        // Create test users
        user1 = new User();
        user1.setId(1);
        user1.setFirstName("George");
        user1.setLastName("Washington");
        user1.setUserName("gwashington");
        
        user2 = new User();
        user2.setId(2);
        user2.setFirstName("John");
        user2.setLastName("Adams");
        user2.setUserName("jadams");
        
        user3 = new User();
        user3.setId(3);
        user3.setFirstName("Thomas");
        user3.setLastName("Jefferson");
        user3.setUserName("tjeff");
    }

    @Test
    public void testAddUser() {
        directory.addUser(user1);
        assertEquals("Directory should have 1 user", 1, directory.getUserCount());
        
        directory.addUser(user2);
        assertEquals("Directory should have 2 users", 2, directory.getUserCount());
    }

    @Test
    public void testRemoveUser() {
        directory.addUser(user1);
        directory.addUser(user2);
        
        directory.removeUser(user1);
        assertEquals("Directory should have 1 user after removal", 1, directory.getUserCount());
        assertNull("Should not find removed user", directory.findUserByUsername("gwashington"));
    }

    @Test
    public void testFindUserByUsername_ExistingUser() {
        directory.addUser(user1);
        directory.addUser(user2);
        
        User found = directory.findUserByUsername("gwashington");
        assertNotNull("Should find existing user", found);
        assertEquals("Should find correct user", user1.getId(), found.getId());
    }

    @Test
    public void testFindUserByUsername_NonexistentUser() {
        directory.addUser(user1);
        directory.addUser(user2);
        
        User found = directory.findUserByUsername("nonexistent");
        assertNull("Should not find nonexistent user", found);
    }

    @Test
    public void testFindUserByUsername_NullUsername() {
        directory.addUser(user1);
        
        User found = directory.findUserByUsername(null);
        assertNull("Should handle null username", found);
    }

    @Test
    public void testFindUserByUsername_CaseSensitive() {
        directory.addUser(user1);
        
        User found = directory.findUserByUsername("GWASHINGTON");
        assertNull("Username search should be case sensitive", found);
    }

    @Test
    public void testAddDuplicateUser() {
        directory.addUser(user1);
        
        // Create duplicate user with same name but different ID/username
        User duplicate = new User();
        duplicate.setId(4);
        duplicate.setFirstName("George");
        duplicate.setLastName("Washington");
        duplicate.setUserName("gwash2");
        
        directory.addUser(duplicate);
        assertEquals("Should allow users with same name", 2, directory.getUserCount());
    }

    @Test
    public void testDuplicateNameHandling() {
        // Create two users with the same name but different usernames
        User duplicateUser1 = new User();
        duplicateUser1.setId(4);
        duplicateUser1.setFirstName("John");
        duplicateUser1.setLastName("Smith");
        duplicateUser1.setUserName("jsmith1");
        duplicateUser1.setCity("New York");

        User duplicateUser2 = new User();
        duplicateUser2.setId(5);
        duplicateUser2.setFirstName("John");
        duplicateUser2.setLastName("Smith");
        duplicateUser2.setUserName("jsmith2");
        duplicateUser2.setCity("Los Angeles");

        // Add both users to the directory
        directory.addUser(duplicateUser1);
        directory.addUser(duplicateUser2);

        // Verify both users were added successfully
        assertEquals("Directory should have 2 users", 2, directory.getUserCount());
        assertNotNull("Should find first user by username", directory.findUserByUsername("jsmith1"));
        assertNotNull("Should find second user by username", directory.findUserByUsername("jsmith2"));

        // Verify users can be retrieved and have correct properties
        User found1 = directory.findUserByUsername("jsmith1");
        User found2 = directory.findUserByUsername("jsmith2");

        assertEquals("First user should have correct ID", 4, found1.getId());
        assertEquals("Second user should have correct ID", 5, found2.getId());
        assertEquals("Users should have same first name", found1.getFirstName(), found2.getFirstName());
        assertEquals("Users should have same last name", found1.getLastName(), found2.getLastName());
        assertNotEquals("Users should have different usernames", found1.getUserName(), found2.getUserName());

        // Print directory contents
        System.out.println("\nDirectory contents with duplicate names:");
        System.out.println(directory.toString());
    }

    @Test
    public void testToString() {
        // Test empty directory
        System.out.println("Empty Directory toString():");
        System.out.println(directory.toString());
        System.out.println();

        // Create and add some test users
        User user1 = new User();
        user1.setId(1);
        user1.setFirstName("John");
        user1.setLastName("Smith");
        user1.setUserName("jsmith");
        user1.setCity("New York");
        user1.addInterest("Reading");
        user1.addInterest("Gaming");
        
        User user2 = new User();
        user2.setId(2);
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setUserName("jdoe");
        user2.setCity("Los Angeles");
        // No interests for user2 to test empty interests list
        
        User user3 = new User();
        user3.setId(3);
        // No name set to test null handling
        user3.setUserName("user3");
        // No city set to test null handling
        user3.addInterest("Cooking");

        // Add users to directory
        directory.addUser(user1);
        directory.addUser(user2);
        directory.addUser(user3);

        // Add some friends to test friend count
        user1.addFriend(user2);
        user1.addFriend(user3);
        user2.addFriend(user1);

        System.out.println("Directory with 3 users toString():");
        System.out.println(directory.toString());

        // Verify basic expectations
        String result = directory.toString();
        assertTrue("Should contain header", result.contains("User Directory Contents"));
        assertTrue("Should show total users", result.contains("Total Users: 3"));
        assertTrue("Should contain user1's name", result.contains("John Smith"));
        assertTrue("Should contain user2's city", result.contains("Los Angeles"));
        assertTrue("Should show user3's missing name", result.contains("(no first name)"));
        assertTrue("Should show user2's empty interests", result.contains("Interests: (none)"));
        assertTrue("Should show user1's friend count", result.contains("Friend Count: 2"));
    }
}
