package sudoku;

public class SudokuGenerator {
	/***** ATTRIBUTES *****/
	private int[][] mat;			// matrix for the puzzle
	private int gridSize = 9; 			// number of columns/rows.
	private int sqrtGridSize = 3; 		// square root of gridSize
	private int K; 						// # of missing digits, based on difficulty
	
	private Object[][] answerArr;		// array holding sudoku puzzle's answer

	/***** CONSTRUCTOR *****/
	public SudokuGenerator(int gridSize, int K) {
		this.gridSize = gridSize;
		this.K = K;
		
		// Compute square root of gridSize
		Double sqrtGridSizeD = Math.sqrt(gridSize);
		sqrtGridSize = sqrtGridSizeD.intValue();

		mat = new int[gridSize][gridSize];
		
		fillValues();
		printSudoku();
	}
	
	/***** GETTERS & SETTERS *****/
	public int getK() {
		return K;
	}
	
	public Object[][] getPuzzle() {
		return (toObjArr(mat));
	}
	
	public Object[][] getSolution() {
		return answerArr;
	}
	
	public boolean checkSolution(Object[][] answer) {
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				if (answer[i][j] != answerArr[i][j]) {
					return false;				// provided array is not the answer
				}
			}
		}
		return true;							// it is the answer. congrats!
	}
	
	/***** HELPER METHODS *****/
	
	// fills the entire grid.
	public void fillValues() {
		fillDiagonal(); 							// Fill the diagonal of sqrtGridSize x sqrtGridSize matrices
		fillRemaining(0, sqrtGridSize); 			// Fill remaining blocks
		answerArr = toObjArr(mat);					// grab the answer array for hints and to check user's final soln
		removeDigits(); 							// Remove Randomly K digits to make game
	}

	// fills diagonal 3x3 matrices
	private void fillDiagonal() {
		for (int i = 0; i < gridSize; i += sqrtGridSize) {
			fillBox(i, i);
		}
	}
	
	// fills a 3x3 matrix for the sudoku puzzle
	private void fillBox(int row, int col) {
		int num;
		for (int i = 0; i < sqrtGridSize; i++) {
			for (int j = 0; j < sqrtGridSize; j++) {
				do {
					num = randomGenerator(gridSize);
				} while (!boxChecker(row, col, num));
				mat[row + i][col + j] = num;
			}
		}
	}

	// Determines if a number is duplicate and returns a boolean based on it.
	private boolean boxChecker(int rowStart, int colStart, int num) {
		for (int i = 0; i < sqrtGridSize; i++) {
			for (int j = 0; j < sqrtGridSize; j++) {
				if (mat[rowStart + i][colStart + j] == num) {
					return false;
				}
			}
		}
		return true;
	}
	
	// fills the rest of the grid based on the existing 3x3 matrices.
	private boolean fillRemaining(int i, int j) {
		if (j >= gridSize && i < gridSize - 1) {
			i = i + 1;
			j = 0;
		}
		if (i >= gridSize && j >= gridSize) {
			return true;
		}
		if (i < sqrtGridSize) {
			if (j < sqrtGridSize) {
				j = sqrtGridSize;
			}
		} 
		else if (i < gridSize - sqrtGridSize) {
			if (j == (int) (i / sqrtGridSize) * sqrtGridSize) {
				j = j + sqrtGridSize;
			}
		} 
		else {
			if (j == gridSize - sqrtGridSize) {
				i = i + 1;
				j = 0;
				if (i >= gridSize) {
					return true;
				}
			}
		}

		for (int num = 1; num <= gridSize; num++) {
			if (uniqueChecker(i, j, num)) {
				mat[i][j] = num;
				if (fillRemaining(i, j + 1))
					return true;

				mat[i][j] = 0;
			}
		}
		return false;
	}
	
	// Removes a random number of digits for the user to fill in
	private void removeDigits() {
		int count = getK();
		while (count != 0) {
			int cellId = randomGenerator(gridSize * gridSize);
			
			int row = (cellId / gridSize);			// get row num
			int col = cellId % gridSize;			// get col num
			if (cellId == gridSize * gridSize) {	// ensure no overflow of row
				row--;
			}
			
			if (mat[row][col] != 0) {				// set row to zero and decrement count
				count--;
				mat[row][col] = 0;
			}
		}
	}
	
	/***** UTILITY METHODS *****/
	
	// converts an int[][] to an Object[][]
	private Object[][] toObjArr(int[][] data) {
		Object[][] arr = new Object[data.length][data.length];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data.length; j++) {
				arr[i][j] = data[i][j];
			}
		}
		return arr;
	}

	// Checks if the num is not used in the row
	private boolean rowChecker(int i, int num) {
		for (int j = 0; j < gridSize; j++)
			if (mat[i][j] == num)
				return false;
		return true;
	}

	// Checks if the num is not used in the column
	private boolean colChecker(int j, int num) {
		for (int i = 0; i < gridSize; i++)
			if (mat[i][j] == num)
				return false;
		return true;
	}
	
	// Checks if the num is already in the row or column OR
	// 		if it is unique to put into the (i, j) cell of the matrix
	private boolean uniqueChecker(int i, int j, int num) {
		return (rowChecker(i, num) && colChecker(j, num) && boxChecker(i - (i % sqrtGridSize), j - (j % sqrtGridSize), num));
	}
	
	// generates a random number from 0 to num.
	private int randomGenerator(int num) {
		return (int) Math.floor((Math.random() * num + 1));
	}

	// Prints sudoku puzzle to the console.
	private void printSudoku() {
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++)
				System.out.print(mat[i][j] + " ");
			System.out.println();
		}
		System.out.println();
	}
}
