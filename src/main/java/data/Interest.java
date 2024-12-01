package data;

/**
 * Represents an interest in the system:
 * Each interest has a unique ID and name, and can be used to find users with
 * similar interests.
 *
 * @author Benjamin Liou
 * @see data.InterestManager for interest management and user grouping
 * @see data.User#addInterest for how interests are associated with users
 * @see model.Friend#getFriendRecommendations(int) for how interests are
 * associated with users
 * CIS 22C, Course Project
 */
public class Interest {
    private String name;
    private int id;

    /**
     * Creates a new Interest with the given name and ID
     *
     * @param name The name of the interest
     * @param id   The unique ID for the interest
     */
    public Interest(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Interest other = (Interest) obj;
        return name != null && name.equalsIgnoreCase(other.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.toLowerCase().hashCode() : 0;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Creates an Interest object from its string representation
     * Used for parsing interests from HashTable toString output
     *
     * @param str The string representation of the interest
     * @return A new Interest object, or null if string is invalid
     */
    public static Interest fromString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        return new Interest(str.trim(), -1); // ID doesn't matter for lookup...
    }
}
