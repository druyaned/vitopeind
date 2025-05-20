package druyaned.vitopeind.base.util;

/**
 * A simple resizable array with addition and removal only of the last element.
 * 
 * @author druyaned
 * @param <T> the type of elements in this list
 */
public class List<T> {
    
    /** Capacity for the no argument constructor. */
    public static final int DEFAULT_CAPACITY = (1 << 4);
    
    /** Maximum capacity of the list. */
    public static final int MAX_CAPACITY = (1 << 23);
    
    /** Capacity of {@link #array}. */
    protected int capacity;
    
    /** Wrapped array of elements. */
    protected Object[] array;
    
    /** Amount of the elements in the array which is lower or equal to the capacity. */
    protected int size;
    
    /**
     * Constructs an empty list with the given initial capacity.
     * 
     * @param capacity initial capacity of the array that will be doubled on demand
     *      and which must be in [1, {@link #MAX_CAPACITY}]
     */
    public List(int capacity) {
        throwIfInvalidCapacity(capacity);
        this.capacity = capacity;
        this.array = new Object[capacity];
        this.size = 0;
    }
    
    /** Constructs an empty list with the {@link #DEFAULT_CAPACITY defaul capacity}. */
    public List() {
        this(DEFAULT_CAPACITY);
    }
    
    /**
     * Adds the given element to the end of the list.
     * Throws {@link IllegalStateException} if {@link #MAX_CAPACITY} is reached.
     * If the size reaches the capacity it also makes an {@link #extendOnDemand() extension}.
     * Must be the only addition method.
     * 
     * @param value to be added; can be {@code null}
     */
    public void add(T value) {
        throwIfMaxCapacityReached();
        extendOnDemand();
        array[size++] = value;
    }
    
    /**
     * Removes the last element from the list.
     * Throws {@link IllegalStateException} if the list is empty.
     * Doesn't change the capacity.
     * Must be the only removal method.
     * 
     * @return removed element
     */
    public T remove() {
        throwIfZeroSize();
        @SuppressWarnings("unchecked") T removed = (T)array[--size];
        array[size] = null;
        return removed;
    }
    
    /**
     * Removes all the elements from the list (complexity is O(n)).
     * Also sets the capacity to {@link #DEFAULT_CAPACITY default value},
     * reassigns the array and the size.
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        capacity = DEFAULT_CAPACITY;
        array = new Object[capacity];
        size = 0;
    }
    
    /**
     * Returns an element by the given index of the array.
     * Throws {@link IndexOutOfBoundsException} if the given index is invalid.
     * 
     * @param i given index of the array
     * @return an element by the given index of the array
     */
    public T get(int i) {
        throwIfInvalidIndex(i);
        @SuppressWarnings("unchecked") T value = (T)array[i];
        return value;
    }
    
    /**
     * Returns the first element of the array.
     * Simply calls {@link #get(int) get(0)}.
     * 
     * @return the first element of the array
     */
    public T getFirst() {
        return get(0);
    }
    
    /**
     * Returns the last element of the array.
     * Simply calls {@link #get(int) get(size - 1)}.
     * 
     * @return the last element of the array
     */
    public T getLast() {
        return get(size - 1);
    }
    
    /**
     * Determines whether the array is empty.
     * 
     * @return true if the size is zero
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Returns capacity of the array.
     * 
     * @return capacity of the array
     */
    public int getCapacity() {
        return capacity;
    }
    
    /**
     * Returns size of the array.
     * 
     * @return size of the array
     */
    public int getSize() {
        return size;
    }
    
    /**
     * Doubles the capacity and reassigns the array if the size reaches the capacity.
     * Uses {@link System#arraycopy(Object, int, Object, int, int) System.arraycopy}.
     */
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
