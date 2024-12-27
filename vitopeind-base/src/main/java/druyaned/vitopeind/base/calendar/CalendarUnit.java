package druyaned.vitopeind.base.calendar;

import druyaned.vitopeind.base.entry.Score;
import druyaned.vitopeind.base.entry.Entries;
import druyaned.vitopeind.base.entry.Entry;
import java.time.Duration;
import java.time.ZonedDateTime;

public class CalendarUnit extends Entries {
    
    protected final ZonedDateTime start;
    protected final ZonedDateTime stop;
    protected final long durationMillis;
    private double unitEntryAreaSum = 0d;
    private long unitEntryDurationMillisSum = 0L;

    protected CalendarUnit(ZonedDateTime start, ZonedDateTime stop) {
        super();
        this.start = start;
        this.stop = stop;
        this.durationMillis = Duration.between(start, stop).toMillis();
    }
    
    public double getProgress() {
        return ((double)unitEntryDurationMillisSum / (double)durationMillis) * 100d;
    }
    
    public Score getScore() {
        return isEmpty()
                ? null
                : new Score(unitEntryAreaSum / (double)unitEntryDurationMillisSum);
    }
    
    public ZonedDateTime getStart() {
        return start;
    }
    
    public ZonedDateTime getStop() {
        return stop;
    }
    
    public long getDurationMillis() {
        return durationMillis;
    }
    
    protected final void updateByUnitEntryAddition(
            Entry entry,
            ZonedDateTime unitEntryStart,
            ZonedDateTime unitEntryStop
    ) {
        long unitEntryDuration = Duration.between(unitEntryStart, unitEntryStop).toMillis();
        unitEntryDurationMillisSum += unitEntryDuration;
        double unitEntryArea = entry.getScore().getValue() * (double)unitEntryDuration;
        unitEntryAreaSum += unitEntryArea;
    }
    
    protected final void updateByUnitEntryRemoval(
            Entry removed,
            ZonedDateTime unitEntryStart,
            ZonedDateTime unitEntryStop
    ) {
        long unitEntryDuration = Duration.between(unitEntryStart, unitEntryStop).toMillis();
        unitEntryDurationMillisSum -= unitEntryDuration;
        double unitEntryArea = removed.getScore().getValue() * (double)unitEntryDuration;
        unitEntryAreaSum -= unitEntryArea;
    }
    
    protected final void throwIfInvalidEntry(ZonedDateTime entryStart, ZonedDateTime entryStop) {
        if (entryStop.compareTo(start) <= 0 || stop.compareTo(entryStart) <= 0) {
            throw new IllegalArgumentException("start=" + start + ", stop=" + stop
                    + ", entryStart=" + entryStart + ", entryStop=" + entryStop
                    + ", (start < entryStop && entryStart < stop) is false");
        }
    }
    
}
