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
     * INCOMPLETE
     */
    private static class FriendTempClass {
        private final User userObject;
        private final double score;

        public FriendTempClass(User userObject, double score) {
            this.userObject = userObject;
            this.score = score;
        }

        public User getObject() {
            return this.userObject;
        }

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

                    StringTokenizer StringTokenized = new StringTokenizer(potentialFriend.getInterestsByString());
                    while (StringTokenized.hasMoreTokens()) {
                        String next = StringTokenized.nextToken();
                        if (user.getInterestsByString().contains(next)) {
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
     * INCOMPLETE
     *
     * @param dist
     * @param interestScore
     * @return
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

