package poo.circuitboard.cell;

import android.graphics.Canvas;
import android.graphics.Color;

/**
 * Created by Rasta Smurf on 22-Dez-2014.
 */
public class Dot extends  Piece
{
	public Dot()
	{

	}

	@Override
	public void draw(Canvas canvas, int side)
	{
		paint.setColor(Color.BLACK);
		canvas.drawCircle(side/2, side/2, side/6, paint);
	}
}
