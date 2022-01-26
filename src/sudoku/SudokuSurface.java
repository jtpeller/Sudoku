package sudoku;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SpringLayout;


/**
 * Creates & initializes a Sudoku surface for a sudoku game.
 * @author JP
 * @version 1.0 (12/27/2020)
 */
public class SudokuSurface extends Surface implements MouseListener {
	/***** ATTRIBUTES *****/
	// Constants
	private final int IMG_WIDTH = 40,
					  IMG_HEIGHT = 40,
					  GRID_SIZE = 9,
					  GRID_DIMS = 500,
					  SMALL_PAD = 10,
					  MEDIUM_PAD = 25,
					  LARGE_PAD = 50,
					  LINE_COUNT = 2;
	private Pair<Integer> optionsDims = new Pair<Integer>(600, 400);	// default option menu dimensions
	
	// Labels
	private JLabel title = new JLabel("Sudoku!"),				// title lable. largest
				   subtitle,									// subtitle label. shows the difficulty
				   infoTitle = new JLabel("Good Luck!");		// info title. gives info about the game.
	private ArrayList<JLabel> labels = new ArrayList<JLabel>();
	
	// Images
	private String backgroundImageName = "bkgd1.png";
	private String iconName = "logo.png";
	
	private ArrayList<ImageButton> numberImages = new ArrayList<ImageButton>(),
								   circleImages = new ArrayList<ImageButton>();
	private String[] circleNames = {"erase.png", "hint.png", "settings.png", "toMenu.png"},
					 toolTips = {"Erase a number", "Hint", "Settings", "Back to Menu"};
	
	// Fonts
	private Font f1 = new Font("Serif", Font.BOLD, 40),
				 f2 = new Font("Serif", Font.PLAIN, 28),
				 f3 = new Font("Serif", Font.ITALIC | Font.BOLD, 24);
	// Colors
	private Color gridBackground = Color.WHITE,
				  gridForeground = new Color(53, 77, 35),		// nice light green
				  gridBorder = new Color(014426);
	// Sudoku grid lines
	private ArrayList<ImagePanel> vertLines = new ArrayList<ImagePanel>(),
								   horizLines = new ArrayList<ImagePanel>();
	// Sudoku attributes
	private SudokuMenu menu;
	private SudokuGenerator gen;
	private SudokuGrid grid;
	
	private String horizImage = "horizontalLine.png",
					vertImage = "verticalLine.png";
	
	private Object[][] puzzle, 				// puzzle, with empty cells
						answer;				// fully filled out puzzle.
	private int numberMode = 0;				// current number to be applied to table values
	private boolean eraseMode = false,		// if the erase button was selected
					hintMode = false;		// if the hint button was selected
	
	/***** CONSTRUCTOR *****/
	public SudokuSurface(SudokuMenu menu, Pair<Integer> dims, int K) {
		super(dims);				// initialize parent
		this.menu = menu;
		gen = new SudokuGenerator(GRID_SIZE, K);
		answer = gen.getSolution();
		surfaceCreated();			// initialize this
	}
	
	/***** METHODS *****/
	private void initLabels(int K) {
		// properly set subtitle text
		subtitle = new JLabel();
		switch(K) {
		case 30:
			subtitle.setText("Difficulty: Easy");
			break;
		case 40:
			subtitle.setText("Difficulty: Normal");
			break;
		case 50:
			subtitle.setText("Difficulty: Hard");
			break;
		case 60:
			subtitle.setText("Difficulty: Impossible");
			break;
		default:
			subtitle.setText("Difficulty: " + K + " tiles removed");
			break;
		}
		
		// set fonts
		title.setFont(f1);
		subtitle.setFont(f2);
		infoTitle.setFont(f3);
		
		// add to panel
		labels.add(title);
		labels.add(subtitle);
		labels.add(infoTitle);
		
		for (int i = 0; i < labels.size(); i++)
			panel.add(labels.get(i));
	}
	
