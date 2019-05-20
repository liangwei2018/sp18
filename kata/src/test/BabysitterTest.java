
package test;

import org.junit.Test;
import src.Babysitter;

import static org.junit.Assert.assertEquals;

public class BabysitterTest {

    @Test
    public void testStartEndTime() {
        String start = "7:00 pm";
        String end = "1:00 am";
        int family = 1;
        Babysitter bb = new Babysitter(start, end, family);
        assertEquals(7, bb.getStartHour());
        assertEquals(1, bb.getEndHour());
    }


}
