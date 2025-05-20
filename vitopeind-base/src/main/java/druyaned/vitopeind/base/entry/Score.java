package druyaned.vitopeind.base.entry;

import java.awt.Color;
import java.util.Objects;

public class Score {
    
    public static final double MIN_VALUE = -10d;
    public static final double MAX_VALUE = +10d;
    
    private final double value;
    private final Color color;
    
    public Score(double value) {
        throwIfInvalidValue(value);
        this.value = value;
        this.color = convertValueToColor(value);
    }
    
    public double getValue() {
        return value;
    }
    
    public Color getColor() {
        return color;
    }
    
    @Override public int hashCode() {
        int hash = 7;
        hash = 47 * hash
                + (int)(Double.doubleToLongBits(this.value)
                ^ (Double.doubleToLongBits(this.value) >>> 32));
        hash = 47 * hash + Objects.hashCode(this.color);
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
        final Score other = (Score)obj;
        if (Double.doubleToLongBits(this.value) != Double.doubleToLongBits(other.value)) {
            return false;
        }
        return Objects.equals(this.color, other.color);
    }
    
    @Override public String toString() {
        return "Score{value=" + value + ", color=" + color + '}';
    }
    
    /**
     * Converts score value into the color. Zero value is converted into
     * white color. The higher the positive value, the greener the color.
     * The lower the negative value, the redder the color.
     * 
     * <P><i>Examples of conversion</i><pre>
       *-10 -> ff|00|00
       * -2 -> 33|00|00
       *  0 -> 00|00|00
       * +2 -> 00|33|00
       *+10 -> 00|ff|00</pre>
     * 
     * @param value validated score value to convert it into the color
     * @return converted color, which is shade of red, shade of green or it is white
     */
    private static Color convertValueToColor(double value) {
        int red, green, blue = 0;
        if (value == 0) {
            red = green = 0;
        } else if (value < 0) {
            red = (int)(value * 0xff / MIN_VALUE);
            green = 0;
        } else {
            red = 0;
            green = (int)(value * 0xff / MAX_VALUE);
        }
        return new Color(red, green, blue);
    }
    
    private void throwIfInvalidValue(double value) {
        if (Double.compare(value, MIN_VALUE) < 0|| Double.compare(MAX_VALUE, value) < 0) {
            throw new IllegalArgumentException("value=" + value
                    + ", (" + MIN_VALUE + " <= value <= " + MAX_VALUE + ") is false");
        }
    }
    
}
