package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class Solver {
    private int minimumMoves;
    private List<WorldState> solutionList = new ArrayList<>();

    /**
     * Constructor which solves the puzzle, computing
     * everything necessary for moves() and solution() to
     * not have to solve the problem again. Solves the
     * puzzle using the A* algorithm. Assumes a solution exists.
     *
     * @param initial a WorldState object.
     */
    public Solver(WorldState initial) {
        if (initial == null) {
            throw new IllegalArgumentException("WorldState object initial is null!");
        }
        MinPQ<SearchNode> pq = new MinPQ<>();
        SearchNode initialNode = new SearchNode(initial, null);
        pq.insert(initialNode);

        while (!pq.min().currentState().isGoal()) {
            for (SearchNode nbrs: pq.delMin().nbrNodes()) {
                pq.insert(nbrs);
            }
        }
        SearchNode currentNode = pq.min();
        minimumMoves = currentNode.movesMadeSoFar();

        Stack<SearchNode> tmpStack = new Stack<>();
        while (currentNode.prev != null) {
            tmpStack.push(currentNode);
            currentNode = currentNode.prev;
        }
        tmpStack.push(initialNode);

        while (!tmpStack.isEmpty()) {
            WorldState ws = tmpStack.pop().currentState();
            solutionList.add(ws);
        }
    }

    /**
     * A class of SearchNode which represents one "move sequence" of
     * the puzzle. It includes a WorldState, the number of moves made
     * to reach this world state from the initial state, a reference
     * to the previous search node, and an estimated distance to goal.
     */
    private class SearchNode implements Comparable<SearchNode> {
        WorldState init;
        int movesFromInitial;
        SearchNode prev;
        int estimatedDistanceToGoal;

        SearchNode(WorldState init, SearchNode prev) {
            this.init = init;
            this.prev = prev;
            if (prev == null) {
                movesFromInitial = 0;
            } else {
                movesFromInitial = prev.movesFromInitial + 1;
            }
            estimatedDistanceToGoal = init.estimatedDistanceToGoal();
        }

        WorldState currentState() {
            return init;
        }

        int movesMadeSoFar() {
            return movesFromInitial;
        }

        /**
         * Find the neighbor SearchNodes, except its previous node (prev)
         * @return the neighboring nodes.
         */
        Iterable<SearchNode> nbrNodes() {
            Set<SearchNode> nbrSet = new HashSet<>();
            for (WorldState ws : init.neighbors()) {
                if (prev == null || !ws.equals(prev.init)) {
                    SearchNode sn = new SearchNode(ws, this);
                    nbrSet.add(sn);
                }
            }
            return nbrSet;
        }

        @Override
        public int compareTo(SearchNode o) {
            return Integer.compare(this.priority(), o.priority());
        }

        private int priority() {
            return movesFromInitial + estimatedDistanceToGoal;
        }
    }

    /**
     * Returns the minimum number of moves to solve the puzzle starting
     * at the initial WorldState.
     * @return the minimum number of moves
     */
    public int moves() {
        return minimumMoves;
    }

    /**
     * Returns a sequence of WorldStates from the initial WorldState
     * to the solution.
     * @return a sequence of WorldStates
     */

    public Iterable<WorldState> solution() {
        return solutionList;
    }

}
