package jason.environment.grid;

import java.util.Random;

/**
 * Simple model for a grid world (with agents and obstacles).
 *
 * <p>
 * Every agent gets an identification (a integer from 0 to the number of ag -
 * 1). The relation of this identification with agent's name should be done in
 * the environment class and is application dependent.
 * 
 * <p>
 * Every type of object in the environment is represented by a bit mask: an
 * agent is 000010; an obstacle is 000100; .... New types of objects should
 * follow this pattern, for example, GOLD = 8 (001000), ENEMY=16 (010000), ... A
 * place with two object is represented by the OR between the masks: an agent
 * and a gold is 001010.
 *
 * <p>
 * Limitations:
 * <ul>
 * <li>The number of agents can not change dynamically</li>
 * <li>Two agents can not share the same place. More generally, two objects with
 * the same "mask" can not share a place.</li>
 * </ul>
 */
public class GridWorldModel {
	// each different object is represented by having a single bit
	// set (a bit mask is used in the model), so any power of two
	// represents different objects. Other numbers represent combinations
	// of objects which are all located in the same cell of the grid.
	public static final int CLEAN = 0;
	public static final int AGENT = 2;
	public static final int OBSTACLE = 4;

	protected int width, height;
	protected int[][] data = null;
	protected Location[] agPos;
	protected GridWorldView view;

	protected Random random = new Random();

	protected GridWorldModel(final int w, final int h, final int nbAgs) {
		this.width = w;
		this.height = h;

		// int data
		this.data = new int[this.width][this.height];
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				this.data[i][j] = CLEAN;
			}
		}

		this.agPos = new Location[nbAgs];
		for (int i = 0; i < this.agPos.length; i++) {
			this.agPos[i] = new Location(-1, -1);
		}
	}

	public void setView(final GridWorldView v) {
		this.view = v;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public int getNbOfAgs() {
		return this.agPos.length;
	}

	public boolean inGrid(final Location l) {
		return inGrid(l.x, l.y);
	}

	public boolean inGrid(final int x, final int y) {
		return y >= 0 && y < this.height && x >= 0 && x < this.width;
	}

	public boolean hasObject(final int obj, final Location l) {
		return inGrid(l.x, l.y) && (this.data[l.x][l.y] & obj) != 0;
	}

	public boolean hasObject(final int obj, final int x, final int y) {
		return inGrid(x, y) && (this.data[x][y] & obj) != 0;
	}

	// gets how many objects of some kind are in the grid
	public int countObjects(final int obj) {
		int c = 0;
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				if (hasObject(obj, i, j)) {
					c++;
				}
			}
		}
		return c;
	}

	public void set(final int value, final int x, final int y) {
		this.data[x][y] = value;
		if (this.view != null) {
			this.view.update(x, y);
		}
	}

	public void add(final int value, final Location l) {
		add(value, l.x, l.y);
	}

	public void add(final int value, final int x, final int y) {
		this.data[x][y] |= value;
		if (this.view != null) {
			this.view.update(x, y);
		}
	}

	public void addWall(final int x1, final int y1, final int x2, final int y2) {
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				add(OBSTACLE, x, y);
			}
		}
	}

	public void remove(final int value, final Location l) {
		remove(value, l.x, l.y);
	}

	public void remove(final int value, final int x, final int y) {
		this.data[x][y] &= ~value;
		if (this.view != null) {
			this.view.update(x, y);
		}
	}

	public void setAgPos(final int ag, final Location l) {
		final Location oldLoc = getAgPos(ag);
		if (oldLoc != null) {
			remove(AGENT, oldLoc.x, oldLoc.y);
		}
		this.agPos[ag] = l;
		add(AGENT, l.x, l.y);
	}

	public void setAgPos(final int ag, final int x, final int y) {
		setAgPos(ag, new Location(x, y));
	}

	public Location getAgPos(final int ag) {
		try {
			if (this.agPos[ag].x == -1) {
				return null;
			} else {
				return (Location) this.agPos[ag].clone();
			}
		} catch (final Exception e) {
			return null;
		}
	}

	/** returns the agent at location l or -1 if there is not one there */
	public int getAgAtPos(final Location l) {
		return getAgAtPos(l.x, l.y);
	}

	/** returns the agent at x,y or -1 if there is not one there */
	public int getAgAtPos(final int x, final int y) {
		for (int i = 0; i < this.agPos.length; i++) {
			if (this.agPos[i].x == x && this.agPos[i].y == y) {
				return i;
			}
		}
		return -1;
	}

	/** returns true if the location l has no obstacle neither agent */
	public boolean isFree(final Location l) {
		return isFree(l.x, l.y);
	}

	/** returns true if the location x,y has neither obstacle nor agent */
	public boolean isFree(final int x, final int y) {
		return inGrid(x, y) && (this.data[x][y] & OBSTACLE) == 0 && (this.data[x][y] & AGENT) == 0;
	}

	/** returns true if the location l has not the object obj */
	public boolean isFree(final int obj, final Location l) {
		return inGrid(l.x, l.y) && (this.data[l.x][l.y] & obj) == 0;
	}

	/** returns true if the location x,y has not the object obj */
	public boolean isFree(final int obj, final int x, final int y) {
		return inGrid(x, y) && (this.data[x][y] & obj) == 0;
	}

	public boolean isFreeOfObstacle(final Location l) {
		return isFree(OBSTACLE, l);
	}

	public boolean isFreeOfObstacle(final int x, final int y) {
		return isFree(OBSTACLE, x, y);
	}

	/**
	 * returns a random free location using isFree to test the availability of some
	 * possible location (it means free of agents and obstacles)
	 */
	protected Location getFreePos() {
		for (int i = 0; i < (getWidth() * getHeight() * 5); i++) {
			final int x = this.random.nextInt(getWidth());
			final int y = this.random.nextInt(getHeight());
			final Location l = new Location(x, y);
			if (isFree(l)) {
				return l;
			}
		}
		return null; // not found
	}

	/**
	 * returns a random free location using isFree(object) to test the availability
	 * of some possible location
	 */
	protected Location getFreePos(final int obj) {
		for (int i = 0; i < (getWidth() * getHeight() * 5); i++) {
			final int x = this.random.nextInt(getWidth());
			final int y = this.random.nextInt(getHeight());
			final Location l = new Location(x, y);
			if (isFree(obj, l)) {
				return l;
			}
		}
		return null; // not found
	}
}
