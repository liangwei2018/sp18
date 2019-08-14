package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private int period;
    private int state;

    public AcceleratingSawToothGenerator(int period, double factor) {
        state = 0;
        this.period = (int) factor * period;
    }

    public double next() {
        state = (state + 1) % period;
        return normalize(state);
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
