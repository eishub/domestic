package domesticenv;

import javax.swing.SwingUtilities;

/**
 * The house env. Contains a model and possibly a view on the model.
 * 
 * @author W.Pasman 2oct14
 *
 */
public class HouseEnv {

	HouseModel model = null; // the model of the grid
	HouseView view = null;

	public void init(boolean showGUI) {
		model = new HouseModel();
		if (showGUI) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (view != null) { // in case init is called twice
						view.setVisible(false);
						view.dispose();
					}
					view = new HouseView(model);
					model.setView(view);

				}
			});
		}
	}

	public void stop() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (view != null) {
					view.setVisible(false);
					view.dispose();
				}

			}
		});
	}
}
