package poo.circuitboard.cell;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import poo.lib.Direction;
import poo.lib.tile.Tile;
/**
 * Created by Rasta Smurf on 22-Dez-2014.
 */
public abstract class Piece implements Tile, Parcelable
{
	public class Link	implements Parcelable
	{
		/* Not having a link means direction = null */

		public Piece piece;
//		public Direction direction = Direction.get (1,0);
		public Direction direction;
		public boolean hasLink = false;

		public Link(){};

		private Link(Piece p, Direction d)
		{
			piece = p;
			direction = d;
		}

		public boolean hasLink() { return this.hasLink; }
		public boolean canLink(Direction direction)
		{
			return false;
		}
		public void removeLink()
		{
			direction = null;
			hasLink = false;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		public final Parcelable.Creator<Link> CREATOR
			  = new Parcelable.Creator<Link>() {
			public Link createFromParcel(Parcel in) {
				// read the bundle containing key value pairs from the parcel
				Bundle bundle = in.readBundle();
				Direction d = Direction.get(bundle.getInt("KEY_DIRECTION_X"), bundle.getInt("KEY_DIRECTION_Y"));
				// instantiate a piece using values from the bundle
				return new Link((Piece)bundle.getParcelable("KEY_LINKED_PIECE"), d);
			}

			public Link[] newArray(int size) {
				return new Link[size];
			}
		};

		@Override
		public void writeToParcel(Parcel out, int flags)
		{
			// create a bundle for the key value pairs
			Bundle bundle = new Bundle();
			// insert the key value pairs to the bundle
			bundle.putParcelable("KEY_LINKED_PIECE", piece);
			bundle.putInt("KEY_DIRECTION_X", direction.dx);
			bundle.putInt("KEY_DIRECTION_Y", direction.dy);
			// write the key value pairs to the parcel
			out.writeBundle(bundle);
		}
	}

	protected Link linkTo;
	protected Link linkFrom;
	public Link getLinkTo() { return linkTo; }
	public Link getLinkFrom() { return linkFrom; }
	protected boolean isLinked = false;
	public boolean isLinked() { return isLinked; }

	public void setLinkStart() {};
	public void setLinkTo(Direction direction, Piece piece){};
	public void setLinkFrom(Direction direction, Piece piece){};
	public abstract boolean canLink(Direction direction, Piece piece);
	public void removeLink() {}

	protected int x;
	public int getX() { return this.x; }
	protected int y;
	public int getY() { return this.y; }
	// original color for each piece type
	protected int originalColor;
	public int getOriginalColor()	{ return originalColor; }
	// color of the piece associated with its current connection
	protected int color;
	public int getColor()	{ return color; }

	protected static Paint paint;

	public Piece(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.paint = new Paint();
	}

//	@Override
//	public abstract void draw(Canvas canvas, int side);

	@Override
	public void draw(Canvas canvas, int side)
	{
		if(isLinked)
		{
			int green = Color.argb(255,0,100,0);
			paint.setColor(green);
			canvas.drawRect(0, 0, side, side, paint);
		}
	}

//	@Override
//	public boolean setSelect(boolean selected)
//	{
//		return isLinked = selected;
//	}

	@Override
	public boolean setSelect(boolean selected)
	{
		return selected;
	}



	public int describeContents() {
		return 0;
	}

//	public void writeToParcel(Parcel out, int flags) {
//
//		// create a bundle for the key value pairs
//		Bundle bundle = new Bundle();
//
//		// insert the key value pairs to the bundle
//		bundle.putInt("KEY_X", getX());
//		bundle.putInt("KEY_Y", getY());
//
//		// write the key value pairs to the parcel
//		out.writeBundle(bundle);
//	}

//	public static final Parcelable.Creator<Piece> CREATOR;
//
//	static {
//		CREATOR = new Creator<Piece>() {
//
//			@Override
//			public Piece createFromParcel(Parcel source) {
//				// read the bundle containing key value pairs from the parcel
//				Bundle bundle = source.readBundle();
//
//				// instantiate a person using values from the bundle
//				return new Piece(bundle.getInt("KEY_X"),
//					  bundle.getInt("KEY_Y")) {
//					@Override
//					public boolean canLink(Direction direction, Piece piece) {
//						return false;
//					}
//				};
//			}
//
//			@Override
//			public Piece[] newArray(int size) {
//				return new Piece[size];
//			}
//
//		};
//	}
}
