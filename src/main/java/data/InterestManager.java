package data;

import java.util.ArrayList;

import util.HashTable;
import util.BST;

/**
 * InterestManager.java
 * Manages the existing Interests and tracks which Users share each Interest.
 *
 * @author Rolen Louie
 * @author Kenneth Garcia
 * @author Kevin Young
 * CIS 22C, Course Project
 */
public class InterestManager {
    private final HashTable<Interest> interestHashTable; // storing all existing Interests
    private final ArrayList<BST<User>> usersSharedInterest; // tracking which Users share each Interest

    /**
     * Default Constructor for InterestManager initializing interestHashTable
     * with the set INITIAL_CAPACITY and usersSharedInterest as empty.
     */
    public InterestManager() {
        final int INITIAL_CAPACITY = 100;

        interestHashTable = new HashTable<>(INITIAL_CAPACITY);
        usersSharedInterest = new ArrayList<>();
    }

    /**
     * Adds User to interest, if interest doesn't exist, adds interests and user to said interest.
     *
     * @param interestName the name of the Interest
     * @param user         the User to add the Interest to
     */
    public void addUserToInterest(String interestName, User user) {
        Interest temp = new Interest(interestName, -1);
        Interest checkedInterest = interestHashTable.get(temp);

        if (checkedInterest == null) {
            int interestId = usersSharedInterest.size();
            Interest newInterest = new Interest(interestName, interestId);
            interestHashTable.add(newInterest);
            usersSharedInterest.add(new BST<>());
            checkedInterest = newInterest;
        }

        int id = checkedInterest.getInterestID();
        usersSharedInterest.get(id).insert(user, UserDirectory.nameComparator);
    }

    /**
     * Retrieves a BST containing users that share an existing Interest.
     *
     * @param interestName the given name of an Interest
     * @return a BST of Users that share the given Interest
     */
    public BST<User> retrieveInterestBST(String interestName) {
        Interest tempInterest = interestHashTable.get(new Interest(interestName, -1));

        if (tempInterest != null) {
            int id = tempInterest.getInterestID();
            return usersSharedInterest.get(id);
        }
        return null;
    }

}
