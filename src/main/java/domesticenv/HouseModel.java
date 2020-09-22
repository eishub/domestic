package domesticenv;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.GridWorldView;
import jason.environment.grid.Location;

/**
 * class that implements the Model of Domestic Robot application.
 *
 * Not really a clean interface, as it has a hard link to the view (see the
 * reference to {@link GridWorldView} in {@link GridWorldModel}. This was left
 * as it was in JASON.
 */
public class HouseModel extends GridWorldModel {
	// constants for the grid objects
	public static final int FRIDGE = 16;
	public static final int OWNER = 32;
	// the grid size
	private static final int GSize = 7;

	public boolean fridgeOpen = false; // whether the fridge is open
	public boolean carryingBeer = false; // whether the robot is carrying beer
	public int sipCount = 0; // how many sip the owner did
	public int availableBeers = 2; // how many beers are available

	/**
	 * these are public targets for the moveTowards action.
	 */
	public Location lFridge = new Location(0, 0);
	public Location lOwner = new Location(GSize - 1, GSize - 1);

	public HouseModel() {
		// create a 7x7 grid with one mobile agent
		super(GSize, GSize, 1);

		// initial location of robot (column 3, line 3)
		// ag code 0 means the robot
		setAgPos(0, GSize / 2, GSize / 2);

		// initial location of fridge and owner
		add(FRIDGE, this.lFridge);
		add(OWNER, this.lOwner);
	}

	/**
	 * works ok if fridge already open.
	 */
	public synchronized void openFridge() {
		if (!this.fridgeOpen) {
			this.fridgeOpen = true;
		}
	}

	/**
	 * works ok if fridge already closed.
	 */
	public synchronized void closeFridge() {
		if (this.fridgeOpen) {
			this.fridgeOpen = false;
		}
	}

	/**
	 * Let the robot make 1 step towards a position.
	 *
	 * @param dest
	 */
	public synchronized void moveTowards(final Location dest) {
		final Location r1 = getAgPos(0);
		if (r1.x < dest.x) {
			r1.x++;
		} else if (r1.x > dest.x) {
			r1.x--;
		}
		if (r1.y < dest.y) {
			r1.y++;
		} else if (r1.y > dest.y) {
			r1.y--;
		}
		setAgPos(0, r1); // move the robot in the grid

		// repaint the fridge and owner locations
		if (this.view != null) {
			this.view.update(this.lFridge.x, this.lFridge.y);
			this.view.update(this.lOwner.x, this.lOwner.y);
		}
	}

	public synchronized void getBeer() {
		if (!this.fridgeOpen) {
			throw new IllegalStateException("can't get beer, fridge is closed");
		}
		if (this.availableBeers <= 0) {
			throw new IllegalStateException("can't get beer, out of stock");
		}
		if (this.availableBeers <= 0) {
			throw new IllegalStateException("can't get beer, already carrying beer");
		}

		this.availableBeers--;
		this.carryingBeer = true;
		if (this.view != null) {
			this.view.update(this.lFridge.x, this.lFridge.y);
		}
	}

	/**
	 * deliver n beers to the fridge
	 *
	 * @param n
	 */
	public synchronized void addBeer(final int n) {
		try { // wait 4 seconds to finish "deliver". FIXME this is odd??
			Thread.sleep(4000);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		this.availableBeers += n;
		if (this.view != null) {
			this.view.update(this.lFridge.x, this.lFridge.y);
		}
	}

	public synchronized void handInBeer() {
		if (!this.carryingBeer) {
			throw new IllegalStateException("bot has no beer in hand");
		}
		this.sipCount = 10;
		this.carryingBeer = false;
		if (this.view != null) {
			this.view.update(this.lOwner.x, this.lOwner.y);
		}
	}

	public synchronized void sipBeer() {
		if (this.sipCount > 0) {
			this.sipCount--;
			if (this.view != null) {
				this.view.update(this.lOwner.x, this.lOwner.y);
			}
		} else {
			throw new IllegalStateException("owner failed to sip, out of beer!");
		}
	}

	public synchronized Location getRobotLocation() {
		return getAgPos(0);
	}

	public synchronized boolean isFridgeOpen() {
		return this.fridgeOpen;
	}

	public synchronized int getAvailableBeers() {
		return this.availableBeers;
	}

	public synchronized int getSipCount() {
		return this.sipCount;
	}
}
