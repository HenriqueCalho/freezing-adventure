package poo.circuitboard;

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
import poo.lib.tile.TilePanel;

import android.os.Bundle;
import android.os.Parcelable;

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

	public Piece[][] save() { return gb; }

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		this.paint.setColor(gb[xTail][yTail].getColor());
//		anim.drawAnims(canvas, this.paint);
//		drawLinks(canvas, getSideTile());
	}


	public void removeLinks(Piece piece)
	{

//		piece.removeLink();
//		invalidate();
		Piece p = piece;

		// ver se a tile tail está isLinked
		Log.d("TESTE","");

		while(p.isLinked())
		{
			Log.d("TESTE","tou no while");
			// FROM
//			Log.d("TESTE", Integer.toString(p.linkFrom.direction.dx) );
//			Log.d("TESTE", Integer.toString(p.linkFrom.direction.dy) );
//			Log.d("TESTE", Integer.toString(p.linkFrom.piece.linkFrom.direction.dx) );
//			Log.d("TESTE", Integer.toString(p.linkFrom.piece.linkFrom.direction.dy) );

			p.removeLink();
			Log.d("TESTE", "REMOVI CARALHO");
			invalidate(p.getX(),p.getY());
			p = p.linkFrom.piece;
		}

//		p = piece.linkFrom.piece;
//		while(p.isLinked())
//		{
//			Log.d("TESTE","tou no while");
//			p.removeLink();
//			Log.d("TESTE", "REMOVI CARALHO");
//			invalidate(p.getX(),p.getY());
//			p = p.linkFrom.piece;
//		}



//		invalidate();

//		piece.linkFrom.piece.removeLink();
//		piece.linkTo.piece.removeLink();
	}

	// The tiles animator
	private Animator anim = null;

	private int xTail, yTail;	// x and y of last touched Tail Piece
	private int xTouch, yTouch;	// x and y of last touched tail


	@Override
	public boolean onClick(int xTile, int yTile)
	{
		if (this.gb[xTile][yTile].isLinked())
			removeLinks(this.gb[xTile][yTile]);
//		invalidate();
//		gb[xTile+1][yTile].removeLink();
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

//			this.gb[xTile][yTile].setLinkStart();
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

			if (this.gb[this.xTouch][this.yTouch].canLink(direction, this.gb[xTile][yTile]))
				if (this.gb[xTile][yTile].canLink(direction.opposite(), this.gb[this.xTouch][this.yTouch]))
				{
					this.gb[this.xTouch][this.yTouch].setLinkTo(direction, this.gb[xTile][yTile]);
					this.gb[xTile][yTile].setLinkFrom(direction.opposite(), this.gb[this.xTouch][this.yTouch]);
				}
			invalidate();
		}
		this.xTouch = xTile;
		this.yTouch = yTile;
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




//	private Piece[][] stateToSave;
	//...

//	@Override
//	public Parcelable onSaveInstanceState() {
//		//begin boilerplate code that allows parent classes to save state
//		Parcelable superState = super.onSaveInstanceState();
//
//		SavedState ss = new SavedState(superState);
//		//end
//
//		ss.stateToSave = this.gb;
//
//		return ss;
//	}
//
//	@Override
//	public void onRestoreInstanceState(Parcelable state) {
//		//begin boilerplate code so parent classes can restore state
//		if(!(state instanceof SavedState)) {
//			super.onRestoreInstanceState(state);
//			return;
//		}
//
//		SavedState ss = (SavedState)state;
//		super.onRestoreInstanceState(ss.getSuperState());
//		//end
//
//		this.gb = ss.stateToSave;
//	}
//
//	static class SavedState extends BaseSavedState {
//		Piece[][] stateToSave;
//
//		SavedState(Parcelable superState) {
//			super(superState);
//		}
//
//		private SavedState(Parcel in) {
//			super(in);
////			this.stateToSave = in.readInt();
//			this.stateToSave = (Piece[][]) in.readArray(Piece.class.getClassLoader());
//		}
//
//		@Override
//		public void writeToParcel(Parcel out, int flags) {
//			super.writeToParcel(out, flags);
//	//		out.writeInt(this.stateToSave);
//			out.writeArray(this.stateToSave);
//		}
//
//		//required field that makes Parcelables from a Parcel
//		public static final Parcelable.Creator<SavedState> CREATOR =
//			  new Parcelable.Creator<SavedState>() {
//				  public SavedState createFromParcel(Parcel in) {
//					  return new SavedState(in);
//				  }
//				  public SavedState[] newArray(int size) {
//					  return new SavedState[size];
//				  }
//			  };
//	}




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
