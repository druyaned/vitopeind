package druyaned.vitopeind.base.calendar;

import druyaned.vitopeind.base.entry.Score;
import druyaned.vitopeind.base.entry.Entry;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DayTest {
    
    private static final ZoneId ZONE_ID = ZoneId.of("GMT");
    
    private Year year;
    private Month month;
    private Day day;
    
    @BeforeEach public void setUp() {
        ZonedDateTime yearStart = ZonedDateTime.of(1983, 1, 1, 0, 0, 0, 0, ZONE_ID);
        ZonedDateTime yearStop = yearStart.plusYears(1L);
        ZonedDateTime monthStart = ZonedDateTime.of(1983, 2, 1, 0, 0, 0, 0, ZONE_ID);
        ZonedDateTime monthStop = monthStart.plusMonths(1L);
        ZonedDateTime dayStart = ZonedDateTime.of(1983, 2, 28, 0, 0, 0, 0, ZONE_ID);
        ZonedDateTime dayStop = dayStart.plusDays(1L);
        year = new Year(yearStart, yearStop);
        month = new Month(monthStart, monthStop, year);
        day = new Day(dayStart, dayStop, month);
    }
    
    @Test public void updateBySeveralEntries() {
        assertEquals(Day.DAY_MILLIS, day.durationMillis);
        Entry[] entries = {
            new Entry(day.start.minusDays(3L), day.start.plusHours(3L), new Score(-2d), ""),
            new Entry(day.start.plusHours(6L), day.start.plusHours(12L), new Score(+7d), ""),
            new Entry(day.stop.minusHours(9L), day.stop.plusDays(3L), new Score(+8d), "")
        };
        final int len = entries.length;
        double[] progresses = { 12.5, 37.5, 75.0 };
        Score[] scores = { new Score(-2d), new Score(4d), new Score(6d) };
        for (int i = 0; i < len; i++) {
            day.add(entries[i]);
            assertEquals(progresses[i], day.getProgress());
            assertEquals(scores[i], day.getScore());
        }
        for (int i = len - 1; i > 0; i--) {
            day.remove();
            assertEquals(progresses[i - 1], day.getProgress());
            assertEquals(scores[i - 1], day.getScore());
        }
    }
    
    @Test public void updateByInvalidEntries() {
        Entry[] entries = {
            new Entry(day.start.minusDays(3L), day.start.minusHours(6L), new Score(2d), ""),
            new Entry(day.start.minusHours(6L), day.start, new Score(2d), ""),
            new Entry(day.stop, day.stop.plusHours(6L), new Score(2d), ""),
            new Entry(day.stop.plusHours(6L), day.stop.plusDays(3L), new Score(2d), "")
        };
        final int len = entries.length;
        for (int i = 0; i < len; i++) {
            final Entry entry = entries[i];
            assertThrows(IllegalArgumentException.class, () -> day.add(entry));
        }
    }
    
}
