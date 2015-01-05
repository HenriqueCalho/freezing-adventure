package poo.circuitboard.cell;

import android.graphics.Canvas;
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
	protected static Paint paint;

	public int getColor()	{ return this.color; }

	public Piece(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.paint = new Paint();
	}

	@Override
	public abstract void draw(Canvas canvas, int side);

	@Override
	public boolean setSelect(boolean selected)
	{
		return false;
	}
}
