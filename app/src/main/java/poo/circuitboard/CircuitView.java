package poo.circuitboard;

import android.app.Fragment;
import android.content.Context;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import poo.circuitboard.cell.Block;
import poo.circuitboard.cell.Column;
import poo.circuitboard.cell.Dot;
import poo.circuitboard.cell.Line;
import poo.circuitboard.cell.Piece;
import poo.circuitboard.cell.Portal;
import poo.circuitboard.cell.Tail;
import poo.circuitboard.cell.TwoWay;
import poo.lib.Direction;
import poo.lib.tile.AnimTile;
import poo.lib.tile.Animator;
import poo.lib.tile.OnGameListener;
import poo.lib.tile.OnTileTouchListener;
import poo.lib.tile.Tile;
import poo.lib.tile.TilePanel;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import static android.app.Fragment.SavedState;

/**
 * Created by Rasta Smurf on 22-Dez-2014.
 */
public class CircuitView extends TilePanel implements OnTileTouchListener
{
	// name of the levels files
	private final String LEVEL_FILE = "level";
	private int currLevel = 1;
	// number of connections to be made on the current level
	private int numberOfConnections;
//	private String file = "first.txt";
	//	private TilePanel panel;
	private Context context;
	public Piece board[][];

	/* assuming each side has the same dimension */
	private int boardSize;
	private Paint paint = new Paint();  // To draw some parts.

	public CircuitView(Context context) {
		super(context);
		this.context = context;
		this.boardSize = getBoardSize();
		setSize(boardSize,boardSize);
//		numberOfConnections = getNumberOfConnections();
		if (board == null) {
			this.board = new Piece[this.boardSize][this.boardSize];
			loadNextLevel();
		}
//		setAllTiles(board);
		setListener(this);
	}


	/**
	 * Sets the listener for tile touches
	 */
	public void setOnGameListener(OnGameListener l) { onGameListener = l; }

	OnGameListener onGameListener;


	public Piece[] getBoard()
	{
		Piece[] b = new Piece[boardSize*boardSize];
		int idx = 0;
		for (int i=0; i < boardSize; ++i)
			for (int j=0; j < boardSize; ++j,++idx) {
				b[idx] = board[i][j];
			}
		return b;
	}

	public void restoreBoard(Piece[] p)
	{
		int idx = 0;
		for (int i=0; i < boardSize; ++i)
			for (int j=0; j < boardSize; ++j,++idx)
				board[i][j] = p[idx];
		setAllTiles(board);
		setListener(this);
	}

	public Piece[][] save() {
		return board;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}


	private void removeLinks(Piece piece)
	{
		Piece p = piece;
		while (p.isLinked()) {
			p.removeLink();
			invalidate(p.getX(), p.getY());
			if (p.getLinkTo().piece != null)
				p = p.getLinkTo().piece;
		}
		p = piece.getLinkFrom().piece;
		while (p.isLinked()) {
//			Log.d("TESTE", "tou no while");
			p.removeLink();
//			Log.d("TESTE", "REMOVI CARALHO");
			invalidate(p.getX(), p.getY());
			p = p.getLinkFrom().piece;
		}

	}

	// The tiles animator
	private Animator anim = null;

	private int xTail, yTail;      // x and y of last touched Tail Piece
	private int xTouch, yTouch;      // x and y of last touched tail


	@Override
	public boolean onClick(int xTile, int yTile)
	{
		if (this.board[xTile][yTile].isLinked())
		{
			removeLinks(this.board[xTile][yTile]);
		}
//		invalidate();
//		board[xTile+1][yTile].removeLink();
		return false;
	}

	@Override
	public boolean onTouch(int xTile, int yTile) {
		if (this.board[xTile][yTile] instanceof Tail && !board[xTile][yTile].isLinked()) {
			this.xTail = xTile;
			this.yTail = yTile;
			this.xTouch = xTile;
			this.yTouch = yTile;
		}
		if (this.board[xTile][yTile] instanceof Portal) {
			Portal p = (Portal)this.board[xTile][yTile];
			if(p.IsConnected())
			{
				this.xTail = xTile;
				this.yTail = yTile;
				this.xTouch = xTile;
				this.yTouch = yTile;
			}
		}

		return false;
	}

