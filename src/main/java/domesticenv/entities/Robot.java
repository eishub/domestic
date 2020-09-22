package domesticenv.entities;

import java.util.ArrayList;
import java.util.List;

import domesticenv.HouseModel;
import eis.eis2java.annotation.AsAction;
import eis.eis2java.annotation.AsPercept;
import eis.eis2java.translation.Filter;
import jason.environment.grid.Location;

/**
 * Robot is an EIS entity. Its job is to order beer to get the fridge filled,
 * pick up the beers from the fridge and deliver it to the owner.
 */
public class Robot {
	private final HouseModel house;

	public Robot(final HouseModel env) {
		this.house = env;
	}

	@AsAction(name = "open")
	public void open(final String what) {
		if (what.equals("fridge")) {
			this.house.openFridge();
		} else {
			throw new IllegalArgumentException("robot can not open '" + what + "'");
		}
	}

	@AsAction(name = "get")
	public void get(final String what) {
		if (what.equals("beer")) {
			this.house.getBeer();
		} else {
			throw new IllegalArgumentException("robot can not get '" + what + "'");
		}
	}

	@AsAction(name = "close")
	public void close(final String what) {
		if (what.equals("fridge")) {
			this.house.closeFridge();
		} else {
			throw new IllegalArgumentException("robot can not close '" + what + "'");
		}
	}

	@AsAction(name = "hand_in")
	public void hand_in(final String what) {
		if (what.equals("beer")) {
			this.house.handInBeer();
		} else {
			throw new IllegalArgumentException("robot can not hand in '" + what + "'");
		}
	}

	@AsAction(name = "move_towards")
	public void move_towards(final String target) {
		Location dest = null;
		if (target.equals("fridge")) {
			dest = this.house.lFridge;
		} else if (target.equals("owner")) {
			dest = this.house.lOwner;
		}
		this.house.moveTowards(dest);
	}

	@AsPercept(name = "at", filter = Filter.Type.ALWAYS, multipleArguments = true)
	public List<String> at() {
		final Location lRobot = this.house.getRobotLocation();
		final List<String> params = new ArrayList<>(2);
		params.add("robot");
		if (lRobot.equals(this.house.lFridge)) {
			params.add("fridge");
			return params;
		} else if (lRobot.equals(this.house.lOwner)) {
			params.add("owner");
			return params;
		} else {
			return null;
		}
	}

	@AsPercept(name = "stock", filter = Filter.Type.ALWAYS, multipleArguments = true)
	public List<Object> stock() {
		if (this.house.isFridgeOpen()) {
			final List<Object> params = new ArrayList<>(2);
			params.add("beer");
			params.add(this.house.getAvailableBeers());
			return params;
		} else {
			return null; // no percept.
		}
	}

	@AsPercept(name = "has", filter = Filter.Type.ALWAYS, multipleArguments = true)
	public List<String> has() {
		if (this.house.getSipCount() > 0) {
			final List<String> params = new ArrayList<>(2);
			params.add("owner");
			params.add("beer");
			return params;
		} else {
			return null; // no percept.
		}
	}
}
