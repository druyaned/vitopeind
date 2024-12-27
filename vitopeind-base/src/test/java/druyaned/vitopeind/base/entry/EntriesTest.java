package druyaned.vitopeind.base.entry;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class EntriesTest {
    
    private static final ZoneId ZONE_ID = ZoneId.of("GMT");
    private static final long DURATION_SEC = 2L;
    private static final ZonedDateTime START = ZonedDateTime.of(1984, 4, 24, 8, 0, 0, 0, ZONE_ID);
    private static final ZonedDateTime STOP = START.plusSeconds(DURATION_SEC);
    private static final Score SCORE = new Score(2d);
    private static final String DESC = "[description]";
    
    @Test public void addValidEntries() {
        ZonedDateTime[] starts = new ZonedDateTime[] {
            START,
            START.plusSeconds(DURATION_SEC),
            START.plusSeconds(3L * DURATION_SEC + 1L),
            START.plusYears(1L)
        };
        int amount = starts.length;
        ZonedDateTime[] stops = new ZonedDateTime[amount];
        for (int i = 0; i < amount; i++) {
            stops[i] = starts[i].plusSeconds(DURATION_SEC);
        }
        Entries entries = new Entries();
        for (int i = 0; i < amount; i++) {
            Entry entry = new Entry(starts[i], stops[i], SCORE, DESC);
            entries.add(entry);
            assertEquals(entry, entries.get(i));
        }
    }
    
    @Test public void addNullEntry() {
        Entries entries = new Entries();
        assertThrows(NullPointerException.class, () -> entries.add(null));
        entries.add(new Entry(START, STOP, SCORE, DESC));
        assertThrows(NullPointerException.class, () -> entries.add(null));
    }
    
    @Test public void addInvalidEntries() {
        ZonedDateTime[] starts = new ZonedDateTime[] {
            START.minusSeconds(10L * DURATION_SEC),
            START.minusSeconds(3L * DURATION_SEC),
            START.minusSeconds(DURATION_SEC),
            START.minusSeconds(DURATION_SEC / 2L),
            START,
            START.plusSeconds(DURATION_SEC / 2L),
            START.plusSeconds(DURATION_SEC).minusNanos(1L),
        };
        int amount = starts.length;
        ZonedDateTime[] stops = new ZonedDateTime[amount];
        for (int i = 0; i < amount; i++) {
            stops[i] = starts[i].plusSeconds(DURATION_SEC);
        }
        final Entry ENTRY = new Entry(START, STOP, SCORE, DESC);
        for (int i = 0; i < amount; i++) {
            Entries entries = new Entries();
            entries.add(ENTRY);
            Entry entry = new Entry(starts[i], stops[i], SCORE, DESC);
            assertThrows(IllegalArgumentException.class, () -> entries.add(entry));
        }
    }
    
}
