package druyaned.vitopeind.base.calendar;

import static druyaned.vitopeind.base.calendar.Calendar.CAPACITY_OF_YEARS;
import druyaned.vitopeind.base.entry.Score;
import druyaned.vitopeind.base.entry.Entry;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class CalendarTest {
    
    private static final ZoneId ZONE_ID = ZoneId.of("GMT");
    
    @Test public void updateByOneEntry() {
        Entry entry = new Entry(
                ZonedDateTime.of(1984, 4, 24, 6, 0, 0, 0, ZONE_ID),
                ZonedDateTime.of(1984, 4, 24, 8, 0, 0, 0, ZONE_ID),
                new Score(2d),
                ""
        );
        Calendar calendar = new Calendar();
        calendar.add(entry);
        assertEquals(1, calendar.getSizeOfYears());
        Year year = calendar.getYearAt(0);
        assertEquals(ZonedDateTime.of(1984, 1, 1, 0, 0, 0, 0, ZONE_ID), year.getStart());
        assertEquals(366L * Day.DAY_MILLIS, year.durationMillis);
        Month month = year.getMonthAt(0);
        assertEquals(ZonedDateTime.of(1984, 4, 1, 0, 0, 0, 0, ZONE_ID), month.getStart());
        assertEquals(30L * Day.DAY_MILLIS, month.durationMillis);
        Day day = month.getDayAt(0);
        assertEquals(ZonedDateTime.of(1984, 4, 24, 0, 0, 0, 0, ZONE_ID), day.getStart());
        assertEquals(Day.DAY_MILLIS, day.durationMillis);
        Entry removed = calendar.remove();
        assertEquals(entry, removed);
        assertTrue(calendar.isEmpty());
    }
    
    @Test public void updateBySeveralEntries() {
        Entry entry0 = new Entry(
                ZonedDateTime.of(1981, 1, 1, 0, 0, 0, 0, ZONE_ID),
                ZonedDateTime.of(1982, 7, 2, 12, 0, 0, 0, ZONE_ID),
                new Score(2d),
                ""
        );
        Entry entry1 = new Entry(
                ZonedDateTime.of(1982, 7, 2, 12, 0, 0, 0, ZONE_ID),
                ZonedDateTime.of(1983, 1, 1, 0, 0, 0, 0, ZONE_ID),
                new Score(6d),
                ""
        );
        double progressOf1981 = 100d;
        double progressOf1982AfterEntry0 = 50d;
        double progressOf1982AfterEntry1 = 100d;
        double progressOf1982_07_02AfterEntry0 = 50d;
        double progressOf1982_07_02AfterEntry1 = 100d;
        Score scoreOf1981 = new Score(2d);
        Score scoreOf1982AfterEntry0 = new Score(2d);
        Score scoreOf1982AfterEntry1 = new Score(4d);
        Score scoreOf1982_07_02AfterEntry0 = new Score(2d);
        Score scoreOf1982_07_02AfterEntry1 = new Score(4d);
        Calendar calendar = new Calendar();
        assertTrue(calendar.isEmpty());
        calendar.add(entry0);
        assertEquals(2, calendar.getSizeOfYears());
        Year year_1981 = calendar.getYearBy(1981);
        Year year_1982 = calendar.getYearBy(1982);
        assertEquals(12, year_1981.getSizeOfMonths());
        assertEquals(progressOf1981, year_1981.getProgress());
        assertEquals(progressOf1982AfterEntry0, year_1982.getProgress());
        Day day_1982_07_02 = year_1982.getMonthBy(7).getDayBy(2);
        assertEquals(2, year_1982.getMonthBy(7).getSizeOfDays());
        assertEquals(progressOf1982_07_02AfterEntry0, day_1982_07_02.getProgress());
        assertEquals(scoreOf1981, year_1981.getScore());
        assertEquals(scoreOf1982AfterEntry0, year_1982.getScore());
        assertEquals(scoreOf1982_07_02AfterEntry0, day_1982_07_02.getScore());
        calendar.add(entry1);
        assertEquals(progressOf1981, year_1981.getProgress());
        assertEquals(progressOf1982AfterEntry1, year_1982.getProgress());
        assertEquals(progressOf1982_07_02AfterEntry1, day_1982_07_02.getProgress());
        assertEquals(scoreOf1981, year_1981.getScore());
        assertEquals(scoreOf1982AfterEntry1, year_1982.getScore());
        assertEquals(scoreOf1982_07_02AfterEntry1, day_1982_07_02.getScore());
        calendar.remove();
        assertEquals(progressOf1981, year_1981.getProgress());
        assertEquals(progressOf1982AfterEntry0, year_1982.getProgress());
        assertEquals(progressOf1982_07_02AfterEntry0, day_1982_07_02.getProgress());
        assertEquals(scoreOf1981, year_1981.getScore());
        assertEquals(scoreOf1982AfterEntry0, year_1982.getScore());
        assertEquals(scoreOf1982_07_02AfterEntry0, day_1982_07_02.getScore());
        calendar.remove();
        assertTrue(calendar.isEmpty());
    }
    
    @Test public void addLongEntry() {
        Entry entry = new Entry(
                ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZONE_ID),
                ZonedDateTime.of(2000 + CAPACITY_OF_YEARS, 1, 1, 0, 0, 0, 0, ZONE_ID),
                new Score(2d),
                ""
        );
        Calendar calendar = new Calendar();
        calendar.add(entry);
        assertEquals(CAPACITY_OF_YEARS, calendar.getSizeOfYears());
        for (int i = 0; i < calendar.getSizeOfYears(); i++) {
            Year year = calendar.getYearAt(i);
            assertEquals(12, year.getSizeOfMonths());
            int yearValue = year.getStart().getYear();
            for (int j = 0; j < year.getSizeOfMonths(); j++) {
                Month month = year.getMonthAt(j);
                int monthValue = month.getStart().getMonthValue();
                YearMonth yearMonth = YearMonth.of(yearValue, monthValue);
                assertEquals(yearMonth.lengthOfMonth(), month.getSizeOfDays());
            }
        }
    }
    
    @Test public void additionOfTooLongEntry() {
        final Entry entry = new Entry(
                ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZONE_ID),
                ZonedDateTime.of(2000 + CAPACITY_OF_YEARS, 1, 1, 0, 0, 0, 1, ZONE_ID),
                new Score(2d),
                ""
        );
        assertThrows(IllegalArgumentException.class, () -> new Calendar().add(entry));
    }
    
}
