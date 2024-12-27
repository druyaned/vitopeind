package druyaned.vitopeind.base.calendar;

import druyaned.vitopeind.base.entry.Entry;
import druyaned.vitopeind.base.util.Table;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Year extends CalendarUnit {
    
    private final Table<Month> months;
    private transient ZonedDateTime entryStart;
    private transient ZonedDateTime entryStop;
    private transient ZonedDateTime yearEntryStart;
    private transient ZonedDateTime yearEntryStop;
    
    protected Year(ZonedDateTime start, ZonedDateTime stop) {
        super(start, stop);
        this.months = new Table<>(12);
    }
    
    private void setBy(Entry entry) {
        entryStart = entry.getStart();
        entryStop = entry.getStop();
        yearEntryStart = (entryStart.compareTo(start) < 0) ? start : entryStart;
        yearEntryStop = (stop.compareTo(entryStop) < 0) ? stop : entryStop;
    }
    
    @Override public void add(Entry entry) {
        setBy(entry);
        throwIfInvalidEntry(entryStart, entryStop);
        super.add(entry);
        updateByUnitEntryAddition(entry, yearEntryStart, yearEntryStop);
        updateMonthsByAddition(entry);
    }
    
    private void updateMonthsByAddition(Entry entry) {
        int y = start.getYear();
        ZoneId zoneId = start.getZone();
        int monthFirst = yearEntryStart.getMonthValue();
        int monthLast = yearEntryStop.minusNanos(1L).getMonthValue();
        for (int m = monthFirst; m <= monthLast; m++) {
            Month month = months.getBy(m);
            if (month == null) {
                ZonedDateTime monthStart = ZonedDateTime.of(y, m, 1, 0, 0, 0, 0, zoneId);
                ZonedDateTime monthStop = monthStart.plusMonths(1L);
                month = new Month(monthStart, monthStop, this);
                months.add(m, month);
            }
            month.add(entry);
        }
    }
    
    @Override public Entry remove() {
        Entry removed = super.remove();
        setBy(removed);
        updateByUnitEntryRemoval(removed, yearEntryStart, yearEntryStop);
        updateMonthsByRemoval();
        return removed;
    }
    
    private void updateMonthsByRemoval() {
        int monthLast = yearEntryStop.minusNanos(1L).getMonthValue();
        int monthFirst = yearEntryStart.getMonthValue();
        for (int m = monthLast; m >= monthFirst; m--) {
            Month month = months.getBy(m);
            month.remove();
            if (month.isEmpty()) {
                months.remove();
            }
        }
    }
    
    public Month getMonthBy(int monthValue) {
        return months.getBy(monthValue);
    }
    
    public Month getMonthAt(int i) {
        return months.getAt(i);
    }
    
    public int getCapacityOfMonths() {
        return months.getCapacity();
    }
    
    public int getSizeOfMonths() {
        return months.getSize();
    }
    
}
