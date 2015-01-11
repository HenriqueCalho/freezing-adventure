package poo.circuitboard.cell;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import poo.lib.Direction;

/**
 * Created by Rasta Smurf on 22-Dez-2014.
 */
public class Tail extends Piece
{
//	private int color;
//	public int getColor(){return this.color;}

	private Link linkTo;

	public Tail(int x, int y, int color)
	{
		super(x,y);
		this.color = color;
		this.linkTo = new Link();
	}

	public void setLinkTo(Direction direction)
	{
		this.linkTo.direction = direction;
		linkTo.hasLink = true;
	}

	@Override
	public void draw(Canvas canvas, int side)
	{
		super.draw(canvas,side);
		paint.setColor(this.color);
		canvas.drawCircle(side/2, side/2, side/3, paint);
		paint.setColor(Color.BLACK);
		canvas.drawCircle(side/2, side/2, side/6, paint);

		if (this.linkTo.hasLink())
		{
			paint.setColor(this.color);
			paint.setStrokeWidth(2*side/6);
			canvas.drawLine(side/2, side/2, side/2 + side/2*linkTo.direction.dx, side/2 + side/2*linkTo.direction.dy, paint);
		}
	}

}
