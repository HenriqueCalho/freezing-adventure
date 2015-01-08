package poo.lib.tile;


/**
 * Listener of tiles touches and moves.
 * @author Palex
 */
public interface OnTileTouchListener {
	/**
	 * When a tile is clicked.
	 * @param xTile x coordinate of the tile clicked
	 * @param yTile y coordinate of the tile clicked
	 * @return true if it has effect
	 */
	boolean onClick(int xTile, int yTile);
//	/**
//	 * When a tile is dragged.
//	 * This method must call "setTile" or "floatTile" of TilePanel to change the tiles positions.
//	 * @param xFrom x coordinate of the tile that was trying to drag
//	 * @param yFrom y coordinate of the tile that was trying to drag
//	 * @param xTo x coordinate to drag to
//	 * @param yTo y coordinate to drag to
//	 * @return true if the drag can continue
//	 */
//	boolean onDrag(int xFrom, int yFrom, int xTo, int yTo);

	boolean onDrag(int xTo, int yTo);

	boolean onTouch(int xTile, int yTile);
}