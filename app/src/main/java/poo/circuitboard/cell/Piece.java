package poo.circuitboard.cell;

import android.graphics.Canvas;
import android.graphics.Paint;

import poo.lib.tile.Tile;
/**
 * Created by Rasta Smurf on 22-Dez-2014.
 */
public abstract class Piece implements Tile
{
	protected static Paint paint;

	public Piece()
	{
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
