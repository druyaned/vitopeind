package druyaned.vitopeind.base.util;

import static druyaned.vitopeind.base.util.List.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ListTest {
    
    private static final String VALUE = "stringValue";
    
    @Test public void createWithNoArgs() {
        List<String> list = new List<>();
        assertEquals(DEFAULT_CAPACITY, list.getCapacity());
        assertEquals(0, list.getSize());
    }
    
    @Test public void createWithNegativeCapacity() {
        int capacity = -408;
        assertThrows(IllegalArgumentException.class, () -> new List<String>(capacity));
    }
    
    @Test public void createWithZeroCapacity() {
        int capacity = 0;
        assertThrows(IllegalArgumentException.class, () -> new List<String>(capacity));
    }
    
    @Test public void createWithCapacityOfOne() {
        int capacity = 1;
        assertEquals(capacity, new List<>(capacity).getCapacity());
    }
    
    @Test public void createWithMaxCapacityMinusOne() {
        int capacity = MAX_CAPACITY - 1;
        assertEquals(capacity, new List<>(capacity).getCapacity());
    }
    
    @Test public void createWithMaxCapacity() {
        int capacity = MAX_CAPACITY;
        assertEquals(capacity, new List<>(capacity).getCapacity());
    }
    
    @Test public void createWithMaxCapacityPlusOne() {
        int capacity = MAX_CAPACITY + 1;
        assertThrows(IllegalArgumentException.class, () -> new List<String>(capacity));
    }
    
    @Test public void addAndRemoveOneValue() {
        List<String> list = new List<>();
        list.add(VALUE);
        assertEquals(DEFAULT_CAPACITY, list.getCapacity());
        assertEquals(1, list.getSize());
        assertEquals(VALUE, list.getFirst());
        String removed = list.remove();
        assertEquals(DEFAULT_CAPACITY, list.getCapacity());
        assertEquals(0, list.getSize());
        assertEquals(VALUE, removed);
    }
    
    @Test public void addAndRemoveSeveralValues() {
        int initCapacity = 3;
        List<String> list = new List<>(initCapacity);
        int additionCount = 4 * DEFAULT_CAPACITY + 2;
        for (int i = 0; i < additionCount; i++) {
            list.add(VALUE);
            int capacity = initCapacity;
            while (capacity <= i) {
                capacity <<= 1;
            }
            assertEquals(capacity, list.getCapacity());
            assertEquals(i + 1, list.getSize());
            assertEquals(VALUE, list.get(i));
        }
        int capacity = list.getCapacity();
        for (int i = additionCount; i > 0; i--) {
            String removed = list.remove();
            assertEquals(capacity, list.getCapacity());
            assertEquals(i - 1, list.getSize());
            assertEquals(VALUE, removed);
        }
    }
    
    @Test public void addTooManyValuesAndClear() {
        List<String> list = new List<>(MAX_CAPACITY);
        for (int i = 0; i < MAX_CAPACITY; i++) {
            list.add(VALUE);
        }
        assertThrows(IllegalStateException.class, () -> list.add(VALUE));
        list.clear();
        assertEquals(DEFAULT_CAPACITY, list.getCapacity());
        assertEquals(0, list.getSize());
        assertTrue(list.isEmpty());
    }
    
    @Test public void removeFromEmptiness() {
        List<String> list = new List<>();
        assertThrows(IllegalStateException.class, list::remove);
    }
    
    @Test public void getValues() {
        List<String> list = new List<>();
        String value0 = "stringValue0";
        String value1 = "stringValue1";
        String value2 = "stringValue2";
        list.add(value0);
        list.add(value1);
        list.add(value2);
        assertEquals(value0, list.get(0));
        assertEquals(value1, list.get(1));
        assertEquals(value2, list.get(2));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-408));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(3));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(2048));
    }
    
    @Test public void getWithoutAddition() {
        List<String> list = new List<>();
        assertThrows(IndexOutOfBoundsException.class, () -> list.getFirst());
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.getLast());
    }
    
}
