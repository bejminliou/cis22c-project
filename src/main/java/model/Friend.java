package model;

import util.Graph;
import util.LinkedList;
import data.UserDirectory;

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
public class Friend {
    private final Graph friendGraph;

    /**
     * Creates a new friend search utility with the Graph of friend connections
     * from UserDirectory.
     *
     * @param userDirectory the UserDirectory to get the friend network (as a Graph) from
     */
    public Friend(UserDirectory userDirectory) {;
        this.friendGraph = userDirectory.getFriendNetwork();
    }

    /**
     * Adds a user's friend connections to the graph representation
     * Updates or creates the adjacency list for the user's ID
     *
     * @param user User whose connections to add
     * @see util.Graph for graph implementation details
     * @see data.User#getFriendIds() for friend ID retrieval
     */
    private void addToGraph(User user) {
        if (user == null)
            return;

        // Ensure space for user
        while (friendGraph.size() <= user.getId()) {
            friendGraph.add(new LinkedList<>());
        }

        LinkedList<Integer> userFriends = friendGraph.get(user.getId());
        userFriends.clear();
        userFriends.addAll(user.getFriendIds());
    }


    /**
     * Get all users directly connected to specified user in the network
     * Returns an empty list for invalid user IDs or users with no connections
     *
     * @param userId ID of user to get connections for
     * @return List of connected user IDs (defensive copy)
     * @see util.Graph for graph traversal concepts
     * @see data.User#getFriendIds() for friend relationship storage
     */
    public List<Integer> getConnections(int userId) {
        if (userId < 0 || userId >= friendGraph.size()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(friendGraph.get(userId));
    }

    /**
     * Check if two users are directly connected in the network
     * A direct connection: if the users are in each other's friend lists
     *
     * @param userId1 First user's ID
     * @param userId2 Second user's ID
     * @return true if users are connected
     * @see util.Graph for graph structure details
     * @see data.User#getFriendIds() for friend relationship verification
     */
    public boolean areConnected(int userId1, int userId2) {
        if (userId1 < 0 || userId2 < 0 ||
                userId1 >= friendGraph.size() ||
                userId2 >= friendGraph.size()) {
            return false;
        }
        return friendGraph.get(userId1).contains(userId2);
    }

    /**
     * Refresh the network graph with current user data
     * Rebuilds the entire graph to ensure consistency with UserDirectory
     * Call this when friend relationships change
     *
     * @see util.Graph for graph structure details
     * @see data.UserDirectory#getAllUsers() for user iteration
     */
    public void refreshNetwork() {
        friendGraph.clear();

        for (User user : userDirectory.getUsersAL()) {
            addToGraph(user);
        }
    }

    /**
     * Helper method to find a user by their ID
     *
     * @param userId ID to search for
     * @return User with matching ID, or null if not found
     */
    private User findUserById(int userId) {
        for (User user : userDirectory.getUsersAL()) {
            if (user.getId() == userId) {
                return user;
            }
        }
        return null;
    }

    /**
     * Get friend recommendations for a user using Breadth First Search
     * Finds potential friends within the user's extended network (friends of
     * friends)
     * Only includes users who share at least one connection in the network
     * Does not include the user's existing friends or the user themselves
     *
     * @param userId ID of user to get recommendations for
     * @return List of recommended User objects, sorted by network distance
     * @see util.Graph for BFS implementation details
     * @see data.User#getFriendIds() for existing friend filtering
     */
    public ArrayList<User> getBaseRecommendations(int userId) {
        if (userId < 0 || userId >= friendGraph.size()) {
            return new ArrayList<>();
        }

        ArrayList<User> recommendations = new ArrayList<>();
        boolean[] visited = new boolean[friendGraph.size()];
        LinkedList<Integer> queue = new LinkedList<>();

        // exclusion
        visited[userId] = true;
        for (int friendId : friendGraph.get(userId)) {
            visited[friendId] = true;
        }

        queue.addAll(friendGraph.get(userId));

        while (!queue.isEmpty()) {
            int currentId = queue.poll();

            for (int potentialFriendId : friendGraph.get(currentId)) {
                if (!visited[potentialFriendId]) {
                    visited[potentialFriendId] = true;
                    queue.add(potentialFriendId);

                    User potentialFriend = findUserById(potentialFriendId);
                    if (potentialFriend != null) {
                        recommendations.add(potentialFriend);
                    }
                }
            }
        }

        return recommendations;
    }

    /**
     * Helper class to store recommendation with ranking information
     */
    private class RecommendationRank implements Comparable<RecommendationRank> {
        User user;
        double interestSimilarity;
        int distance;
        double score;

        RecommendationRank(User user, double interestSimilarity, int distance) {
            this.user = user;
            this.interestSimilarity = interestSimilarity;
            this.distance = distance;
            // Score formula ; interest similarity has more weight than network distance
            // Distance is inverted (1/distance), so closer users have higher scores
            // (e.g. 0.3 * 1/1 = 0.3, 0.3 * 1/2 = 0.15)
            this.score = (0.7 * interestSimilarity) + (0.3 * (1.0 / distance));
        }

        @Override
        public int compareTo(RecommendationRank other) {
            // by descending order
            return Double.compare(other.score, this.score);
        }
    }

    /**
     * Calculate interest similarity between two users using Jaccard similarity
     *
     * @param user1 First user
     * @param user2 Second user
     * @return Similarity score between 0 and 1
     * @see https://medium.com/@mayurdhvajsinhjadeja/jaccard-similarity-34e2c15fb524
     */
    private double calculateInterestSimilarity(User user1, User user2) {
        util.LinkedList<String> interests1 = user1.getInterests();
        util.LinkedList<String> interests2 = user2.getInterests();

        if (interests1.isEmpty() || interests2.isEmpty()) {
            return 0.0;
        }

        // Count shared interests
        int sharedCount = 0;
        interests1.positionIterator();

        while (!interests1.offEnd()) {
            String interest1 = interests1.getIterator();

            interests2.positionIterator();
            while (!interests2.offEnd()) {
                if (interest1.equals(interests2.getIterator())) {
                    sharedCount++;
                    break;
                }
                interests2.advanceIterator();
            }

            interests1.advanceIterator();
        }

        // Jaccard: intersection size / union size
        int unionSize = interests1.getLength() + interests2.getLength() - sharedCount;
        return unionSize > 0 ? (double) sharedCount / unionSize : 0.0;
    }

    /**
     * Sort friend recommendations by interest similarity and network distance
     *
     * @param userId ID of user to get recommendations for
     * @return List of recommended users, sorted by relevance
     */
    public ArrayList<User> getFriendRecommendations(int userId) {
        // base recommendations, BFS
        ArrayList<User> baseRecommendations = getBaseRecommendations(userId);
        if (baseRecommendations.isEmpty()) {
            return baseRecommendations;
        }

        User sourceUser = findUserById(userId);
        if (sourceUser == null) {
            return baseRecommendations;
        }

        // Calculate scores for each recommendation
        ArrayList<RecommendationRank> rankedRecommendations = new ArrayList<>();
        for (User recommendedUser : baseRecommendations) {
            // Calculate network distance using BFS
            int distance = 1;
            boolean[] visited = new boolean[friendGraph.size()];
            LinkedList<Integer> queue = new LinkedList<>();

            visited[userId] = true;
            queue.add(userId);

            boolean found = false;
            int currentLevelSize = 1;
            int nextLevelSize = 0;

            while (!queue.isEmpty() && !found) {
                int currentId = queue.poll();
                currentLevelSize--;

                for (int neighborId : friendGraph.get(currentId)) {
                    if (!visited[neighborId]) {
                        if (neighborId == recommendedUser.getId()) {
                            found = true;
                            break;
                        }
                        visited[neighborId] = true;
                        queue.add(neighborId);
                        nextLevelSize++;
                    }
                }

                if (currentLevelSize == 0) {
                    distance++;
                    currentLevelSize = nextLevelSize;
                    nextLevelSize = 0;
                }
            }

            double similarity = calculateInterestSimilarity(sourceUser, recommendedUser);
            rankedRecommendations.add(new RecommendationRank(recommendedUser, similarity, distance));
        }

        // Sort by combined score
        Collections.sort(rankedRecommendations);

        // Convert back to list of users
        ArrayList<User> result = new ArrayList<>();
        for (RecommendationRank rank : rankedRecommendations) {
            result.add(rank.user);
        }

        return result;
    }
}
