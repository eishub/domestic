package domesticenv.entities;

import java.util.ArrayList;
import java.util.List;

import domesticenv.HouseModel;
import eis.eis2java.annotation.AsAction;
import eis.eis2java.annotation.AsPercept;
import eis.eis2java.translation.Filter;

/**
 * The owner does just one thing for living - sip beer.
 */
public class Owner {
	private final HouseModel house;

	public Owner(final HouseModel env) {
		this.house = env;
	}

	@AsAction(name = "sip")
	public void hand_in(final String what) {
		if (what.equals("beer")) {
			this.house.sipBeer();
		} else {
			throw new IllegalArgumentException("owner can not sip '" + what + "'");
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
