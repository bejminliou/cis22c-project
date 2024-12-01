package util;
import java.util.NoSuchElementException;

/**
 * LinkedList.java
 * Defines a doubly-linked list class
 *
 * @author Rolen Louie
 * @param <T> a generic dadta type
 * CIS 22C, Applied Lab 3.1
 */
public class LinkedList<T> {
    private class Node {
        private T data;
        private Node next;
        private Node prev;

        public Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private int length;
    private Node first;
    private Node last;
    private Node iterator;

    /**** CONSTRUCTORS ****/

    /**
     * Instantiates a new LinkedList with default values
     *
     * @postcondition first = last = iterator = null, length = 0
     */
    public LinkedList() {
        first = null;
        last = null;
        iterator = null;

        length = 0;
    }

    /**
     * Converts the given array into a LinkedList
     *
     * @param array the array of values to insert into this LinkedList
     * @postcondition converts Array to LinkedList
     */
    public LinkedList(T[] array) {
        this();

        if (array == null || array.length == 0) {
            return;
        }

        for (T element : array) {
            addLast(element);
        }
    }

    /**
     * Instantiates a new LinkedList by copying another List
     *
     * @param original the LinkedList to copy
     * @postcondition a new List object, which is an identical, but separate, copy of the LinkedList
     *                original
     */
    public LinkedList(LinkedList<T> original) {
        this();

        if (original == null || original.isEmpty()) {
            return;
        }

        for (Node temp = original.first; temp != null; temp = temp.next) {
            addLast(temp.data);
        }

        iterator = null;
    }

    /**** ACCESSORS ****/

    /**
     * Returns the value stored in the first node
     *
     * @precondition length > 0
     * @return the value stored at node first
     * @throws NoSuchElementException if isEmpty() == true
     */
    public T getFirst() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return first.data;
    }

