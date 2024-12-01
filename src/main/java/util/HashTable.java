package util;
import java.util.ArrayList;

/**
 * HashTable.java
 *
 * @author Rolen Louie
 * @param <T> a generic data type
 * CIS 22C, Lab 14
 */
public class HashTable<T> {

    private int numElements;
    private ArrayList<LinkedList<T>> table;

    /**
     * Constructor for the HashTable class. Initializes the Table to be sized according to value
     * passed in as a parameter. Inserts size empty Lists into the table. Sets numElements to 0
     *
     * @param size the table size
     * @precondition size > 0
     * @throws IllegalArgumentException when size <= 0
     */
    public HashTable(int size) throws IllegalArgumentException {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0.");
        }

        table = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            table.add(new LinkedList<>());
        }

        numElements = 0;
    }

    /**
     * Constructor for HashTable class. Inserts the contents of the given array into the Table at
     * the appropriate indices
     *
     * @param array an array of elements to insert
     * @param size the size of the Table
     * @precondition size > 0
     * @throws IllegalArgumentException when size <= 0
     */
    public HashTable(T[] array, int size) throws IllegalArgumentException {
        this(size);
        if (array == null) {
            return;
        }
        for (T element : array) {
            add(element);
        }
    }

    /** Accessors */

    /**
     * Returns the hash value in the table for a given Object.
     *
     * @param obj the Object
     * @return the index in the table
     */
    private int hash(T obj) {
        int code = obj.hashCode();

        return Math.abs(code % table.size()); // pos
    }

    /**
     * Counts the number of elements at this index.
     *
     * @param index the index in the table
     * @precondition 0 <= index < table.size()
     * @return the count of elements at this index
     * @throws IndexOutOfBoundsException when the precondition is violated
     */
    public int countBucket(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= table.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds.");
        }

        return table.get(index).getLength();
    }

    /**
     * Determines total number of elements in the table
     *
     * @return total number of elements
     */
    public int getNumElements() {
        return numElements;
    }

    /**
     * Accesses a specified key in the Table
     *
     * @param elmt the key to search for
     * @return the value to which the specified key is mapped, or null if this table contains no
     *         mapping for the key.
     * @precondition elmt != null
     * @throws NullPointerException when the precondition is violated.
     */
    public T get(T elmt) throws NullPointerException {
        if (elmt == null) {
            throw new NullPointerException("Element cannot be null.");
        }

        int index = hash(elmt);

        LinkedList<T> list = table.get(index);

        list.positionIterator();

        while (!list.offEnd()) {
            if (list.getIterator().equals(elmt)) {
                return list.getIterator();
            }

            list.advanceIterator();
        }

        return null;
    }

    /**
     * Accesses a specified element in the table.
     *
     * @param elmt the element to locate
     * @return the bucket number where the element is located or -1 if it is not found.
     * @precondition elmt != null
     * @throws NullPointerException when the precondition is violated.
     */
    public int find(T elmt) throws NullPointerException {
        if (elmt == null) {
            throw new NullPointerException("Element cannot be null.");
        }

        int index = hash(elmt);

        LinkedList<T> list = table.get(index);

        list.positionIterator();

        while (!list.offEnd()) {
            if (list.getIterator().equals(elmt)) {
                return index;
            }

            list.advanceIterator();
        }

        return -1;
    }

    /**
     * Determines whether a specified element is in the table.
     *
     * @param elmt the element to locate
     * @return whether the element is in the table
     * @precondition elmt != null
     * @throws NullPointerException when the precondition is violated
     */
    public boolean contains(T elmt) throws NullPointerException {
        if (elmt == null) {
            throw new NullPointerException("Element cannot be null.");
        }

        return find(elmt) != -1;
    }

    /** Mutators */

    /**
     * Inserts a new element in the table at the end of the chain of the correct bucket.
     *
     * @param elmt the element to insert
     * @precondition elmt != null
     * @throws NullPointerException when the precondition is violated.
     */
    public void add(T elmt) throws NullPointerException {
        if (elmt == null) {
            throw new NullPointerException("Element cannot be null.");
        }

        int index = hash(elmt);

        table.get(index).addLast(elmt);

        numElements++;
    }

    /**
     * Removes the given element from the table.
     *
     * @param elmt the element to remove
     * @precondition elmt != null
     * @return whether elmt exists and was removed from the table
     * @throws NullPointerException when the precondition is violated
     */
    public boolean delete(T elmt) throws NullPointerException {
        if (elmt == null) {
            throw new NullPointerException("Element cannot be null.");
        }

        int bucket = find(elmt);

        if (bucket != -1) {
            LinkedList<T> list = table.get(bucket);

            list.positionIterator();

            while (!list.offEnd()) {
                if (list.getIterator().equals(elmt)) {
                    list.removeIterator();
                    numElements--;
                    return true;
                }

                list.advanceIterator();
            }
        }

        return false;
    }

    /**
     * Resets the hash table back to the empty state, as if the one argument constructor has just
     * been called.
     */
    public void clear() {
        for (LinkedList<T> list : table) {
            list.clear();
        }

        numElements = 0;
    }

    /** Additional Methods */

    /**
     * Computes the load factor.
     *
     * @return the load factor
     */
    public double getLoadFactor() {
        return (double) numElements / table.size();
    }

    /**
     * Creates a String of all elements at a given bucket
     *
     * @param bucket the index in the table
     * @return a String of elements, separated by spaces with a new line character at the end
     * @precondition 0 <= bucket < table.size()
     * @throws IndexOutOfBoundsException when bucket is out of bounds
     */
    public String bucketToString(int bucket) throws IndexOutOfBoundsException {
        if (bucket < 0 || bucket >= table.size()) {
            throw new IndexOutOfBoundsException("Bucket index out of bounds.");
        }

        return table.get(bucket).toString();
    }

    /**
     * Creates a String of the bucket number followed by a colon followed by the first element at
     * each bucket followed by a new line. For empty buckets, add the bucket number followed by a
     * colon followed by empty.
     *
     * @return a String of all first elements at each bucket.
     */
    public String rowToString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < table.size(); i++) {
            sb.append("Bucket ").append(i).append(": ");

            if (!table.get(i).isEmpty()) {
                sb.append(table.get(i).getFirst());
            } else {
                sb.append("empty");
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Starting at the 0th bucket, and continuing in order until the last bucket, concatenates all
     * elements at all buckets into one String, with a new line between buckets and one more new
     * line at the end of the entire String.
     *
     * @return a String of all elements in this HashTable.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (numElements == 0) {
            return "\n";
        }

        for (LinkedList<T> list : table) {
            if (!list.isEmpty()) {
                sb.append(list.toString()).append("\n");

            }
        }
        int length = sb.length();
        while (length > 0 && sb.charAt(sb.length() - 1) == '\n') {
            sb.setLength(--length);
        }

        sb.append("\n\n");

        return sb.toString();
    }
}
