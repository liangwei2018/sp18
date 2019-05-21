
package test;

import org.junit.Test;
import src.Babysitter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BabysitterTest {
    String start;
    String end;
    int family;
    Babysitter bb;
    boolean thrown = false;

    @Test
    public void testStartEndTime() {
        start = "7:00 pm";
        end = "1:00 am";
        family = 1;
        bb = new Babysitter(start, end, family);
        assertEquals(7, bb.getStartHour());
        assertEquals(1, bb.getEndHour());
    }

    @Test
    public void testStartEndTimeException() {
        start = "4:00 pm";
        end = "1:00 am";
        family = 1;
        try {
            bb = new Babysitter(start, end, family);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        assertTrue(thrown);

        thrown = false;
        start = "4:00 am";
        try {
            bb = new Babysitter(start, end, family);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testStartEndTime2() {
        start = "7 pm";
        end = "0 AM";
        family = 1;
        bb = new Babysitter(start, end, family);
        assertEquals(7, bb.getStartHour());
        assertEquals(0, bb.getEndHour());
    }

}
