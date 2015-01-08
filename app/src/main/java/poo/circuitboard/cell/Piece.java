package poo.circuitboard.cell;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import poo.lib.tile.Tile;
/**
 * Created by Rasta Smurf on 22-Dez-2014.
 */
public abstract class Piece implements Tile
{
	protected int x;
	protected int y;
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
		if (isAnimated)
		{
			int green = Color.argb(255, 0, 100, 0);
			paint.setColor(green);
			canvas.drawRect(0, 0, side, side, paint);
		}
	}

	@Override
	public boolean setSelect(boolean selected)
	{
		return isAnimated = selected;
	}
}
