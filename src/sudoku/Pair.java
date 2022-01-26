package sudoku;

public class Pair<T> {
	private T v1;
	private T v2;
	
	public Pair(T v1, T v2) {
		this.v1 = v1;
		this.v2 = v2;
	}
	
	public Pair(Pair<T> p1) {
		this.v1 = p1.getV1();
		this.v2 = p1.getV2();
	}
	
	public String getType () {
		return v1.getClass().getSimpleName();
	}
	
	/**
	 * @return v1	first pair value
	 */
	public T getV1() {
		return v1;
	}
	
	/**
	 * @return v2	second pair value
	 */
	public T getV2() {
		return v2;
	}
	
	/**
	 * @param v1	new v1 value
	 */
	public void setV1(T v1) {
		this.v1 = v1;
	}
	
	/**
	 * @param v2	new v2 value
	 */
	public void setV2(T v2) {
		this.v2 = v2;
	}
	
	/**
	 * sets the entire pair
	 * @param v1	new v1 value
	 * @param v2	new v2 value
	 */
	public void setPair(T v1, T v2) {
		this.v1 = v1;
		this.v2 = v2;
	}
	
	/**
	 * sets the entire pair
	 * @param p1	Pair<T> type; each val is extracted.
	 */
	public void setPair(Pair<T> p1) {
		this.v1 = p1.getV1();
		this.v2 = p1.getV2();
	}
	
	public boolean equals(T v1, T v2) {
		return (v1 == this.v1 && v2 == this.v2);
	}
	
	public boolean equals(Pair<T> p1) {
		return (p1.getV1() == this.getV1() && p1.getV2() == this.getV2());
	}
}
