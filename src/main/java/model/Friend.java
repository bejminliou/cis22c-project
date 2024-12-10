package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

import data.User;
import data.UserDirectory;

import util.Graph;
import util.LinkedList;

/**
 * Friend.java
 * Search utility for finding users and analyzing network relationships.
 * Uses UserDirectory as the source truth for user data, providing
 * name-based search and network connection analysis through a graph.
 *
 * @author Benjamin Liou
 * @author Kevin Young
 * @author Rolen Louie
 * @see data.UserDirectory for user storage and management
 * @see data.User for individual user's friend connections
 * CIS 22C, Course Project
 */
public class Friend {
    private final Graph friendGraph;
    private final UserDirectory userDirectory;

    public Friend(UserDirectory userDirectory) {
        friendGraph = userDirectory.getFriendNetwork();
        this.userDirectory = userDirectory;
    }

    /**
     * Private static class for temporarily storing a friend along with
     * an associated score. This class is useful for sorting a
     * user's friend recommendation by score, and their user object
     */
    private static class FriendTempClass {
        private final User userObject;
        private final double score;

        /**
         * Constructor for storing the friend user and their score.
         *
         * @param userObject friend's user object to return.
         * @param score      friend's score recommendation based on calculateScore
         */
        public FriendTempClass(User userObject, double score) {
            this.userObject = userObject;
            this.score = score;
        }

        /**
         * Gets the Friend's object
         *
         * @return will return friend object
         */
        public User getObject() {
            return this.userObject;
        }

        /**
         * Gets the friend's recommendation score
         *
         * @return will return the recommendation score
         */
        public double getScore() {
            return score;
        }
    }

    /**
     * Gets friend recommendations for a given user by calculating each
     * User's recommendation score to the given user and adding Users
     * with a good score to an ArrayList.
     *
     * @param user the User to find recommended friends for
     * @return an ArrayList of Users that are recommended (by algorithm) to the user
     */
    public ArrayList<User> getFriendRecommendations(User user) {
        int interestScore = 0;
        ArrayList<User> recommendations = new ArrayList<>();
        ArrayList<FriendTempClass> pFriendsList = new ArrayList<>();

        try {
            friendGraph.BFS(user.getId());  // BFS updates the distance array
        } catch (IndexOutOfBoundsException e) {
            return new ArrayList<>(); // Return an empty list in case of error
        }

        // Get the current friends of the user
        LinkedList<Integer> currentFriends = friendGraph.getAdjacencyList(user.getId());
        ArrayList<Integer> currentFriendsList = new ArrayList<>();
        currentFriends.positionIterator();
        while (!currentFriends.offEnd()) {
            currentFriendsList.add(currentFriends.getIterator());
            currentFriends.advanceIterator();
        }

        // Iterate over all users in the directory and find recommendations
        for (User potentialFriend : userDirectory.getUsersAL()) {
            if (!potentialFriend.equals(user) && !currentFriendsList.contains(potentialFriend.getId())) {
                int dist = friendGraph.getDistance(potentialFriend.getId());  // Get distance from the user
                if (dist >= 2 && dist <= 4) {// Check valid distance for recommendation

                    StringTokenizer StringTokenized = new StringTokenizer(potentialFriend.getInterests().toString());
                    while (StringTokenized.hasMoreTokens()) {
                        String next = StringTokenized.nextToken();
                        if (user.getInterests().toString().contains(next)) {
                            interestScore++;
                        }
                    }

                    FriendTempClass pUser = new FriendTempClass(potentialFriend, calculateScore(dist, interestScore));

                    pFriendsList.add(pUser);
                }
            }
        }
        Collections.sort(pFriendsList, (o1, o2) ->
                Double.compare(o2.getScore(), o1.getScore()));
        for (FriendTempClass friendTemp : pFriendsList) {
            if (friendTemp.getObject() == user) {
                continue;
            }
            recommendations.add(friendTemp.getObject());
        }
        return recommendations;
    }

    /**
     * Calculates the score for a relationship between two Users based on their distance
     * in friendGraph and interest score. The distance is influenced by mutual friends
     * (tracked in the Graph of Users) and the interest score is influenced by shared Interests,
     * with shared Interests having a bigger weight on the score than distance.
     *
     * @param dist          the distance between two Users in the friendGraph
     * @param interestScore the number of shared interests between two Users
     * @return the calculated score for the friendship between two Users, with a higher
     * score indicating a higher likelihood of being recommended as a friend
     */
    private double calculateScore(int dist, int interestScore) {
        final double interestWeight = 2.0;  // Give shared interests more weight
        final double distanceWeight = 1.0; // Give distance less weight

        // Normalize distance: Min dist = 2, Max dist = 8
        final int minDist = 2;
        final int maxDist = 4;
        double normalizedDistance = (double) (dist - minDist) / (maxDist - minDist);

        // Calculate final score based on weighted formula
        return (interestWeight * interestScore) - (distanceWeight * normalizedDistance);
    }

}
