package hw4.puzzle;
import edu.princeton.cs.algs4.StdOut;

public class WordPuzzleSolver {
    /***********************************************************************
     * Test routine for your Solver class. Uncomment and run to test
     * your basic functionality.
     **********************************************************************/
    public static void main(String[] args) {
        //String start = "cube";
        //String goal = "tubes";
        //String start = "stories";
        //String goal = "shore";

        String start = "horse";
        String goal = "nurse";

        Word startState = new Word(start, goal);

        //long startTime = System.nanoTime();
        Solver solver = new Solver(startState);

        StdOut.println("Minimum number of moves = " + solver.moves());
        for (WorldState ws : solver.solution()) {
            StdOut.println(ws);
        }
        /*
        long endTime   = System.nanoTime();
        double totalTime = (endTime - startTime) / 1000000000.0D;
        System.out.println("Total time Solver takes (s): " + totalTime);
        */
    }
}
