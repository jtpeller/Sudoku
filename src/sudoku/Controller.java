package sudoku;

public class Controller {
	private static final Pair<Integer> dims = new Pair<Integer>(700, 800);
	
	private static final String[] IMG_NAMES = {"title.png", "play.png", "options.png", "quit.png"};
	
	/***** MAIN *****/
	public static void main(String[] args) {
		new SudokuMenu(dims, IMG_NAMES);
	}
}
