package kamene.core;

public class Number extends Tile {
	private String value;

	/**
	 * Constructor.
	 * 
	 * @param value
	 *            value of the Number
	 */
	public Number(String value) {
		this.value = value;
	}

	/**
	 * set valuer of Number
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * return value of Number
	 * 
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}

}
