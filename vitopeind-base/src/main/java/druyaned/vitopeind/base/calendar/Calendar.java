package druyaned.vitopeind.base.calendar;

import druyaned.vitopeind.base.entry.Entries;
import druyaned.vitopeind.base.entry.Entry;
import druyaned.vitopeind.base.entry.Score;
import druyaned.vitopeind.base.util.Table;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Provides {@link Score colored} {@link Year years} (also
 * {@link Month months} and {@link Day days}) that can help
 * in life activity tracking.
 * 
 * @author druyaned
 */
public class Calendar extends Entries {
    
    /**
     * Difference between the first and the last year of a calendar
     * cannot be greater than or equal this value.
     */
    public static final int CAPACITY_OF_YEARS = 256;
    
    private final Table<Year> years = new Table<>(CAPACITY_OF_YEARS);
    
    /**
     * Creates a new calendar which is {@link Entries entries} and
     * {@link Table table of years}.
     * @see Entries#Entries()
     */
    public Calendar() {
        super();
    }
    
    @Override public void add(Entry entry) {
        super.add(entry);
        int yearFirst = entry.getStart().getYear();
        int yearLast = entry.getStop().minusNanos(1L).getYear();
        ZoneId zoneId = entry.getStart().getZone();
        for (int y = yearFirst; y <= yearLast; y++) {
            Year year = years.getBy(y);
            if (year == null) {
                ZonedDateTime yearStart = ZonedDateTime.of(y, 1, 1, 0, 0, 0, 0, zoneId);
                ZonedDateTime yearStop = yearStart.plusYears(1L);
                year = new Year(yearStart, yearStop);
                years.add(y, year);
            }
            year.add(entry);
        }
    }
    
    @Override public Entry remove() {
        Entry removed = super.remove();
        int yearLast = removed.getStop().minusNanos(1L).getYear();
        int yearFirst = removed.getStart().getYear();
        for (int y = yearLast; y >= yearFirst; y--) {
            Year year = years.getBy(y);
            year.remove();
            if (year.isEmpty()) {
                years.remove();
            }
        }
        return removed;
    }
    
    /**
     * Returns year by the given year value as {@link Table#getBy} does.
     * @param yearValue value of an added year, like 2002 or 1984
     * @return year by the given year value as {@link Table#getBy} does
     */
    public Year getYearBy(int yearValue) {
        return years.getBy(yearValue);
    }
    
    /**
     * Returns year by the given index as {@link Table#getAt} does.
     * @param i index of an added year
     * @return year by the given index as {@link Table#getAt} does
     */
    public Year getYearAt(int i) {
        return years.getAt(i);
    }
    
    /**
     * Returns the size of added years as {@link Table#getSize} does.
     * @return size of added years as {@link Table#getSize} does
     */
    public int getSizeOfYears() {
        return years.getSize();
    }
    
}
