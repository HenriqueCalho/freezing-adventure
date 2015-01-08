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
		this.paint.setColor(gb[xTail][yTail].getColor());
		anim.drawAnims(canvas, this.paint);
	}

	// The tiles animator
	private Animator anim = null;

	private int xTail, yTail;	// x and y of last touched Tail Piece
	private int xEvent, yEvent;	// x and y of last event


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
			this.xEvent = xTile;
			this.yEvent = yTile;
		}
		return false;
	}

	@Override
	public boolean onDrag(int xTo, int yTo)
	{
		this.anim.addAnim(new AnimTile(xEvent,yEvent,300,this,xTo,yTo));
		this.xEvent = xTo;
		this.yEvent = yTo;
		return false;
	}

	@Override
	public boolean onTouch(int xTile, int yTile)
	{
		if (this.gb[xTile][yTile] instanceof Tail)
		{
			this.xTail = xTile;
			this.yTail = yTile;

		}
		return false;
	}

	@Override
	public boolean onMove(int xTile, int yTile) {

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
					this.gb[j/2][i] = createPiece(i, j/2, line.charAt(j));
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
