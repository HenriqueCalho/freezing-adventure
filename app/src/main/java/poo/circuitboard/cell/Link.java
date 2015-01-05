package poo.circuitboard.cell;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Rasta Smurf on 05-Jan-2015.
 */
public class Link
{
	private Piece start;
	private Piece end;
	private static Paint paint;

	public Link(Piece start, Piece end)
	{
		this.start = start;
		this.end = end;
	}

	public void drawLink(Canvas canvas, int side)
	{
		paint.setColor(start.color);
//		canvas.drawLine(start.x, start.y, k + direction.dx * (k + 1), l + direction.dy * (l + 1), paint);

	}
}
