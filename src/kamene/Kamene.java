package kamene;

import kamene.consoleui.ConsoleUI;
import kamene.core.Field;
import kamene.UserInterface;

/**
 * Main application class.
 */
public class Kamene {
	/** User interface. */
	private UserInterface userInterface;
	private static Kamene instance;
	/**
	 *  return instance of Kamene
	 * @return instance
	 */
	public static Kamene getInstance() {
		return instance;
	}
	/**
	 * Constructor.
	 */
	private Kamene(){

		userInterface = new ConsoleUI();
		Field field = new Field(4,4);
		userInterface.newGameStarted(field);
	}
	/**
	 * Main method.
	 * 
	 * @param args
	 *            arguments
	 */
	public static void main(String[] args) {
		
		new Kamene();
		
	}

}
