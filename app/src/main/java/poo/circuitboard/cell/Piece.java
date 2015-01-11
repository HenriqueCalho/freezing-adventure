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

	public class Link
	{
		public Piece piece;
//		public Direction direction = Direction.get(1,0);
		public Direction direction;
		public boolean hasLink = false;

		public boolean hasLink() { return this.hasLink; }
		public boolean canLink(Direction direction)
		{
			return false;
		}
	}

	public void setLinkTo(Direction direction){};
	public void setLinkFrom(Direction direction, Piece piece){};

	private int x;
	public int getX() { return this.x; }
	private int y;
	public int getY() { return this.y; }
	protected int color;
	protected boolean isAnimated = false;
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

	@Override
	public boolean setSelect(boolean selected)
	{
		return isAnimated = selected;
	}
}
