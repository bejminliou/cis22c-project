package data;

import java.util.Objects;

/**
 * Interest.java
 * Interest class managing the details of an individual Interest.
 *
 * @author Benjamin Liou
 * @author Kenneth Garcia
 * @author Kevin Young
 * @author Rolen Louie
 * @author Tu Luong
 * @author Yukai Qiu
 * CIS 22C, Course Project
 */
public class Interest {
    private final String interestName;
    private final int interestID;

    // Constructors

    /**
     * Creates a new Interest with the given interestName and interestID.
     *
     * @param interestName the name of the Interest.
     * @param interestID   the ID of the Interest
     */
    public Interest(String interestName, int interestID) {
        this.interestName = interestName;
        this.interestID = interestID;
    }

    // Accessors

    /**
     * Gets the ID of the Interest.
     *
     * @return the ID of the Interest
     */
    public int getInterestID() {
        return interestID;
    }

    // Additional Methods

    /**
     * Overrides the hashCode method to return a hash of only the Interest's name.
     *
     * @return a hashcode of the Interest ONLY based on the interestName
     */
    @Override
    public int hashCode() {
        return Objects.hash(interestName.toLowerCase());
    }

    /**
     * Determines whether two Interest have the same name (ignoring case).
     *
     * @param obj the Object to compare to this
     * @return whether obj and this are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) { // comparing Interest to itself
            return true;
        } else if (!(obj instanceof Interest)) { // if comparing Interest to non-Interest or null
            return false;
        } else {
            Interest otherIn = (Interest) obj;
            return interestName.equalsIgnoreCase(otherIn.interestName);
        }
    }

    /**
     * Returns a String containing the Interest's information.
     *
     * @return a String containing the Interest name and id
     */
    @Override
    public String toString() {
        return "Interest{" +
                "interestName='" + interestName + '\'' +
                ", id=" + interestID +
                '}';
    }

}
