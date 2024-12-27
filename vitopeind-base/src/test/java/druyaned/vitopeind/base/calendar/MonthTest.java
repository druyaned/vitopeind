package druyaned.vitopeind.base.calendar;

import druyaned.vitopeind.base.entry.Score;
import druyaned.vitopeind.base.entry.Entry;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MonthTest {
    
    private static final ZoneId ZONE_ID = ZoneId.of("GMT");
    
    private Year year;
    private Month month;
    
    @BeforeEach public void setUp() {
        ZonedDateTime yearStart = ZonedDateTime.of(1983, 1, 1, 0, 0, 0, 0, ZONE_ID);
        ZonedDateTime yearStop = yearStart.plusYears(1L);
        ZonedDateTime monthStart = ZonedDateTime.of(1983, 2, 1, 0, 0, 0, 0, ZONE_ID);
        ZonedDateTime monthStop = monthStart.plusMonths(1L);
        year = new Year(yearStart, yearStop);
        month = new Month(monthStart, monthStop, year);
    }
    
    @Test public void createWithLeapYear() {
        ZonedDateTime yearStart = ZonedDateTime.of(1984, 1, 1, 0, 0, 0, 0, ZONE_ID);
        ZonedDateTime yearStop = yearStart.plusYears(1L);
        ZonedDateTime monthStart = ZonedDateTime.of(1984, 2, 1, 0, 0, 0, 0, ZONE_ID);
        ZonedDateTime monthStop = monthStart.plusMonths(1L);
        Year localYear = new Year(yearStart, yearStop);
        Month localMonth = new Month(monthStart, monthStop, localYear);
        assertEquals(29, localMonth.getCapacityOfDays());
    }
    
    @Test public void createWithNotLeapYear() {
        assertEquals(28, month.getCapacityOfDays());
        assertEquals(Day.DAY_MILLIS * 28, month.durationMillis);
    }
    
    @Test public void updateBySeveralEntries() {
        Entry[] entries = {
            new Entry(month.start.minusDays(3L), month.start.plusDays(14L), new Score(2d), ""),
            new Entry(month.start.plusDays(15L), month.start.plusDays(22L), new Score(8d), "")
        };
        final int len = entries.length;
        double[] progresses = { 50.0, 75.0 };
        Score[] scores = { new Score(2d), new Score(4d) };
        int[] sizesOfDays = { 14, 21 };
        for (int i = 0; i < len; i++) {
            final Entry entry = entries[i];
            int prevSizeOfDays = month.getSizeOfDays();
            month.add(entry);
            assertEquals(progresses[i], month.getProgress());
            assertEquals(scores[i], month.getScore());
            assertEquals(sizesOfDays[i], month.getSizeOfDays());
            for (int j = prevSizeOfDays; j < month.getSizeOfDays(); j++) {
                assertEquals(month.getDayAt(j).getScore(), entry.getScore());
            }
        }
        for (int i = len - 1; i > 0; i--) {
            month.remove();
            assertEquals(progresses[i - 1], month.getProgress());
            assertEquals(scores[i - 1], month.getScore());
            assertEquals(sizesOfDays[i - 1], month.getSizeOfDays());
        }
    }
    
    @Test public void updateByInvalidEntries() {
        Entry[] entries = {
            new Entry(month.start.minusDays(3L), month.start.minusHours(3L), new Score(2d), ""),
            new Entry(month.start.minusHours(3L), month.start, new Score(2d), ""),
            new Entry(month.stop, month.stop.plusHours(3L), new Score(2d), ""),
            new Entry(month.stop.plusHours(3L), month.stop.plusDays(3L), new Score(2d), ""),
        };
        final int len = entries.length;
        for (int i = 0; i < len; i++) {
            final Entry entry = entries[i];
            assertThrows(IllegalArgumentException.class, () -> month.add(entry));
        }
    }
    
}
