package domesticenv.entities;

import domesticenv.HouseModel;
import eis.eis2java.annotation.AsAction;

/**
 * The supermarket entity can deliver free beer on request.
 * 
 * @author W.Pasman 2oct14
 *
 */
public class Supermarket {

	private HouseModel house;

	public Supermarket(HouseModel model) {
		house = model;
	}

	@AsAction(name = "deliver")
	public void deliver(String what, Integer n) {
		if (!what.equals("beer")) {
			throw new IllegalArgumentException("supermarket can not deliver '"
					+ what + "'");
		}
		if (n < 1 || n > 100) {
			throw new IllegalArgumentException(
					"supermarket can deliver between 1 and 100 beers, not " + n);
		}
		house.addBeer(n);

	}

}
