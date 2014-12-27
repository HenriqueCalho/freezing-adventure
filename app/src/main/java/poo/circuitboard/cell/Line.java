package poo.circuitboard.cell;

import android.graphics.Canvas;
import android.graphics.Color;

/**
 * Created by Rasta Smurf on 22-Dez-2014.
 */
public class Line extends Piece
{
	public Line()
	{

	}

	@Override
	public void draw(Canvas canvas, int side)
	{
		paint.setColor(Color.BLACK);
		canvas.drawRect(0, side/3, side, 2*side/3, paint);
	}
}
