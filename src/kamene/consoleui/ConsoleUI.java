package kamene.consoleui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Formatter;

import kamene.BestTimes;
import kamene.Kamene;
import kamene.UserInterface;
import kamene.core.Field;
import kamene.core.GameState;
import kamene.core.WrongInput;

public class ConsoleUI implements UserInterface {
	public static final String URL = "jdbc:mysql://localhost/school";
	public static final String USER = "root";
	public static final String PASSWORD = "MAT.246.mar.";
	public static final String INSERT = "INSERT INTO best (name, time) VALUES (?, ?)";
	public static final String SELECT = "SELECT * FROM best";
	private static long startMillis;
	private BestTimes bestTimes = new BestTimes();
	private Field field;
	private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

	private String readLine() {
		try {
			return input.readLine();
		} catch (IOException e) {
			return null;
		}
	}

	public static long getStartMillis() {
		return startMillis;
	}

	public static void setStartMillis() {
		startMillis = System.currentTimeMillis();
	}

	public static int getPlayingSeconds() {
		return (int) (System.currentTimeMillis() - startMillis) / 1000;
	}

	/**
	 * provides game interface - waiting for your output, checking game state, communicate with database and starting new game after your win
	 */
	public void newGameStarted(Field field) {
		this.field = field;
		setStartMillis();
		startMillis = getStartMillis();
		String name = System.getProperty("user.name");
		do {
			update();
			processInput();
			if (field.getState() == GameState.SOLVED) {
				update();
				loadBest();
				bestTimes.addPlayerTime(name, getPlayingSeconds() + field.getStartTime());
				System.out.println("You win !!!");
				System.out.println(bestTimes.toString());
				saveBest(name, (int) getPlayingSeconds() + field.getStartTime());
				field.setStartTime(0);
				newGameStarted(new Field(field.getRowCount(),field.getColumnCount()));
			}
		} while (true);
	}
	/**
	 * load list of players and their time from database
	 */
	private void loadBest() {
		try {
			Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
			Statement stmt = con.createStatement();
			bestTimes = new BestTimes();
			ResultSet rs = stmt.executeQuery(SELECT);
			while (rs.next()) {
				bestTimes.addPlayerTime(rs.getString(1), rs.getInt(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * save to database list of players and their time
	 * @param name of player
	 * @param time of playing
	 */
	private void saveBest(String name, int time) {
		try {
			Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement stmt = con.prepareStatement(INSERT);
			stmt.setString(1, name);
			stmt.setInt(2, time);
			stmt.executeUpdate();
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
/**
 * read input and call function move, new game or end game with or without save
 */
	private void processInput() {
		String action = readLine();
		action = action.toUpperCase();
		switch (action) {
		case "N":
		case "NEW":
			newGameStarted(new Field(4, 4));
			break;
		case "SAVE":
			field.saveGame();
			System.out.println("you saved end game");
			System.exit(0);
		case "E":
		case "EXIT":
			System.out.println("you end game");
			System.exit(0);
		default:
			try {
				field.move(action);
			} catch (WrongInput e) {
				e.printStackTrace();
			}
			break;
		}
	}

	/**
	 * function for showing game field with instructions
	 */
	public void update() {
		String[] message ={"up|w down|s e|exit","left|a rigth|d save new|n"};
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb);
		formatter.format("%s", "      Time:");
		Kamene.getInstance();
		formatter.format("%4s\n", getPlayingSeconds() + field.getStartTime());
		for (int row = 0; row < field.getRowCount(); row++) {
			for (int column = 0; column < field.getColumnCount(); column++) {
				formatter.format("%4s", field.getTiles(row, column));
			}
			if(row==0){
				formatter.format("%5s", " ");
				formatter.format("%10s", message[0]);
			}
			if(row==1){
				formatter.format("%5s", " ");
				formatter.format("%10s", message[1]);
			}
			formatter.format("%4s", "\n");
		}
		System.out.println(sb);
		formatter.close();
	}

}
