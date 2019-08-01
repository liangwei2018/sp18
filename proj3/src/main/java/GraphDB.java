import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map;



/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    private final Map<Long, Node> nodes = new HashMap<>();
    private final Map<Long, Place> namedPlaces = new HashMap<>();
    private final TriSet triPlaceNames = new TriSet();
    private final Map<String, List<Place>> cleanNames = new HashMap<>();


    //private final Map<Long, Long> nodeToWay = new HashMap<>();
    //private final Map<Long, Way> ways = new HashMap<>();

    /**
     * A Node/Vertex, a single point in space defined by
     * node id, longitude, and latitude.
     * Note: Only connected nodes are kept in a graph.
     *
     */
    static class Node {
        long id;
        double lat;
        double lon;
        String name;
        Set<GraphDB.Node> adj;
        Set<String> wayName;


        Node(long id, double lat, double lon) {
            this.id = id;
            this.lat = lat;
            this.lon = lon;
            this.adj = new HashSet<>();
            wayName = new HashSet<>();

        }
        void setName(String name) {
            this.name = name;
        }
    }

    /**
     * A place class, a place in space defined by
     * node id, longitude, latitude, and name.
     * Note: not all namedPlaces are connected.
     */
    static class Place {
        long id;
        double lat;
        double lon;
        String name;


        Place(long id, double lat, double lon, String name) {
            this.id = id;
            this.lat = lat;
            this.lon = lon;
            this.name = name;
        }
    }
    /**
     * Add a node to the graph
     * @param v the node
     */
    void addNode(Node v) {
        nodes.put(v.id, v);
    }

    /**
     * Add a Place to the graph and create a map with cleanNames
     * @param id the place id
     */
    void addPlace(long id, double lat, double lon, String name) {
        String cleanName = cleanString(name);

        Place newPlace = new Place(id, lat, lon, name);
        namedPlaces.put(id, newPlace);

        cleanNames.computeIfAbsent(cleanName, k -> new LinkedList<>()).add(newPlace);

        addToTrie(cleanName);
    }

    /**
     * Add a place name to the Trie.
     * @param s the name of the place
     */
    void addToTrie(String s) {
        triPlaceNames.put(s);
    }


    /**
     * Get all Trie keys
     *
     */
    List<String> getAllTrieKeys() {
        return triPlaceNames.getKeys();
    }
    /**
     * Get information about a Place
     * @param id the place id
     */
    Place getPlace(long id) {
        return namedPlaces.get(id);
    }

    /**
     *
     * @return all namedPlaces in the graph
     */
    Iterable<Place> getAllPlaces() {
        return namedPlaces.values();
    }

    /**
     *
     * @return the list of places with the same cleanName.
     */
    Iterable<Place> getPlaceList(String cleanName) {
        return cleanNames.get(cleanName);
    }

    /**
     *
     * @return the list of places with the same cleanName.
     */
    Iterable<String> getAllCleanNames() {
        return cleanNames.keySet();
    }
    /**
     *
     * @return the number of namedPlaces
     */
    int numPlaces() {
        return namedPlaces.size();
    }

    /**
     * Replace a node in the graph. the new node
     * has the same id as the one in the graph.
     * @param v the new node
     */
    void replaceNode(Node v) {
        nodes.replace(v.id, v);
    }


    /**
     *
     * @param id the id key
     * @return the node that the specified id is associated with.
     */

    private Node getNode(long id) {
        return nodes.get(id);
    }



    /**
     * Remove a specified Node
     * @param id the id key
     * @return the specified Node associated with the id.
     */
    private Node removeNode(long id) {
        return nodes.remove(id);
    }
    /**
     * Add an Edge,  defined by connecting two nodes.
     */
    void addEdge(long a, long b) {
        Node v = getNode(a);
        Node w = getNode(b);
        v.adj.add(w);
        w.adj.add(v);
        nodes.replace(a, v);
        nodes.replace(b, w);
    }
    /**
     * Add a way where a node is located to the node.
     */

    void addWayToNode(long nodeId, String name) {
        Node v = getNode(nodeId);
        //v.wayId.add(wayId);
        v.wayName.add(name);
        //v.wayMaxSpeed.add(speed);
        nodes.replace(nodeId, v);
    }

    /**
     * Returns the name of the way that the node id belongs to.
     * @param id the id of the node
     * @return its way name
     */
    Set<String> getWayName(long id) {
        return getNode(id).wayName;
    }


    static class Way {
        long id;
        ArrayList<Long> nodeList;
        boolean validHighway;
        int maxSpeed;
        String name;
        Way(long id) {
            this.id = id;
            nodeList = new ArrayList<>();
            validHighway = false;
            maxSpeed = 0;
            name = "unknown road";
        }
        long getId() {
            return id;
        }
        void clear() {
            nodeList.clear();
            validHighway = false;
            maxSpeed = 0;
            name = null;
        }
    }

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with or without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {

        if (prefix == null) {
            return null;
        }
        List<String> returnList = new LinkedList<>();


        List<String> pList = triPlaceNames.keysWithPrefix(prefix);
        if (pList == null ) {
            return null;
        }
        for (String topName : pList) {
            Iterable<Place> list = getPlaceList(topName);
            if (list == null) {
                continue;
            }
            for (Place p : list) {
                returnList.add(p.name);
            }
        }
        return returnList;
    }

    /**
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" : Number, The latitude of the node. <br>
     * "lon" : Number, The longitude of the node. <br>
     * "name" : String, The actual name of the node. <br>
     * "id" : Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        if (locationName == null || locationName.isEmpty()) {
            return null;
        }
        List<Map<String, Object>> placeMapList = new LinkedList<>();

        String cleanLocationName = GraphDB.cleanString(locationName);

        for (Place p : cleanNames.get(cleanLocationName)) {
            Map<String, Object> nodeMap = new HashMap<>();
            nodeMap.put("lat", p.lat);
            nodeMap.put("lon", p.lon);
            nodeMap.put("name", p.name);
            nodeMap.put("id", p.id);
            placeMapList.add(nodeMap);
        }
        return placeMapList;
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        // TO DO: Your code here.
        if (isEmpty()) {
            return;
            //throw new RuntimeException("Nodes null or empty!");
        }
        java.util.Set<Long> removeKeys = new HashSet<>();
        for (long id : vertices()) {
            Node c = getNode(id);
            if (c.adj.isEmpty()) {
                removeKeys.add(id);
            }
        }
        for (long id : removeKeys) {
            removeNode(id);
        }

    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.

        return nodes.keySet();
    }

    /**
     * Check if the graph is empty.
     * @return true if the graph is empty, false otherwise.
     */
    boolean isEmpty() {
        //YOUR CODE HERE, this currently returns only an empty list.

        return nodes.isEmpty();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        java.util.Set<Long> vertices = new HashSet<>();
        for (Node c : nodes.get(v).adj) {
            vertices.add(c.id);
        }
        return vertices;
    }


    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        for (long v : vertices()) {
            if (getNode(v).lon == lon && getNode(v).lat == lat) {
                return v;
            }
        }
        double nearest = Double.MAX_VALUE;
        long givenId = Long.MAX_VALUE;
        while (nodes.containsKey(givenId)) {
            givenId -= 1;
        }
        Node givenNode = new GraphDB.Node(givenId, lat, lon);
        addNode(givenNode);
        long nodeId = 0;
        for (long v : vertices()) {
            double dis = distance(v, givenId);
            if (dis < nearest && dis > 0) {
                nearest = dis;
                nodeId = v;
            }
        }
        removeNode(givenId);
        return nodeId;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return nodes.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return nodes.get(v).lat;
    }
}