	@Override
	public void surfaceCreated() {
		setBackground(backgroundImageName, surfaceDims);
		ImageIcon icon = new ImageIcon(getClass().getResource(iconName));
		setIconImage(icon.getImage());
		
		// initialize images & labels
		initLabels(gen.getK());
		initImages();
		
		// initialize the grid
		puzzle = gen.getPuzzle();
		grid = new SudokuGrid(GRID_SIZE, GRID_DIMS, puzzle);
		grid.setBackground(gridBackground);
		grid.setForeground(gridForeground);
		grid.setBorderColor(gridBorder);
		grid.addMouseListener(this);
		
		// add everything
		for (int i = 0; i < LINE_COUNT; i++) {		// create & add the lines
			horizLines.add(new ImagePanel(getImage(horizImage), new Pair<Integer>(500, 7)));
			panel.add(horizLines.get(i));
			vertLines.add(new ImagePanel(getImage(vertImage), new Pair<Integer>(7, 500)));
			panel.add(vertLines.get(i));
		}
		panel.add(grid);									// add the grid
		for (int i = 0; i < numberImages.size(); i++)		// add the number images
			panel.add(numberImages.get(i));
		for (int i = 0; i < circleImages.size(); i++)		// add circle images
			panel.add(circleImages.get(i));
		
		// frame & stuff
		setLayout();
		setResizable(false);
		setTitle("Sudoku!");
		pack();
	}
	
