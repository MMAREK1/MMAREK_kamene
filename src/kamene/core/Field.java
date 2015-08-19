package kamene.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kamene.consoleui.ConsoleUI;
import kamene.core.GameState;


/**
 * Field represents playing field and game logic.
 */
public class Field implements Serializable {
	/**
	 * Time of playing from saved game
	 */
	private int startTime;
	/**
	 * Playing field tiles.
	 */
	private Tile[][] tiles;
	/**
	 * Field row count. Rows are indexed from 0 to (rowCount - 1).
	 */
	private final int rowCount;
	/**
	 * Column count. Columns are indexed from 0 to (columnCount - 1).
	 */
	private final int columnCount;
	/**
	 * Game state.
	 */
	private GameState state = GameState.PLAYING;
	/**
	 * Constructor.
	 * 
	 * @param rowCount
	 *            - row count
	 * @param columnCount
	 *            - column count
	 */
	public Field(int rowCount, int columnCount) {
		this.columnCount = columnCount;
		this.rowCount = rowCount;
		tiles = new Tile[rowCount][columnCount];
		generate();
	}
/**
 * return time of saved game
 * @return startTime
 */
	public int getStartTime() {
		return startTime;
	}
	/**
	 * set time of load game
	 * @param startTime
	 */
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
/**
 * save game to file
 */
	public void saveGame() {
		String fileName = "register.bin";
		try (FileOutputStream out = new FileOutputStream(fileName);
				ObjectOutputStream so = new ObjectOutputStream(out);) {
			so.writeObject(ConsoleUI.getPlayingSeconds());
			so.writeObject(tiles);
			so.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
/**
 * generate playing field, generate random numbers which represent game - first fill arrayList with 
 * numbers from 1 to n(row*column-1) and " " than it shuffle
 */
	private void generate() {
		String fileName = "register.bin";
		File f = new File(fileName);
		if (f.exists()) {
			loadFromFile(fileName);
		} else {
			setStartTime(0);
			List<String> createRandomNumber = new ArrayList<>();
			for (int i = 1; i <= getColumnCount() * getRowCount(); i++) {
				if (i != getColumnCount() * getRowCount()) {
					createRandomNumber.add(Integer.toString(i));
				} else {
					createRandomNumber.add(" ");
				}
			}
			Collections.shuffle(createRandomNumber);
			String[] randomNumbers = createRandomNumber.toArray(new String[createRandomNumber.size()]);
			int count = 0;
//			randomNumbers[getColumnCount() * getRowCount() - 1] = Integer
//					.toString(getColumnCount() * getRowCount() - 1);
//			randomNumbers[getColumnCount() * getRowCount() - 2] = " ";
			for (int row = 0; row < getRowCount(); row++) {
				for (int column = 0; column < getColumnCount(); column++) {
					tiles[row][column] = new Number(randomNumbers[count]);
					count++;
				}
			}
		}
	}
	/**
	 * load game from file
	 * @param fileName - name of file
	 */
	private void loadFromFile(String fileName) {
		try (FileInputStream in = new FileInputStream(fileName); ObjectInputStream si = new ObjectInputStream(in);) {
			setStartTime((int) si.readObject());
			tiles = (Tile[][]) si.readObject();
			si.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		new File(fileName).delete();
	}
	/**
	 * action with tiles or throws exception when wrong input
	 * @param command read input
	 * @throws WrongInput
	 */
	public void move(String command) throws WrongInput{
		int row = 0;
		int column = 0;
		Number number = null;
		out: for (row = 0; row < getRowCount(); row++) {
			for (column = 0; column < getColumnCount(); column++) {
				number = (Number) tiles[row][column];
				if (number.getValue().equals(" ")) {
					break out;
				}
			}
		}
		Number up = null;
		switch (command) {
		case "W":
		case "UP":
			if (row != getRowCount() - 1) {
				up = (Number) tiles[row + 1][column];
				number.setValue(up.getValue());
				up.setValue(" ");
			}
			break;
		case "S":
		case "DOWN":
			if (row != 0) {
				up = (Number) tiles[row - 1][column];
				number.setValue(up.getValue());
				up.setValue(" ");
			}
			break;
		case "A":
		case "LEFT":
			if (column != (getColumnCount() - 1)) {
				up = (Number) tiles[row][column + 1];
				number.setValue(up.getValue());
				up.setValue(" ");
			}
			break;
		case "D":
		case "RIGHT":
			if (column != 0) {
				up = (Number) tiles[row][column - 1];
				number.setValue(up.getValue());
				up.setValue(" ");
			}
			break;
		default:
			throw new WrongInput("Wrong Input: w|up - s|down - a|left - d|right - save - exit ");
		}
		if (isSolved()) {
			state = GameState.SOLVED;
			return;
		}
		else
		{
			state = GameState.PLAYING;
		}
	}
	/**
	 * Returns true if game is solved, false otherwise.
	 * 
	 * @return true if game is solved, false otherwise
	 */
	private boolean isSolved() {
		int count = 1;
		for (int row = 0; row < getRowCount(); row++) {
			for (int column = 0; column < getColumnCount(); column++) {
				if (count != getColumnCount() * getRowCount()) {
					Number number = (Number) tiles[row][column];
					if (!number.getValue().equals(Integer.toString(count))) {
						return false;
					}
					count++;
				}
			}
		}
		return true;
	}

	public Tile getTiles(int row, int column) {
		return tiles[row][column];
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public GameState getState() {
		return state;
	}
}
