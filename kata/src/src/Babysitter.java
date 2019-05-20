package src;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Simulates a babysitter working and getting paid for one night.
 * startHour: work start time
 * endHour: work end time
 * Note that 12 was added to AM hours for comparison purpose.
 * family: family type
 *
 */

public class Babysitter {
    private int startHour;
    private int endHour;
    private int family;

    /**
     *
     * @param s the start time string
     * @param e the end time string
     * @param f the family type
     */
    public Babysitter(String s, String e, int f) {
        if (s == null || e == null) {
            throw new IllegalArgumentException("Null time string!");
        }
        startHour = getHour(s);
        endHour = getHour(e);
        if (startHour > endHour) {
            throw new IllegalArgumentException("End time before start time!");
        }
        if (startHour < 5 || endHour > 16) {
            throw new IllegalArgumentException("Outside of allowable work hours!");
        }
        family = f;
    }

    /**
     * Converts the time string s to hours.
     * Add 12 to AM Hours for comparison with PM Hours.
     * @param s the time string
     * @return the hour in int format
     */
    private int getHour(String s) {
        String hour = s.substring(0, s.indexOf(":"));
        int returnHour = Integer.parseInt(hour);
        if (s.contains("am") || s.contains("AM")) {
            returnHour += 12;
        }
        return returnHour;
    }

    public int getStartHour() {
        return startHour % 12;
    }

    public int getEndHour() {
        return endHour % 12;
    }

}
