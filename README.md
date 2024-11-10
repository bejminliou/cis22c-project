# cis22c-project
F24 CIS D022C Data Abstract &amp; Structures 62Z Parrish 22855

# Project: U.S. Presidents Social Network

This project simulates a social network platform specifically for U.S. Presidents (dataset TBD). It implements core social networking features like adding friends, viewing profiles, and provides friend recommendations using a graph-based approach.  This project is for CIS 22C and fulfills the requirements outlined by Jennifer Parrish.

## Project Structure

The project follows a standard Maven structure (final structure will probably be different):

```
cis-22c-project/
├── src/
│   └── main/
│       └── java/
│           ├── data/
│           │   ├── User.java
│           │   └── Interest.java
│           ├── model/
│           │   ├── Account.java
│           │   └── Friend.java
│           ├── ui/
│           │   ├── DataStorage.java
│           │   └── Menu.java
│           ├── util/
│           │   ├── ArrayList.java
│           │   ├── BST.java
│           │   ├── Graph.java
│           │   ├── HashTable.java
│           │   ├── LinkedList.java
│           │   └── App.java
│           └── data.txt
└── README.md
```

## Core Features

* **User Management:** Create and manage user accounts (U.S. Presidents). Includes profile information, friend lists, and interests.
* **Friend Connections:**  Users can add and remove friends.
* **Friend Search:** Search for users by name or shared interests.
* **Friend Recommendations:**  Friend recommendations based on network proximity and shared interests, using a Breadth-First Search (BFS) algorithm (?) on the friend graph.
* **Data Persistence:**  User data is loaded from and saved to a file.

## Data Structures

This project utilizes several data structures to efficiently manage and retrieve user and relationship data:

* **Binary Search Trees (BST):**
    * **`BST<User>` for Friends:** Each `User` object contains a BST storing their friends, sorted by friend name, enabling efficient searching and display of a user's friend list.
    * **`BST<User>` for All Users:** A separate BST stores all users in the system, sorted by name, allowing for searching new friends by name. This BST must handle duplicate names, potentially returning an `ArrayList<User>` for a given name.

* **Hash Tables:**
    * **`HashTable<String, User>` for Authentication:**  Stores user credentials (username as key, User object as value) for login. Passwords should be securely hashed before storage.
    * **`HashTable<String, Integer>` for Interests:** Stores unique interests (interest string as key, interest ID as value).  Used for efficient lookup of interest IDs.
    * **`HashTable<Integer, Interest>` (Clarification):** While not explicitly stated in the instructions, a hash table mapping interest IDs to `Interest` objects might be easier to use for managing interests.

* **Graph (Adjacency List):**  An `ArrayList<LinkedList<Integer>>` represents the social network graph. Each index in the ArrayList corresponds to a User's ID, and the LinkedList at that index stores the IDs of their friends.  An additional `ArrayList<User>` indexed by User ID is used to quickly access `User` objects by ID.

* **ArrayList for Interest-Based Search:** An `ArrayList<BST<User>>` stores BSTs of users who share a common interest. Each index in the ArrayList corresponds to an interest ID, and the BST at that index contains users with that interest.  This structure enables efficient searching for new friends based on shared interests.

* **Linked List:** Each `User` object contains a `LinkedList<Interest>` to store their list of interests.

## Data File Format

The project reads user data from a file with the following format:

```
id
name
username
password
total number of friends
list of ids of all friends (one ID per line)
city
total number of interests
list of interests (one interest per line)
```

The file must contain at least 15 users, including at least one duplicate name for testing purposes.

## Team Roles and Responsibilities

While each team member will contribute to multiple areas, the primary responsibilities are divided as follows:

1. **Hash Tables:** Implementing hash table operations (insert, delete, search by name/interest).
2. **BST:** Implementing and managing the BSTs for users and friend lists.
3. **Graphs:** Implementing the friend graph and the friend recommendation system (BFS).  This will likely involve multiple team members..
4. **UI:**  Designing and implementing the user interface (text / console interface).

## Development Process

The project will be developed in phases:

1. **Design:**  Define and finalize classes, data structures, and algorithms. Create UML diagrams.
2. **Implementation:** Implement core functionalities and data structures.
3. **Testing & Refinement:** Conduct thorough testing and incorporate feedback from walkthroughs.
4. **Presentation & Finalization:**  Prepare and deliver the final presentation and submit the project.


## Getting Started

This project will mainly be coordinated through the Discord server. If you have any questions, feel free to message the team leader (`askioe`).


## Contributors

* **Rolen Louie** - 
* **Benjamin Liou** - 
* **Kevin Young** - 
* **Kenneth Garcia** - 
* **Yukai Qiu** - 