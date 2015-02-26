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
public class Block extends Piece implements Parcelable
{
	public Block(int x, int y)
	{
		super(x,y);
//		this.isLinked = true;
	}

	public boolean canLink(Direction direction, Piece piece) { return false; }

	@Override
	public void draw(Canvas canvas, int side)
	{
		super.draw(canvas,side);
		int green = Color.argb(255,0,100,0);
		paint.setColor(green);
		canvas.drawRect(0, 0, side, side, paint);
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

	public static final Parcelable.Creator<Block> CREATOR
		  = new Parcelable.Creator<Block>() {
		public Block createFromParcel(Parcel in) {
			// read the bundle containing key value pairs from the parcel
			Bundle bundle = in.readBundle();
			// instantiate a piece using values from the bundle
			return new Block(bundle.getInt("KEY_X"),bundle.getInt("KEY_Y"));
		}

		public Block[] newArray(int size) {
			return new Block[size];
		}
	};
}
