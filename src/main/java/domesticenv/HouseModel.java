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
	public static final int GSize = 7;

	boolean fridgeOpen = false; // whether the fridge is open
	boolean carryingBeer = false; // whether the robot is carrying beer
	int sipCount = 0; // how many sip the owner did
	int availableBeers = 2; // how many beers are available

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
		add(FRIDGE, lFridge);
		add(OWNER, lOwner);
	}

	/**
	 * works ok if fridge already open.
	 */
	public synchronized void openFridge() {
		if (!fridgeOpen) {
			fridgeOpen = true;
		}
	}

	/**
	 * works ok if fridge already closed.
	 */
	public synchronized void closeFridge() {
		if (fridgeOpen) {
			fridgeOpen = false;
		}
	}

	/**
	 * Let the robot make 1 step towards a position.
	 * 
	 * @param dest
	 */
	public synchronized void moveTowards(Location dest) {
		Location r1 = getAgPos(0);
		if (r1.x < dest.x)
			r1.x++;
		else if (r1.x > dest.x)
			r1.x--;
		if (r1.y < dest.y)
			r1.y++;
		else if (r1.y > dest.y)
			r1.y--;
		setAgPos(0, r1); // move the robot in the grid

		// repaint the fridge and owner locations
		if (view != null) {
			view.update(lFridge.x, lFridge.y);
			view.update(lOwner.x, lOwner.y);
		}
	}

	public synchronized void getBeer() {
		if (!fridgeOpen) {
			throw new IllegalStateException("can't get beer, fridge is closed");
		}
		if (availableBeers <= 0) {
			throw new IllegalStateException("can't get beer, out of stock");
		}
		if (availableBeers <= 0) {
			throw new IllegalStateException("can't get beer, already carrying beer");
		}

		availableBeers--;
		carryingBeer = true;
		if (view != null) {
			view.update(lFridge.x, lFridge.y);
		}
	}

	/**
	 * deliver n beers to the fridge
	 * 
	 * @param n
	 */
	public synchronized void addBeer(int n) {
		// wait 4 seconds to finish "deliver". FIXME this is odd??
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		availableBeers += n;
		if (view != null)
			view.update(lFridge.x, lFridge.y);
	}

	public synchronized void handInBeer() {
		if (!carryingBeer) {
			throw new IllegalStateException("bot has no beer in hand");
		}
		sipCount = 10;
		carryingBeer = false;
		if (view != null)
			view.update(lOwner.x, lOwner.y);
	}

	public synchronized void sipBeer() {
		if (sipCount > 0) {
			sipCount--;
			if (view != null)
				view.update(lOwner.x, lOwner.y);
		} else {
			throw new IllegalStateException("owner failed to sip, out of beer!");
		}
	}

	public synchronized Location getRobotLocation() {
		return getAgPos(0);
	}

	public synchronized boolean isFridgeOpen() {
		return fridgeOpen;
	}

	public synchronized int getAvailableBeers() {
		return availableBeers;
	}

	public synchronized int getSipCount() {
		return sipCount;
	}
}
