
package model;

import util.Graph;
import util.LinkedList;
import data.UserDirectory;
import data.User;

import java.util.Collections;
import java.util.StringTokenizer;

/**
 * Friend.java
 * Search utility for finding users and analyzing network relationships.
 * Uses UserDirectory as the source truth for user data, providing
 * name-based search and network connection analysis through a graph.
 *
 * @author Benjamin Liou
 * @author Kevin Young
 * @see data.UserDirectory for user storage and management
 * @see data.User for individual user's friend connections
 * CIS 22C, Course Project
 */

import java.util.ArrayList;

public class Friend {
    private final Graph friendGraph;
    private final UserDirectory userDirectory;
    public Friend(UserDirectory userDirectory) {
        friendGraph = userDirectory.getFriendNetwork();
        this.userDirectory = userDirectory;
    }

    private class FriendTempClass {
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
     *
     * @param user
     * @return
     */
    public ArrayList<User> getFriendRecommendations(User user) {
        int interestScore = 0;
        ArrayList<User> recommendations = new ArrayList<>();
        ArrayList<FriendTempClass> pFriendsList = new ArrayList<>();
        ArrayList<Double> scoreTracker = new ArrayList<>();// Stores recommended friends
        try {
            friendGraph.BFS(user.getId());  // BFS updates the distance array
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error during BFS: " + e.getMessage());
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
        Collections.sort(pFriendsList, (o1, o2) -> Double.compare(o2.getScore(), o1.getScore()));
        for(FriendTempClass friendTemp : pFriendsList) {
            recommendations.add(friendTemp.getObject());
        }
        return recommendations;
    }

    /**
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

