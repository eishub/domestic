package domesticenv;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import jason.environment.grid.GridWorldView;
import jason.environment.grid.Location;

/** class that implements the View of Domestic Robot application */
public class HouseView extends GridWorldView {
	private static final long serialVersionUID = 1L;
	private final HouseModel hmodel;

	public HouseView(final HouseModel model) {
		super(model, "Domestic Robot", 700);
		this.hmodel = model;
		this.defaultFont = new Font("Arial", Font.BOLD, 16); // change default font
		setVisible(true);
		repaint();
	}

	/** draw application objects */
	@Override
	public void draw(final Graphics g, final int x, final int y, final int object) {
		final Location lRobot = this.hmodel.getAgPos(0);
		super.drawAgent(g, x, y, Color.lightGray, -1);
		switch (object) {
		case HouseModel.FRIDGE:
			if (lRobot.equals(this.hmodel.lFridge)) {
				super.drawAgent(g, x, y, Color.yellow, -1);
			}
			g.setColor(Color.black);
			drawString(g, x, y, this.defaultFont, "Fridge (" + this.hmodel.availableBeers + ")");
			break;
		case HouseModel.OWNER:
			if (lRobot.equals(this.hmodel.lOwner)) {
				super.drawAgent(g, x, y, Color.yellow, -1);
			}
			String o = "Owner";
			if (this.hmodel.sipCount > 0) {
				o += " (" + this.hmodel.sipCount + ")";
			}
			g.setColor(Color.black);
			drawString(g, x, y, this.defaultFont, o);
			break;
		}
	}

	@Override
	public void drawAgent(final Graphics g, final int x, final int y, Color c, final int id) {
		final Location lRobot = this.hmodel.getAgPos(0);
		if (!lRobot.equals(this.hmodel.lOwner) && !lRobot.equals(this.hmodel.lFridge)) {
			c = Color.yellow;
			if (this.hmodel.carryingBeer) {
				c = Color.orange;
			}
			super.drawAgent(g, x, y, c, -1);
			g.setColor(Color.black);
			super.drawString(g, x, y, this.defaultFont, "Robot");
		}
	}
}
