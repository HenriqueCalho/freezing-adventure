package poo.circuitboard.cell;

import android.graphics.Canvas;
import android.graphics.Color;

/**
 * Created by Rasta Smurf on 22-Dez-2014.
 */
public class Column extends Piece
{
	public Column(int x, int y)
	{
		super(x,y);
		this.color = Color.BLACK;
	}

	@Override
	public void draw(Canvas canvas, int side)
	{
		super.draw(canvas,side);
		paint.setColor(Color.BLACK);
		canvas.drawRect(side/3, 0, 2*side/3, side, paint);
	}
}
