package druyaned.vitopeind.base.entry;

import druyaned.vitopeind.base.util.List;
import java.time.Duration;
import java.time.ZonedDateTime;

public class Entries extends List<Entry> {
    
    public Entries(int capacity) {
        super(capacity);
    }
    
    public Entries() {
        super();
    }
    
    @Override public void add(Entry entry) {
        throwIfNull(entry);
        throwIfInvalidEntry(entry);
        super.add(entry);
    }
    
    public static void throwIfNull(Entry entry) throws NullPointerException {
        if (entry == null) {
            throw new NullPointerException("entry=null that is invalid");
        }
    }
    
    private void throwIfInvalidEntry(Entry entry) {
        if (isEmpty()) {
            return;
        }
        ZonedDateTime lastStop = getLast().getStop();
        ZonedDateTime entryStart = entry.getStart();
        if (
                !lastStop.getZone().equals(entryStart.getZone())
                || Duration.between(lastStop, entryStart).isNegative()
        ) {
            throw new IllegalArgumentException("lastStop=" + getLast().getStop()
                    + " and entryStart=" + entry.getStart()
                    + ", zones must be equal"
                    + " and duration between lastStop and entryStart must be not negative");
        }
    }
    
}
