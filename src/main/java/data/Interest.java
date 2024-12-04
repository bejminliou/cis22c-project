package data;

import java.util.Objects;

/**
 * Interest.java
 * Interest class managing the details of an individual interest.
 *
 * @author Rolen Louie
 * CIS 22C, Course Project
 */
public class Interest {
    private String interestName;
    private int interestID;

    // Constructors

    /**
     * Creates a new Interest with the given interestName and interestID.
     *
     * @param interestName the name of the Interest.
     * @param interestID the ID of the Interest
     */
    public Interest(String interestName, int interestID) {
        this.interestName = interestName;
        this.interestID = interestID;
    }

    public String getName() {
        return interestName;
    }

    public int getId() {
        return id;
    }

    // Mutators

    /**
     * Sets the name of the Interest to the given name.
     *
     * @param interestName the name of the interest
     */
    public void setName(String interestName) {
        this.interestName = interestName;
    }

    /**
     * Sets the ID of the Interest to the given ID.
     *
     * @param interestID the ID of the interest
     */
    public void setId(int interestID) {
        this.interestID = interestID;
    }
}
