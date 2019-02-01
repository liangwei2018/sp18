/**  
 * Simulate planets moving in a universe specified in one data file.
 * @author Liang Wei
 */

public class NBody {

    public static double readRadius(String fileName) {
    /**  
     * The method readRadius takes in a text file FILENAME about a universe 
     * and returns the radius of the universe.
     */
        In in = new In(fileName);
        int N = in.readInt();
        double R = in.readDouble();
        return R;
    }

    public static Planet[] readPlanets(String fileName) {
    /**  
     * The method readPlanets takes in a text file FILENAME about a universe 
     * and returns an array of Planets.
     */
        In in = new In(fileName);
        int N = in.readInt();
        double R = in.readDouble();
        Planet[] allP = new Planet[N];

        for (int i = 0; i < allP.length; i++) {
            double xxPos = in.readDouble();
            double yyPos = in.readDouble();
            double xxVel = in.readDouble();
            double yyVel = in.readDouble();
            double mass = in.readDouble();
            String imgFileName = in.readString();
            allP[i] = new Planet(xxPos, yyPos, xxVel, yyVel, 
                                 mass, imgFileName);
        }
        return allP;
    }

    public static void main(String[] args) {
        if(args.length < 3) {
            System.out.println("Please supply T, dt, and filename"
                               + "as command line arguments.");
        }
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        Double R = readRadius(filename);
        Planet[] allPlanets = readPlanets(filename);
        int numP = allPlanets.length;

        /* Set up the universe, so that it goes from -R, -R to R, R. */
        StdDraw.setScale(-R, R);

        /* Enable double buffering for a smooth animation. */
        StdDraw.enableDoubleBuffering();

        for (double time = 0; time < T; time += dt) {
            double[] xForces = new double[numP]; 
            double[] yForces = new double[numP]; 

            /* Calculate forces exerted on planets and update their positions. */
            for (int i = 0; i < numP; i++) {
                xForces[i] = allPlanets[i].calcNetForceExertedByX(allPlanets);
                yForces[i] = allPlanets[i].calcNetForceExertedByY(allPlanets);
            }
            for (int i = 0; i < numP; i++) {
                allPlanets[i].update(dt, xForces[i], yForces[i]);
            }

            /* Clears the drawing window. */
            StdDraw.clear();

            /* Draw the background. */
            StdDraw.picture(0, 0, "images/starfield.jpg");

            /* Draw all planets. */
            for(Planet p: allPlanets) {
                p.draw();
            }

            /* Shows the drawing to the screen, and waits 10 milliseconds. */
            StdDraw.show();
            StdDraw.pause(10); 
        }

        /* Print out the final state of the universe. */
        StdOut.printf("%d\n", numP);
        StdOut.printf("%.2e\n", R);
        for (int i = 0; i < numP; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                          allPlanets[i].xxPos, allPlanets[i].yyPos, 
                          allPlanets[i].xxVel, allPlanets[i].yyVel,  
                          allPlanets[i].mass, allPlanets[i].imgFileName);   
        }       
    }
}
