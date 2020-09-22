package domesticenv;

import javax.swing.SwingUtilities;

/**
 * The house env. Contains a model and possibly a view on the model.
 */
public class HouseEnv {
	protected HouseModel model = null; // the model of the grid
	protected HouseView view = null;

	public void init(final boolean showGUI) {
		this.model = new HouseModel();
		if (showGUI) {
			SwingUtilities.invokeLater(() -> {
				if (HouseEnv.this.view != null) { // in case init is called twice
					HouseEnv.this.view.setVisible(false);
					HouseEnv.this.view.dispose();
				}
				HouseEnv.this.view = new HouseView(HouseEnv.this.model);
				HouseEnv.this.model.setView(HouseEnv.this.view);
			});
		}
	}

	public void stop() {
		SwingUtilities.invokeLater(() -> {
			if (HouseEnv.this.view != null) {
				HouseEnv.this.view.setVisible(false);
				HouseEnv.this.view.dispose();
			}
		});
	}
}
