package poo.circuitboard.cell;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import poo.lib.Direction;

/**
 * Created by Rasta Smurf on 22-Dez-2014.
 */
public class Column extends Piece implements Parcelable
{
	/* allowed directions */
	private Direction d1 = Direction.UP;
	private Direction d2 = Direction.DOWN;

//	public Link linkFrom;
//	public Link linkTo;

	public Column(int x, int y)
	{
		super(x,y);
//		this.color = Color.BLACK;
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
//		paint.setColor(Color.BLACK);
		if(isLinked)	paint.setColor(color);
		else			paint.setColor(originalColor);
		canvas.drawRect(side/3+1, 0, 2*side/3+1, side, paint);
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
		// write the key value pairs to the parcel
		out.writeBundle(bundle);
	}

	public static final Parcelable.Creator<Column> CREATOR
		  = new Parcelable.Creator<Column>() {
		public Column createFromParcel(Parcel in) {
			// read the bundle containing key value pairs from the parcel
			Bundle bundle = in.readBundle();
			// instantiate a piece using values from the bundle
			return new Column(bundle.getInt("KEY_X"),bundle.getInt("KEY_Y"));
		}

		public Column[] newArray(int size) { return new Column[size]; }
	};


}
