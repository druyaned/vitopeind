package druyaned.vitopeind.base.calendar;

import druyaned.vitopeind.base.entry.Score;
import druyaned.vitopeind.base.entry.Entry;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class YearTest {
    
    private static final ZoneId ZONE_ID = ZoneId.of("GMT");
    
    private Year year;
    
    @BeforeEach public void setUp() {
        ZonedDateTime yearStart = ZonedDateTime.of(1983, 1, 1, 0, 0, 0, 0, ZONE_ID);
        ZonedDateTime yearStop = yearStart.plusYears(1L);
        year = new Year(yearStart, yearStop);
    }
    
    @Test public void createWithLeapYear() {
        ZonedDateTime yearStart = ZonedDateTime.of(1984, 1, 1, 0, 0, 0, 0, ZONE_ID);
        ZonedDateTime yearStop = yearStart.plusYears(1L);
        Year localYear = new Year(yearStart, yearStop);
        assertEquals(366L * Day.DAY_MILLIS, localYear.durationMillis);
    }
    
    @Test public void createWithNotLeapYear() {
        assertEquals(365L * Day.DAY_MILLIS, year.durationMillis);
    }
    
    @Test public void updateBySeveralEntries() {
        Entry[] entries = {
            new Entry(year.start, year.start.plusDays(73L), new Score(8d), ""),
            new Entry(year.start.plusDays(146L), year.start.plusDays(292L), new Score(2d), ""),
            new Entry(year.start.plusDays(292L), year.start.plusDays(800L), new Score(8d), "")
        };
        final int len = entries.length;
        double[] progresses = { 20.0, 60.0, 80.0 };
        Score[] scores = { new Score(8d), new Score(4d), new Score(5d) };
        int[] sizesOfMonths = { 3, 9, 11 };
        for (int i = 0; i < len; i++) {
            final Entry entry = entries[i];
            int prevSizeOfMonths = year.getSizeOfMonths();
            year.add(entry);
            assertEquals(progresses[i], year.getProgress());
            assertEquals(scores[i], year.getScore());
            assertEquals(sizesOfMonths[i], year.getSizeOfMonths());
            for (int j = prevSizeOfMonths; j < year.getSizeOfMonths(); j++) {
                assertEquals(year.getMonthAt(j).getScore(), entry.getScore());
            }
        }
        for (int i = len - 1; i > 0; i--) {
            year.remove();
            assertEquals(progresses[i - 1], year.getProgress());
            assertEquals(scores[i - 1], year.getScore());
            assertEquals(sizesOfMonths[i - 1], year.getSizeOfMonths());
        }
    }
    
    @Test public void updateByInvalidEntries() {
        Entry[] entries = {
            new Entry(year.start.minusDays(3L), year.start.minusNanos(1L), new Score(2d), ""),
            new Entry(year.start.minusDays(3L), year.start, new Score(2d), ""),
            new Entry(year.stop, year.stop.plusHours(3L), new Score(2d), ""),
            new Entry(year.stop.plusNanos(1L), year.stop.plusHours(3L), new Score(2d), ""),
        };
        final int len = entries.length;
        for (int i = 0; i < len; i++) {
            final Entry entry = entries[i];
            assertThrows(IllegalArgumentException.class, () -> year.add(entry));
        }
    }
    
}
