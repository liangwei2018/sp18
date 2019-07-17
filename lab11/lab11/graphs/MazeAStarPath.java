package lab11.graphs;

import edu.princeton.cs.algs4.MinPQ;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int ss;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        ss = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[ss] = 0;
        edgeTo[ss] = ss;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        return Math.abs(maze.toX(v) - maze.toX(t))
                + Math.abs(maze.toY(v) - maze.toY(t));
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /**
     * A node class that include the current vertex, its distance to
     * source and goal, respectively, and a link to its previous/parent
     * Node/vertex used for edgeTo info, etc.
     */
    private class Node implements Comparable<Node> {
        private int vertex;
        private int distanceToSource;
        private int estimatedDistanceToGoal;
        private Node prev;

        Node(int v, Node previous) {
            vertex = v;
            estimatedDistanceToGoal = h(v);
            prev = previous;
            if (prev == null) {
                distanceToSource = 0;
            } else {
                distanceToSource = prev.distanceToSource + 1;
            }
        }

        int getVertex() {
            return vertex;
        }

        Node getPrevNode() {
            return prev;
        }

        int getDistanceToSource() {
            return distanceToSource;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.priority(), o.priority());
        }

        private int priority() {
            return distanceToSource + estimatedDistanceToGoal;
        }
    }

    /** Performs an A star search from vertex s. */
    private void astar(int ss) {
        // TO DO
        if (ss == t) {
            marked[ss] = true;
            announce();
            targetFound = true;
            return;
        }

        MinPQ<Node> pq = new MinPQ<>();
        Node start = new Node(ss, null);
        pq.insert(start);

        while (pq.min().getVertex() != t) {
            Node currentNode = pq.delMin();
            int v = currentNode.getVertex();
            marked[v] = true;
            if (v != ss) {
                edgeTo[v] = currentNode.getPrevNode().getVertex();
            } else {
                edgeTo[v] = v;
            }
            distTo[v] = currentNode.getDistanceToSource();
            announce();

            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    if (w == t) {
                        marked[w] = true;
                        edgeTo[w] = v;
                        distTo[w] = distTo[v] + 1;
                        announce();
                        targetFound = true;
                        return;
                    }
                    pq.insert(new Node(w, currentNode));
                }
            }
        }

    }

    @Override
    public void solve() {
        long ta = System.nanoTime();
        astar(ss);
        long tb = System.nanoTime();
        double duration = (tb - ta) / 1e9;
        System.out.println("running time: " + duration);
    }

}

