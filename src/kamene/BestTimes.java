package kamene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;

import kamene.BestTimes;

public class BestTimes implements Iterable<BestTimes.PlayerTime> {
	/** List of best player times. */
	private List<PlayerTime> playerTimes = new ArrayList<PlayerTime>();
	/** StringBuilder with Formatter for print of field*/
	StringBuilder sb = new StringBuilder();
	Formatter f = new Formatter(sb);
	/**
	 * Returns an iterator over a set of best times.
	 * 
	 * @return an iterator
	 */
	public Iterator<PlayerTime> iterator() {
		return playerTimes.iterator();
	}

	/**
	 * Adds player time to best times.
	 * 
	 * @param name
	 *            name of the player
	 * @param time
	 *            player time in seconds
	 */
	public void addPlayerTime(String name, int time) {
		playerTimes.add(new PlayerTime(name, time));
        Collections.sort(playerTimes);
	}

	/**
	 * Returns a string representation of the object.
	 * 
	 * @return a string representation of the object
	 */
	public String toString() {
		Iterator<PlayerTime> iterator=iterator();
		while(iterator.hasNext())
		{
			PlayerTime pt=iterator.next();
			f.format("%s",pt);
		}
		return f.toString();
	}

	/**
	 * Player time.
	 */
	public static class PlayerTime implements Comparable<PlayerTime> {
		/** Player name. */
		private final String name;

		public int getTime() {
			return time;
		}

		public String getName() {
			return name;
		}

		/** Playing time in seconds. */
		private final int time;

		/**
		 * Constructor.
		 * 
		 * @param name
		 *            player name
		 * @param time
		 *            playing game time in seconds
		 */
		public PlayerTime(String name, int time) {
			this.name = name;
			this.time = time;
		}

		@Override
		public int compareTo(PlayerTime o) {
			if (this.time > o.getTime()) {
				return 1;
			} else if (this.time < o.getTime()) {
				return -1;
			} else {
				return 0;
			}

		}
		@Override
		public String toString() {
			StringBuilder stb = new StringBuilder();
			stb.append(this.name).append(" : ").append(this.time).append("\n");
			return stb.toString();
		}

	}
}
