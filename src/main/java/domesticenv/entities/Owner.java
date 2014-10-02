package domesticenv.entities;

import java.util.ArrayList;
import java.util.List;

import domesticenv.HouseModel;
import eis.eis2java.annotation.AsAction;
import eis.eis2java.annotation.AsPercept;
import eis.eis2java.translation.Filter;

/**
 * The owner does just one thing for living - sip beer.
 * 
 * @author W.Pasman 2oct14
 *
 */
public class Owner {

	private HouseModel house;

	public Owner(HouseModel env) {
		house = env;
	}

	@AsAction(name = "sip")
	public void hand_in(String what) {

		if (!what.equals("beer")) {
			throw new IllegalArgumentException("owner can not sip '" + what
					+ "'");
		}
		house.sipBeer();
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
