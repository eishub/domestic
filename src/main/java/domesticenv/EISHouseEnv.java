package domesticenv;

import java.util.Map;

import domesticenv.entities.Owner;
import domesticenv.entities.Robot;
import domesticenv.entities.Supermarket;
import eis.eis2java.environment.AbstractEnvironment;
import eis.exceptions.EntityException;
import eis.exceptions.ManagementException;
import eis.iilang.Action;
import eis.iilang.EnvironmentState;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

/**
 * The EIS interface for the house environment.
 */
public class EISHouseEnv extends AbstractEnvironment {
	private static final long serialVersionUID = 4559965867901684470L;
	private HouseEnv env = null;

	@Override
	public void init(final Map<String, Parameter> parameters) throws ManagementException {
		// Prepare the game.
		reset(parameters);

		try { // Try creating and registering the entities.
			registerEntity("robot", new Robot(this.env.model));
			registerEntity("owner", new Owner(this.env.model));
			registerEntity("supermarket", new Supermarket(this.env.model));
		} catch (final EntityException e) {
			throw new ManagementException("Could not create an entity", e);
		}
	}

	@Override
	public void reset(final Map<String, Parameter> parameters) throws ManagementException {
		closeOldEnv();
		this.env = new HouseEnv();
		final Parameter gui = parameters.get("gui");
		final boolean guiOff = gui != null && gui instanceof Identifier && ((Identifier) gui).getValue().equals("off");
		this.env.init(!guiOff);

		setState(EnvironmentState.PAUSED);
	}

	@Override
	public void kill() throws ManagementException {
		setState(EnvironmentState.KILLED);
		closeOldEnv();
	}

	private void closeOldEnv() {
		if (this.env != null) {
			this.env.stop();
			this.env = null;
		}
	}

	@Override
	protected boolean isSupportedByEnvironment(final Action arg0) {
		return true;
	}

	@Override
	protected boolean isSupportedByType(final Action arg0, final String arg1) {
		return true;
	}
}
