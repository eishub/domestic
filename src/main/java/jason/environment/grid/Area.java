package jason.environment.grid;

import java.io.Serializable;

public final class Area implements Serializable {
	private static final long serialVersionUID = 1L;
	public final Location tl, br;

	public Area(final int topLeftX, final int topLeftY, final int bottonRightX, final int bottonRightY) {
		this.tl = new Location(topLeftX, topLeftY);
		this.br = new Location(bottonRightX, bottonRightY);
	}

	public Area(final Location topLeft, final Location bottonRight) {
		this.tl = topLeft;
		this.br = bottonRight;
	}

	public boolean contains(final Location l) {
		return l.x >= this.tl.x && l.x <= this.br.x && l.y >= this.tl.y && l.y <= this.br.y;
	}

	public Location center() {
		return new Location((this.tl.x + this.br.x) / 2, (this.tl.y + this.br.y) / 2);
	}

	/** @deprecated renamed to chebyshevDistanceToBorder */
	@Deprecated
	public int distanceMaxBorder(final Location l) {
		return chebyshevDistanceToBorder(l);
	}

	/** returns the minimal distance from <i>l</i> to the border of the area */
	public int chebyshevDistanceToBorder(final Location l) {
		if (contains(l)) {
			return 0;
		} else {
			final int x = Math.min(Math.abs(this.tl.x - l.x), Math.abs(this.br.x - l.x));
			final int y = Math.min(Math.abs(this.tl.y - l.y), Math.abs(this.br.y - l.y));
			return Math.max(x, y);
		}
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + this.tl.x + this.tl.y;
		result = PRIME * result + this.br.x + this.br.y;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		} else if (this == obj) {
			return true;
		} else if (getClass() != obj.getClass()) {
			return false;
		}
		final Area other = (Area) obj;
		if (!this.tl.equals(other.tl)) {
			return false;
		} else if (!this.br.equals(other.br)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Object clone() {
		return new Area(this.tl, this.br);
	}

	@Override
	public String toString() {
		return (this.tl + ":" + this.br);
	}
}
