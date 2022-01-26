package sudoku;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Image;

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class SudokuMenu extends Surface implements ActionListener {
	/***** ATTRIBUTES *****/
	private final int BUTTON_COUNT = 4,
					  TITLE_PAD = 75,
					  SUBTITLE_PAD = 50;
	
	// Lists
	protected ArrayList<Image> imageList = new ArrayList<Image>();		// title, play, options, quit
	protected ArrayList<ImageButton> buttonList = new ArrayList<ImageButton>();
	
	// Components
	private ImagePanel panel;
	
	protected ImageButton title;
	protected ImageButton play;
	protected ImageButton options;
	protected ImageButton quit;
	
	private String backgroundImageName = "bkgd1.png";
	private String iconName = "logo.png";
	
	// default values
	protected Pair<Integer> dims = new Pair<Integer>(700, 800);			// default menu dimensions
	private Color surfaceBackground = Color.WHITE;						// default background color
	private Color surfaceForeground = Color.BLACK;						// default foreground color
	private Pair<Integer> optionsDims = new Pair<Integer>(600, 400);	// default option menu dimensions
	
	/***** CONSTRUCTORS *****/
	public SudokuMenu(int d1, int d2, String[] imgNames) {
		super(d1, d2);						// build base surface
		dims = new Pair<Integer>(d1, d2);
		loadImages(imgNames);				// get the images required, set in the necessary interface
		surfaceCreated();					// create surface
	}
	
	public SudokuMenu(Pair<Integer> dims, String[] imgNames) {
		super(dims);						// build base surface
		this.dims = dims;
		loadImages(imgNames);				// get the images required, set in the necessary interface
		surfaceCreated();					// create surface
	}
	
	/***** METHODS *****/
	// loads the images in for the buttons
	private void loadImages(String[] imgNames) {
		for (int i = 0; i < BUTTON_COUNT; i++) {
			if (this.getClass().getResource(imgNames[i]) != null) {
				Image img = new ImageIcon(this.getClass().getResource(imgNames[i])).getImage();
				imageList.add(img);
			} else {
				imageList.add(null);
				System.err.println("Menu Surface Error: Could not access image.");
			}
		}
	}
	
	@Override
	public void surfaceCreated() {
		// set icon
		ImageIcon icon = new ImageIcon(getClass().getResource(iconName));
		setIconImage(icon.getImage());
		
		title = new ImageButton(imageList.get(0));
		title.addActionListener(this);
		play = new ImageButton(imageList.get(1));
		play.addActionListener(this);
		options = new ImageButton(imageList.get(2));
		options.addActionListener(this);
		quit = new ImageButton(imageList.get(3));
		quit.addActionListener(this);
		
		// add to list
		buttonList.add(title);
		buttonList.add(play);
		buttonList.add(options);
		buttonList.add(quit);
		
		backgroundImage = getImage(backgroundImageName, dims.getV1(), dims.getV2());
		if (backgroundImage != null) {
			panel = new ImagePanel(backgroundImage);
			setContentPane(panel);
		}
		else {						// no image could be found, so set it default colors
			System.err.println("The background image could not be set...");
			JPanel panel = new JPanel();
			panel.setBackground(surfaceBackground);
			panel.setForeground(surfaceForeground);
			setContentPane(panel);
		}
				
		add(title);
		add(play);
		add(options);
		add(quit);
		setLayout();
		setTitle("Sudoku!");
		pack();
	}
	
	@Override
	protected void setLayout() {
		SpringLayout l1 = new SpringLayout();
		
		l1.putConstraint(SpringLayout.NORTH, title, TITLE_PAD, SpringLayout.NORTH, this);
		l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, title, 0, SpringLayout.HORIZONTAL_CENTER, this);
		
		l1.putConstraint(SpringLayout.NORTH, buttonList.get(1), TITLE_PAD, SpringLayout.SOUTH, buttonList.get(0));
		l1.putConstraint(SpringLayout.WEST, buttonList.get(1), 0, SpringLayout.WEST, buttonList.get(0));
		for (int i = 2; i < buttonList.size(); i++) {
			l1.putConstraint(SpringLayout.NORTH, buttonList.get(i), SUBTITLE_PAD, SpringLayout.SOUTH, buttonList.get(i - 1));
			l1.putConstraint(SpringLayout.WEST, buttonList.get(i), 0, SpringLayout.WEST, buttonList.get(i - 1));
		}
		setLayout(l1);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == play) {			// play, initialize Game Surface
			System.out.println("playing...");
			new SudokuDifficulty(this, dims);
		}
		else if (e.getSource() == options) {
			System.out.println("options...");
			new SudokuOptions(optionsDims);
		}
		else if (e.getSource() == quit) {	// quit
			System.out.println("quitting...");
			System.exit(0);
		}
	}
	
	@Override
	public void surfaceChanged() { }

	@Override
	public void surfaceDestroyed() { }
}