    /**
     * Returns the value stored in the last node
     *
     * @precondition length > 0
     * @return the value stored in the node last
     * @throws NoSuchElementException if isEmpty() == true
     */
    public T getLast() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return last.data;
    }

    /**
     * Returns the data stored in the iterator node
     *
     * @precondition iterator != null;
     * @return the data stored in the iterator node
     * @throw NullPointerException when iterator == null
     */
    public T getIterator() throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("iterator is null: getIterator() failed");
        }

        return iterator.data;
    }

    /**
     * Returns the current length of the LinkedList
     *
     * @return the length of the LinkedList from 0 to n
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns whether the LinkedList is currently empty
     *
     * @return whether the LinkedList is empty
     */
    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * Returns whether the iterator is offEnd, i.e. null
     *
     * @return whether the iterator is null
     */
    public boolean offEnd() {
        return (iterator == null);
    }

    /**** MUTATORS ****/

    /**
     * Creates a new first element
     *
     * @param data the data to insert at the front of the LinkedList
     * @postcondition inserts a node at front. increases length by 1
     */
    public void addFirst(T data) {
        Node node = new Node(data);

        if (isEmpty()) {
            first = node;
            last = node;
        } else {
            node.next = first;
            first.prev = node;
            first = node;
        }

        length++;
    }

    /**
     * Creates a new last element
     *
     * @param data the data to insert at the end of the LinkedList
     * @postcondition inserts node at last position and connects it to the previous last node
     */
    public void addLast(T data) {
        Node node = new Node(data);

        if (isEmpty()) {
            first = node;
            last = node;
        } else {
            last.next = node;
            node.prev = last;
            last = node;
        }

        length++;
    }

    /**
     * Inserts a new element after the iterator
     *
     * @param data the data to insert
     * @precondition iterator exists
     * @throws NullPointerException iterator doesn't exist (iterator == null)
     */
    public void addIterator(T data) throws NullPointerException {
        if (offEnd()) {
            throw new NullPointerException("Iterator is set to null!");
        }

        Node node = new Node(data);

        node.next = iterator.next;
        node.prev = iterator;

        if (iterator.next != null) {
            iterator.next.prev = node;
        } else {
            last = node;
        }

        iterator.next = node;

        length++;
    }

    /**
     * removes the element at the front of the LinkedList
     *
     * @precondition length > 0
     * @postcondition removes first node, sets previous node as first, subtracts length by 1
     * @throws NoSuchElementException if length = 0
     */
    public void removeFirst() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        } else if (length == 1) {
            first = null;
            last = null;
        } else {
            first = first.next;
            first.prev = null;
        }

        length--;

        if (iterator == first) {
            iterator = null;
        }
    }

    /**
     * removes the element at the end of the LinkedList
     *
     * @precondition length > 0
     * @postcondition removes last node, subtracts length by 1
     * @throws NoSuchElementException if length == 0
     */
    public void removeLast() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        } else if (length == 1) {
            first = null;
            last = null;
        } else {
            last = last.prev;
            last.next = null;
        }

        length--;
    }

    /**
     * removes the element referenced by the iterator
     *
     * @precondition iterator != null
     * @postcondition iterator == null
     * @throws NullPointerException if iterator == null
     */
    public void removeIterator() throws NullPointerException {
        if (offEnd()) {
            throw new NullPointerException("iterator is null: removeIterator() failed");
        }

        if (iterator == first) {
            removeFirst();
        } else if (iterator == last) {
            removeLast();
        } else {
            iterator.prev.next = iterator.next;
            iterator.next.prev = iterator.prev;

            length--;
        }

        iterator = null;
    }

    /**
     * places the iterator at the first node
     *
     * @postcondition sets iterator to first
     */
    public void positionIterator() {
        iterator = first;
    }

    /**
     * Moves the iterator one node towards the last
     *
     * @precondition iterator && next != null; iterator and next both exist
     * @postcondition iterator = iterator.next
     * @throws NullPointerException if either iterator or iterator.next don't exist
     */
    public void advanceIterator() throws NullPointerException {
        if (isEmpty()) {
            throw new NullPointerException("list is empty: advanceIterator() failed");
        }

        if (offEnd()) {
            throw new NullPointerException("iterator is null: advanceIterator() failed");
        }

        if (iterator.next == null) {
            iterator = null;

            return;
        }

        iterator = iterator.next;
    }

    /**
     * Moves the iterator one node towards the first
     *
     * @precondition iterator != null
     * @postcondition iterator = iterator.prev
     * @throws NullPointerException if iterator or previous iterator == null
     */
    public void reverseIterator() throws NullPointerException {
        if (offEnd()) {
            throw new NullPointerException("iterator is null: reverseIterator() failed");
        }

        if (iterator.prev == null) {
            iterator = null;

            return;
        }

        iterator = iterator.prev;
    }

    /**** ADDITIONAL OPERATIONS ****/

    /**
     * Re-sets LinkedList to empty as if the default constructor had just been called
     */
    public void clear() {
        first = null;
        last = null;
        iterator = null;
        length = 0;
    }

    /**
     * Converts the LinkedList to a String, with each value separated by a blank line At the end of
     * the String, place a new line character
     *
     * @return the LinkedList as a String
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        Node temp = first;

        while (temp != null) {
            str.append(temp.data + " ");

            temp = temp.next;
        }

        return str.toString() + "\n";
    }

    /**
     * Determines whether the given Object is another LinkedList, containing the same data in the
     * same order
     *
     * @param obj another Object
     * @return whether there is equality
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof LinkedList)) {
            return false;
        }

        LinkedList other = (LinkedList) obj;

        if (this.length != other.length) {
            return false;
        }

        Node currentThis = this.first;
        Node currentOther = other.first;

        while (currentThis != null) {
            if (currentThis.data == null && currentOther.data != null) {
                return false;
            }

            if (currentThis.data != null && !currentThis.data.equals(currentOther.data)) {
                return false;
            }

            currentThis = currentThis.next;
            currentOther = currentOther.next;
        }

        return true;
    }

    /** CHALLENGE METHODS */

    /**
     * Moves all nodes in the list towards the end of the list the number of times specified Any
     * node that falls off the end of the list as it moves forward will be placed the front of the
     * list For example: [1, 2, 3, 4, 5], numMoves = 2 -> [4, 5, 1, 2 ,3] For example: [1, 2, 3, 4,
     * 5], numMoves = 4 -> [2, 3, 4, 5, 1] For example: [1, 2, 3, 4, 5], numMoves = 7 -> [4, 5, 1, 2
     * ,3]
     *
     * @param numMoves the number of times to move each node.
     * @precondition numMoves >= 0
     * @postcondition iterator position unchanged (i.e. still referencing the same node in the list,
     *                regardless of new location of Node)
     * @throws IllegalArgumentException when numMoves < 0
     */
    public void spinList(int numMoves) throws IllegalArgumentException {
        if (length <= 1 || numMoves == 0) {
            return;
        }

        if (numMoves < 0) {
            throw new IllegalArgumentException();
        }

        numMoves = numMoves % length;

        for (int i = 0; i < numMoves; i++) {
            Node temp = last;

            last = last.prev;
            last.next = null;

            temp.next = first;
            first.prev = temp;
            first = temp;

            first.prev = null;
        }
    }

    /**
     * Splices together two LinkedLists to create a third List which contains alternating values
     * from this list and the given parameter For example: [1,2,3] and [4,5,6] -> [1,4,2,5,3,6] For
     * example: [1, 2, 3, 4] and [5, 6] -> [1, 5, 2, 6, 3, 4] For example: [1, 2] and [3, 4, 5, 6]
     * -> [1, 3, 2, 4, 5, 6]
     *
     * @param list the second LinkedList
     * @return a new LinkedList, which is the result of interlocking this and list
     * @postcondition this and list are unchanged
     */
    public LinkedList<T> altLists(LinkedList<T> list) {
        LinkedList<T> result = new LinkedList<>();

        if (list == null) {
            return this;
        }

        if (this.first == null && list.first == null) {
            return result;
        }

        Node current = this.first;
        Node other = list.first;

        while (current != null || other != null) {
            if (current != null) {
                result.addLast(current.data);

                current = current.next;
            }

            if (other != null) {
                result.addLast(other.data);

                other = other.next;
            }
        }
        return result;
    }

    /**
     * Returns each element in the LinkedList along with its numerical position from 1 to n,
     * followed by a newline.
     *
     * @return a String containing each element and its numerical position, followed by a newline
     */
    public String numberedListString() {
        StringBuilder result = new StringBuilder();
        Node current = first;
        int position = 1;

        while (current != null) {
            result.append(position).append(". ").append(current.data).append("\n");

            current = current.next;

            position++;
        }

        result.append("\n");

        return result.toString();
    }

    /**
     * Searches the LinkedList for a given element's index.
     *
     * @param data the data whose index to locate.
     * @return the index of the data or -1 if the data is not contained in the LinkedList
     */
    public int findIndex(T data) {
        int index = 0;
        Node current = first;

        while (current != null) {
            if (current.data.equals(data)) {
                return index;
            }
            current = current.next;
            index++;
        }

        return -1;
    }

    /**
     * Advances the iterator to location within the LinkedList specified by the given index
     *
     * @param index the index at which to place the iterator
     * @precondition index >= 0, index < length
     * @throws IndexOutOfBoundsException when index is out of bounds
     */
    public void advanceIteratorToIndex(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException();
        }

        iterator = first;
        for (int i = 0; i < index; i++) {
            iterator = iterator.next;
        }
    }
}
