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

		Log.d("TESTE", "cor : " + gb[0][0].getColor());
		Log.d("TESTE", "cor : " + gb[1][0].getColor());
		Log.d("TESTE", "cor : " + gb[2][0].getColor());
		Log.d("TESTE", "cor : " + gb[3][0].getColor());
		Log.d("TESTE", "cor : " + gb[4][0].getColor());

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		this.paint.setColor(gb[yDown][xDown].getColor());
//		Log.d("TESTE", "cor111 : " + this.paint.getColor());
		//	paint.setColor(Color.RED);

		paint.setStrokeWidth(30);
		anim.drawAnims(canvas, this.paint);
	}

	// The tiles animator
	private Animator anim = null;

	private int xDown, yDown;	// x and y of last Down event


	@Override
	public boolean onClick(int xTile, int yTile)
	{
		this.xDown = xTile;
		this.yDown = yTile;
		return false;
	}

	@Override
	public boolean onDrag(int xFrom, int yFrom, int xTo, int yTo)
	{
		this.anim.addAnim(new AnimTile(xFrom,yFrom,300,this,xTo,yTo));
//		invalidate();
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
					this.gb[i][j/2] = createPiece(i, j/2, line.charAt(j));
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
