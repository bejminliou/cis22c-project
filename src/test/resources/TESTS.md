# Testing and Validation for Presidential Social Network Project

## Overview
This document outlines the testing strategy and guidelines for the Presidential Social Network project. Each test class is designed to verify specific components of the system 🙂✨

## Running Tests
Tests can be run using Maven:
```bash
mvn test                 # Run all tests
mvn test -Dtest=WhichTest # Run specific test class
```

## Test Structure
Each test class follows a standard structure:
```java
public class ComponentTest {
    @Test
    public void testSpecificFeature() {
        // Arrange - Set up test data
        // Act - Perform the operation
        // Assert - Verify the results
    }
}
```

## Test Classes

### `Data` Package Tests
1. **InterestManagerTest.java**
   - `Purpose`: Test interest management and recommendation system
   - Key test cases:
     * Interest creation and assignment
     * Interest-based user matching
     * Interest recommendation algorithms
     * Interest group management
     * Multiple interest handling
     * Interest removal and cleanup

2. **UserTest.java**
   - `Purpose`: Test core user functionality and profile management
   - Key test cases:
     * User profile creation and validation
     * Profile data management
     * Interest association/dissociation
     * Friend relationship handling
     * User data persistence
     * Profile update validation

3. **UserDirectoryTest.java**
   - `Purpose`: Test user lookup and management system
   - Key test cases:
     * User registration and storage
     * Efficient user lookup operations
     * Username uniqueness validation
     * User search functionality
     * Directory consistency checks
     * User removal and cleanup
     * Directory persistence

4. **AuthTest.java**
   - `Purpose`: Test registration and authentication
   - Key test cases:
     * User registration workflow
     * Login validation
     * Invalid access attempts ((?) ui)

### `Model` Package Tests
1. **AccountTest.java**
   - `Purpose`: Test account management
   - Key test cases:
     * Account creation workflow
     * Account state management
     * Account deletion process (?)

2. **FriendTest.java**
   - `Purpose`: Test friendship system and social connections
   - Key test cases:
     * Friend list management
     * Friend recommendation accuracy
     * Social network traversal
     * Friend adding and removal (?)

### `IO` Package Tests
1. **DataStorageTest.java**
   - `Purpose`: Test data persistence and recovery
   - Key test cases:
     * User data serialization
     * User data persistence
     * Friend network storage (?)
     * Interest data management (?)
     * Data format validation
     * Backup and restore operations

## Testing Guidelines

### 1. Test Data
- Use the presidential dataset if you want realistic testing scenarios
- Create small, focused test datasets for specific cases
- Include edge cases and boundary conditions

### 2. Test Coverage
- Keep tests independent and isolated (no logging in main classes pls)
- Aim for comprehensive coverage of core functionality
- Include boundary conditions and edge cases
- Test data structure-specific operations thoroughly
- Test every single method if possible :)

### 4. Common Test Scenarios
- Valid/Invalid input handling
- Null value handling
- Empty collection handling
- Duplicate data handling
- Error conditions and recovery

## Example Test Pattern
```java
@Test
public void testSetAndGetUsername() {
    // arrange
    User user = new User();
    
    // act
    user.setUserName("jsmith");
    
    // assert
    assertEquals("Username should be jsmith", "jsmith", user.getUserName());
    
    // test null case
    user.setUserName(null);
    assertNull("Username should be null", user.getUserName());
}
```

## Troubleshooting
1. **Test Failures**
   - Always check test data setup first
   - If setup is correct, then verify expected vs. actual results
   - Ctrl + Z
   - Check for environment-specific issues
   - Consult team leader

2. **Common Issues**
   - Incorrect test data
   - Environment configuration

## Future Improvements
- Comprehensive UI / Walkthrough tests
- Improve error scenario coverage
- Improve test feedback
