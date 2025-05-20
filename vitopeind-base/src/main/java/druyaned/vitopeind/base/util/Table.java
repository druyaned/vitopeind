package druyaned.vitopeind.base.util;

/**
 * A simple linked hash table with fixed capacity
 * where hash is calculated as difference between a given key and the first table's key.
 * Also it provides addition and removal only of the last element.
 * Useful for tracking years of human life.
 * Has array of values (table) and array of keys.
 * 
 * @author druyaned
 * @param <T> the type of elements in this list
 */
public class Table<T> {
    
    /** Maximum capacity of the table. */
    public static final int MAX_CAPACITY = (1 << 23);
    
    private final int capacity;
    private final Object[] table;
    private final int[] keys;
    private int size;
    
    /**
     * Constructs a new table with the given initial capacity.
     * Throws {@link IllegalArgumentException} if the capacity is invalid.
     * Assign array of elements (table) and array of keys.
     * 
     * @param capacity initial capacity of the table which must be in [1, {@link #MAX_CAPACITY}]
     */
    public Table(int capacity) {
        throwIfInvalidCapacity(capacity);
        this.capacity = capacity;
        this.table = new Object[capacity];
        this.keys = new int[capacity];
        this.size = 0;
    }
    
    /**
     * Adds the given value by key to the array of elements and adds key to the array of keys.
     * Throws {@link NullPointerException} if the value is null.
     * Hash is calculated as difference between the given key and the first key of the table.
     * Throws {@link IllegalArgumentException} if hash is not in (last hash, capacity).
     * 
     * @param key {@code hash = key - key[0]}, hash must be in (last hash, capacity)
     * @param value not null value to be added
     */
    public void add(int key, T value) {
        throwIfNull(value);
        if (size == 0) {
            int hash = 0;
            table[hash] = value;
            keys[size++] = key;
        } else {
            int hash = getHash(key);
            throwIfInvalidHash(hash);//TODO:fix
            throwIfHashNotGreaterThanLast(hash, getLastHash());
            table[hash] = value;
            keys[size++] = key;
        }
    }
    
    public T remove() {
        throwIfZeroSize();
        int lastHash = getLastHash();
        @SuppressWarnings("unchecked") T removed = (T)table[lastHash];
        table[lastHash] = null;
        keys[--size] = 0;
        return removed;
    }
    
    private int getHash(int key) {
        return key - keys[0];
    }
    
    private int getLastHash() {
        return keys[size - 1] - keys[0];
    }
    
    public T getBy(int key) {
        if (size == 0) {
            return null;
        }
        int hash = getHash(key);
        throwIfInvalidHash(hash);
        @SuppressWarnings("unchecked") T value = (T)table[hash];
        return value;
    }
    
    public T getAt(int i) {
        throwIfInvalidIndex(i);
        @SuppressWarnings("unchecked") T value = (T)table[getHash(keys[i])];
        return value;
    }
    
    public T getFirst() {
        return getAt(0);
    }
    
    public T getLast() {
        return getAt(size - 1);
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public int getSize() {
        return size;
    }
    
    private void throwIfInvalidCapacity(int capacity) {
        if (capacity < 1 || MAX_CAPACITY < capacity) {
            throw new IllegalArgumentException("capacity=" + capacity
                    + " which must be in [1, " + MAX_CAPACITY + ']');
        }
    }
    
    private void throwIfNull(T value) {
        if (value == null) {
            throw new NullPointerException("value=null which is not valid");
        }
    }
    
    private void throwIfInvalidHash(int hash) {
        if (hash < 0 || capacity <= hash) {
            throw new IllegalArgumentException("hash=" + hash
                    + ", capacity=" + capacity
                    + ", (0 <= hash < capacity) is false");
        }
    }
    
    private void throwIfHashNotGreaterThanLast(int hash, int lastHash) {
        if (hash <= lastHash) {
            throw new IllegalArgumentException("lastHash=" + lastHash
                    + ", hash=" + hash
                    + ", (lastHash < hash) is false");
        }
    }
    
    private void throwIfZeroSize() {
        if (size == 0) {
            throw new IllegalStateException("empty table");
        }
    }
    
    private void throwIfInvalidIndex(int index) {
        if (index < 0 || size <= index) {
            throw new IndexOutOfBoundsException("index=" + index
                    + ", size=" + size + ", (0 <= index < size) is false");
        }
    }
    
}
