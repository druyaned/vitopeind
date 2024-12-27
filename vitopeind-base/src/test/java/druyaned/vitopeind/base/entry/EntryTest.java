package druyaned.vitopeind.base.entry;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class EntryTest {
    
    private static final ZoneId ZONE_ID = ZoneId.of("GMT");
    private static final ZonedDateTime START = ZonedDateTime.of(1984, 4, 24, 8, 0, 0, 0, ZONE_ID);
    private static final Score SCORE = new Score(2);
    private static final String DESC = "[description]";
    
    @Test public void createWithNegativeDuration() {
        ZonedDateTime stop = START.minusHours(2L);
        assertThrows(IllegalArgumentException.class, () -> new Entry(START, stop, SCORE, DESC));
    }
    
    @Test public void createWithDurationOfZero() {
        ZonedDateTime stop = START.plusSeconds(0L);
        assertThrows(IllegalArgumentException.class, () -> new Entry(START, stop, SCORE, DESC));
    }
    
    @Test public void createWithDurationLessThanOneSec() {
        ZonedDateTime stop = START.plusNanos(1L);
        Entry entry = new Entry(START, stop, SCORE, DESC);
        assertEntry(stop, entry);
    }
    
    @Test public void createWithDurationOfOneSec() {
        ZonedDateTime stop = START.plusSeconds(1L);
        Entry entry = new Entry(START, stop, SCORE, DESC);
        assertEntry(stop, entry);
    }
    
    @Test public void createWithDurationGreaterThanOneSec() {
        ZonedDateTime stop = START.plusSeconds(2L);
        Entry entry = new Entry(START, stop, SCORE, DESC);
        assertEntry(stop, entry);
    }
    
    @Test public void createWithNull() {
        ZonedDateTime stop = START.plusSeconds(2L);
        assertThrows(NullPointerException.class, () -> new Entry(null, stop, SCORE, DESC));
        assertThrows(NullPointerException.class, () -> new Entry(START, null, SCORE, DESC));
        assertThrows(NullPointerException.class, () -> new Entry(START, stop, null, DESC));
        assertThrows(NullPointerException.class, () -> new Entry(START, stop, SCORE, null));
    }
    
    @Test public void createWithDifferentZones() {
        ZonedDateTime start = ZonedDateTime.of(1984, 4, 24, 8, 0, 0, 0, ZoneId.of("UTC"));
        ZonedDateTime stop = ZonedDateTime.of(1984, 4, 24, 9, 0, 0, 0, ZoneId.of("UTC+03"));
        assertThrows(IllegalArgumentException.class, () -> new Entry(start, stop, SCORE, DESC));
    }
    
    private void assertEntry(ZonedDateTime stop, Entry entry) {
        assertEquals(START, entry.getStart());
        assertEquals(stop, entry.getStop());
        assertEquals(SCORE, entry.getScore());
        assertEquals(DESC, entry.getDesc());
    }
    
}