	@Override
	protected void setLayout() {
		SpringLayout l1 = new SpringLayout();
		
		// layout labels
		l1.putConstraint(SpringLayout.NORTH, title, SMALL_PAD, SpringLayout.NORTH, panel);
		l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, title, 0, SpringLayout.HORIZONTAL_CENTER, panel);
		for (int i = 1; i < labels.size(); i++) {
			l1.putConstraint(SpringLayout.NORTH, labels.get(i), SMALL_PAD, SpringLayout.SOUTH, labels.get(i - 1));
			l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, labels.get(i), 0, SpringLayout.HORIZONTAL_CENTER, labels.get(i - 1));			
		}
		
		// layout grid
		l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, grid, -MEDIUM_PAD, SpringLayout.HORIZONTAL_CENTER, labels.get(0) );
		l1.putConstraint(SpringLayout.NORTH, grid, MEDIUM_PAD, SpringLayout.SOUTH, labels.get(labels.size() - 1));
		
		// layout lines
		for (int i = 0; i < LINE_COUNT; i++) {
			l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, vertLines.get(i), linePlacement(i, grid.getDimensions()), SpringLayout.WEST, grid);
			l1.putConstraint(SpringLayout.VERTICAL_CENTER, vertLines.get(i), 0, SpringLayout.VERTICAL_CENTER, grid);
			
			l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, horizLines.get(i), 0, SpringLayout.HORIZONTAL_CENTER, grid);
			l1.putConstraint(SpringLayout.VERTICAL_CENTER, horizLines.get(i), linePlacement(i, grid.getDimensions()), SpringLayout.NORTH, grid);
		}
		
		// layout number images
		l1.putConstraint(SpringLayout.NORTH, numberImages.get(0), MEDIUM_PAD, SpringLayout.NORTH, grid);
		l1.putConstraint(SpringLayout.WEST, numberImages.get(0), LARGE_PAD, SpringLayout.EAST, grid);
		for (int i = 1; i < numberImages.size(); i++) {
			l1.putConstraint(SpringLayout.NORTH, numberImages.get(i), SMALL_PAD, SpringLayout.SOUTH, numberImages.get(i - 1));
			l1.putConstraint(SpringLayout.WEST, numberImages.get(i), 0, SpringLayout.WEST, numberImages.get(i - 1));
		}
		
		// layout circle images
		l1.putConstraint(SpringLayout.NORTH, circleImages.get(0), MEDIUM_PAD, SpringLayout.SOUTH, grid);
		l1.putConstraint(SpringLayout.WEST, circleImages.get(0), SMALL_PAD+5, SpringLayout.WEST, grid);
		for (int i = 1; i < circleImages.size(); i++) {
			l1.putConstraint(SpringLayout.NORTH, circleImages.get(i), 0, SpringLayout.NORTH, circleImages.get(i - 1));
			l1.putConstraint(SpringLayout.WEST, circleImages.get(i), 2*LARGE_PAD, SpringLayout.EAST, circleImages.get(i - 1));
		}
		
		// commit to panel
		panel.setLayout(l1);
	}
	
	@Override
	public void mousePressed(MouseEvent m) {
		if (m.getSource() == grid) {
			repaint(0, 0, 0, surfaceDims.getV1(), surfaceDims.getV2());

			System.out.println("Click on the grid occurred");
			int row = grid.rowAtPoint(m.getPoint());
			int col = grid.columnAtPoint(m.getPoint());

			// check if the number already exists in the row or column
			if (eraseMode) {
				if (isEditable(row, col)) {
					int val = (int) grid.getValueAt(row, col);
					infoTitle.setText("Erased " + val + " at " + row + ", " + col);
					grid.setValueAt(null, row, col);
				}
				else {
					infoTitle.setText("Erase your own numbers, silly");
				}
			} 
			else if (hintMode) {
				if (isEditable(row, col)) {
					infoTitle.setText("Hint given at " + row + ", " + col);
					grid.setValueAt(answer[row][col], row, col);
				}
				else {
					infoTitle.setText("I'm sure you don't need a hint for that number");
				}
			} 
			else if (numberMode == 0) {
				infoTitle.setText("Please choose a number on the right");
			}
			else if (grid.isInRow(numberMode, row)) { // check if the number is in the row
				infoTitle.setText(numberMode + " is already in that row");
			}
			else if (grid.isInCol(numberMode, col)) { // check if the number is in the column
				infoTitle.setText(numberMode + " is already in that column");
			}
			else if (grid.isInBox(numberMode, row, col)) { // check if the number is in the box
				infoTitle.setText(numberMode + " is already in that box");
			}
			else if (row >= 0 && col >= 0 && isEditable(row, col)) { // set that value
				grid.setValueAt(numberMode, row, col);
			}
			
			// check the solution
			if (gen.checkSolution(grid.getCurrentPuzzle())) {
				infoTitle.setText("Congratulations! You solved the puzzle.");
			}
			
		} else if (numberImages.contains(m.getSource())) {
			System.out.println("Click on number occurred");
			// find which image was pressed
			int idx = getIndex(m, numberImages);

			// set the number value
			if (idx != -1) {
				numberMode = idx + 1; // off-by-one for indexing.
				System.out.println("Number has been set to " + numberMode + "...");
				infoTitle.setText("Number has been set to " + numberMode);
				eraseMode = false;
				hintMode = false;
			} 
			else {
				System.err.println("Error: Problem encountered with the number set.");
			}
		} else if (circleImages.contains(m.getSource())) {
			System.out.println("Click on circle image");

			int idx = getIndex(m, circleImages);

			switch (idx) {
			case 0:
				System.out.println("erasing mode");
				infoTitle.setText("Erasing mode. Click on a cell to erase it");
				eraseMode = true;
				hintMode = false;
				break;
			case 1:
				System.out.println("giving out a hint...");
				infoTitle.setText("Hint mode. Click on a cell top reveal its value");
				eraseMode = false;
				hintMode  = true;
				break;
			case 2:
				System.out.println("settings...");
				new SudokuOptions(optionsDims);
				break;
			case 3:
				System.out.println("heading back to menu...");
				menu.setVisible(true);
				dispose();
				break;
			default:
				System.out.println("Error: Problem encountered with the circle set");
				break;
			}
		}
	}
	
	/***** UTILITY METHODS *****/
	private void initImages() {
		// get the number images
		for (Integer i = 1; i <= GRID_SIZE; i++) {			// light themed images
			Image temp = getImage("Number" + i.toString() + ".png", IMG_WIDTH, IMG_HEIGHT);
			if (temp != null) {
				numberImages.add(new ImageButton(temp));
				numberImages.get(i - 1).addMouseListener(this);
				numberImages.get(i - 1).setVisible(true);
			}
			else {
				System.err.println("Error in initImages(), block 1. Image could not be found, or it could not be decoded");
			}
		}
		
 		for (int i = 0; i < circleNames.length; i++) {
 			Image temp = getImage(circleNames[i], IMG_WIDTH, IMG_HEIGHT);
			if (temp != null) {
				circleImages.add(new ImageButton(temp));
				circleImages.get(i).setToolTipText(toolTips[i]);
				circleImages.get(i).addMouseListener(this);
				circleImages.get(i).setVisible(true);
			}
			else {
				System.err.println("Error in initImages(), block 2. Image could not be found, or it could not be decoded");
			}
 		}
	}
	
	private boolean isEditable(int row, int col) {
		if (puzzle[row][col] != (Object)0) {
			return false;
		}
		else {
			return true;
		}
	}
	
	private int linePlacement(int lineNum, int gridSize) {
		int offset = 8;
		return (int) Math.floor(((lineNum + 1) * gridSize) / 3) - (int)(offset / 3f);
	}
	
	private int getIndex(MouseEvent m, ArrayList<ImageButton> list) {
		for (int i = 0; i < list.size(); i++) {
			if (m.getSource() == list.get(i)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public void surfaceChanged() { }

	@Override
	public void surfaceDestroyed() { }
	
	@Override
	public void mouseClicked(MouseEvent m) { }
	
	@Override
	public void mouseEntered(MouseEvent m) { }

	@Override
	public void mouseExited(MouseEvent m) { }

	@Override
	public void mouseReleased(MouseEvent m) { }

}
