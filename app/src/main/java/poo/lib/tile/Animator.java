package poo.lib.tile;

import java.util.LinkedList;
import java.util.ListIterator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Animator {

	private LinkedList<AnimTile> anims = new LinkedList<>();
	private TilePanel panel;

	private long nextTime;
	private int adjust = 0;

	// Time interval for steps of animations
	static int STEP_TIME = 40; 

	public Animator(TilePanel p) {
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
		   if (as.tile1==tile) return as;
	   return null;
	}

	// Draw animations. Called by onDraw()
	public void drawAnims(Canvas canvas, Paint paint) {
	   long tm = System.currentTimeMillis();
	   if (anims.size()==0 || nextTime > tm) return;
	   ListIterator<AnimTile> i = anims.listIterator();   	// Get iterator
	   while( i.hasNext() ) {
		   AnimTile a = i.next();					// Next animation
		   if (adjust>0)
			   a.steps -= Math.min(adjust, a.steps-1);
		   a.stepDraw(canvas,panel.sideTile,paint);			// draw in next position
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


}