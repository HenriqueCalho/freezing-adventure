package poo.circuitboard.cell;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import poo.lib.Direction;

/**
 * Created by Rasta Smurf on 02-Mar-2015.
 */
public class TwoWay extends Piece implements Parcelable
{
	/* allowed directions */
	private Direction d1;
	private Direction d2;

	public TwoWay(int x, int y, Direction d1, Direction d2)
	{
		super(x,y);
		this.linkTo = new Link();
		this.linkFrom = new Link();
		originalColor = Color.BLACK;
		this.d1 = d1;
		this.d2 = d2;
	}

	public boolean canLink(Direction direction, Piece piece)
	{
		if (direction.equals(d1) || direction.equals(d2))
			return true;
		return false;
	}

	public void removeLink()
	{
		this.linkFrom.removeLink();
		this.linkTo.removeLink();
		this.isLinked = false;
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

		if(isLinked)	paint.setColor(color);
		else			paint.setColor(originalColor);

		paint.setStrokeWidth(2*side/6);
		canvas.drawCircle(side/2, side/2, side/6, paint);
		canvas.drawLine(side / 2, side / 2, side / 2 + side / 2 * d1.dx, side / 2 + side / 2 * d1.dy, paint);
		canvas.drawLine(side/2, side/2, side/2 + side/2*d2.dx, side/2 + side/2*d2.dy, paint);
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
		bundle.putInt("KEY_D1_X", d1.dx);
		bundle.putInt("KEY_D1_Y", d1.dy);
		bundle.putInt("KEY_D2_X", d2.dx);
		bundle.putInt("KEY_D2_Y", d2.dy);
		// write the key value pairs to the parcel
		out.writeBundle(bundle);
	}

	public static final Parcelable.Creator<TwoWay> CREATOR
		  = new Parcelable.Creator<TwoWay>() {
		public TwoWay createFromParcel(Parcel in) {
			// read the bundle containing key value pairs from the parcel
			Bundle bundle = in.readBundle();
			Direction d1 = Direction.get(bundle.getInt("KEY_D1_X"), bundle.getInt("KEY_D1_Y"));
			Direction d2 = Direction.get(bundle.getInt("KEY_D2_X"), bundle.getInt("KEY_D2_Y"));
			// instantiate a piece using values from the bundle
			return new TwoWay(bundle.getInt("KEY_X"),bundle.getInt("KEY_Y"),d1,d2);
		}

		public TwoWay[] newArray(int size) {
			return new TwoWay[size];
		}
	};

}
