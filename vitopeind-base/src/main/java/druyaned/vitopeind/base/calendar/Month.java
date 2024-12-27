package druyaned.vitopeind.base.calendar;

import static druyaned.vitopeind.base.calendar.Day.DAY_MILLIS;
import druyaned.vitopeind.base.entry.Entry;
import druyaned.vitopeind.base.util.Table;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Month extends CalendarUnit {
    
    private final Year year;
    private final Table<Day> days;
    private transient ZonedDateTime entryStart;
    private transient ZonedDateTime entryStop;
    private transient ZonedDateTime monthEntryStart;
    private transient ZonedDateTime monthEntryStop;
    
    protected Month(ZonedDateTime start, ZonedDateTime stop, Year year) {
        super(start, stop);
        this.year = year;
        int capacityOfDays = (int)(durationMillis / DAY_MILLIS);
        this.days = new Table<>(capacityOfDays);
    }
    
    private void setBy(Entry entry) {
        entryStart = entry.getStart();
        entryStop = entry.getStop();
        monthEntryStart = (entryStart.compareTo(start) < 0) ? start : entryStart;
        monthEntryStop = (stop.compareTo(entryStop) < 0) ? stop : entryStop;
    }
    
    @Override public void add(Entry entry) {
        setBy(entry);
        throwIfInvalidEntry(entryStart, entryStop);
        super.add(entry);
        updateByUnitEntryAddition(entry, monthEntryStart, monthEntryStop);
        updateDaysByAddition(entry);
    }
    
    private void updateDaysByAddition(Entry entry) {
        int y = start.getYear();
        int m = start.getMonthValue();
        ZoneId zoneId = start.getZone();
        int dayFirst = monthEntryStart.getDayOfMonth();
        int dayLast = monthEntryStop.minusNanos(1L).getDayOfMonth();
        for (int d = dayFirst; d <= dayLast; d++) {
            Day day = days.getBy(d);
            if (day == null) {
                ZonedDateTime dayStart = ZonedDateTime.of(y, m, d, 0, 0, 0, 0, zoneId);
                ZonedDateTime dayStop = dayStart.plusDays(1L);
                day = new Day(dayStart, dayStop, this);
                days.add(d, day);
            }
            day.add(entry);
        }
    }
    
    @Override public Entry remove() {
        Entry removed = super.remove();
        setBy(removed);
        updateByUnitEntryRemoval(removed, monthEntryStart, monthEntryStop);
        updateDaysByRemoval();
        return removed;
    }
    
    private void updateDaysByRemoval() {
        int dayLast = monthEntryStop.minusNanos(1L).getDayOfMonth();
        int dayFirst = monthEntryStart.getDayOfMonth();
        for (int d = dayLast; d >= dayFirst; d--) {
            Day day = days.getBy(d);
            day.remove();
            if (day.isEmpty()) {
                days.remove();
            }
        }
    }
    
    public Year getYear() {
        return year;
    }
    
    public Day getDayBy(int dayValue) {
        return days.getBy(dayValue);
    }
    
    public Day getDayAt(int i) {
        return days.getAt(i);
    }
    
    public int getCapacityOfDays() {
        return days.getCapacity();
    }
    
    public int getSizeOfDays() {
        return days.getSize();
    }
    
}
