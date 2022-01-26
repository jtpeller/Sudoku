package sudoku;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JWindow;

/**
 * Class Surface: provides a simple way to build a surface
 * @author JP
 *
 */
public abstract class Window extends JWindow {
	/***** ATTRIBUTES *****/
	protected Image backgroundImage;
	protected ImagePanel panel;
	
	// default values
	protected Pair<Integer> windowDims = new Pair<Integer>(1000,1000);
	protected Color surfaceBackground = Color.BLACK;
	protected Color surfaceForeground = Color.WHITE;
	
	/***** CONSTRUCTORS *****/
	// Dimensions via 2 independent parameters
	public Window(int d1, int d2) {
		super();
		windowDims = new Pair<Integer>(d1, d2);
		initFrame(d1, d2);
	}

	// Dimensions via a Pair
	public Window(Pair<Integer> d1) {
		super();
		windowDims = d1;
		initFrame(d1.getV1(), d1.getV2());
	}
	
	/***** METHODS *****/
	protected void setColor(Color color) {
		getContentPane().setBackground(color);
		getRootPane().setBackground(color);
		setBackground(color);
		pack();
	}
	
	private void initFrame(int d1, int d2) {
		setPreferredSize(new Dimension(d1, d2));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/***** GETTERS & SETTERS &*****/	
	public Pair<Integer> getDimensions() { return windowDims; }
	
	public int getWidth() { return windowDims.getV1();	}
	
	public int getHeight() { return windowDims.getV2(); }

	public void setBackground(String imgName, Pair<Integer> size) {
		// panel
		backgroundImage = getImage(imgName, size.getV1(), size.getV2());
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
	}
	
	protected Image getImage(String imgName, int width, int height) {
		URL url = this.getClass().getResource(imgName);
		if (url != null) {
			Image image1 = new ImageIcon(url).getImage();
			Image scaled1 = image1.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			return scaled1;
		}
		else {
			System.err.println("Error in getImage(String, int, int). Image could not be found, or it could not be decoded");
			return null;
		}
	}
	
	protected Image getImage(String imgName) {
		URL url = this.getClass().getResource(imgName);
		if (url != null) {
			return new ImageIcon(url).getImage();
		}
		else {
			System.err.println("Error in getImage(String). Image could not be found, or it could not be decoded");
			return null;
		}
	}
	
	/***** TO BE OVERRIDDEN *****/
	public abstract void surfaceCreated();
	
	public abstract void surfaceChanged();
	
	public abstract void surfaceDestroyed();
	
	protected abstract void setLayout();
}
