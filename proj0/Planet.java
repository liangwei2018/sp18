/** 
 * The Planet Class has planet information: x/y positions, 
 * x/y velocities, mass, Planet image file, and related methods 
 * to calculate forces, update locations, and draw planets. 
 * @author Liang Wei
 */

public class Planet {
    private double xxPos;
    private double yyPos;
    private double xxVel;
    private double yyVel;
    private double mass;
    private String imgFileName;

    public Planet(double xP, double yP, double xV,
                  double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }
    public Planet(Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }
    
    public double calcDistance(Planet p) {
    /** 
     * The calcDistance method takes in a Planet P
     * and returns the distance between this Planet
     * and the Planet P.
     */

        double dx = p.xxPos - xxPos;
        double dy = p.yyPos - yyPos;
        return Math.sqrt(dx * dx + dy * dy);
    }

    
    public double calcForceExertedBy(Planet p) {
    /**
     * The calcForceExertedBy method takes in a Planet P
     * and returns the force exerted on this Planet 
     * by the Planet P.
     */

        final double GRAV = 6.67e-11;
        double r = this.calcDistance(p);
        return GRAV * mass * p.mass / (r * r);
    }
    public double calcForceExertedByX(Planet p) {
    /**
     * The calcForceExertedByX method takes in a Planet p
     * and returns the X force exerted on this Planet by 
     * the Planet p.
     */

        double dx = p.xxPos - xxPos;
        double r = this.calcDistance(p);
        return calcForceExertedBy(p) * dx / r; 
    }

    public double calcForceExertedByY(Planet p) {
    /**
     * The calcForceExertedByY method takes in a Planet p
     * and returns the Y force exerted on this Planet by 
     * the Planet p.
     */

        double dy = p.yyPos - yyPos;
        double r = this.calcDistance(p);
        return calcForceExertedBy(p) * dy / r; 
    }

    public double calcNetForceExertedByX(Planet[] allP) {
    /**
     * The calcNetForceExertedByX method takes in an array of 
     * Planets allP and returns the net X force exerted on
     * this Planet by the Planets allP.
     */

        double totalFx = 0;
        for (Planet p : allP) {
            if (!this.equals(p)) {
                totalFx = totalFx + calcForceExertedByX(p); 
            }
        }
        return totalFx;
    }

    public double calcNetForceExertedByY(Planet[] allP) {
    /**
     * The calcNetForceExertedByY method takes in an array of 
     * Planets allP and returns the net Y force exerted on
     * this Planet by the Planets allP.
     */

        double totalFy = 0;
        for (Planet p : allP) {
            if (!this.equals(p)) {
                totalFy = totalFy + calcForceExertedByY(p); 
            }
        }
        return totalFy;
    }

    public void update(double dt, double fX, double fY) {
    /**
     * The update method determines how much the forces exerted 
     * on the planet will cause that planet to accelerate, 
     * and then update the planet's positions and velocities.
     *
     * @param dt  A small period of time
     * @param fX  X-forces exerted on the planet
     * @param fY  Y-forces exerted on the planet
     */

        xxVel += dt * fX / mass;
        yyVel += dt * fY / mass;
        xxPos += dt * xxVel;
        yyPos += dt * yyVel;
    }

    public void draw() {
    /**
     * The draw method draws one Planet.
     */
    StdDraw.picture(xxPos, yyPos, "images/"+imgFileName); 

    }
}
