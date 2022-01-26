package sudoku;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class SudokuGrid extends JTable implements TableModelListener {
	/***** ATTRIBUTES *****/
	private int size;					// # rows/cols
	private int gridDim;				// width/height of grid
	private Object[][] data;
	private DefaultTableModel dataModel;
	
	/***** CONSTANTS *****/
	private Color backgroundColor = Color.BLACK;
	private Color foregroundColor = Color.WHITE;
    private Color borderColor = Color.DARK_GRAY;
    private Color nullColor = Color.LIGHT_GRAY;
    
	private Font f1 = new Font("Sans", Font.PLAIN, 28);
	private Font f2 = new Font("Serif", Font.BOLD, 36);
	
	/***** CONSTRUCTORS *****/
	public SudokuGrid(int size, int gridDim, Object[][] data) {
		this.size = size;
		this.gridDim = gridDim;
		this.data = data;
		dataModel = new DefaultTableModel(size, size) {
			@Override
			public boolean isCellEditable(int row, int column) { return false; }
		};
		setModel(dataModel);
		formatTable();
		getModel().addTableModelListener(this);
	}
	
	/***** METHODS *****/
	public int getGridSize() {
		return size;
	}
	
	public int getDimensions() {
		return gridDim;
	}
	
	public Object[][] getCurrentPuzzle() {
		int size = data.length;
		Object[][] arr = new Object[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				arr[i][j] = getValueAt(i, j);
			}
		}
		return arr;
	}
	
	public void setBackground(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		super.setBackground(backgroundColor);
	}
	
	public void setForeground(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
		super.setForeground(foregroundColor);
	}
	
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}
	
	public void setNullColor(Color nullColor) {
		this.nullColor = nullColor;
	}
	
	public void setData(Object[][] arr) {
		if (!isValid(arr)) {
			System.err.println("setData() error. Array has incorrect dimensions");
			return;
		}
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr.length; j++) {		// should be a square
				if (arr[i][j] == (Object)0) {
					System.out.println("Setting cell at (" + i + ", " + j + ") via null block");
					setValueAt(null, i, j);				// empty cell to be filled in
				}
				else {
					System.out.println("Setting cell at (" + i + ", " + j + ") via non-null block");
					setValueAt(arr[i][j], i, j);		// default value
				}
			}
		}
		this.data = arr;								// store the array
	}
	
	private boolean isValid(Object[][] arr) {
		for (int i = 0; i < arr.length; i++) {
			if (arr.length != arr[i].length) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isInRow(Object num, int row) {
		int col = getColumnCount();
		for (int i = 0; i < col; i++) {
			if (getValueAt(row, i) == num) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isInCol(Object num, int col) {
		int row = getRowCount();
		for (int i = 0; i < row; i++) {
			if (getValueAt(i, col) == num) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isInBox(Object num, int row, int col) {
		int sqrtGridSize = (int) Math.sqrt(getRowCount());		// get the sqrt of grid size
		
		// ensure row & col start at the beginning of the box
		if (row >= 2*sqrtGridSize)		// start of final box
			row = 2*sqrtGridSize;
		else if (row >= sqrtGridSize)	// start of middle box
			row = sqrtGridSize;
		else 
			row = 0;
		
		if (col >= 2*sqrtGridSize)		// start of final box
			col = 2*sqrtGridSize;
		else if (col >= sqrtGridSize)	// start of middle box
			col = sqrtGridSize;
		else 
			col = 0;
		
		for (int i = 0; i < sqrtGridSize; i++) {
			for (int j = 0; j < sqrtGridSize; j++) {
				if (getValueAt(row + i, col + j) == num) {
					return true;
				}
			}
		}
		return false;
	}
	
	// Handles the formatting for the table
	private void formatTable() {
		// appearance
		setBackground(backgroundColor);
		setForeground(foregroundColor);
		setShowGrid(false);
		setRowHeight( (int) Math.ceil(gridDim / 9));
		for (int i = 0; i < size; i++) {
			getColumnModel().getColumn(i).setPreferredWidth( (int) Math.ceil(gridDim / 9));
		}
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment( JLabel.CENTER );
		setDefaultRenderer(Object.class, r);
		
		// functionality
		setRowSelectionAllowed(false);
		setDragEnabled(false);
		setAutoResizeMode(AUTO_RESIZE_OFF);
		setTableHeader(null);
	}
	
	@Override
	public Component prepareRenderer (TableCellRenderer renderer, int rowIndex, int columnIndex){  
		JComponent c = (JComponent)super.prepareRenderer(renderer, rowIndex, columnIndex);  

		if (data[rowIndex][columnIndex] == null || data[rowIndex][columnIndex] == (Object)0) {  
			c.setFont(f2);
			c.setBorder(BorderFactory.createLineBorder(nullColor, 3));
		} 
		else {
			c.setFont(f1);
			c.setBorder(BorderFactory.createLineBorder(borderColor, 3));
		}
		
		return c;
	}
}
