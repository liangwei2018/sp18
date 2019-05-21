
package test;

import org.junit.Test;
import src.Babysitter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Babysitter tests
 * @author Liang Wei
 */

public class BabysitterTest {
    String start;
    String end;
    Babysitter bb;
    boolean thrown = false;

    @Test
    public void testStartEndTime() {
        start = "7:00 pm";
        end = "1:00 am";
        bb = new Babysitter(start, end);
        assertEquals(7, bb.getStartHour());
        assertEquals(1, bb.getEndHour());
    }

    /** test wrong start/end times */
    @Test
    public void testStartEndTimeException() {
        start = "4:00 pm";
        end = "1:00 am";
        try {
            bb = new Babysitter(start, end);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        assertTrue(thrown);

        thrown = false;
        start = "12:00 pm";
        end = "4:00 am";
        try {
            bb = new Babysitter(start, end);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    /** test fractional times */
    @Test
    public void testFractionalTimes() {
        start = "4:25 pm";
        end = "1:30 am";
        try {
            bb = new Babysitter(start, end);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testStartEndTime2() {
        start = "7 pm";
        end = "0 AM";
        bb = new Babysitter(start, end);
        assertEquals(7, bb.getStartHour());
        assertEquals(12, bb.getEndHour());
    }

    @Test
    public void testStartEndTimeSpecial() {
        start = "7 pm";
        end = "12 AM";
        bb = new Babysitter(start, end);
        assertEquals(7, bb.getStartHour());
        assertEquals(12, bb.getEndHour());
    }
    /** assuming start end time works  */
    @Test
    public void testTotalPay() {
        start = "7 pm";
        end = "2 AM";
        bb = new Babysitter(start, end);
        assertEquals(120, bb.getTotalPay(1));
        assertEquals(84, bb.getTotalPay(2));
        assertEquals(117, bb.getTotalPay(3));

    }

    /** assuming start end time works  */
    @Test
    public void testTotalPaySpecial() {
        start = "5:00 pm";
        end = "9:00 PM";
        bb = new Babysitter(start, end);
        assertEquals(60, bb.getTotalPay(1));
        assertEquals(48, bb.getTotalPay(2));
        assertEquals(84, bb.getTotalPay(3));

        start = "10:00 pm";
        end = "11:00 PM";
        bb = new Babysitter(start, end);
        assertEquals(15, bb.getTotalPay(1));
        assertEquals(8, bb.getTotalPay(2));
        assertEquals(15, bb.getTotalPay(3));

        start = "12:00 am";
        end = "4:00 aM";
        bb = new Babysitter(start, end);
        assertEquals(80, bb.getTotalPay(1));
        assertEquals(64, bb.getTotalPay(2));
        assertEquals(60, bb.getTotalPay(3));
    }


}
