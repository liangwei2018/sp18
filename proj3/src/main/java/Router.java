import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     *
     * @param g       The graph to use.
     * @param stlon   The longitude of the start location.
     * @param stlat   The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */


    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        long stid = g.closest(stlon, stlat);
        long destid = g.closest(destlon, destlat);

        if (stid == destid) {
            throw new IllegalArgumentException("Start and destination locations are the same!");
        }

        Map<Long, Double> distToSource = new HashMap<>();
        Set<Long> marked = new HashSet<>();
        Queue<RouterNode> fringe = new PriorityQueue<>();
        for (long id : g.vertices()) {
            distToSource.put(id, Double.MAX_VALUE);
        }
        distToSource.put(stid, 0.0);

        RouterNode currentNode = new RouterNode(stid, 0, Double.MAX_VALUE, null);

        fringe.add(currentNode);

        while (!fringe.isEmpty() && fringe.peek().getVertexId() != destid) {
            currentNode = fringe.poll();
            long v = 0;
            if (currentNode != null) {
                v = currentNode.getVertexId();
            }
            if (!marked.contains(v)) {
                marked.add(v);
                for (long w : g.adjacent(v)) {
                    double edgeLength = g.distance(v, w);
                    double newDistToStart = currentNode.distToStart + edgeLength;
                    double disToGoal = g.distance(w, destid);
                    if (newDistToStart < distToSource.get(w)) {
                        distToSource.replace(w, newDistToStart);
                        fringe.add(new RouterNode(w, newDistToStart, disToGoal, currentNode));
                    }

                }
            }

        }

        List<Long> returnList = new ArrayList<>();
        long goal = 0;
        if (!fringe.isEmpty()) {
            currentNode = fringe.poll();
            goal = currentNode.getVertexId();
        }
        if (goal != destid) {
            throw new RuntimeException("No route to destination!");
        }

        Stack<Long> routeReverse = new Stack<>();
        routeReverse.push(destid);
        while (currentNode.parent != null) {
            long currentId = currentNode.parent.getVertexId();
            routeReverse.push(currentId);
            currentNode = currentNode.getParent();
        }
        while (!routeReverse.isEmpty()) {
            returnList.add(routeReverse.pop());
        }
        return returnList;
    }

    private static class RouterNode implements Comparable<RouterNode> {
        private long vertexId;
        private double distToStart;
        private double estimatedDistToDest;
        private RouterNode parent;

        RouterNode(long v, double distToS, double distToD, RouterNode prev) {
            vertexId = v;
            distToStart = distToS;
            estimatedDistToDest = distToD;
            parent = prev;
        }

        long getVertexId() {
            return vertexId;
        }

        RouterNode getParent() {
            return parent;
        }

        @Override
        public int compareTo(RouterNode o) {
            return Double.compare(this.priority(), o.priority());
        }

        private double priority() {
            return distToStart + estimatedDistToDest;
        }
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     *
     * @param g     The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        return null; // FIXME
    }


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /**
         * Integer constants representing directions.
         */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /**
         * Number of directions supported.
         */
        public static final int NUM_DIRECTIONS = 8;

        /**
         * A mapping of integer values to directions.
         */
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /**
         * Default name for an unknown way.
         */
        public static final String UNKNOWN_ROAD = "unknown road";

        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /**
         * The direction a given NavigationDirection represents.
         */
        int direction;
        /**
         * The name of the way I represent.
         */
        String way;
        /**
         * The distance along this way I represent.
         */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         *
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                        && way.equals(((NavigationDirection) o).way)
                        && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
