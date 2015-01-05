package poo.lib.tile;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import poo.circuitboard.cell.Piece;

/**
 * Base type for each animation.
 * @author Palex
 */
public class AnimTile {

//	private static Paint paint = new Paint();
	/**
	 * Tiles to animate
	 */
	protected Tile tile1;
	protected Tile tile2;
	/**
	 * Tiles current positions in this animation.
	 * In pixel units.
	 */
	protected int x1,y1;
	protected int x2,y2;
	/**
	 * Steps remaining to complete animation
	 */
	protected int steps;
	/**
	 * Animation started after this
	 */
	protected AnimTile after;
	
	/**
	 * Constructs an animation to the tile of the indicated position
	 * @param x1 Column of the tile
	 * @param y1 Line of the tile
	 * @param time Total time of the animation 
	 * @param p TilePanel where the animation runs
	 */
	public AnimTile(int x1, int y1, int time, TilePanel p, int x2, int y2) {
		Rect r1 = p.tileRect(x1, y1);
		this.x1 = r1.left; this.y1 = r1.top;
		tile1 = p.getTile(x1, y1);
		steps = Math.max(time/Animator.STEP_TIME,1);

		Rect r2 = p.tileRect(x2, y2);
		this.x2 = r2.left; this.y2 = r2.top;
		tile2 = p.getTile(x2, y2);
	}
	/**
	 * Called by the animator in each step of the animation.
	 * Must be invoked to redefine this method in subclasses.
	 * @param cv 
	 * @param side
	 */
//	public void stepDraw(Canvas cv, int side) {
//		cv.save();
//		cv.clipRect(x,y,x+side,y+side);
//		cv.translate(x,y);
//		tile.draw(cv,side);
//		cv.restore();
//	}

	public void stepDraw(Canvas cv, int side, Paint paint) {
		cv.save();
		cv.clipRect(x1,y1,x1+side,y1+side);
		cv.translate(x1,y1);
		tile1.draw(cv,side);
		cv.restore();

//		cv.drawRect(x1+side/3, y1+side/2, x1+2*side/3, y1+3*side/2, new Paint());
		paint.setStrokeWidth(2*side/6);
		cv.drawLine(x1+side/2, y1+side/2, x2+side/2, y2+side/2, paint);


	}
}