/* 
 * MagicSquare.java
 * 
 * Computer Science E-22
 * 
 * Daniel Athey, daniel.t.athey@gmail.com
 */

import java.util.*;

public class MagicSquare {
	// the current contents of the cells of the puzzle values[r][c]
	// gives the value in the cell at row r, column c
	private int[][] values;

	// the order (i.e., the dimension) of the puzzle
	private int order;

	// magic number
	private int magicSum;
	//total square count in grid
	private int totalSquares;
	//1D arrays to track sum of each row/col
	private int[] rowSum;
	private int[] colSum;
	// we'll use number tracker to indicate whether a number has been used or not
	private boolean[] numberTracker;

	/*
	 * Creates a MagicSquare object for a puzzle with the specified dimension/order.
	 */
	public MagicSquare(int order) {
		this.values = new int[order][order];
		this.order = order;
		this.magicSum = ((order * order * order) + order) / 2;

		this.totalSquares = order * order;
		this.rowSum = new int[order];
		this.colSum = new int[order];
		this.numberTracker = new boolean[totalSquares];
	}

	/**
	 * sets a value on the magic square board. maintains state of row and column
	 * sums
	 * 
	 * @param value
	 * @param x
	 * @param y
	 */
	private void assignValue(int x, int y, int value) {
		this.values[x][y] = value;
		this.colSum[y] += value;
		this.rowSum[x] += value;
		this.numberTracker[value - 1] = true;
	}

	private void removeValue(int x, int y, int value) {
		this.values[x][y] = 0;
		this.colSum[y] -= value;
		this.rowSum[x] -= value;
		this.numberTracker[value - 1] = false;
	}

	/**
	 * helper method to indicate if the puzzle has been solved
	 * check if each rowSum and colSum are equal to the magic sum. 
	 * @return true is the puzzle is solved
	 */
	private boolean isSolved() {
		//base case
		for (int i = 0; i < order; i++) {
			if (rowSum[i] != magicSum || colSum[i] != magicSum)
				return false;
		}
		return true;
	}


	private boolean isSafe(int x, int y, int value) {
		return (rowSum[x] + value <= magicSum &&
				colSum[y]+ value <= magicSum && 
				numberTracker[value - 1] == false);
	}

	public boolean findSolution(int row, int col) {

		// base case
		if (isSolved()) {
			return true;
		}
		//recursive case
		for (int value = 1; value <= totalSquares; value++) {
			if (isSafe(row, col, value)) {
				assignValue(row, col, value);
				if (col == order - 1) {
					if (rowSum[row] == this.magicSum && findSolution(row + 1, 0)) {
						return true;
					}
				} else if (row == order - 1) {
					if (colSum[col] == magicSum && findSolution(row, col + 1)) {
						return true;
					}
				} else {
					if (findSolution(row, col + 1)) {
						return true;
					}
				}
				removeValue(row, col, value);
			}
		}
		return false;
	}

	/*
	 * solves a magic square puzzle
	 * @return true if the puzzle was solved. 
	 */
	public boolean solve() {
		return findSolution(0, 0);
	}

	/*
	 * Displays the current state of the puzzle. 
	 */
	public void display() {
		for (int r = 0; r < order; r++) {
			printRowSeparator();
			for (int c = 0; c < order; c++) {
				System.out.print("|");
				if (values[r][c] == 0) {
					System.out.print("   ");
				} else {
					if (values[r][c] < 10) {
						System.out.print(" ");
					}
					System.out.print(" " + values[r][c] + " ");
				}
			}
			System.out.println("|");
		}
		printRowSeparator();
	}

	// A private helper method used by display()
	// to print a line separating two rows of the puzzle.
	private void printRowSeparator() {
		for (int i = 0; i < order; i++) {
			System.out.print("-----");
		}
		System.out.println("-");
	}

	public static void main(String[] args) {
		
		Scanner console = new Scanner(System.in);
		System.out.print("What order Magic Square? ");
		int order = console.nextInt();

		MagicSquare puzzle = new MagicSquare(order);
		if (puzzle.solve()) {
			System.out.println("Here's the solution:");
			puzzle.display();
		} else {
			System.out.println("No solution found.");
		}
	}
}