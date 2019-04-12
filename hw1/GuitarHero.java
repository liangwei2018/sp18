/** A client that uses the synthesizer package to replicate a plucked guitar string sound */
import synthesizer.GuitarString;

public class GuitarHero {
    private static final int NUMKEYS = 37;
    private static final double CONCERT = 440.0;


    public static void main(String[] args) {
        /* create 37 piano keys, for concert */
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        GuitarString[] stringKey = new GuitarString[NUMKEYS];
        for (int i = 0; i < stringKey.length; i += 1) {
            stringKey[i] = new GuitarString(CONCERT * Math.pow(2, (i - 24.0) / 12.0));
        }

        GuitarString pluckString = new GuitarString(CONCERT);

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = keyboard.indexOf(key);
                if (index < 0) {
                    continue;
                }
                pluckString = stringKey[index];
                pluckString.pluck();
            }

            /* compute the superposition of samples */
            //double sample = stringA.sample() + stringC.sample();

            /* play the sample on standard audio */
            StdAudio.play(pluckString.sample());


            /* advance the simulation of each guitar string by one step */
            pluckString.tic();


        }
    }
}

