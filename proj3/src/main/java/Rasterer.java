import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 *
 * @author Liang Wei
 */
public class Rasterer {

    private static final int MAX_DEPTH = 7;
    private static final double ROOT_LRLON = MapServer.ROOT_LRLON;
    private static final double ROOT_ULLON = MapServer.ROOT_ULLON;
    private static final double ROOT_LRLAT = MapServer.ROOT_LRLAT;
    private static final double ROOT_ULLAT = MapServer.ROOT_ULLAT;

    private static final double ROOT_WIDTH = ROOT_LRLON - ROOT_ULLON;
    private static final double ROOT_HEIGHT = ROOT_ULLAT - ROOT_LRLAT;
    private static final double ROOT_LON_DPP = ROOT_WIDTH / MapServer.TILE_SIZE;

    private double rasterUlLon;
    private double rasterUlLat;
    private double rasterLrLon;
    private double rasterLrLat;
    private int depth;
    private boolean querySuccess;
    private String[][] renderGrid;



    public Rasterer() {

        // YOUR CODE HERE
        //rasterUlLon, rasterUlLat, rasterLrLon, rasterLrLat,
        // depth, querySuccess, renderGrid
        rasterUlLon = 0;
        rasterUlLat = 0;
        rasterLrLon = 0;
        rasterLrLat = 0;
        depth = 0;
        querySuccess = false;
        renderGrid = null;
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        //System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        //System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in "
        //                   + "your browser.");
        double lrLon = params.get("lrlon");
        double ulLon = params.get("ullon");
        double w = params.get("w");
        double h = params.get("h");
        double lrLat = params.get("lrlat");
        double ulLat = params.get("ullat");
        if (lrLon <= ROOT_ULLON || ulLon >= ROOT_LRLON || lrLat >= ROOT_ULLAT
             || ulLat <= ROOT_LRLAT || lrLon <= ulLon || lrLat >= ulLat) {
            querySuccess = false;
            return null;
        }

        double lonDpp = (lrLon - ulLon) / w;
        int dep = (int) Math.ceil(Math.log(ROOT_LON_DPP / lonDpp) / Math.log(2.0));
        depth = Math.min(dep, MAX_DEPTH);
        int num = (int) Math.pow(2, depth);

        //System.out.println("lonDpp=" + lonDpp + " dep=" + dep + " depth=" + depth);
        int xnUl = getXnYp(ROOT_ULLON, ulLon, ROOT_WIDTH, num, 1);
        int ypUl = getXnYp(ulLat, ROOT_ULLAT, ROOT_HEIGHT, num, 1);
        int xnLr = getXnYp(lrLon, ROOT_LRLON, ROOT_WIDTH, num, 0);
        int ypLr = getXnYp(ROOT_LRLAT, lrLat, ROOT_HEIGHT, num, 0);
        //System.out.println("xnUl:" + xnUl + " ypUl:" + ypUl
        //        + " xnLr:" + xnLr + " ypLr:" + ypLr);

        rasterUlLon = ROOT_ULLON + xnUl * ROOT_WIDTH / num;
        rasterUlLat = ROOT_ULLAT - ypUl * ROOT_HEIGHT / num;
        rasterLrLon = ROOT_LRLON - (num - xnLr - 1) * ROOT_WIDTH / num;
        rasterLrLat = ROOT_LRLAT + (num - ypLr - 1) * ROOT_HEIGHT / num;
        int nCols = xnLr - xnUl + 1;
        int nRows = ypLr - ypUl + 1;
        //System.out.println("numColumns:" + nCols + " numRows:" + nRows);

        renderGrid = new String[nRows][nCols];

        for (int row = ypUl; row <= ypLr; row += 1) {
            int j = row - ypUl;
            for (int col = xnUl; col <= xnLr; col += 1) {
                int i = col - xnUl;
                renderGrid[j][i] = "d" + depth + "_x" + col + "_y" + row + ".png";
            }
        }
        querySuccess = true;

        results.put("raster_ul_lon", rasterUlLon);
        results.put("raster_ul_lat", rasterUlLat);
        results.put("raster_lr_lon", rasterLrLon);
        results.put("raster_lr_lat", rasterLrLat);
        results.put("depth", depth);
        results.put("render_grid", renderGrid);
        results.put("query_success", querySuccess);

        return results;
    }

    /**
     * Find the corner figure number of the query box
     * e.g. for dm_xn_yp.png, m is the depth, n and p are the figure numbers 0:m-1;
     *
     * @param locLeft Left or bottom location
     * @param locRight Right or Top location
     * @param len length of the root figure in lon or lat direction
     * @param num total number of the figures in one direction
     * @param pos  = 1 for upper left corner; 0 for lower right corner
     * @return the corner figure number of the query box
     */
    private int getXnYp(double locLeft, double locRight, double len, int num, int pos) {
        int np = (int) Math.floor(Math.max(locRight - locLeft, 0.0) / len * num);
        return  np * (2 * pos - 1) + (1 - pos) * (num - 1);
    }

}
