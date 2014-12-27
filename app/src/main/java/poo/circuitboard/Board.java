package poo.circuitboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.util.Log;

import poo.circuitboard.cell.*;
import poo.lib.tile.TilePanel;

/**
 * Created by Rasta Smurf on 22-Dez-2014.
 */
public class Board extends TilePanel
{
	private String file = "first.txt";
//	private TilePanel panel;
	private Context context;
	private Piece gb[][];
	/* assuming each side has the same dimension */
	private int boardSize;

	public Board(Context context)
	{
		super(context);
		this.context = context;
		this.boardSize = getBoardSize();
		this.gb = new Piece[this.boardSize][this.boardSize];
		loadLevel();
		setAllTiles(gb);
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
					this.gb[i][j/2] = createPiece(line.charAt(j));
			}
		} catch (Exception e) { }
	}

	private Piece createPiece(char c)
	{
		switch (c)
		{
			case 'A' : return new Tail(Color.RED);
			case 'B' : return new Tail(Color.YELLOW);
			case 'C' : return new Tail(Color.GREEN);
			case 'D' : return new Tail(Color.BLUE);
			case '|' : return new Column();
			case '*' : return new Block();
			case '-' : return new Line();
			case '.' : return new Dot();
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
