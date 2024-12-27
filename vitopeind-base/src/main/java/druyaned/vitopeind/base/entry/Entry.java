package druyaned.vitopeind.base.entry;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Objects;

public class Entry {
    
    private final ZonedDateTime start;
    private final ZonedDateTime stop;
    private final Score score;
    private final String desc;
    
    public Entry(ZonedDateTime start, ZonedDateTime stop, Score score, String desc) {
        throwIfNull(start, stop, score, desc);
        throwIfInvalidInstants(start, stop);
        this.start = start;
        this.stop = stop;
        this.score = score;
        this.desc = desc;
    }
    
    public ZonedDateTime getStart() {
        return start;
    }
    
    public ZonedDateTime getStop() {
        return stop;
    }
    
    public Score getScore() {
        return score;
    }
    
    public String getDesc() {
        return desc;
    }
    
    @Override public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.start);
        hash = 53 * hash + Objects.hashCode(this.stop);
        hash = 53 * hash + Objects.hashCode(this.score);
        hash = 53 * hash + Objects.hashCode(this.desc);
        return hash;
    }
    
    @Override public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Entry other = (Entry)obj;
        if (!Objects.equals(this.start, other.start)) {
            return false;
        }
        if (!Objects.equals(this.stop, other.stop)) {
            return false;
        }
        if (!Objects.equals(this.desc, other.desc)) {
            return false;
        }
        return Objects.equals(this.score, other.score);
    }
    
    @Override public String toString() {
        return "Entry{start=" + start
                + ", stop=" + stop
                + ", score=" + score
                + ", desc=" + desc
                + '}';
    }
    
    private void throwIfNull(ZonedDateTime start,ZonedDateTime stop,Score score,String desc) {
        if (start == null) {
            throw new NullPointerException("start=null that is invalid");
        }
        if (stop == null) {
            throw new NullPointerException("stop=null that is invalid");
        }
        if (score == null) {
            throw new NullPointerException("score=null that is invalid");
        }
        if (desc == null) {
            throw new NullPointerException("desc=null that is invalid");
        }
    }
    
    private void throwIfInvalidInstants(ZonedDateTime start, ZonedDateTime stop) {
        Duration dur = Duration.between(start, stop);
        if (!start.getZone().equals(stop.getZone()) || dur.isZero() || dur.isNegative()) {
            throw new IllegalArgumentException("start=" + start + " and stop=" + stop
                    + ", zones must be equal"
                    + " and duration between start and stop must be positive");
        }
    }
    
}
