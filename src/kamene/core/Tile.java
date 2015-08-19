package kamene.core;

import java.io.Serializable;
/**
 * Tile of a field.
 */
public abstract class Tile implements Serializable {
	/**
	 * return Tile at specific position
	 * 
	 * @param row
	 *            - row number
	 * @param column
	 *            - column number
	 * @return tile - Tile
	 */
	public Tile getTile(int row, int column) {
		Tile tile = null;
		return tile;
	}
}
