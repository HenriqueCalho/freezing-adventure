package poo.circuitboard.cell;

import android.graphics.Canvas;
import android.graphics.Color;

import poo.lib.Direction;

/**
 * Created by Rasta Smurf on 22-Dez-2014.
 */
public class Line extends Piece
{
	/* allowed directions */
	private Direction d1;
	private Direction d2;

	public Line(int x, int y)
	{
		super(x,y);
		this.color = Color.BLACK;
	}

	@Override
	public void draw(Canvas canvas, int side)
	{
		super.draw(canvas,side);
		paint.setColor(Color.BLACK);
		canvas.drawRect(0, side/3, side, 2*side/3, paint);
	}
}
