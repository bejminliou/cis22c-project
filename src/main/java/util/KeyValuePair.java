package util;

/**
 * A simple key-value pair class for use in HashTable
 *
 * @author Benjamin Liou
 * @param <K> The key type
 * @param <V> The value type
 * CIS 22C, Course Project
 */
public class KeyValuePair<K, V> {
    private K key;
    private V value;
    
    /**
     * Creates a new key-value pair
     * 
     * @param key The key
     * @param value The value
     */
    public KeyValuePair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    /**
     * Gets the key
     * 
     * @return The key
     */
    public K getKey() {
        return key;
    }
    
    /**
     * Gets the value
     * 
     * @return The value
     */
    public V getValue() {
        return value;
    }
    
    /**
     * Sets the value
     * 
     * @param value The new value
     */
    public void setValue(V value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        KeyValuePair<?, ?> other = (KeyValuePair<?, ?>) obj;
        return key.equals(other.key);
    }
    
    @Override
    public int hashCode() {
        return key.hashCode();
    }
    
    @Override
    public String toString() {
        return key + "=" + value;
    }
}
