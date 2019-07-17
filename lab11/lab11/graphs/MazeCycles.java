package lab11.graphs;

import java.util.Stack;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    protected int[] distTo;
    protected int[] edgeTo;
    protected boolean[] marked;
    protected Maze maze;
    */
    private int s;
    private int t;
    private boolean targetFound = false;
    private Stack<Integer> fringe;
    //private Maze maze;

    public MazeCycles(Maze m) {
        super(m);
    }

    @Override
    public void solve() {
        // TO DO: Your code here!
        s = maze.xyTo1D(1, 1);
        //distTo[s] = 0;
        fringe = new Stack<>();
        cycleDetectDFS(s);
        if (targetFound) {
            int u = fringe.pop();
            int x = u;
            while (fringe.peek() != u) {
                int w = fringe.pop();
                edgeTo[w] = x;
                announce();
                x = w;
            }
            edgeTo[u] = x;
            announce();
        }
    }

    private void cycleDetectDFS(int v) {
        // Helper methods go here

        marked[v] = true;
        announce();
        fringe.push(v);

        for (int w : maze.adj(v)) {
            if (!marked[w]) {
                marked[w] = true;
                cycleDetectDFS(w);
                if (targetFound) {
                    return;
                }
            } else {
                if (fringe.search(w) > 2) {
                    fringe.push(w);
                    targetFound = true;
                    return;
                }
            }
        }

    }

}

