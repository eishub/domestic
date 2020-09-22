package domesticenv.entities;

import domesticenv.HouseModel;
import eis.eis2java.annotation.AsAction;

/**
 * The supermarket entity can deliver free beer on request.
 */
public class Supermarket {
	private final HouseModel house;

	public Supermarket(final HouseModel model) {
		this.house = model;
	}

	@AsAction(name = "deliver")
	public void deliver(final String what, final Integer n) {
		if (!what.equals("beer")) {
			throw new IllegalArgumentException("supermarket can not deliver '" + what + "'");
		} else if (n < 1 || n > 100) {
			throw new IllegalArgumentException("supermarket can deliver between 1 and 100 beers, not " + n);
		} else {
			this.house.addBeer(n);
		}
	}
}
