package poo.circuitboard.cell;

import android.graphics.Canvas;
import android.graphics.Color;

/**
 * Created by Rasta Smurf on 22-Dez-2014.
 */
public class Block extends Piece
{
	public Block(int x, int y)
	{
		super(x,y);
		this.isAnimated = true;
	}

	@Override
	public void draw(Canvas canvas, int side)
	{
		super.draw(canvas,side);
		int green = Color.argb(255,0,100,0);
		paint.setColor(green);
		canvas.drawRect(0, 0, side, side, paint);

	}
}
