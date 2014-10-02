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
 * The EIS interface for the house environment
 * 
 * @author W.Pasman 2oct2014
 *
 */
public class EISHouseEnv extends AbstractEnvironment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4559965867901684470L;

	HouseEnv env = null;

	@Override
	public void init(Map<String, Parameter> parameters)
			throws ManagementException {
		// Prepare the game.
		reset(parameters);

		// Try creating and registering an entity.
		try {
			registerEntity("robot", new Robot(env.model));
			registerEntity("owner", new Owner(env.model));
			registerEntity("supermarket", new Supermarket(env.model));
		} catch (EntityException e) {
			throw new ManagementException("Could not create an entity", e);
		}
	}

	public void reset(Map<String, Parameter> parameters)
			throws ManagementException {
		closeOldEnv();
		env = new HouseEnv();
		Parameter gui = parameters.get("gui");
		boolean guiOff = gui != null && gui instanceof Identifier
				&& ((Identifier) gui).getValue().equals("off");
		env.init(!guiOff);

		setState(EnvironmentState.PAUSED);
	}

	@Override
	public void kill() throws ManagementException {
		setState(EnvironmentState.KILLED);
		closeOldEnv();
	}

	private void closeOldEnv() {
		if (env != null) {
			env.stop();
			env = null;
		}
	}

	@Override
	protected boolean isSupportedByEnvironment(Action arg0) {
		return true;
	}

	@Override
	protected boolean isSupportedByType(Action arg0, String arg1) {
		return true;
	}

}
