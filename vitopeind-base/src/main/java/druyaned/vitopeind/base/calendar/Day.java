package druyaned.vitopeind.base.calendar;

import druyaned.vitopeind.base.entry.Entry;
import java.time.ZonedDateTime;

public class Day extends CalendarUnit {
    
    public static final long DAY_MILLIS = 1000L * 60L * 60L * 24L;
    
    protected final Month month;
    private transient ZonedDateTime entryStart;
    private transient ZonedDateTime entryStop;
    private transient ZonedDateTime dayEntryStart;
    private transient ZonedDateTime dayEntryStop;
    
    protected Day(ZonedDateTime start, ZonedDateTime stop, Month month) {
        super(start, stop);
        this.month = month;
    }
    
    private void setBy(Entry entry) {
        entryStart = entry.getStart();
        entryStop = entry.getStop();
        dayEntryStart = (entryStart.compareTo(start) < 0) ? start : entryStart;
        dayEntryStop = (stop.compareTo(entryStop) < 0) ? stop : entryStop;
    }
    
    @Override public void add(Entry entry) {
        setBy(entry);
        throwIfInvalidEntry(entryStart, entryStop);
        super.add(entry);
        updateByUnitEntryAddition(entry, dayEntryStart, dayEntryStop);
    }
    
    @Override public Entry remove() {
        Entry removed = super.remove();
        setBy(removed);
        updateByUnitEntryRemoval(removed, dayEntryStart, dayEntryStop);
        return removed;
    }
    
    public Month getMonth() {
        return month;
    }
    
}
