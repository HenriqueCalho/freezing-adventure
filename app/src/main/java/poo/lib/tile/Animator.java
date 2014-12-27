package poo.lib.tile;

import java.util.LinkedList;
import java.util.ListIterator;

import android.graphics.Canvas;
import android.graphics.Rect;

public class Animator {

	private LinkedList<AnimTile> anims = new LinkedList<>();
	private TilePanel panel;

	private long nextTime;
	private int adjust = 0;

	// Time interval for steps of animations
	static int STEP_TIME = 40; 

	Animator(TilePanel p) {
		panel = p;
	}

	// The listener of animations ends 
	private OnFinishAnimationListener listener;
	private Object listenerTag;
	
	/**
	 * Trigger for the listener of animations ends  
	 */
	public void triggerOnFinishAnimations(Object tag, OnFinishAnimationListener l) {
		if (listener!=null) 
			throw new IllegalStateException("last listener is not trigger");
		if (anims.size()==0) 
			l.onFinish(tag);
		else {
			listener = l;
			listenerTag = tag;
		}
	}

	
	public void addAnim(AnimTile anim) {
		anims.add(anim);
		if (anims.size()==1) {
			panel.postInvalidateDelayed(STEP_TIME);
			nextTime = System.currentTimeMillis()+STEP_TIME;
		}		
	}

	public void addAnimAfter(AnimTile anim, AnimTile before) {
		if (before==null)
			addAnim(anim);
		else
			before.after = anim;
	}

	public boolean hasAnimations() { return anims.size()>0; }
	
	// Returns the animation if the tile is animated
	public AnimTile getAnim(Tile tile) {
	   if (anims.size()==0) return null;
	   for(AnimTile as : anims)
		   if (as.tile==tile) return as;
	   return null;
	}

	// Draw animations. Called by onDraw()
	void drawAnims(Canvas canvas) {
	   long tm = System.currentTimeMillis();
	   if (anims.size()==0 || nextTime > tm) return;
	   ListIterator<AnimTile> i = anims.listIterator();   	// Get iterator
	   while( i.hasNext() ) {						
		   AnimTile a = i.next();					// Next animation
		   if (adjust>0)
			   a.steps -= Math.min(adjust, a.steps-1);
		   a.stepDraw(canvas,panel.sideTile);			// draw in next position
		   if (--a.steps<=0)							// Last step?
			   if (a.after!=null) i.set(a.after);		// Swap with the next 
			   else i.remove();		  					// Remove from animation list
	   }
	   adjust = 0;
	   if (anims.size()==0) {
		   if (listener!=null) {
			   OnFinishAnimationListener l = listener;
			   listener = null;
			   l.onFinish(listenerTag);
		   }
	   } else {
		   tm = System.currentTimeMillis();
		   nextTime += STEP_TIME;						// Time for next animation
		   long remain = nextTime - tm;
		   if (remain < 0) {
			   adjust = (int)(-remain / STEP_TIME) + 1;
			   remain += adjust*STEP_TIME;
			   nextTime += adjust*STEP_TIME;
			   //if (adjust>1) STEP_TIME += adjust-1;
		   }
		   panel.postInvalidateDelayed(remain);			// draw past remain time
	   }
	}
	
	private class FloatTile extends AnimTile {
		int fx,fy;  // final position
		
		FloatTile(int xF, int yF, int xTo, int yTo, int tm) {
			super(xF,yF,tm, Animator.this.panel);
			Rect r = panel.tileRect(xTo, yTo);
			fx = r.left; fy = r.top;
		}
		public void stepDraw(Canvas cv, int side) {
			x += (fx-x)/steps;
			y += (fy-y)/steps;
			super.stepDraw(cv,side);
		}		
	}

	/**
	 *  Animate the tile from one position to another.
	 * @param xFrom  x coordinate of original position
	 * @param yFrom  y coordinate of original position
	 * @param xTo  x coordinate of destination position
	 * @param yTo  y coordinate of destination position
	 * @param time total time of animation 
	 * @return 
	 */
	public AnimTile floatTile(int xFrom, int yFrom, int xTo, int yTo, int time) {
		Tile t = panel.getTile(xFrom,yFrom);
		panel.setTileNoInvalidate(xTo,yTo, t);
		int delta = Math.abs(xTo-xFrom + yTo-yFrom);
		AnimTile ma = new FloatTile(xFrom, yFrom, xTo, yTo, delta*time);
		addAnimAfter(ma, getAnim(t));
		panel.setTileNoInvalidate(xFrom, yFrom, null);
		return ma;
	}
	
	private class ExpandTile extends AnimTile {
		float factor = 0.1F;
		int xf, yf;
		Tile t; int xt, yt;
		
		ExpandTile(int x, int y, int tm, Tile newTile, TilePanel p) {
			super(x, y, tm, p);
			if (newTile!=null) {
				t = newTile;
				xt = x; yt = y;
			}
			float dif = p.sideTile*(1-factor)/2;
			xf = this.x; yf = this.y;
			this.x+=(int)dif; this.y+=(int)dif;
		}
		public void stepDraw(Canvas cv, int side) {
			if (t!=null) {
				panel.setTileNoInvalidate(xt, yt, t);
				tile = t;
				t=null;
			}
			x += (xf-x)/steps;
			y += (yf-y)/steps;
			factor += (1-factor)/steps;
			super.stepDraw(cv, (int)(side*factor));
		}				
	}
	
	/**
	 * Expands the size of the mosaic from zero to the natural dimension
	 * @param x
	 * @param y
	 * @param time
	 * @param before
	 * @param newTile
	 */
	public AnimTile expandTile(int x, int y, int time, AnimTile before, Tile newTile) {
		AnimTile et = new ExpandTile(x, y, time, newTile, Animator.this.panel);
		addAnimAfter(et, before);
		return et;
	}

}