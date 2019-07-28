import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
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
        Map<Long, Long> parent = new HashMap<>();
        Set<Long> marked = new HashSet<>();
        Queue<RouterNode> fringe = new PriorityQueue<>();
        for (long id : g.vertices()) {
            distToSource.put(id, Double.MAX_VALUE);
        }
        distToSource.put(stid, 0.0);
        parent.put(stid, null);

        double priority = Double.MAX_VALUE;
        RouterNode currentNode = new RouterNode(stid, priority);
        fringe.add(currentNode);

        long v = stid;

        while (!fringe.isEmpty() && fringe.peek().getVertexId() != destid) {
            currentNode = fringe.poll();
            if (currentNode != null) {
                v = currentNode.getVertexId();
            }
            if (!marked.contains(v)) {
                marked.add(v);
                for (long w : g.adjacent(v)) {
                    double edgeLength = g.distance(v, w);
                    double newDistToStart = distToSource.get(v) + edgeLength;
                    if (newDistToStart < distToSource.get(w)) {
                        distToSource.replace(w, newDistToStart);
                        parent.put(w, v);
                        priority = newDistToStart + g.distance(w, destid);
                        currentNode = new RouterNode(w, priority);
                        fringe.add(currentNode);
                    }
                }
            }
        }
        if (fringe.peek() == null) {
            System.out.println("No path to destination!");
            if (currentNode != null) {
                v = currentNode.getVertexId();
                System.out.println("The closest reachable Node to the destination:"
                        + v + " Distance to Destination:" + g.distance(v, destid));
            }
            //throw new RuntimeException("No path to destination!");
        } else {
            v = fringe.peek().getVertexId();
        }

        LinkedList<Long> shortestPathList = new LinkedList<>();
        Long currentId = v;
        while (currentId != null) {
            shortestPathList.addFirst(currentId);
            currentId = parent.get(currentId);
        }
        return shortestPathList;
    }

    private static class RouterNode implements Comparable<RouterNode> {
        private long vertexId;
        private double priority;

        RouterNode(long v, double p) {
            vertexId = v;
            priority = p;
        }

        long getVertexId() {
            return vertexId;
        }

        @Override
        public int compareTo(RouterNode o) {
            return Double.compare(this.priority, o.priority);
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
        if (route.size() < 2) {
            System.out.println("Route size < 2!");
            return null;
        }
        List<NavigationDirection> ndList = new ArrayList<>();


        double prevAngle = 0;
        int dir = NavigationDirection.START;

        int routeSize = route.size();
        long[] node = new long[routeSize];
        for (int i = 0; i < routeSize; i += 1) {
            node[i] = route.get(i);
        }

        int i = 1;
        while (i < routeSize) {

            String wayName = "";
            Set<String> prevWayNames = g.getWayName(node[i - 1]);
            Set<String> currentWayNames =g.getWayName(node[i]);
            boolean sameWay = false;
            for (String p : prevWayNames) {
                if (currentWayNames.contains(p)) {
                    sameWay = true;
                    break;
                }
            }

            double wayLength = 0;
            while (sameWay) {
                wayLength += g.distance(node[i - 1], node[i]);
                i += 1;
                if (i > routeSize - 1) {
                    break;
                }
                currentWayNames = g.getWayName(node[i]);
                if (prevWayNames.size() == 1) {
                    for (String s : prevWayNames) {
                        wayName = s;
                        sameWay = currentWayNames.contains(wayName);
                    }
                } else {
                    for (String p : prevWayNames) {
                        if (currentWayNames.contains(p)) {
                            sameWay = true;
                            break;
                        }
                    }
                }
            }

            NavigationDirection nd = new NavigationDirection();
            nd.way = wayName;
            nd.distance = wayLength;
            nd.direction = dir;
            ndList.add(nd);

            if (i > 1) {
                prevAngle = g.bearing(node[i - 2], node[i - 1]);
            }
            if (i < routeSize) {
                double currentAngle = g.bearing(node[i - 1], node[i]);
                double angle = currentAngle - prevAngle;
                dir = getDirection(angle);
                i += 1;
            }

        }
        return ndList; // FIX ME
    }


    private static int getDirection(double angle) {
        int dir = 0;
        if (angle > 180) {
            angle = angle - 360;
        } else if (angle < -180) {
            angle = angle + 360;
        }

        if (angle <= 15 && angle > -15) {
            dir = NavigationDirection.STRAIGHT;
        } else if (angle > 15 && angle <= 30) {
            dir = NavigationDirection.SLIGHT_RIGHT;
        } else if (angle < -15 && angle >= -30) {
            dir = NavigationDirection.SLIGHT_LEFT;
        } else if (angle > 30 && angle <= 100) {
            dir = NavigationDirection.RIGHT;
        } else if (angle < -30 && angle >= -100) {
            dir = NavigationDirection.LEFT;
        } else if (angle > 100) {
            dir = NavigationDirection.SHARP_RIGHT;
        } else if (angle < -100) {
            dir = NavigationDirection.SHARP_LEFT;
        }
        return dir;
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
