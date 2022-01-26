package sudoku;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SpringLayout;

public class SudokuDifficulty extends Window implements MouseListener {
	/***** ATTRIBUTES *****/
	private JLabel difficultyPrompt = new JLabel("Choose your difficulty:");
	private ArrayList<ImageButton> buttonList = new ArrayList<ImageButton>();
	private SudokuMenu menu;
	
	// Appearance
	private Font labelFont = new Font("Serif", Font.BOLD, 56);
	private String[] imgNames = {"easy.png", "normal.png", "hard.png", "impossible.png", "back.png"};
	private String backgroundImageName = "bkgd1.png";
	private ArrayList<Image> imageList = new ArrayList<Image>();
	
	// Constants
	private final int EASY = 30,
					  NORMAL = 40,
					  HARD = 50,
					  IMPOSSIBLE = 60,
					  TITLE_PAD = 75,
					  SUBTITLE_PAD = 50;
	
	/***** CONSTRUCTORS *****/
	public SudokuDifficulty(SudokuMenu menu, Pair<Integer> d1) {
		super(d1);
		System.out.println("SudokuDifficulty...");
		this.menu = menu;
		surfaceCreated();	
	}
	
	/***** METHODS *****/
	@Override
	public void surfaceCreated() {
		// colors
		setBackground(backgroundImageName, windowDims);
		setForeground(surfaceForeground);
		
		// initialize buttons
		difficultyPrompt.setFont(labelFont);
		add(difficultyPrompt);
		
		// initialize image
		loadImages();
		for (int i = 0; i < imageList.size(); i++) {
			buttonList.add(new ImageButton(imageList.get(i)));		// create buttons
			buttonList.get(i).addMouseListener(this);
			add(buttonList.get(i));								// add to this
		}
		
		// layout & pack
		setLayout();
		pack();
	}
	
	private void loadImages() {
		for (int i = 0; i < imgNames.length; i++) {
			if (this.getClass().getResource(imgNames[i]) != null) {
				Image img = new ImageIcon(this.getClass().getResource(imgNames[i])).getImage();
				imageList.add(img);
			} else {
				imageList.add(null);
				System.err.println("Difficulty Error: Could not access image.");
			}
		}
	}
	
	@Override
	protected void setLayout() { 
		SpringLayout l1 = new SpringLayout();
		
		l1.putConstraint(SpringLayout.NORTH, difficultyPrompt, TITLE_PAD, SpringLayout.NORTH, this);
		l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, difficultyPrompt, 0, SpringLayout.HORIZONTAL_CENTER, this);
		
		l1.putConstraint(SpringLayout.NORTH, buttonList.get(0), TITLE_PAD, SpringLayout.SOUTH, difficultyPrompt);
		l1.putConstraint(SpringLayout.WEST, buttonList.get(0), SUBTITLE_PAD, SpringLayout.WEST, this);
		for (int i = 1; i < buttonList.size(); i++) {
			l1.putConstraint(SpringLayout.NORTH, buttonList.get(i), SUBTITLE_PAD, SpringLayout.SOUTH, buttonList.get(i - 1));
			l1.putConstraint(SpringLayout.WEST, buttonList.get(i), 0, SpringLayout.WEST, buttonList.get(i - 1));
		}
		setLayout(l1);
	}

	@Override
	public void mouseClicked(MouseEvent m) {
		if (buttonList.contains(m.getSource())) {
			System.out.print("Difficulty chosen: ");
			int diff = 0;
			int idx = getIndex(m, buttonList);
			switch (idx) {
			case 0:
				System.out.println("Easy...");
				diff = EASY;
				break;
			case 1:
				System.out.println("Normal...");
				diff = NORMAL;
				break;
			case 2:
				System.out.println("Hard...");
				diff = HARD;
				break;
			case 3:
				System.out.println("Impossible...");
				diff = IMPOSSIBLE;
				break;
			case 4:
				System.out.println("Back to menu...");
				menu.setVisible(true);
				dispose();
				return;
			default:
				System.err.println("Difficult Error: Improper difficulty setting...");
				System.out.println("Setting to difficulty to normal");
				diff = NORMAL;
				break;
			}
			if (diff != 0) {
				dispose();
				menu.setVisible(false);
				new SudokuSurface(menu, windowDims, diff);
			}
		}
		
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
	public void mouseEntered(MouseEvent m) { }

	@Override
	public void mouseExited(MouseEvent m) { }

	@Override
	public void mousePressed(MouseEvent m) { }

	@Override
	public void mouseReleased(MouseEvent m) { }

}
