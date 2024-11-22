package data;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Auth functionality
 */
public class AuthTest {
    private Auth auth;
    private UserDirectory userDirectory;
    private User testUser;
    
    @Before
    public void setUp() {
        userDirectory = new UserDirectory();
        auth = new Auth(userDirectory);
        testUser = new User();
        testUser.setUserName("testuser");
        testUser.setPassword("testpass");
    }
    
    @Test
    public void testRegisterNewUser() {
        assertTrue("Should successfully register new user", 
                  auth.register("newuser", "password123"));
        assertEquals("Should have one registered user", 
                    1, auth.getRegisteredUserCount());
    }
    
    @Test
    public void testRegisterDuplicateUser() {
        // First register a user through Auth
        auth.register("user1", "pass1");
        
        // Try to register with same username
        assertFalse("Should fail to register duplicate username",
                   auth.register("user1", "pass2"));
        assertEquals("Should still have one registered user",
                    1, auth.getRegisteredUserCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRegisterNullUsername() {
        auth.register(null, "password");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRegisterEmptyUsername() {
        auth.register("", "password");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRegisterNullPassword() {
        auth.register("username", null);
    }
    
    @Test
    public void testAuthenticateValidCredentials() {
        // Register user through Auth
        auth.register("validuser", "validpass");
        assertTrue("Should authenticate with correct credentials",
                  auth.authenticate("validuser", "validpass"));
    }
    
    @Test
    public void testAuthenticateInvalidPassword() {
        auth.register("user1", "correctpass");
        assertFalse("Should fail with wrong password",
                   auth.authenticate("user1", "wrongpass"));
    }
    
    @Test
    public void testAuthenticateNonexistentUser() {
        assertFalse("Should fail for nonexistent user",
                   auth.authenticate("nonexistent", "anypass"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAuthenticateNullUsername() {
        auth.authenticate(null, "password");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAuthenticateNullPassword() {
        auth.authenticate("username", null);
    }
    
    @Test
    public void testRegisterUserValid() {
        assertTrue("Should register valid User object",
                  auth.registerUser(testUser));
        assertTrue("Should find registered username",
                  auth.isUserRegistered(testUser.getUserName()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRegisterUserNull() {
        auth.registerUser(null);
    }
    
    @Test
    public void testIsUserRegistered() {
        auth.register("existinguser", "pass");
        assertTrue("Should find existing user",
                  auth.isUserRegistered("existinguser"));
        assertFalse("Should not find nonexistent user",
                   auth.isUserRegistered("nonexistent"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testIsUserRegisteredNull() {
        auth.isUserRegistered(null);
    }
    
    @Test
    public void testGetRegisteredUserCount() {
        assertEquals("Should start with zero users",
                    0, auth.getRegisteredUserCount());
        
        auth.register("user1", "pass1");
        assertEquals("Should have one user",
                    1, auth.getRegisteredUserCount());
        
        auth.register("user2", "pass2");
        assertEquals("Should have two users",
                    2, auth.getRegisteredUserCount());
        
        // Duplicate registration should not increase count
        auth.register("user1", "newpass");
        assertEquals("Should still have two users",
                    2, auth.getRegisteredUserCount());
    }
}
