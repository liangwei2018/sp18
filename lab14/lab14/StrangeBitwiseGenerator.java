package lab14;

import lab14lib.Generator;

/**
 * The class is to generate a Saw Tooth wave
 */
public class StrangeBitwiseGenerator implements Generator {
    private int period;
    private int state;

    public StrangeBitwiseGenerator(int period) {
        state = 0;
        this.period = period;
    }

    public double next() {
        state = (state + 1);
        //int weirdState = state & (state >>> 3) % period;
        int weirdState = state & (state >> 3) & (state >> 8) % period;
        return normalize(weirdState);
    }
    /**
     * converts values between 0 and period - 1 to values between -1.0 and 1.0
     * @param s input state
     * @return normalized state
     */
    private double normalize(int s) {
        return s * 2.0 / (period - 1) - 1;
    }
}
