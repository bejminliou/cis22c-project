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

    // Accessors

    /**
     * Gets the name of the Interest.
     *
     * @return the name of Interest
     */
    public String getName() {
        return interestName;
    }

    /**
     * Gets the ID of the Interest.
     * @return the ID of the Interest
     */
    public int getInterestID() {
        return interestID;
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

    /**
     * Overrides the hashCode method to return a hash of only an Interest's name.
     *
     * @return a hashcode of an Interest ONLY based on the interestName
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
