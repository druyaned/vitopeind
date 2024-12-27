package druyaned.vitopeind.base.util;

public class Table<T> {
    
    public static final int MAX_CAPACITY = (1 << 23);
    
    private final int capacity;
    private final Object[] table;
    private final int[] keys;
    private int size;
    
    public Table(int capacity) {
        throwIfInvalidCapacity(capacity);
        this.capacity = capacity;
        this.table = new Object[capacity];
        this.keys = new int[capacity];
        this.size = 0;
    }
    
    public void add(int key, T value) {
        throwIfNull(value);
        if (size == 0) {
            int hash = 0;
            table[hash] = value;
            keys[size++] = key;
        } else {
            int hash = getHash(key);
            throwIfInvalidHash(hash);
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
