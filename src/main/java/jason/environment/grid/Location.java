package jason.environment.grid;

import java.io.Serializable;

public final class Location implements Serializable {
	private static final long serialVersionUID = 1L;

	public int x, y;

	public Location(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	/** calculates the Manhattan distance between two points */
	public int distanceManhattan(final Location l) {
		return Math.abs(this.x - l.x) + Math.abs(this.y - l.y);
	}

	/** calculates the Manhattan distance between two points */
	public int distance(final Location l) {
		return Math.abs(this.x - l.x) + Math.abs(this.y - l.y);
	}

	/** calculates the Euclidean distance between two points */
	public double distanceEuclidean(final Location l) {
		return Math.sqrt(Math.pow(this.x - l.x, 2) + Math.pow(this.y - l.y, 2));
	}

	/**
	 * returns the chessboard king (or Chebyshev) distance between two locations :
	 * max( |this.x - l.x| , |this.y - l.y|)
	 */
	public int distanceChebyshev(final Location l) {
		return Math.max(Math.abs(this.x - l.x), Math.abs(this.y - l.y));
	}

	/** @deprecated renamed to distanceChessboard */
	@Deprecated
	public int maxBorder(final Location l) {
		return Math.max(Math.abs(this.x - l.x), Math.abs(this.y - l.y));
	}

	public boolean isInArea(final Location tl, final Location br) {
		return this.x >= tl.x && this.x <= br.x && this.y >= tl.y && this.y <= br.y;
	}

	public boolean isInArea(final Area a) {
		return a.contains(this);
	}

	public boolean isNeigbour(final Location l) {
		return distance(l) == 1 || equals(l) || Math.abs(this.x - l.x) == 1 && Math.abs(this.y - l.y) == 1;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + this.x;
		result = PRIME * result + this.y;
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
		final Location other = (Location) obj;
		if (this.x != other.x) {
			return false;
		} else if (this.y != other.y) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Object clone() {
		return new Location(this.x, this.y);
	}

	@Override
	public String toString() {
		return (this.x + "," + this.y);
	}
}
