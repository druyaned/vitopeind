package druyaned.vitopeind.base.entry;

import java.awt.Color;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class ScoreTest {
    
    @Test public void createWithNegativeValue() {
        double value = -10d;
        int red = 0xff, green = 0, blue = 0;
        Color color = new Color(red, green, blue);
        Score score = new Score(value);
        assertScore(value, color, score);
    }
    
    @Test public void createWithZeroValue() {
        int value = 0;
        int red = 0, green = 0, blue = 0;
        Color color = new Color(red, green, blue);
        Score score = new Score(value);
        assertScore(value, color, score);
    }
    
    @Test public void createWithPositiveValue() {
        double value = 2d;
        int red = 0, green = 0x33, blue = 0;
        Color color = new Color(red, green, blue);
        Score score = new Score(value);
        assertScore(value, color, score);
    }
    
    @Test public void createWithTooBigValue() {
        double value = 11d;
        assertThrows(IllegalArgumentException.class, () -> new Score(value));
    }
    
    @Test public void createWithTooSmallValue() {
        int value = -408;
        assertThrows(IllegalArgumentException.class, () -> new Score(value));
    }
    
    private void assertScore(double value, Color color, Score score) {
        assertEquals(value, score.getValue());
        assertEquals(color, score.getColor());
    }
    
}
