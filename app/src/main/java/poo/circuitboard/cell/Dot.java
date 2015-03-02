package poo.circuitboard.cell;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import poo.lib.Direction;

/**
 * Created by Rasta Smurf on 22-Dez-2014.
 */
public class Dot extends Piece implements Parcelable
{

	public Dot(int x, int y)
	{
		super(x,y);
		this.linkTo = new Link();
		this.linkFrom = new Link();
		originalColor = Color.BLACK;
	}

	public void removeLink()
	{
		this.linkFrom.removeLink();
		this.linkTo.removeLink();
		this.isLinked = false;
	}


	public boolean canLink(Direction direction, Piece piece)
	{
		if(linkTo.hasLink() && linkFrom.hasLink())	return false;
		if(piece instanceof Tail   && !piece.isLinked())	return true;
		if(piece instanceof Tail   &&  piece.isLinked())	return false;
		if(piece instanceof Portal && !piece.isLinked())	return true;
		if(piece instanceof Portal &&  piece.isLinked())	return false;
		if(piece.isLinked() || isLinked)	return true;
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
		linkFrom.piece = piece;
		linkFrom.hasLink = true;
		this.color = piece.getColor();
		this.isLinked = true;
	}

	@Override
	public void draw(Canvas canvas, int side)
	{
		super.draw(canvas,side);

		if(isLinked)	paint.setColor(color);
		else			paint.setColor(originalColor);
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

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		// create a bundle for the key value pairs
		Bundle bundle = new Bundle();
		// insert the key value pairs to the bundle
		bundle.putInt("KEY_X", getX());
		bundle.putInt("KEY_Y", getY());
		bundle.putParcelable("KEY_LINK_FROM", linkFrom);
		bundle.putParcelable("KEY_LINK_TO", linkTo);
		// write the key value pairs to the parcel
		out.writeBundle(bundle);
	}

	public static final Parcelable.Creator<Dot> CREATOR
		  = new Parcelable.Creator<Dot>() {
		public Dot createFromParcel(Parcel in) {
			// read the bundle containing key value pairs from the parcel
			Bundle bundle = in.readBundle();
			// instantiate a piece using values from the bundle
			return new Dot(bundle.getInt("KEY_X"),bundle.getInt("KEY_Y"));
		}

		public Dot[] newArray(int size) {
			return new Dot[size];
		}
	};

}
