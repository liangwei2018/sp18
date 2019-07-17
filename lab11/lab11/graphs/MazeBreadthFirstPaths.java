package lab11.graphs;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits protected fields:
    protected int[] distTo;
    protected int[] edgeTo;
    protected boolean[] marked;
    protected Maze maze;
    */
    private int s;
    private int t;
    private boolean targetFound = false;
    //private Maze maze;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        // Add more variables here!
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs(int v) {
        // TO DO: Your code here. Don't forget to update distTo, edgeTo,
        // and marked, as well as call announce()
        Queue<Integer> fringe = new ArrayDeque<>();
        marked[v] = true;
        announce();

        if (v == t) {
            targetFound = true;
            return;
        }

        fringe.add(v);
        while (!fringe.isEmpty()) {
            int u = fringe.remove();
            for (int w : maze.adj(u)) {
                if (!marked[w]) {
                    marked[w] = true;
                    edgeTo[w] = u;
                    distTo[w] = distTo[u] + 1;
                    announce();

                    if (w == t) {
                        targetFound = true;
                        return;
                    }
                    fringe.add(w);
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs(s);
    }
}

