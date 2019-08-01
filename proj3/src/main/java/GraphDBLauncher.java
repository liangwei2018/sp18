import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * This class provides a main method for experimenting with GraphDB construction.
 * You could also use MapServer, but this class lets you play around with
 * GraphDB in isolation from all the rest of the parts of this assignment.
 */
public class GraphDBLauncher {
    private static final String OSM_DB_PATH = "../library-sp18/data/berkeley-2018.osm.xml";
    //private static final String OSM_DB_PATH = "../library-sp18/data/berkeley-2019.osm.xml";

    public static void main(String[] args) {
        GraphDB g = new GraphDB(OSM_DB_PATH);

        Iterable<Long> verticesIterable = g.vertices();

        /* Convert iterable to a list. */
        List<Long> vertices = new ArrayList<Long>();
        for (long v : verticesIterable) {
            vertices.add(v);
        }

        System.out.println("There are " + vertices.size() + " vertices in the graph.");

        System.out.println("There are " + g.numPlaces() + " named places");

        System.out.println("The first 10 vertices are:");
        for (int i = 0; i < 10; i += 1) {
            if (i < vertices.size()) {
                System.out.println(vertices.get(i));
            }
        }

        int i = 0;
        for (GraphDB.Place p : g.getAllPlaces()) {
            String name = p.name;
            if (name == null) {
                //System.out.println("Node " + i + " is null ");
                continue;
            }
            i += 1;
            //System.out.println(i + "th place " + p.id + " : " + name);
        }

        /*
        for (String cleanName : g.getAllCleanNames()) {
            Iterable<GraphDB.Place> list = g.getPlaceList(cleanName);
            for (GraphDB.Place p : list) {
               //System.out.println(i + "th place " + p.name + " : " + cleanName);
            }
        }*/

        if (false) {
            i = 0;
            for (String cleanName : g.getAllTrieKeys()) {
                i += 1;
                //System.out.println(i + "th key " + cleanName);
            }
        }

        for (String cleanName : g.getLocationsByPrefix("J")) {
            System.out.println("prefix a: " + cleanName);
        }
        for (Map<String, Object> p : g.getLocations("")) {
            System.out.println("location name:" + p.get("name"));
        }

        long v = g.closest(-122.258207, 37.875352);
        System.out.print("The vertex number closest to -122.258207, 37.875352 is " + v + ", which");
        System.out.println(" has longitude, latitude of: " + g.lon(v) + ", " + g.lat(v));

        System.out.println("To get started, uncomment print statements in GraphBuildingHandler.");
    }
}
