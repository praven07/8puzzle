/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Board {

    private int[][] tiles;

    private int n;

    private int blankRow;

    private int blankCol;


    public Board(int[][] tiles) {

        if (tiles == null) {
            throw new IllegalArgumentException();
        }

        n = tiles.length;

        this.tiles = new int[n][n];

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                this.tiles[i][j] = tiles[i][j];

                if (tiles[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                }
            }
        }
    }

    public String toString() {

        StringBuilder gridString = new StringBuilder();

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                gridString.append(tiles[i][j]);
                gridString.append("\t");
            }
            gridString.append("\n");
        }

        return tiles.length + "\n" + gridString;
    }

    //
    public int dimension() {
        return tiles.length;
    }

    public int hamming() {

        int distance = 0;

        for (int i = 0; i < n * n; i++) {

            int row = i / n;
            int col = i % n;

            if (tiles[row][col] != 0 && tiles[row][col] != i + 1) {
                distance++;
            }
        }

        return distance;
    }

    public int manhattan() {

        int distance = 0;

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {

                int value = tiles[row][col];

                if (value != 0) {
                    int targetRow = (value - 1) / n;
                    int targetCol = (value - 1) % n;

                    distance += Math.abs(row - targetRow) + Math.abs(col - targetCol);
                }
            }
        }

        return distance;
    }


    public boolean isGoal() {
        return manhattan() == 0;
    }


    public boolean equals(Object y) {

        if (this == y) return true;

        if (y == null || y.getClass() != getClass()) {
            return false;
        }

        Board that = (Board) y;

        if (dimension() != that.dimension()) {
            return false;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != that.tiles[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public Iterable<Board> neighbors() {

        ArrayList<Board> neighbors = new ArrayList<>();

        int[][] directions = {
                { 0, -1 },
                { 0, 1 },
                { -1, 0 },
                { 1, 0 },
                };

        for (int[] direction : directions) {

            int row = blankRow + direction[0];
            int col = blankCol + direction[1];

            if (row >= 0 && row < n && col >= 0 && col < n) {
                int[][] newNeighborTiles = copyTiles();

                newNeighborTiles[blankRow][blankCol] = tiles[row][col];
                newNeighborTiles[row][col] = 0;
                neighbors.add(new Board(newNeighborTiles));
            }
        }

        return neighbors;
    }

    private int[][] copyTiles() {

        int[][] copy = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copy[i][j] = tiles[i][j];
            }
        }

        return copy;
    }

    public Board twin() {

        int[][] newTiles = copyTiles();
        int row = (tiles[0][0] == 0 || tiles[0][1] == 0) ? 1 : 0;
        int temp = newTiles[row][0];
        newTiles[row][0] = newTiles[row][1];
        newTiles[row][1] = temp;
        return new Board(newTiles);
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
        StdOut.println(initial);

        for (Board board : initial.neighbors()) {
            StdOut.println(board);
        }

        System.out.println("Twin board:");
        System.out.println(initial.twin());
    }
}