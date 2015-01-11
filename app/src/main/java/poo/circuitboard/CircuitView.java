package poo.circuitboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import poo.circuitboard.cell.Block;
import poo.circuitboard.cell.Column;
import poo.circuitboard.cell.Dot;
import poo.circuitboard.cell.Line;
import poo.circuitboard.cell.Piece;
import poo.circuitboard.cell.Tail;
import poo.lib.Direction;
import poo.lib.tile.AnimTile;
import poo.lib.tile.Animator;
import poo.lib.tile.OnTileTouchListener;
import poo.lib.tile.TilePanel;

/**
 * Created by Rasta Smurf on 22-Dez-2014.
 */
public class CircuitView extends TilePanel implements OnTileTouchListener
{
	private String file = "first.txt";
//	private TilePanel panel;
	private Context context;
	private Piece gb[][];
	/* assuming each side has the same dimension */
	private int boardSize;
	private Paint paint = new Paint();  // To draw some parts.


	public CircuitView(Context context)
	{
		super(context);
		this.context = context;
		this.boardSize = getBoardSize();
		this.gb = new Piece[this.boardSize][this.boardSize];
		loadLevel();
		setAllTiles(gb);
		setListener(this);
		anim = new Animator(this);
		setListener(this);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		this.paint.setColor(gb[xTail][yTail].getColor());
//		anim.drawAnims(canvas, this.paint);
//		drawLinks(canvas, getSideTile());
	}

//	private void drawLinks(Canvas canvas, int side)
//	{
//		paint.setStrokeWidth(2*side/6);
//		for (int i=0; i < this.boardSize; ++i)
//		{
//			for (int j=0; j < this.boardSize; ++j)
//				if (this.gb[i][j].hasLink) {
//					paint.setColor(this.gb[i][j].getColor());
//					canvas.drawLine(PieceCenterX(this.gb[i][j]), PieceCenterY(this.gb[i][j]), PieceCenterX(this.gb[i][j].to), PieceCenterY(this.gb[i][j].to), paint);
//				}
//
//		}
//
//
//		canvas.drawLine(PieceCenterX(this.gb[0][0]), PieceCenterY(this.gb[0][0]), PieceCenterX(this.gb[0][1]), PieceCenterY(this.gb[0][1]), paint);
//	}

	/* position x of the center of the piece in pixels */
	private int PieceCenterX(Piece p)
	{
		int side = getSideTile();
		int grid = getGridLine();
		return grid + side/2 + side * p.getX() + p.getX() * grid;
	}

	/* position x of the center of the piece in pixels */
	private int PieceCenterY(Piece p)
	{
		int side = getSideTile();
		int grid = getGridLine();
		return grid + side/2 + side * p.getY() + p.getY() * grid;
	}

	// The tiles animator
	private Animator anim = null;

	private int xTail, yTail;	// x and y of last touched Tail Piece
	private int xTouch, yTouch;	// x and y of last touched tail


	@Override
	public boolean onClick(int xTile, int yTile)
	{
		if (this.gb[xTile][yTile] instanceof Tail)
		{
//			Log.d("TESTE", "TOU AUI");
//			Log.d("TESTE", "valor de x : " + xTile);
//			Log.d("TESTE", "valor de y : " + yTile);

			this.xTail  = xTile;
			this.yTail  = yTile;
			this.xTouch = xTile;
			this.yTouch = yTile;
		}
		return false;
	}

	@Override
	public boolean onTouch(int xTile, int yTile)
	{
		if (this.gb[xTile][yTile] instanceof Tail)
		{
			this.xTail = xTile;
			this.yTail = yTile;
			this.xTouch = xTile;
			this.yTouch = yTile;
		}
		return false;
	}

	@Override
	public boolean onMove(int xTile, int yTile)
	{
		if (this.xTouch != xTile || this.yTouch != yTile)
		{
			Direction direction = Direction.get(xTile - this.xTouch, yTile - this.yTouch);
			if (direction == null)	return false;


			this.gb[this.xTouch][this.yTouch].setLinkTo(direction);
			this.gb[xTile][yTile].setLinkFrom(direction.opposite());



//			this.gb[this.xTouch][this.yTouch].setLink(null, this.gb[xTile][yTile]);
//			this.gb[xTile][yTile].setLink(this.gb[this.xTouch][this.yTouch], null);
		}
		this.xTouch = xTile;
		this.yTouch = yTile;
		invalidate();
		return false;
	}

	@Override
	public boolean onDrag(int xTo, int yTo)
	{
		this.anim.addAnim(new AnimTile(xTouch,yTouch,300,this,xTo,yTo));
		this.xTouch = xTo;
		this.yTouch = yTo;
		return false;
	}

	private int getBoardSize()
	{
		String line;
		InputStream in;
		BufferedReader reader;
		try {
			in = this.context.getAssets().open(this.file);
			reader = new BufferedReader(new InputStreamReader(in));
			line = reader.readLine();
			return Character.getNumericValue(line.charAt(line.indexOf('x') - 1));
		} catch (Exception e) { }
		return -1;
	}

	private void loadLevel()
	{
		String line;
		InputStream in;
		BufferedReader reader;
		try {
			in = context.getAssets().open(this.file);
			reader = new BufferedReader(new InputStreamReader(in));
			/* consume the first line which has the level info */
			line = reader.readLine();
			for (int i=0; i < this.boardSize; ++i)
			{
				line = reader.readLine();
				for (int j=0; j < this.boardSize*2-1; j+=2)
					this.gb[j/2][i] = createPiece(j/2, i, line.charAt(j));
			}
		} catch (Exception e) { }
	}


	private Piece createPiece(int x, int y, char c)
	{
		switch (c)
		{
			case 'A' : return new Tail(x, y, Color.RED);
			case 'B' : return new Tail(x, y, Color.YELLOW);
			case 'C' : return new Tail(x, y, Color.GREEN);
			case 'D' : return new Tail(x, y, Color.BLUE);
			case '|' : return new Column(x, y);
			case '*' : return new Block(x, y);
			case '-' : return new Line(x, y);
			case '.' : return new Dot(x, y);
		}
		return null;
	}
}

// =================================
//
//	Pieces code on the level file
//
//	Leter : corresponding file
//	A : Red Tail
//	B : Yellow Tail
//	C : Green Tail
//	D : Blue Tail
//	| : Column
//	* : Block
//	- : Line
//	. : Dot
//
// =================================
