package sudoku;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SpringLayout;

public class SudokuOptions extends Window implements ActionListener {
	// attributes
	private Font labelFont = new Font("Serif", Font.BOLD, 32);
	private String backgroundImageName = "bkgd1.png";
	
	// buttons
	private ImageButton music = new ImageButton();
	private ImageButton sound = new ImageButton();
	private ImageButton close = new ImageButton();
	private ArrayList<Image> imageList = new ArrayList<Image>();
	
	// button values
	private boolean isMusicOn = true;
	private boolean isSoundOn = true;
	
	// labels
	private JLabel musicLabel = new JLabel("Music");
	private JLabel soundLabel = new JLabel("Sound");
	
	// constants
	private final Color textColor = Color.BLACK;
	private final int PADDING = 15;
	private final String[] imgNames = { "on.png", "off.png", "close.png" };

	/***** CONSTRUCTORS *****/
	public SudokuOptions(Pair<Integer> d1) {
		super(d1);
		surfaceCreated();
		setColor(Color.BLACK);
	}
	
	@Override
	public void surfaceCreated() {
		// set background
		setBackground(backgroundImageName, windowDims);
		
		// initialize images
		loadImages();
		setMusic(isMusicOn);
		setSound(isSoundOn);
		close.setImage(imageList.get(2));

		// action listeners
		music.addActionListener(this);
		sound.addActionListener(this);
		close.addActionListener(this);

		// add to surface
		add(musicLabel);
		add(soundLabel);
		add(music);
		add(sound);
		add(close);

		// layout & pack
		initLabels();
		setLayout();
		pack();
	}
	
	private void initLabels() {
		musicLabel.setFont(labelFont);
		soundLabel.setFont(labelFont);
		musicLabel.setForeground(textColor);
		soundLabel.setForeground(textColor);
	}
	
	protected void setLayout() {
		SpringLayout l1 = new SpringLayout();
		
		// buttons
		l1.putConstraint(SpringLayout.VERTICAL_CENTER, music, 0, SpringLayout.VERTICAL_CENTER, this);
		l1.putConstraint(SpringLayout.EAST, music, -PADDING, SpringLayout.HORIZONTAL_CENTER, this);
		
		l1.putConstraint(SpringLayout.VERTICAL_CENTER, sound, 0, SpringLayout.VERTICAL_CENTER, this);
		l1.putConstraint(SpringLayout.WEST, sound, PADDING, SpringLayout.HORIZONTAL_CENTER, this);
		
		l1.putConstraint(SpringLayout.SOUTH, close, -(4 * PADDING), SpringLayout.SOUTH, this);
		l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, close, 0, SpringLayout.HORIZONTAL_CENTER, this);
		
		// labels
		l1.putConstraint(SpringLayout.SOUTH, musicLabel, -PADDING, SpringLayout.NORTH, music);
		l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, musicLabel, 0, SpringLayout.HORIZONTAL_CENTER, music);
		l1.putConstraint(SpringLayout.SOUTH, soundLabel, -PADDING, SpringLayout.NORTH, sound);
		l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, soundLabel, 0, SpringLayout.HORIZONTAL_CENTER, sound);
		
		setLayout(l1);
	}
	
	// loads the images in for the buttons
	private void loadImages() {
		for (int i = 0; i < imgNames.length; i++) {
			if (this.getClass().getResource(imgNames[i]) != null) {
				Image img = new ImageIcon(this.getClass().getResource(imgNames[i])).getImage();
				imageList.add(img);
			} else {
				imageList.add(null);
				System.err.println("Option Surface Error: Could not access image.");
			}
		}
	}
	
	public void setMusic(boolean flag) {
		if (flag) {			// music turned on
			System.out.println("turning music on...");
			music.setImage(imageList.get(0));
		}
		else {				// music turned off
			System.out.println("turning music off...");
			music.setImage(imageList.get(1));
		}
		isMusicOn = flag;
		music.revalidate();
		pack();
	}
	
	public void setSound(boolean flag) {
		if (flag) {			// sound turned on
			System.out.println("turning sound on...");
			sound.setImage(imageList.get(0));
		}
		else {				// sound turned off
			System.out.println("turning sound off...");
			sound.setImage(imageList.get(1));
		}
		isSoundOn = flag;
		sound.revalidate();
		pack();
	}
	
	private boolean toggle(boolean flag) {
		if (flag)	return false;
		else 		return true;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == music) {
			isMusicOn = toggle(isMusicOn);
			setMusic(isMusicOn);
		}
		else if (e.getSource() == sound) {
			isSoundOn = toggle(isSoundOn);
			setSound(isSoundOn);
		}
		else if (e.getSource() == close) {
			System.out.println("closing options...");
			dispose();
		}
	}

	@Override
	public void surfaceChanged() { }

	@Override
	public void surfaceDestroyed() { }

	

}
