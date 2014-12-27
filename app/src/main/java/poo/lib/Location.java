package poo.lib;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Identifies a two-dimensional location (x,y)
 * @author Palex
 */
public class Location {
	public int x;  // valid = [0..MAX_X[
	public int y;  // valid = [0..MAX_Y[

	public Location(int x, int y) { this.x=x; this.y=y;	}
	public Location clone() { return new Location(x,y); }

	/**
	 * Advances location in the indicated direction
	 * @param dir The forward direction
	 */
	public void step(Direction dir) { step(dir.dx,dir.dy); }

	/**
	 * Advances location by dx and dy
	 * @param dx value to add x
	 * @param dy value to add y
	 */
	public void step(int dx, int dy) {
		y += dy;
		x += dx;
	}
	/**
	 * Returns a new location equal to the current, but advanced in the indicated direction
	 * @param dir The forward direction
	 * @return The new location
	 */
	public Location add(Direction dir) {
		Location res = new Location(x, y);
		res.step(dir);
		return res;
	}
	/**
	 * Indicates if the location is valid. x=[0..MAX_X-1] and y=[0..MAX_Y-1]
	 * @return true if is a valid location
	 */
	public boolean valid() {
		return y>=0 && y<MAX_Y && x>=0 && x<MAX_X;
	}

	public String toString() { return "("+x+','+y+")"; }
	
	/**
	 * Limits of positions
	 * Values defined by client:  Location.MAX_X = ...
	 */
	private static int MAX_X=-1, MAX_Y=-1;
	
	public static void setMAX(int x, int y) {
		MAX_X = x;
		MAX_Y = y;
		EVERY = EVERY_ITERABLE;
	}
	
	private static Iterable<Location> EVERY_ITERABLE = new Iterable<Location>() {
		public Iterator<Location> iterator() {
			LinkedList<Location> lst = new LinkedList<>();
			for(int x=0 ; x<MAX_X ; ++x)
				for(int y=0 ; y<MAX_Y ; ++y)
					lst.add( new Location(x,y) );
			EVERY = Collections.unmodifiableList(lst);				
			return EVERY.iterator();
		}
	};
	
	/**
	 * Iterable to get all the valid locations.<br/>
	 * The locations are traversed from left to right and top to bottom.<br/>
	 * EXAMPLE TO USE:   for(Location l : Location.EVERY) System.out.println(l);
	 */
	public static Iterable<Location> EVERY = null;
	
	/**
	 * Try advance in first direction and than in second direction 
	 * if after the first results in a invalid location.
	 * @param d1 First direction
	 * @param d2 Second direction
	 */
	public void step(Direction d1, Direction d2) {
		step(d1);
		if (!valid()) {
			if (d1.dx<0 && x<0) x=MAX_X-1; 
			else if (d1.dx>0 && x>=MAX_X) x=0; 
			else if (d1.dy<0 && y<0) y=MAX_Y-1; 
			else if (d1.dy>0 && y>=MAX_Y) y=0;
			step(d2);
		}
	}

	/**
	 * Advance the locations from left to right and than from top to bottom.<br/>
	 * Equivalent to:  <code>step(Direction.RIGHT,Direction.DOWN)</code>
	 */
	public void next() {
		++x;
		if (x>=MAX_X) {	x = 0; ++y;	}
	}	
	
	public boolean equals(Location l) { return x==l.x && y==l.y ; }
}
