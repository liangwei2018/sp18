package src;


/**
 * Simulates a babysitter working and getting paid for one night.
 * startHour: work start time
 * endHour: work end time
 * Note that 12 was added to AM hours for comparison purpose.
 *
 * @author Liang Wei
 */

public class Babysitter {
    private int startHour;
    private int endHour;


    /**
     * Constructor to initialize object with input parameters.
     * @param s the start time string (e.g.: "hh:mm am", "hh:mm PM")
     * @param e the end time string
     * @param f the family type
     */
    public Babysitter(String s, String e) {
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
    }

    /**
     * Converts the time string s to hours.
     * Adds 12 to AM Hours (except 12am) for comparison with PM Hours.
     * Sets 12PM to 0pm for comparison purpose.
     * Also accommodates time formats: e.g. 6pm, 6 PM, 3AM.
     * @param s the time string
     * @return the hour in int format
     */
    private int getHour(String s) {
        String hour;
        String minutes;
        int lastIndex = s.length() - 2;
        if (s.contains(" ")) {
            lastIndex = s.indexOf(" ");
        }
        if (s.contains(":")) {
            int colonIndex = s.indexOf(":");
            hour = s.substring(0, colonIndex);
            minutes = s.substring(colonIndex + 1, lastIndex);
        } else {
            hour = s.substring(0, lastIndex);
            minutes = "00";
        }
        if (Integer.parseInt(minutes) > 0) {
            throw new IllegalArgumentException("No fractional hours!");
        }
        int returnHour = Integer.parseInt(hour);

        boolean isPM = s.contains("pm") || s.contains("PM")
                || s.contains("pM") || s.contains("Pm");
        if (returnHour == 12 && isPM) {
            return 0;
        }

        boolean isAM = s.contains("am") || s.contains("AM")
                || s.contains("aM") || s.contains("Am");
        if (returnHour < 12 && isAM) {
            returnHour += 12;

        }
        return returnHour;
    }

    /**
     * Gets the work start time (hour) between 5pm and 4am.
     * @return the start time (hour)
     */
    public int getStartHour() {
        return startHour % 12;
    }

    /**
     * Gets the work end time (hour) between 5pm and 4am.
     * @return the end time (hour)
     */
    public int getEndHour() {
        return endHour % 12;
    }

    /**
     * Compute the total payment the babysitter gets for working one night for a family
     * @param i the ith family
     * @return the total payment
     */
    public int getTotalPay(int i) {
        if (i < 1 || i > 3) {
            throw new IllegalArgumentException("Family number must be 1, 2, or 3!");
        }
        switch (i) {
            case 1:
                return (Math.min(endHour, 11) - Math.min(startHour, 11)) * 15
                        + (Math.max(endHour, 11) - Math.max(startHour, 11)) * 20;
            case 2:
                int sum = (Math.min(endHour, 10) - Math.min(startHour, 10)) * 12;
                sum += (Math.min(12, Math.max(endHour, 10))
                        - Math.min(12, Math.max(startHour, 10))) * 8;
                sum += (Math.max(endHour, 12) - Math.max(startHour, 12)) * 16;
                return sum;
            case 3:
                return (Math.min(endHour, 9) - Math.min(startHour, 9)) * 21
                        + (Math.max(endHour, 9) - Math.max(startHour, 9)) * 15;
            default:
                return -1;

        }
    }
}
