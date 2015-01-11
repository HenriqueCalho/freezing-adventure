package poo.lib;

import android.util.Log;

import java.util.Random;

/**
 * Enumerated of directions (LEFT,UP,RIFHT,DOWN)
 * @author Palex
 */
public enum Direction {
  
	LEFT(-1,0), UP(0,-1), RIGHT(+1,0), DOWN(0,+1);
	
	/**
	 * Delta x - RIGHT.dx=1 ; LEFT.dx=-1 ; UP.dx=DOWN.dx=0
	 */
	public final int dx;
	/**
	 * Delta y - DOWN.dy=1 ; UP.dy=-1 ; RIGHT.dy=LEFT.dy=0 
	 */
	public final int dy;

	private static final int LEN = values().length;
	
	private Direction(int dx, int dy) {
		this.dx=dx; this.dy=dy;
	}
	
	/**
	 * Return next direction in clockwise
	 * @param n
	 * @return next direction
	 */
	public Direction next(int n) {
		return values()[(ordinal()+n)%LEN];
	}
	/**
	 * Return next direction in counter clockwise
	 * @param n
	 * @return next direction
	 */
	public Direction prev(int n) { return next(LEN-n); }
	/**
	 * Return the opposite direction. Same as next(2)
	 * @return
	 */
	public Direction opposite() { return next(+2); }
	
	private static Random rnd = new Random();
	
	/**
	 * Return a random direction.
	 * @return
	 */
	public static Direction random() {
		return values()[rnd.nextInt(LEN)];
	}
	
	/*
	 * Returns a random direction that is different from that indicated
	 */
	public static Direction random(Direction except) {
		int r = rnd.nextInt(LEN-1);
		for(Direction d : values()) {
			if (d==except) continue;
			if (r==0) return d;
			--r;
		}
		return null;
	}

	/*
	 * Returns the direction that has the offsets indicated
	 */
	public static Direction get(int dx, int dy)
	{
		if (dx > 0) dx = +1;
		if (dx < 0) dx = -1;
		if (dy > 0) dy = +1;
		if (dy < 0) dy = -1;

		for(Direction d : values())
			if( d.equals(dx, dy) )	return d;
		return null;
	}

	public boolean equals(int dx, int dy) {
		return this.dx==dx && this.dy==dy;
	}
	
	public boolean equals(Direction d) {
		return equals(d.dx, d.dy);
	}
}
