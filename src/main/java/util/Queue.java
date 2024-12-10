package util;

import java.util.NoSuchElementException;

/**
 * Queue.java
 *
 * @author Benjamin Liou
 * @author Kevin Young
 * @author Rolen Louie
 * @author Yukai Qiu
 * @author Kenneth Garcia
 * @author Tu Luong
 * @param <T> a generic data type
 * CIS 22C, Lab 9
 */
public class Queue<T> implements Q<T> {
    private class Node {
        private T data;
        private Node next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private int size;
    private Node front;
    private Node end;

    /**** CONSTRUCTORS ****/

    /**
     * Default constructor for the Queue class
     * 
     * @postcondition a new Queue object with all fields
     *                assigned default values
     */
    public Queue() {
        front = end = null;
        size = 0;
    }

    /**
     * Converts an array into a Queue
     * 
     * @param array the array to copy into Queue
     */
    public Queue(T[] array) {
        this();
        if (array == null || array.length == 0) {
            return;
        }
        for (int i = 0; i < array.length; i++) {
            enqueue(array[i]);
        }
    }

    /**
     * Copy constructor for the Queue class
     * Makes a deep copy of the parameter
     * 
     * @param original the Queue to copy
     * @postcondition creates a new Queue class with the same data as the original
     */
    public Queue(Queue<T> original) {
        this();
        if (original == null) {
            return;
        }
        if (original.isEmpty()) {
            return;
        }
        Node temp = original.front;
        while (temp != null) {
            enqueue(temp.data);
            temp = temp.next;
        }
    }

    /**** ACCESSORS ****/

    /**
     * Returns the value stored at the front
     * of the Queue
     * 
     * @return the value at the front of the queue
     * @precondition !isEmpty()
     * @throws NoSuchElementException when the
     *                                precondition is violated
     */
    public T getFront() throws NoSuchElementException {
        if (front == null) {
            throw new NoSuchElementException();
        } else {
            return front.data;
        }
    }

    /**
     * Returns the size of the Queue
     * 
     * @return the size from 0 to n
     */
    public int getSize() {
        return size;
    }

    /**
     * Determines whether a Queue is empty
     * 
     * @return whether the Queue contains no elements
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**** MUTATORS ****/

    /**
     * Inserts a new value at the end of the Queue
     *
     * @param data the new data to insert
     * @postcondition adds new data to the end
     */
    public void enqueue(T data) {
        Node node = new Node(data);
        if (isEmpty()) {
            front = end = node;
        } else {
            end.next = node;
            end = node;
        }
        size++;
    }

    /**
     * Removes the front element in the Queue
     * 
     * @precondition !isEmpty()
     * @throws NoSuchElementException when precondition is violated
     * @postcondition Removes first node, and decrements size
     */
    public void dequeue() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        } else {
            front = front.next;
            if (isEmpty()) {
                end = null;
            }
        }
        size--;
    }

    /**** ADDITONAL OPERATIONS ****/

    /**
     * Returns values stored in Queue as String, separated by a blank space
     * with a new line character at the end
     * 
     * @return a String of Queue values
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        Node temp = front;
        while (temp != null) {
            str.append(temp.data + " ");
            temp = temp.next;
        }
        return str.toString() + "\n";
    }

    /**
     * Determines whether two Queues contain
     * the same values in the same order
     * 
     * @param obj the Object to compare to this
     * @return whether obj and this are equal
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Queue<T> other = (Queue<T>) obj;
        if (this.size != other.size) {
            return false;
        }

        Node current = this.front;
        Node otherCurrent = other.front;

        while (current != null) {
            if (!current.data.equals(otherCurrent.data)) {
                return false;
            }
            current = current.next;
            otherCurrent = otherCurrent.next;
        }
        return true;
    }
}
