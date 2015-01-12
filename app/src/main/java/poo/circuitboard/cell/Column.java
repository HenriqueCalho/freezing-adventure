package poo.circuitboard.cell;

import android.graphics.Canvas;
import android.graphics.Color;

import poo.lib.Direction;

/**
 * Created by Rasta Smurf on 22-Dez-2014.
 */
public class Column extends Piece
{
	/* allowed directions */
	private Direction d1 = Direction.UP;
	private Direction d2 = Direction.DOWN;

	private Link linkFrom;
	private Link linkTo;

	public Column(int x, int y)
	{
		super(x,y);
		this.color = Color.BLACK;
		this.linkTo = new Link();
		this.linkFrom = new Link();
	}

	public boolean canLink(Direction direction, Piece piece)
	{
		if (direction.equals(this.d1) || direction.equals(this.d2))
			return true;
		return false;
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
		this.color = piece.getColor();
	}

	@Override
	public void draw(Canvas canvas, int side)
	{
		super.draw(canvas,side);
		paint.setColor(Color.BLACK);
		canvas.drawRect(side/3, 0, 2*side/3, side, paint);

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
