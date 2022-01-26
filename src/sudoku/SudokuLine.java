package sudoku;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

import java.util.LinkedList;

public class SudokuLine extends JComponent {
	private final LinkedList<Line> lines = new LinkedList<Line>();
	private final Color defaultColor = Color.WHITE;

	/***** LINE SUBCLASS *****/
	private static class Line {
		final int x1;
		final int y1;
		final int x2;
		final int y2;
		final Color color;

		public Line(int x1, int y1, int x2, int y2, Color color) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.color = color;
		}
	}

	/***** METHODS *****/
	public void addLine(int x1, int x2, int x3, int x4) {
		addLine(x1, x2, x3, x4, defaultColor);
	}

	public void addLine(int x1, int x2, int x3, int x4, Color color) {
		lines.add(new Line(x1, x2, x3, x4, color));
		repaint();
	}

	public void clearLines() {
		lines.clear();
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Line line : lines) {
			g.setColor(line.color);
			g.drawLine(line.x1, line.y1, line.x2, line.y2);
		}
	}

}