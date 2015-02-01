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
	private Direction d1 = Direction.LEFT;
	private Direction d2 = Direction.RIGHT;

//	public Link linkFrom;
//	public Link linkTo;

	public Line(int x, int y)
	{
		super(x,y);
		this.color = Color.BLACK;
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
		if (direction.equals(this.d1) || direction.equals(this.d2))
			return true;
		return false;
	}

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
		linkFrom.hasLink = true;
		linkFrom.piece = piece;
		this.color = piece.getColor();
		this.isLinked = true;
	}

	@Override
	public void draw(Canvas canvas, int side)
	{
		super.draw(canvas,side);
		paint.setColor(Color.BLACK);
		canvas.drawRect(0, side/3, side, 2*side/3, paint);

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
