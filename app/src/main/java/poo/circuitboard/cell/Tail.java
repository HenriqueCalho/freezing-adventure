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

//	public Link linkFrom;
//	public Link linkTo;

	public Tail(int x, int y, int color)
	{
		super(x,y);
		this.color = color;
		this.linkTo = new Link();
		this.linkFrom = new Link();

	}

	public void removeLink()
	{
		this.linkFrom.removeLink();
		this.linkTo.removeLink();
		this.isLinked = false;
	}

	public boolean canLink(Direction direction, Piece piece)
	{
		if (!piece.isLinked)	return true;
		if (piece.getColor() == this.color)		return true;
		return false;
	}

//	public void setLinkStart()
//	{
//		this.isLinked = true;
//	}

	public void setLinkTo(Direction direction, Piece piece)
	{
		this.linkTo.direction = direction;
		linkTo.piece = piece;
		linkTo.hasLink = true;
		this.isLinked = true;
	}

	public void setLinkFrom(Direction direction, Piece piece)
	{
		this.linkFrom.direction = direction;
		this.linkFrom.piece = piece;
		this.linkFrom.hasLink = true;
		this.isLinked = true;
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
			paint.setColor(this.color);
			paint.setStrokeWidth(2*side/6);
			canvas.drawLine(side/2, side/2, side/2 + side/2*this.linkFrom.direction.dx, side/2 + side/2*this.linkFrom.direction.dy, paint);
		}
	}

}
