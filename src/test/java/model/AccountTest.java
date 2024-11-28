package model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import data.User;
import data.Auth;
import data.UserDirectory;

/**
 * Test account management functionality
 */
public class AccountTest {
    private Account account;
    private User testUser;
    private UserDirectory testDirectory;

    @Before
    public void setUp() {
        testDirectory = new UserDirectory();
        account = new Account();
        testUser = new User();
        testUser.setUserName("testuser");
        testUser.setPassword("testpass");
        testDirectory.addUser(testUser);
    }

    /**
     * Test Account constructor with a test UserDirectory
     * @see Account#Account(UserDirectory) for the method being tested
     */
    @Test
    public void testAccountConstructorWithUserDirectory() {
        Account testAccount = new Account(testDirectory);
        assertSame("The Account should use the provided UserDirectory instance",
                testUser, testAccount.getUser(testUser.getUserName()));
    }

    /**
     * Test account creation with valid user
     * @see Account#createAccount(User) for the method being tested
     */
    @Test
    public void testCreateAccountValid() {
        assertTrue("Should create account with valid user",
                  account.createAccount(testUser));
        assertTrue("Should find created username",
                  account.isUsernameTaken(testUser.getUserName()));
    }

    /**
     * Test account creation with null user
     * @see Account#createAccount(User) for the method being tested
     */
    @Test
    public void testCreateAccountNull() {
        assertFalse("Should reject null user",
                   account.createAccount(null));
    }

    /**
     * Test account creation with null username
     * @see Account#createAccount(User) for the method being tested
     */
    @Test
    public void testCreateAccountNullUsername() {
        User invalidUser = new User();
        invalidUser.setPassword("pass");
        assertFalse("Should reject null username",
                   account.createAccount(invalidUser));
    }

    /**
     * Test account creation with null password
     * @see Account#createAccount(User) for the method being tested
     */
    @Test
    public void testCreateAccountNullPassword() {
        User invalidUser = new User();
        invalidUser.setUserName("user");
        assertFalse("Should reject null password",
                   account.createAccount(invalidUser));
    }

    /**
     * Test duplicate account creation
     * @see Account#createAccount(User) for the method being tested
     */
    @Test
    public void testCreateAccountDuplicate() {
        account.createAccount(testUser);
        
        User duplicateUser = new User();
        duplicateUser.setUserName(testUser.getUserName());
        duplicateUser.setPassword("differentpass");
        
        assertFalse("Should reject duplicate username",
                   account.createAccount(duplicateUser));
    }

    /**
     * Test login with valid credentials
     * @see Account#login(String, String) for the method being tested
     */
    @Test
    public void testLoginValid() {
        account.createAccount(testUser);
        assertTrue("Should login with correct credentials",
                  account.login(testUser.getUserName(), testUser.getPassword()));
    }

    /**
     * Test login with invalid password
     * @see Account#login(String, String) for the method being tested
     */
    @Test
    public void testLoginInvalidPassword() {
        account.createAccount(testUser);
        assertFalse("Should reject wrong password",
                   account.login(testUser.getUserName(), "wrongpass"));
    }

    /**
     * Test login with nonexistent user
     * @see Account#login(String, String) for the method being tested
     */
    @Test
    public void testLoginNonexistentUser() {
        assertFalse("Should reject nonexistent user",
                   account.login("nonexistent", "anypass"));
    }

    /**
     * Test login with null username
     * @see Account#login(String, String) for the method being tested
     */
    @Test
    public void testLoginNullUsername() {
        assertFalse("Should reject null username",
                   account.login(null, "pass"));
    }

    /**
     * Test login with null password
     * @see Account#login(String, String) for the method being tested
     */
    @Test
    public void testLoginNullPassword() {
        assertFalse("Should reject null password",
                   account.login("user", null));
    }

    /**
     * Test get user with existing username
     * @see Account#getUser(String) for the method being tested
     */
    @Test
    public void testGetUserExists() {
        account.createAccount(testUser);
        User retrieved = account.getUser(testUser.getUserName());
        assertNotNull("Should find existing user", retrieved);
        assertEquals("Should retrieve correct username",
                    testUser.getUserName(), retrieved.getUserName());
        assertEquals("Should retrieve correct password",
                    testUser.getPassword(), retrieved.getPassword());
    }

    /**
     * Test get user with nonexistent username
     * @see Account#getUser(String) for the method being tested
     */
    @Test
    public void testGetUserNonexistent() {
        assertNull("Should return null for nonexistent user",
                  account.getUser("nonexistent"));
    }

    /**
     * Test username taken check with existing user
     * @see Account#isUsernameTaken(String) for the method being tested
     */
    @Test
    public void testIsUsernameTakenExists() {
        account.createAccount(testUser);
        assertTrue("Should find existing username",
                  account.isUsernameTaken(testUser.getUserName()));
    }

    /**
     * Test username taken check with nonexistent user
     * @see Account#isUsernameTaken(String) for the method being tested
     */
    @Test
    public void testIsUsernameTakenNonexistent() {
        assertFalse("Should not find nonexistent username",
                   account.isUsernameTaken("nonexistent"));
    }

    /**
     * Test getAuth method
     * @see Account#getAuth() for the method being tested
     */
    @Test
    public void testGetAuth() {
        Auth authInstance = account.getAuth();
        assertNotNull("Auth instance should not be null", authInstance);
    }

}
