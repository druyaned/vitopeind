package druyaned.vitopeind.base.util;

import static druyaned.vitopeind.base.util.Table.MAX_CAPACITY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class TableTest {
    
    @Test public void createWithNegativeCapacity() {
        int capacity = -408;
        assertThrows(IllegalArgumentException.class, () -> new Table<String>(capacity));
    }
    
    @Test public void createWithZeroCapacity() {
        int capacity = 0;
        assertThrows(IllegalArgumentException.class, () -> new Table<String>(capacity));
    }
    
    @Test public void createWithCapacityOfOne() {
        int capacity = 1;
        assertEquals(capacity, new Table<String>(capacity).getCapacity());
    }
    
    @Test public void createWithMaxCapacityMinusOne() {
        int capacity = MAX_CAPACITY - 1;
        assertEquals(capacity, new Table<String>(capacity).getCapacity());
    }
    
    @Test public void createWithMaxCapacity() {
        int capacity = MAX_CAPACITY;
        assertEquals(capacity, new Table<String>(capacity).getCapacity());
    }
    
    @Test public void createWithMaxCapacityPlusOne() {
        int capacity = MAX_CAPACITY + 1;
        assertThrows(IllegalArgumentException.class, () -> new Table<String>(capacity));
    }
    
    @Test public void addAndRemoveOneValue() {
        String value = "value";
        int capacity = 2;
        Table<String> table = new Table<>(capacity);
        int key = 1984;
        table.add(key, value);
        assertEquals(1, table.getSize());
        assertEquals(value, table.getBy(key));
        assertEquals(value, table.getAt(0));
        String removed = table.remove();
        assertEquals(0, table.getSize());
        assertEquals(value, removed);
        assertNull(table.getBy(key));
    }
    
    @Test public void addAndRemoveSeveralValue() {
        int capacity = 6;
        Table<String> table = new Table<>(capacity);
        final int[] keys = { 1984, 1987, 1989 };
        final String[] values = { "value0", "value1", "value2" };
        final int len = keys.length;
        for (int i = 0; i < len; i++) {
            table.add(keys[i], values[i]);
            assertEquals(i + 1, table.getSize());
            assertEquals(values[i], table.getBy(keys[i]));
            assertEquals(values[i], table.getAt(i));
        }
        for (int i = len; i > 0; i--) {
            assertEquals(values[i - 1], table.getBy(keys[i - 1]));
            assertEquals(values[i - 1], table.getAt(i - 1));
            String removed = table.remove();
            assertEquals(i - 1, table.getSize());
            assertEquals(values[i - 1], removed);
            assertNull(table.getBy(keys[i - 1]));
        }
    }
    
    @Test public void addNullValue() {
        int capacity = 4;
        Table<String> table = new Table<>(capacity);
        int key = 1984;
        assertThrows(NullPointerException.class, () -> table.add(key, null));
    }
    
    @Test public void addWithNegativeHash() {
        String value = "value";
        int capacity = 4;
        Table<String> table = new Table<>(capacity);
        int key0 = 1984;
        int key1 = key0 - 1;
        table.add(key0, value);
        assertThrows(IllegalArgumentException.class, () -> table.add(key1, value));
        assertThrows(IllegalArgumentException.class, () -> table.getBy(key1));
    }
    
    @Test public void addWithTooBigHash() {
        String value = "value";
        int capacity = 4;
        Table<String> table = new Table<>(capacity);
        int key0 = 1984;
        int key1 = key0 + capacity;
        table.add(key0, value);
        assertThrows(IllegalArgumentException.class, () -> table.add(key1, value));
        assertThrows(IllegalArgumentException.class, () -> table.getBy(key1));
    }
    
    @Test public void addWithHashLessThanLast() {
        int capacity = 6;
        Table<String> table = new Table<>(capacity);
        int[] keys = { 1984, 1988, 1986 };
        String[] values = { "value0", "value1", "value2" };
        table.add(keys[0], values[0]);
        table.add(keys[1], values[1]);
        assertThrows(IllegalArgumentException.class, () -> table.add(keys[2], values[2]));
        assertEquals(2, table.getSize());
    }
    
    @Test public void addWithSameHash() {
        String value = "value";
        int capacity = 4;
        Table<String> table = new Table<>(capacity);
        int key0 = 1984;
        int key1 = key0;
        table.add(key0, value);
        assertThrows(IllegalArgumentException.class, () -> table.add(key1, value));
        assertEquals(1, table.getSize());
    }
    
    @Test public void removeWithZiroSize() {
        int capacity = 6;
        Table<String> table = new Table<>(capacity);
        assertThrows(IllegalStateException.class, table::remove);
    }
    
    @Test public void getWithInvalidIndex() {
        int capacity = 6;
        Table<String> table = new Table<>(capacity);
        int[] keys = { 1984, 1986, 1988 };
        String[] values = { "value0", "value1", "value2" };
        assertTrue(table.isEmpty());
        assertThrows(IndexOutOfBoundsException.class, () -> table.getAt(0));
        table.add(keys[0], values[0]);
        table.add(keys[1], values[1]);
        table.add(keys[2], values[2]);
        assertThrows(IndexOutOfBoundsException.class, () -> table.getAt(3));
    }
    
}
