package poo.circuitboard.cell;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import poo.lib.Direction;
import poo.lib.tile.Tile;
/**
 * Created by Rasta Smurf on 22-Dez-2014.
 */
public abstract class Piece implements Tile
{

	public class Link		/* Not having a link means direction = null */
	{
		public Piece piece;
//		public Direction direction = Direction.get (1,0);
		public Direction direction;
		public boolean hasLink = false;

		public boolean hasLink() { return this.hasLink; }
		public boolean canLink(Direction direction)
		{
			return false;
		}
		public void removeLink()
		{
			direction = null;
			hasLink = false;
		}
	}

	public Link linkTo;
	public Link linkFrom;
	public boolean isLinked = false;
	public boolean isLinked() { return isLinked; }

	public void setLinkStart() {};
	public void setLinkTo(Direction direction, Piece piece){};
	public void setLinkFrom(Direction direction, Piece piece){};
	public abstract boolean canLink(Direction direction, Piece piece);
	public void removeLink() {}

	private int x;
	public int getX() { return this.x; }
	private int y;
	public int getY() { return this.y; }
	protected int color;

	protected static Paint paint;

	public int getColor()	{ return this.color; }

	public Piece(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.paint = new Paint();
	}

//	@Override
//	public abstract void draw(Canvas canvas, int side);

	@Override
	public void draw(Canvas canvas, int side)
	{
//		if (isAnimated)
//		{
//			int green = Color.argb(255, 0, 100, 0);
//			paint.setColor(green);
//			canvas.drawRect(0, 0, side, side, paint);
//		}
	}

//	@Override
//	public boolean setSelect(boolean selected)
//	{
//		return isLinked = selected;
//	}

	@Override
	public boolean setSelect(boolean selected)
	{
		return selected;
	}

}
