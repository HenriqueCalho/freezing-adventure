package poo.circuitboard;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import poo.lib.tile.OnTileTouchListener;

/**
 * Created by Rasta Smurf on 20-Dez-2014.
 */
public class GameActivity extends Activity
{
	private Display mDisplay;
	private int width;
	private int height;

	private static  final String file = "first.txt";
	private final static String LEVEL_LABEL = "Level: ";
	private TextView levelLabel1;
	private TextView levelLabel2;
	private int level = 1;

	private CircuitView game;
//	private GameState game;
//	private TilePanel panel;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.mDisplay = GameActivity.this.getWindowManager().getDefaultDisplay();
		this.width  = mDisplay.getWidth();
		this.height = mDisplay.getHeight();

		/* create game */
//		this.game = new GameState();
		this.game = new CircuitView(this);


		/* game panel layout */
		this.game.setBackgroundColor(Color.DKGRAY);

//		this.panel = new TilePanel(this);
//		loadLevel();
//		this.panel.setBackgroundColor(Color.DKGRAY);

		/* bottom bar layout */
		LinearLayout bottomLayout = new LinearLayout(this);
		bottomLayout.setOrientation(LinearLayout.HORIZONTAL);
		bottomLayout.setBackgroundColor(Color.LTGRAY);
		bottomLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
		this.levelLabel1 = new TextView(this);
		this.levelLabel1.setText(LEVEL_LABEL);
		this.levelLabel2 = new TextView(this);
		this.levelLabel2.setText(Integer.toString(level));
		bottomLayout.addView(levelLabel1);
		bottomLayout.addView(levelLabel2);

		/* game layout */
		LinearLayout root = new LinearLayout(this);
		root.setOrientation(LinearLayout.VERTICAL);
		root.addView(game);
		root.addView(bottomLayout);
		setContentView(root);
	}


}
