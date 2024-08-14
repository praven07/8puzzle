/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private Board initial;

    private Stack<Board> path;

    private boolean solvable;

    public Solver(Board initial) {

        if (initial == null) {
            throw new IllegalArgumentException();
        }

        this.initial = initial;
        path = new Stack<>();
        solvable = false;

        findSolution();
    }

    private void findSolution() {
        MinPQ<SearchNode> queue = new MinPQ<>();

        queue.insert(new SearchNode(initial, null, false));
        queue.insert(new SearchNode(initial.twin(), null, true));

        while (!queue.isEmpty()) {

            SearchNode node = queue.delMin();

            if (node.board.isGoal()) {
                if (node.isTwin) {
                    solvable = false;
                }
                else {
                    solvable = true;
                    reconstructurePath(node);
                }

                break;
            }

            for (Board neighbor : node.board.neighbors()) {
                if (node.previous == null || !node.previous.board.equals(neighbor)) {
                    queue.insert(new SearchNode(neighbor, node, node.isTwin));
                }
            }
        }
    }

    private void reconstructurePath(SearchNode starting) {
        // Traverse the path
        SearchNode pointer = starting;
        while (pointer != null) {
            path.push(pointer.board);
            pointer = pointer.previous;
        }
    }

    public boolean isSolvable() {

        return solvable;
    }

    public int moves() {

        if (isSolvable()) {
            return path.size() - 1;
        }

        return -1;
    }

    public Iterable<Board> solution() {

        if (isSolvable()) {
            return path;
        }

        return null;
    }

    private class SearchNode implements Comparable<SearchNode> {

        private Board board;

        private int moves;

        private int manhattan;

        private int priority;

        private boolean isTwin;

        private SearchNode previous;

        public SearchNode(Board board, SearchNode previous, boolean isTwin) {
            this.board = board;
            this.previous = previous;
            this.isTwin = isTwin;
            this.moves = previous == null ? 0 : previous.moves + 1;
            this.manhattan = board.manhattan();
            this.priority = moves + manhattan;
        }

        @Override
        public int compareTo(SearchNode that) {
            if (this.priority != that.priority) {
                return this.priority - that.priority;
            }
            // Use the Manhattan distance for tie-breaking
            return this.manhattan - that.manhattan;
        }
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }
}