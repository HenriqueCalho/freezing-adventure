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

	private Link linkFrom;
	private Link linkTo;

	public Tail(int x, int y, int color)
	{
		super(x,y);
		this.color = color;
		this.linkTo = new Link();
		this.linkFrom = new Link();

	}

	public void setLinkTo(Direction direction)
	{
		this.linkTo.direction = direction;
		linkTo.hasLink = true;
	}

	public void setLinkFrom(Direction direction, Piece piece)
	{
		this.linkFrom.direction = direction;
		linkFrom.hasLink = true;
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

		if (this.linkFrom.hasLink())
		{
			Log.d("TESTE", "tou aqui ");
			paint.setColor(this.color);
			paint.setStrokeWidth(2*side/6);
			canvas.drawLine(side/2, side/2, side/2 + side/2*this.linkFrom.direction.dx, side/2 + side/2*this.linkFrom.direction.dy, paint);
		}
	}

}
