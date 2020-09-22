package jason.environment.grid;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JFrame;

/**
 * View component for a GirdWorldModel.
 */
public class GridWorldView extends JFrame {
	private static final long serialVersionUID = 1L;

	protected int cellSizeW = 0;
	protected int cellSizeH = 0;

	protected GridCanvas drawArea;
	protected GridWorldModel model;

	protected Font defaultFont = new Font("Arial", Font.BOLD, 10);

	public GridWorldView(final GridWorldModel model, final String title, final int windowSize) {
		super(title);
		this.model = model;
		initComponents(windowSize);
		model.setView(this);
	}

	/** sets the size of the frame and adds the components */
	public void initComponents(final int width) {
		setSize(width, width);
		getContentPane().setLayout(new BorderLayout());
		this.drawArea = new GridCanvas();
		getContentPane().add(BorderLayout.CENTER, this.drawArea);
	}

	@Override
	public void repaint() {
		this.cellSizeW = this.drawArea.getWidth() / this.model.getWidth();
		this.cellSizeH = this.drawArea.getHeight() / this.model.getHeight();
		super.repaint();
		this.drawArea.repaint();
	}

	/** updates all the frame */
	public void update() {
		repaint();
	}

	/** updates only one position of the grid */
	public void update(final int x, final int y) {
		final Graphics g = this.drawArea.getGraphics();
		if (g != null) {
			drawEmpty(g, x, y);
			draw(g, x, y);
		}
	}

	public void drawObstacle(final Graphics g, final int x, final int y) {
		g.setColor(Color.darkGray);
		g.fillRect(x * this.cellSizeW + 1, y * this.cellSizeH + 1, this.cellSizeW - 1, this.cellSizeH - 1);
		g.setColor(Color.black);
		g.drawRect(x * this.cellSizeW + 2, y * this.cellSizeH + 2, this.cellSizeW - 4, this.cellSizeH - 4);
	}

	public void drawAgent(final Graphics g, final int x, final int y, final Color c, final int id) {
		g.setColor(c);
		g.fillOval(x * this.cellSizeW + 2, y * this.cellSizeH + 2, this.cellSizeW - 4, this.cellSizeH - 4);
		if (id >= 0) {
			g.setColor(Color.black);
			drawString(g, x, y, this.defaultFont, String.valueOf(id + 1));
		}
	}

	public void drawString(final Graphics g, final int x, final int y, final Font f, final String s) {
		g.setFont(f);
		final FontMetrics metrics = g.getFontMetrics();
		final int width = metrics.stringWidth(s);
		final int height = metrics.getHeight();
		g.drawString(s, x * this.cellSizeW + (this.cellSizeW / 2 - width / 2),
				y * this.cellSizeH + (this.cellSizeH / 2 + height / 2));
	}

	public void drawEmpty(final Graphics g, final int x, final int y) {
		g.setColor(Color.white);
		g.fillRect(x * this.cellSizeW + 1, y * this.cellSizeH + 1, this.cellSizeW - 1, this.cellSizeH - 1);
		g.setColor(Color.lightGray);
		g.drawRect(x * this.cellSizeW, y * this.cellSizeH, this.cellSizeW, this.cellSizeH);
	}

	/**
	 * method to draw unknown object, probably overridden by the user viewer class
	 */
	public void draw(final Graphics g, final int x, final int y, final int object) {
		// g.setColor(Color.black);
		// drawString(g,x,y,defaultFont,String.valueOf(object));
	}

	private static int limit = (int) Math.pow(2, 14);

	private void draw(final Graphics g, final int x, final int y) {
		if ((this.model.data[x][y] & GridWorldModel.OBSTACLE) != 0) {
			drawObstacle(g, x, y);
		}

		int vl = GridWorldModel.OBSTACLE * 2;
		while (vl < limit) {
			if ((this.model.data[x][y] & vl) != 0) {
				draw(g, x, y, vl);
			}
			vl *= 2;
		}

		if ((this.model.data[x][y] & GridWorldModel.AGENT) != 0) {
			drawAgent(this.drawArea.getGraphics(), x, y, Color.blue, this.model.getAgAtPos(x, y));
		}
	}

	public Canvas getCanvas() {
		return this.drawArea;
	}

	public GridWorldModel getModel() {
		return this.model;
	}

	private final class GridCanvas extends Canvas {
		private static final long serialVersionUID = 1L;

		@Override
		public void paint(final Graphics g) {
			GridWorldView.this.cellSizeW = GridWorldView.this.drawArea.getWidth() / GridWorldView.this.model.getWidth();
			GridWorldView.this.cellSizeH = GridWorldView.this.drawArea.getHeight()
					/ GridWorldView.this.model.getHeight();
			final int mwidth = GridWorldView.this.model.getWidth();
			final int mheight = GridWorldView.this.model.getHeight();

			g.setColor(Color.lightGray);
			for (int l = 1; l <= mheight; l++) {
				g.drawLine(0, l * GridWorldView.this.cellSizeH, mwidth * GridWorldView.this.cellSizeW,
						l * GridWorldView.this.cellSizeH);
			}
			for (int c = 1; c <= mwidth; c++) {
				g.drawLine(c * GridWorldView.this.cellSizeW, 0, c * GridWorldView.this.cellSizeW,
						mheight * GridWorldView.this.cellSizeH);
			}

			for (int x = 0; x < mwidth; x++) {
				for (int y = 0; y < mheight; y++) {
					draw(g, x, y);
				}
			}
		}
	}
}
