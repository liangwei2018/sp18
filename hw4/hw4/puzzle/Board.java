package hw4.puzzle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Board implements WorldState {

    private final int[][] board;
    private final int[][] goal;
    private int size;
    private static final int BLANK = 0;

    /**
     * Constructs a board from an N-by-N array of tiles where
     *  tiles[i][j] = tile at row i, column j
     */
    public Board(int[][] tiles) {
        size = tiles.length;
        if (size != tiles[0].length) {
            throw new IllegalArgumentException("tiles' row and column sizes are not equal!");
        }
        board = new int[size][size];
        for (int i = 0; i < size; i += 1) {
            System.arraycopy(tiles[i], 0, board[i], 0, size);
        }

        goal = new int[size][size];
        for (int i = 0; i < size; i += 1) {
            for (int j = 0; j < size; j += 1) {
                goal[i][j] = j + 1 + i * size;
            }
        }
        goal[size - 1][size - 1] = BLANK;
    }

    /**
     * Returns value of tile at row i, column j (or 0 if blank)
     */
    public int tileAt(int i, int j) {
        if (i < 0 || i >= size || j < 0 || j >= size) {
            throw new IndexOutOfBoundsException("Index i or j is not in [0, N-1]!");
        }
        return board[i][j];
    }

    /**
     * @return the board size N
     */
    public int size() {
        return size;
    }

    /**
     * @return the neighbors of the current board
     */
    public Iterable<WorldState> neighbors() {
        Set<WorldState> neighbs = new HashSet<>();
        int x0 = -9;
        int y0 = -9;
        for (int i = 0; i < size; i += 1) {
            for (int j = 0; j < size; j += 1) {
                if (board[i][j] == BLANK) {
                    x0 = i;
                    y0 = j;
                    break;
                }
            }
        }
        int[][] boardNeighbor = new int[size][size];
        for (int i = 0; i < size; i += 1) {
            System.arraycopy(board[i], 0, boardNeighbor[i], 0, size);
        }

        int x1 = x0 - 1;
        int x2 = x0 + 1;
        int y1 = y0 - 1;
        int y2 = y0 + 1;
        if (x1 >= 0) {
            boardNeighbor[x0][y0] = boardNeighbor[x1][y0];
            boardNeighbor[x1][y0] = BLANK;
            neighbs.add(new Board(boardNeighbor));
            boardNeighbor[x1][y0] = boardNeighbor[x0][y0];
            boardNeighbor[x0][y0] = BLANK;
        }
        if (y1 >= 0) {
            boardNeighbor[x0][y0] = boardNeighbor[x0][y1];
            boardNeighbor[x0][y1] = BLANK;
            neighbs.add(new Board(boardNeighbor));
            boardNeighbor[x0][y1] = boardNeighbor[x0][y0];
            boardNeighbor[x0][y0] = BLANK;
        }
        if (x2 < size) {
            boardNeighbor[x0][y0] = boardNeighbor[x2][y0];
            boardNeighbor[x2][y0] = BLANK;
            neighbs.add(new Board(boardNeighbor));
            boardNeighbor[x2][y0] = boardNeighbor[x0][y0];
            boardNeighbor[x0][y0] = BLANK;
        }
        if (y2 < size) {
            boardNeighbor[x0][y0] = boardNeighbor[x0][y2];
            boardNeighbor[x0][y2] = BLANK;
            neighbs.add(new Board(boardNeighbor));
            boardNeighbor[x0][y2] = boardNeighbor[x0][y0];
            boardNeighbor[x0][y0] = BLANK;
        }
        return neighbs;
    }



    /**
     * Hamming estimate: The number of tiles in the wrong position.
     */

    public int hamming() {
        int differenceCount = 0;
        for (int i = 0; i < size; i += 1) {
            for (int j = 0; j < size; j += 1) {
                if (board[i][j] != goal[i][j] && board[i][j] != BLANK) {
                    differenceCount += 1;
                }
            }
        }
        return differenceCount;
    }

    /**
     * Manhattan estimate: The sum of the Manhattan distances
     * (sum of the vertical and horizontal distance) from the
     * tiles to their goal positions.
     */
    public int manhattan() {
        int sumDistance = 0;
        for (int i = 0; i < size; i += 1) {
            for (int j = 0; j < size; j += 1) {
                if (board[i][j] != goal[i][j] && board[i][j] != BLANK) {
                    int row = (board[i][j] - 1) / size;
                    int column = board[i][j] - 1 - row * size;
                    sumDistance += Math.abs(row - i) + Math.abs(column - j);
                }
            }
        }
        return sumDistance;
    }

    /**
     * Estimated distance to goal. This method should
     * simply return the results of manhattan() when submitted to
     * Gradescope.
     */
    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    /**
     * Returns true if this board's tile values are the same
     * position as y's
     */
    @Override
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }

        Board board1 = (Board) y;

        return Arrays.deepEquals(board, board1.board)
                && Arrays.deepEquals(goal, board1.goal);
    }

    @Override
    public int hashCode() {
        int result = board != null ? Arrays.deepHashCode(board) : 0;
        result = 31 * result + (goal != null ? Arrays.deepHashCode(goal) : 0);
        return result;
    }


    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
