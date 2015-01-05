package poo.circuitboard.cell;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by Rasta Smurf on 22-Dez-2014.
 */
public class Tail extends Piece
{
	private int color;

//	public int getColor(){return this.color;}

	public Tail(int x, int y, int color)
	{
		super(x,y);
		this.color = color;
	}

	@Override
	public void draw(Canvas canvas, int side)
	{
		paint.setColor(this.color);
		canvas.drawCircle(side/2, side/2, side/3, paint);
		paint.setColor(Color.BLACK);
		canvas.drawCircle(side/2, side/2, side/6, paint);
	}

}
