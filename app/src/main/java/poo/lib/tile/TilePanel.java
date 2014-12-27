package poo.lib.tile;

//import isel.poo.lib.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.security.InvalidParameterException;

/**
 * A specialization of View to manage a panel of <b>tiles</b>.</br> 
 * Each tile is a square that implements the "Tile" interface to draw.</br> 
 * The panel dimensions are defined by the attributes "app:widthTiles" and "app:heightTiles" in multiple layout in the tiles.
 * @author Palex
 * @see Tile
 */
public class TilePanel extends View {

	private int wTiles=-1, hTiles=-1;   // Panel dimensions in tiles.

	private Tile[] tiles;				// The tiles.
	private Paint paint = new Paint();  // To draw some parts.

	int sideTile;					// width and height of each tile.
	private int xInit, yInit, xEnd, yEnd;	// Bounds of panel.
	private int gridLine = 4;		// grid lines stroke width.

	/**
	 * Constructor called programatically
	 */
    public TilePanel(Context context) {
        super(context);
        setSize(5,5);
    }
	
	/**
	 * Constructor called in layout inflate
	 */
    public TilePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttrs(context, attrs);
    }

	/**
	 * Constructor called in layout inflate
	 */
    public TilePanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseAttrs(context, attrs);
    }

    // Parse attributes of layout definition
    private void parseAttrs(Context context, AttributeSet attrs) {
//		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TilePanel);
//        wTiles = a.getInteger(R.styleable.TilePanel_widthTiles,-1);
//        hTiles = a.getInteger(R.styleable.TilePanel_heightTiles,-1);
//        gridLine = a.getInteger(R.styleable.TilePanel_gridLine, 1);
        if (wTiles==-1 && hTiles==-1) wTiles=hTiles=10;
        if (wTiles==-1 && hTiles!=-1) wTiles=hTiles;
        if (hTiles==-1 && wTiles!=-1) hTiles=wTiles;
//		paint.setColor(a.getColor(R.styleable.TilePanel_background, Color.DKGRAY));
		paint.setStrokeWidth(gridLine);
//        a.recycle();
  	    tiles = new Tile[wTiles*hTiles];
	}

	public int getWidthInTiles()  	{ return wTiles; }	
	public int getHeightInTiles() 	{ return hTiles; }

    /**
     * Define the size (in tiles) programatically
     */
	public void setSize(int w, int h) {
		wTiles = w; hTiles = h;
  	    tiles = new Tile[wTiles*hTiles];
		resize(getWidth(),getHeight());
	}
	
	/**
	 * Sets all the tiles in panel
	 * @param t Array with all tiles to set
	 * @throws InvalidParameterException If the array and the panel have different dimensions 
	 */	
	public void setAllTiles(Tile[][] t) {
	  if (t.length!=wTiles || t[0].length!=hTiles ) throw new InvalidParameterException();
	  for(int y=0, idx=0 ; y<hTiles ; ++y)
		for(int x=0 ; x<wTiles ; ++x, ++idx)
			tiles[idx] = t[y][x];
	  invalidate();
	}
	
	/**
	 * To change the tile position.</br>
	 * ATENTION: The same tile may be located in more than one position in panel.
	 * @param x coordinate of the position to set
	 * @param y coordinate of the position to set
	 * @param t Tile to put in that position
	 */
	public void setTile(int x, int y, Tile t) {
		setTileNoInvalidate(x,y,t);
		invalidate(tileRect(x, y));
	}
	
	private int idxTile(int x, int y) { return y*wTiles+x; }
	
	void setTileNoInvalidate(int x, int y, Tile t) {
		tiles[idxTile(x,y)] = t;
	}
	
	/**
	 * Gets the tile that is in the position 
	 */
	public Tile getTile(int x, int y) {
		return tiles[idxTile(x,y)];		
	}

	/**
	 * Sets the stroke of grid lines (0 --> no grid)
	 * @param stroke of grid lines in pixel (0..N)
	 */
	public void setGridLine(int stroke) {
		gridLine = stroke;
	}

	// Called by layout manager to define dimensions of the View
	@Override
	protected void onMeasure(int wMS, int hMS) {
		int w = MeasureSpec.getSize(wMS);
		int h = MeasureSpec.getSize(hMS);
		if (MeasureSpec.getMode(hMS)==MeasureSpec.UNSPECIFIED) 
			h = getSuggestedMinimumHeight();
		if (MeasureSpec.getMode(wMS)==MeasureSpec.UNSPECIFIED) 
			w = getSuggestedMinimumWidth();
		calcSideTile(w, h);
		int wm = sideTile*wTiles+gridLine*(wTiles+1); 
		int hm = sideTile*hTiles+gridLine*(hTiles+1);
		setMeasuredDimension(wm,hm);
	}

	private void calcSideTile(int w, int h) {
		int wt = (w-gridLine*(wTiles+1))/wTiles;
		int ht = (h-gridLine*(hTiles+1))/hTiles;
		sideTile = Math.min(ht,wt);
	}

	// Called to draw the View
	@Override
	protected void onDraw(Canvas canvas) {
		try {
			if (isInEditMode()) // In layout editor
				drawGrid(canvas);
			else {
				Tile t;
				for (int y = 0, idx = 0; y < hTiles; ++y)
					for (int x = 0; x < wTiles; ++x, ++idx)
						if ((t = tiles[idx]) != null)
							drawTile(canvas, t, x, y); // draw each tile
				drawGrid(canvas); // draw grid lines
				if (animator!=null)
					animator.drawAnims(canvas); // draw animations in progress
			}
		} catch (Exception e) {
			Toast.makeText(getContext(), "onDraw(): "+e, Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	// Draw each tile. Called by onDraw()
	private void drawTile(Canvas canvas, Tile t, int x, int y) {
	  canvas.save();			// Save canvas context
	  Rect r = tileRect(x, y);	
	  canvas.clipRect(r);				// Clipping area of tile
	  canvas.translate(r.left,r.top);	// Origin (0,0) to call draw
	  if (animator!=null && animator.getAnim(t)!=null)
		  canvas.drawColor(Color.TRANSPARENT); // In move? Draw place holder transparent 
	  else
		  t.draw(canvas,sideTile); // Draw the tile
	  canvas.restore();			// Restore canvas context
	}

	// Draw grid lines. Called by onDraw()
	private void drawGrid(Canvas canvas) {
	  if (gridLine>0) {
		  for(int x=xInit+gridLine/2 ; x<=xEnd ; x+=sideTile+gridLine )
			canvas.drawLine(x, yInit, x, yEnd, paint);
		  for(int y=yInit+gridLine/2 ; y<=yEnd ; y+=sideTile+gridLine )
			canvas.drawLine(xInit, y, xEnd, y, paint);
	  }
	}

	// Called by layout manager if size changed. 
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		resize(w, h);
	}

	// Calculate each tile dimensions and other bounds of internal panel 
	private void resize(int w, int h) {
		calcSideTile(w, h);
		xInit = ((w-gridLine)%wTiles)/2; 
		yInit = ((h-gridLine)%hTiles)/2;
		xEnd = xInit+(sideTile+gridLine)*wTiles+gridLine; 
		yEnd = yInit+(sideTile+gridLine)*hTiles+gridLine;
	}

	private int xTouch, yTouch;	// x and y of last event  
	private int xDown, yDown;	// x and y of last Down event  
	private Tile selected;		// last tile selected 
	private int pointerId;		// pointer of last event
	private boolean inDrag;     // next ACTION_MOVE is to drag
	
	// The listener of tile touches. 
	private OnTileTouchListener listener;
	
	/**
	 * Sets the listener for tile touches  
	 */
	public void setListener(OnTileTouchListener l) {	listener = l; }
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		try {
			int x = (int) ev.getX(), y = (int) ev.getY();
			if (x < xInit || x >= xEnd || y < yInit || y >= yEnd) return false;
			int xt = (x-xInit)/sideTile;
			int yt = (y-yInit)/sideTile;
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				//System.out.printf("TOUCH DOWN (%d,%d) [%s,%d] id=%d\n",xt,yt,x,y,ev.getPointerId(0));
				pointerId=ev.getPointerId(0);
				selectTouched(xt, yt);
				xTouch = xt; yTouch = yt;
				xDown = xt; yDown = yt;
				inDrag = true;
				return true;
			case MotionEvent.ACTION_UP:
				if (selected==getTile(xt,yt) && listener!=null && xDown==xt && yDown==yt)
					listener.onClick(xt, yt);
				//System.out.printf("TOUCH UP (%d,%d) [%s,%d] id=%d\n",xt,yt,x,y,ev.getPointerId(0));
				unselectTouched();
				inDrag=false;
				return true;
			case MotionEvent.ACTION_MOVE:
				if (xt!=xTouch || yt!=yTouch) {
					//System.out.printf("TOUCH MOVE (%d,%d) id=%d\n",xt,yt,ev.getPointerId(0));
					unselectTouched();
					if (listener!=null && ev.getPointerId(0)==pointerId && inDrag) 
						inDrag = listener.onDrag(xTouch, yTouch, xt, yt);
					xTouch = xt; yTouch = yt;
					return true;
				} 
			}
		} catch (Exception e) {	e.printStackTrace(); }
	    return false;
	}
	
	private void selectTouched(int xt, int yt) {
		Tile tile = getTile(xt, yt);
		if (tile!=null && tile.setSelect(true))
		  invalidate(tileRect(xt,yt));
		selected = tile;
	}

	private void unselectTouched() {
		if (selected==null) return; 
		if (selected.setSelect(false)) 
		  invalidate(tileRect(xTouch,yTouch));
		selected = null;
	}

	// To optimize performance, the returned rectangle by "tileRect" is always the same.
	private Rect rect = new Rect();
	
	/**
	 * Returns a rectangle with the dimensions of a tile at the given coordinates.<br/>
	 * ATENTION: The <code>Rect</code> object is reused.
	 * @param xt Column of the tile
	 * @param yt Line of the tile
	 * @return
	 */
	public Rect tileRect(int xt, int yt) {
		int x = xInit + gridLine + xt*(sideTile + gridLine);
		int y = yInit + gridLine + yt*(sideTile + gridLine);
		rect.set(x,y, x+sideTile,y+sideTile);
		return rect;
	}

	/**
	 * Invalidates draw area of one tile.
	 * @param x of the tile
	 * @param y of the tile
	 */
	public void invalidate(int x, int y) {
		Rect r = tileRect(x, y);
		invalidate(r.left,r.top,r.right,r.bottom);
	}

	// The tiles animator 
	private Animator animator = null;
	
	/**
	 * Returns the Animator object used in TilePanel
	 * @return
	 */
	public Animator getAnimator() { 
		if (animator==null)
			animator = new Animator(this);
		return animator; 
	}
}