	@Override
	public boolean onMove(int xTile, int yTile)
	{
		if (this.xTouch != xTile || this.yTouch != yTile) {
			Direction direction = Direction.get(xTile - this.xTouch, yTile - this.yTouch);
			if (direction == null) return false;

			if (this.board[this.xTouch][this.yTouch].canLink(direction, this.board[xTile][yTile]))
				if (this.board[xTile][yTile].canLink(direction.opposite(), this.board[this.xTouch][this.yTouch])) {
					this.board[this.xTouch][this.yTouch].setLinkTo(direction, this.board[xTile][yTile]);
					this.board[xTile][yTile].setLinkFrom(direction.opposite(), this.board[this.xTouch][this.yTouch]);
					invalidate();
					checkGameOver();
				}
		}
		this.xTouch = xTile;
		this.yTouch = yTile;
		return false;
	}

	private void checkGameOver()
	{
		int count = 0;
		for(int i=0; i < boardSize; ++i)
			for(int j=0; j < boardSize; ++j)
				if(board[i][j].isLinked())	++count;
		if( count == boardSize*boardSize)
		{
			currLevel++;
//			GameActivity.levelLabel2.setText(Integer.toString(++currLevel));
//			GameActivity.updatePanel();
//			(GameActivity)Context.updatePanel();
			onGameListener.onLevelEnd();
			invalidate();
			new AlertDialog.Builder(context)
				.setCancelable(false)
//				.setTitle("WIN!!")
//				.setMessage("WIN!!!")
				.setPositiveButton("Next Level", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						loadNextLevel();
					}
				})
				.create()
				.show();
		}
	}

	private int getNumberOfConnections()
	{
		String file = LEVEL_FILE + currLevel + ".txt";
		String line;
		InputStream in;
		BufferedReader reader;
		try {
			in = this.context.getAssets().open(file);
			reader = new BufferedReader(new InputStreamReader(in));
			line = reader.readLine();
			return Character.getNumericValue(line.charAt(7));
		} catch (Exception e) {
		}
		return -1;

	}

	private int getBoardSize() {
		String file = LEVEL_FILE + currLevel + ".txt";
		String line;
		InputStream in;
		BufferedReader reader;
		try {
			in = this.context.getAssets().open(file);
			reader = new BufferedReader(new InputStreamReader(in));
			line = reader.readLine();
			return Character.getNumericValue(line.charAt(line.indexOf('x') - 1));
		} catch (Exception e) {
		}
		return -1;
	}

	private void loadNextLevel() {
		String file = LEVEL_FILE + currLevel + ".txt";
		String line;
		InputStream in;
		BufferedReader reader;
		try {
			in = context.getAssets().open(file);
			reader = new BufferedReader(new InputStreamReader(in));
			/* consume the first line which has the level info */
			line = reader.readLine();
			for (int i = 0; i < this.boardSize; ++i) {
				line = reader.readLine();
				for (int j = 0; j < this.boardSize * 2 - 1; j += 2)
					this.board[j / 2][i] = createPiece(j / 2, i, line.charAt(j));
			}
		} catch (Exception e) {}
		setAllTiles(board);
	}


	private Piece createPiece(int x, int y, char c) {
		switch (c) {
			case 'A':
				return new Tail(x, y, Color.RED);
			case 'B':
				return new Tail(x, y, Color.YELLOW);
			case 'C':
				return new Tail(x, y, Color.GREEN);
			case 'D':
				return new Tail(x, y, Color.BLUE);
			case 'E':
				return new Tail(x, y, Color.rgb(128,0,128));
			case 'F':
				return new Portal(x, y, Color.LTGRAY, 1);
			case 'G':
				return new Portal(x, y, Color.LTGRAY, 2);
			case 'a':
				return new TwoWay(x, y, Direction.LEFT, Direction.UP);
			case 'b':
				return new TwoWay(x, y, Direction.UP, Direction.RIGHT);
			case 'c':
				return new TwoWay(x, y, Direction.RIGHT, Direction.DOWN);
			case 'd':
				return new TwoWay(x, y, Direction.DOWN, Direction.LEFT);
			case 'e':
				return new TwoWay(x, y, Direction.UP, Direction.DOWN);
			case 'f':
				return new TwoWay(x, y, Direction.LEFT, Direction.RIGHT);
			case '|':
				return new Column(x, y);
			case '*':
				return new Block(x, y);
			case '-':
				return new Line(x, y);
			case '.':
				return new Dot(x, y);
		}
		return null;
	}

	public int getCurrentLevel() {
		return currLevel;
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
//	E : Purple Tail
//	F : First Portal
//	G : Second Portal
//	| : Column
//	* : Block
//	- : Line
//	. : Dot
//	a ; TwoWay	_|
//	b ; TwoWay 	 |_
//	c ; TwoWay	 |-
//	d ; TwoWay 	-|
//	e ; TwoWay 	 |
//	f ; TwoWay 	 -
//
// =================================



}