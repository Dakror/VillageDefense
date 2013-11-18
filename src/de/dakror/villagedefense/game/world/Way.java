package de.dakror.villagedefense.game.world;

import java.awt.Graphics2D;
import java.awt.Point;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.util.Assistant;

/**
 * @author Dakror
 */
public class Way extends Tile
{
	public static final float speed = 3.5f;
	
	public Way()
	{
		super(3, "Weg", "way.png");
	}
	
	@Override
	public Point getTexturePos(int x, int y)
	{
		byte[][] n = Game.world.getNeighbors(x, y);
		
		if (n[0][1] != id && n[1][0] != id && n[2][1] != id && n[1][2] != id) return new Point(0, 0); // none adjacent
		
		int c = 0;
		if (n[0][1] == id) c++;
		if (n[2][1] == id) c++;
		if (n[1][0] == id) c++;
		if (n[1][2] == id) c++;
		
		if (c == 1)
		{
			if (n[0][1] == id) return new Point(1, 3); // l
			if (n[1][0] == id) return new Point(0, 4); // t
			if (n[2][1] == id) return new Point(0, 3); // r
			if (n[1][2] == id) return new Point(1, 4); // b
		}
		else if (c == 2)
		{
			if (n[0][1] == id && n[2][1] == id) return new Point(2, 0); // l - r
			if (n[1][0] == id && n[1][2] == id) return new Point(2, 1); // t - b
			
			if (n[2][1] == id && n[1][2] == id) return new Point(0, 1); // r - b
			if (n[0][1] == id && n[1][2] == id) return new Point(1, 1); // l - b
			if (n[2][1] == id && n[1][0] == id) return new Point(0, 2); // r - t
			if (n[0][1] == id && n[1][0] == id) return new Point(1, 2); // l - t
		}
		else if (c == 3)
		{
			if (n[1][0] != id) return new Point(2, 2);
			if (n[1][2] != id) return new Point(2, 3);
			if (n[0][1] != id) return new Point(2, 4);
			if (n[2][1] != id) return new Point(2, 5);
		}
		
		return new Point(1, 0);
	}
	
	@Override
	public void drawTile(int cx, int cy, int i, int j, Graphics2D g)
	{
		int x = i * Tile.SIZE;
		int y = j * Tile.SIZE;
		
		Point tp = getTexturePos(cx * Chunk.SIZE + i, cy * Chunk.SIZE + j);
		
		Assistant.drawImage(Game.getImage("tile/" + getTileset()), x, y, SIZE, SIZE, tp.x * SIZE, tp.y * SIZE, SIZE, SIZE, g);
	}
}
