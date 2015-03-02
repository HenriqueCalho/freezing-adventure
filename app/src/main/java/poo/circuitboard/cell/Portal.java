package poo.circuitboard.cell;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import poo.lib.Direction;

/**
 * Created by Rasta Smurf on 01-Mar-2015.
 */
public class Portal extends Piece
{
	private static final int MAX_PORTALS = 2;
	// all the portals present in the current level
	private static Portal[] portals = new Portal[MAX_PORTALS*2];
	private int portalIdx;
	private Portal other;
	private boolean isConnected = false;
	public boolean IsConnected()	{ return isConnected; }

	public Portal(int x, int y, int color, int idx)
	{
		super(x,y);
		this.color = color;
		originalColor = color;
		this.linkTo = new Link();
		this.linkFrom = new Link();

		if(portals[(idx-1)*2] == null)
			portals[(idx-1)*2] = this;
		else {
			portals[(idx-1)*2+1] = this;
			portals[(idx-1)*2].other = this;
			other = portals[(idx-1)*2];
		}
	}

	public void removeLink()
	{
		linkFrom.removeLink();
		linkTo.removeLink();
		this.isLinked = false;
//		isConnected = false;

		if(!other.isLinked())
		{
			disconnectPortal();
			other.disconnectPortal();
		}
	}

	private void disconnectPortal()
	{
		isConnected = false;
	}

	public boolean canLink(Direction direction, Piece piece)
	{
		if(!isLinked && !other.isLinked())				return true;
		if(other.isLinked() && piece.getColor() == color)	return true;
		if(other.isLinked() && !piece.isLinked())			return true;
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
		this.linkFrom.piece = piece;
		this.linkFrom.hasLink = true;
		this.color = piece.getColor();
		this.isLinked = true;
		other.setPortal(color);
		isConnected = true;
	}

	private void setPortal(int c)
	{
		color = c;
		isConnected = true;
	}


	@Override
	public void draw(Canvas canvas, int side)
	{
		super.draw(canvas,side);
		paint.setColor(originalColor);
		canvas.drawCircle(side/2, side/2, side/3, paint);


		if(other.isLinked())
		{
			paint.setColor(getColor());
			canvas.drawCircle(side/2, side/2, 2*side/7, paint);
		}

		if (this.linkTo.hasLink())
		{
			paint.setColor(color);
			paint.setStrokeWidth(2*side/6);
			canvas.drawLine(side/2, side/2, side/2 + side/2*linkTo.direction.dx, side/2 + side/2*linkTo.direction.dy, paint);
		}

		if (this.linkFrom.hasLink())
		{
			paint.setColor(color);
			canvas.drawCircle(side/2, side/2, 2*side/7, paint);
			paint.setStrokeWidth(2*side/6);
			canvas.drawLine(side/2, side/2, side/2 + side/2*this.linkFrom.direction.dx, side/2 + side/2*this.linkFrom.direction.dy, paint);
		}




//		paint.setColor(Color.RED);
//		canvas.drawCircle(side/2, side/2, 2*side/7, paint);




		paint.setColor(Color.BLACK);
		canvas.drawCircle(side/2, side/2, side/6, paint);
	}


	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		// create a bundle for the key value pairs
		Bundle bundle = new Bundle();
		// insert the key value pairs to the bundle
		bundle.putInt("KEY_X", this.y);
		bundle.putInt("KEY_Y", this.x);
		bundle.putInt("KEY_COLOR", this.color);
		bundle.putInt("KEY_PORTALS_IDX", portalIdx);
		// write the key value pairs to the parcel
		out.writeBundle(bundle);
	}

	public static final Parcelable.Creator<Portal> CREATOR
		  = new Parcelable.Creator<Portal>() {
		public Portal createFromParcel(Parcel in) {
			// read the bundle containing key value pairs from the parcel
			Bundle bundle = in.readBundle();
			// instantiate a piece using values from the bundle
			return new Portal(bundle.getInt("KEY_X"),
				  		bundle.getInt("KEY_Y"),
				 		bundle.getInt("KEY_COLOR"),
				  		bundle.getInt("KEY_PORTAL_IDX"));
		}

		public Portal[] newArray(int size) { return new Portal[size]; }
	};

}
