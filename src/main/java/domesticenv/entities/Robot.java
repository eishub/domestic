package domesticenv.entities;

import jason.environment.grid.Location;

import java.util.ArrayList;
import java.util.List;

import domesticenv.HouseModel;
import eis.eis2java.annotation.AsAction;
import eis.eis2java.annotation.AsPercept;
import eis.eis2java.translation.Filter;

/**
 * 
 * Robot is an EIS entity. Its job is to order beer to get the fridge filled,
 * pick up the beers from the fridge and deliver it to the owner.
 * 
 * 
 * @author W.Pasman 2oct14
 *
 */
public class Robot {
	HouseModel house;

	public Robot(HouseModel env) {
		house = env;
	}

	@AsAction(name = "open")
	public void open(String what) {

		if (!what.equals("fridge")) {
			throw new IllegalArgumentException("robot can not open '" + what
					+ "'");
		}
		house.openFridge();
	}

	@AsAction(name = "get")
	public void get(String what) {

		if (!what.equals("beer")) {
			throw new IllegalArgumentException("robot can not get '" + what
					+ "'");
		}
		house.getBeer();
	}

	@AsAction(name = "close")
	public void close(String what) {

		if (!what.equals("fridge")) {
			throw new IllegalArgumentException("robot can not close '" + what
					+ "'");
		}
		house.closeFridge();
	}

	@AsAction(name = "hand_in")
	public void hand_in(String what) {

		if (!what.equals("beer")) {
			throw new IllegalArgumentException("robot can not hand in '" + what
					+ "'");
		}
		house.handInBeer();
	}

	@AsAction(name = "move_towards")
	public void move_towards(String target) {

		Location dest = null;
		if (target.equals("fridge")) {
			dest = house.lFridge;
		} else if (target.equals("owner")) {
			dest = house.lOwner;
		}
		house.moveTowards(dest);
	}

	@AsPercept(name = "at", filter = Filter.Type.ALWAYS, multipleArguments = true)
	public List<String> at() {
		Location lRobot = house.getRobotLocation();
		List<String> params = new ArrayList<String>();
		params.add("robot");
		if (lRobot.equals(house.lFridge)) {
			params.add("fridge");
			return params;
		}
		if (lRobot.equals(house.lOwner)) {
			params.add("owner");
			return params;
		}
		return null;
	}

	@AsPercept(name = "stock", filter = Filter.Type.ALWAYS, multipleArguments = true)
	public List<Object> stock() {

		if (house.isFridgeOpen()) {
			List<Object> params = new ArrayList<Object>();
			params.add("beer");
			params.add((Integer) house.getAvailableBeers());
			return params;
		}

		return null; // no percept.
	}

	@AsPercept(name = "has", filter = Filter.Type.ALWAYS, multipleArguments = true)
	public List<String> has() {
		if (house.getSipCount() > 0) {
			List<String> params = new ArrayList<String>();
			params.add("owner");
			params.add("beer");
			return params;
		}

		return null; // no percept.
	}

}
