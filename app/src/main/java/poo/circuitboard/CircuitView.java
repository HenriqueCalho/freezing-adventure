package poo.circuitboard;

import android.app.Fragment;
import android.content.Context;
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
import poo.circuitboard.cell.Tail;
import poo.lib.Direction;
import poo.lib.tile.AnimTile;
import poo.lib.tile.Animator;
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
public class CircuitView extends TilePanel implements OnTileTouchListener {
	private String file = "first.txt";
	//	private TilePanel panel;
	private Context context;
	public Piece board[][];

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
		Log.d("TESTE", "length p : " + p.length);

		int idx = 0;
		for (int i=0; i < boardSize; ++i)
			for (int j=0; j < boardSize; ++j,++idx)
				board[i][j] = p[idx];
		setAllTiles(board);
		setListener(this);
	}

	/* assuming each side has the same dimension */
	private int boardSize;
	private Paint paint = new Paint();  // To draw some parts.


	public CircuitView(Context context) {
		super(context);
		this.context = context;
		this.boardSize = getBoardSize();
		if (board == null) {
			this.board = new Piece[this.boardSize][this.boardSize];
			loadLevel();
		}
		setAllTiles(board);
		setListener(this);
//		anim = new Animator(this);
//		setListener(this);
	}

	public Piece[][] save() {
		return board;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		this.paint.setColor(board[xTail][yTail].getColor());
//		anim.drawAnims(canvas, this.paint);
//		drawLinks(canvas, getSideTile());
	}


	public void removeLinks(Piece piece) {
//		Log.d("TESTE", "xt : " + Integer.toString(piece.getX()));
//		Log.d("TESTE", "yt : " + Integer.toString(piece.getY()));
//
//		Log.d("TESTE", Boolean.toString(board[2][4].isLinked));

//		piece.removeLink();
//		invalidate();

		// ver se a tile tail está isLinked
//		Log.d("TESTE", Boolean.toString(board[0][4].isLinked));
//		Log.d("TESTE", Boolean.toString(board[1][4].isLinked));
//		Log.d("TESTE", Boolean.toString(board[2][4].isLinked));
//		Log.d("TESTE", Boolean.toString(board[3][4].isLinked));
//		Log.d("TESTE", Boolean.toString(board[4][4].isLinked));

//		p.linkTo.hasLink()


		Piece p = piece;
		while (p.isLinked()) {
//			Log.d("TESTE","tou no while");

			// FROM
//			Log.d("TESTE", Integer.toString(p.linkFrom.direction.dx) );
//			Log.d("TESTE", Integer.toString(p.linkFrom.direction.dy) );
//			Log.d("TESTE", Integer.toString(p.linkFrom.piece.linkFrom.direction.dx) );
//			Log.d("TESTE", Integer.toString(p.linkFrom.piece.linkFrom.direction.dy) );

			p.removeLink();
//			Log.d("TESTE", "REMOVI CARALHO");
			invalidate(p.getX(), p.getY());
			if (p.linkTo.piece != null)
				p = p.linkTo.piece;
		}

//		Log.d("TESTE", Boolean.toString(board[2][4].isLinked));
//
//		Log.d("TESTE", "ALOOOOOOOOOO");


		p = piece.linkFrom.piece;

//		Log.d("TESTE", "xt : " + Integer.toString(p.getX()));
//		Log.d("TESTE", "yt : " + Integer.toString(p.getY()));


		while (p.isLinked()) {
			Log.d("TESTE", "tou no while");
			p.removeLink();
			Log.d("TESTE", "REMOVI CARALHO");
			invalidate(p.getX(), p.getY());
			p = p.linkFrom.piece;
		}

//		invalidate();

//		piece.linkFrom.piece.removeLink();
//		piece.linkTo.piece.removeLink();
	}

	// The tiles animator
	private Animator anim = null;

	private int xTail, yTail;      // x and y of last touched Tail Piece
	private int xTouch, yTouch;      // x and y of last touched tail


	@Override
	public boolean onClick(int xTile, int yTile) {
		//	Log.d("TESTE", Boolean.toString(board[1][4].isLinked));
//		Log.d("TESTE", Integer.toString(xTile));
//		Log.d("TESTE", Integer.toString(yTile));
//		Log.d("TESTE", Boolean.toString(board[xTile][yTile].isLinked));

		Log.d("TESTE", "tou no onclick2");


		if (this.board[xTile][yTile].isLinked())
			removeLinks(this.board[xTile][yTile]);
//		invalidate();
//		board[xTile+1][yTile].removeLink();
		return false;
	}

	@Override
	public boolean onTouch(int xTile, int yTile) {
		if (this.board[xTile][yTile] instanceof Tail) {
			this.xTail = xTile;
			this.yTail = yTile;
			this.xTouch = xTile;
			this.yTouch = yTile;

//			this.board[xTile][yTile].setLinkStart();
		}
		return false;
	}

	@Override
	public boolean onMove(int xTile, int yTile) {
		if (this.xTouch != xTile || this.yTouch != yTile) {
			Direction direction = Direction.get(xTile - this.xTouch, yTile - this.yTouch);
			if (direction == null) return false;

			if (this.board[this.xTouch][this.yTouch].canLink(direction, this.board[xTile][yTile]))
				if (this.board[xTile][yTile].canLink(direction.opposite(), this.board[this.xTouch][this.yTouch])) {
					this.board[this.xTouch][this.yTouch].setLinkTo(direction, this.board[xTile][yTile]);
					this.board[xTile][yTile].setLinkFrom(direction.opposite(), this.board[this.xTouch][this.yTouch]);
				}
			invalidate();
		}
		this.xTouch = xTile;
		this.yTouch = yTile;
		return false;
	}

	@Override
	public boolean onDrag(int xTo, int yTo) {
		this.anim.addAnim(new AnimTile(xTouch, yTouch, 300, this, xTo, yTo));
		this.xTouch = xTo;
		this.yTouch = yTo;
		return false;
	}

	private int getBoardSize() {
		String line;
		InputStream in;
		BufferedReader reader;
		try {
			in = this.context.getAssets().open(this.file);
			reader = new BufferedReader(new InputStreamReader(in));
			line = reader.readLine();
			return Character.getNumericValue(line.charAt(line.indexOf('x') - 1));
		} catch (Exception e) {
		}
		return -1;
	}

	private void loadLevel() {
		String line;
		InputStream in;
		BufferedReader reader;
		try {
			in = context.getAssets().open(this.file);
			reader = new BufferedReader(new InputStreamReader(in));
			/* consume the first line which has the level info */
			line = reader.readLine();
			for (int i = 0; i < this.boardSize; ++i) {
				line = reader.readLine();
				for (int j = 0; j < this.boardSize * 2 - 1; j += 2)
					this.board[j / 2][i] = createPiece(j / 2, i, line.charAt(j));
			}
		} catch (Exception e) {
		}
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



}