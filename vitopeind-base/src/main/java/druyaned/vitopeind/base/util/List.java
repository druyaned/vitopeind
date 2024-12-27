package druyaned.vitopeind.base.util;

public class List<T> {
    
    public static final int DEFAULT_CAPACITY = (1 << 4);
    public static final int MAX_CAPACITY = (1 << 23);
    
    protected int capacity;
    protected Object[] array;
    protected int size;
    
    public List(int capacity) {
        throwIfInvalidCapacity(capacity);
        this.capacity = capacity;
        this.array = new Object[capacity];
        this.size = 0;
    }
    
    public List() {
        this(DEFAULT_CAPACITY);
    }
    
    public void add(T value) {
        throwIfMaxCapacityReached();
        extendOnDemand();
        array[size++] = value;
    }
    
    public T remove() {
        throwIfZeroSize();
        @SuppressWarnings("unchecked") T removed = (T)array[--size];
        array[size] = null;
        return removed;
    }
    
    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        capacity = DEFAULT_CAPACITY;
        array = new Object[capacity];
        size = 0;
    }
    
    public T get(int i) {
        throwIfInvalidIndex(i);
        @SuppressWarnings("unchecked") T value = (T)array[i];
        return value;
    }
    
    public T getFirst() {
        return get(0);
    }
    
    public T getLast() {
        return get(size - 1);
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
    
    private void extendOnDemand() {
        if (size == capacity) {
            capacity <<= 1;
            if (capacity < 1 || MAX_CAPACITY < capacity) {
                capacity = MAX_CAPACITY;
            }
            Object[] extended = new Object[capacity];
            System.arraycopy(array, 0, extended, 0, size);
            array = extended;
        }
    }
    
    private void throwIfInvalidCapacity(int capacity) {
        if (capacity < 1 || MAX_CAPACITY < capacity) {
            throw new IllegalArgumentException("capacity=" + capacity
                    + ", which must be in [1, " + MAX_CAPACITY + ']');
        }
    }
    
    private void throwIfMaxCapacityReached() {
        if (getSize() == MAX_CAPACITY) {
            throw new IllegalStateException("size=" + size
                    + ", MAX_CAPACITY=" + MAX_CAPACITY + " is reached");
        }
    }
    
    private void throwIfZeroSize() {
        if (size == 0) {
            throw new IllegalStateException("empty list");
        }
    }
    
    private void throwIfInvalidIndex(int index) {
        if (index < 0 || size <= index) {
            throw new IndexOutOfBoundsException("size=" + size
                    + ", index=" + index
                    + ", (0 <= index < size) is false");
        }
    }
    
}
