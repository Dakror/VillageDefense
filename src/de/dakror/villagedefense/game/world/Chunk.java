package de.dakror.villagedefense.game.world;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import de.dakror.villagedefense.game.Game;

/**
 * @author Dakror
 */
public class Chunk
{
	public static final int SIZE = 8;
	
	int x, y;
	
	byte[][] data = new byte[SIZE][SIZE];
	
	BufferedImage image;
	
	public Chunk(int x, int y)
	{
		this.x = x;
		this.y = y;
		
		image = new BufferedImage(SIZE * Tile.SIZE, SIZE * Tile.SIZE, BufferedImage.TYPE_INT_ARGB);
		
		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++)
				data[i][j] = Tile.grass.getId();
	}
	
	public void render()
	{
		image.flush();
		Graphics2D g = (Graphics2D) image.getGraphics();
		
		for (int i = 0; i < SIZE; i++)
		{
			for (int j = 0; j < SIZE; j++)
			{
				int x = i * Tile.SIZE;
				int y = j * Tile.SIZE;
				
				Tile tile = Tile.getTileForId(data[i][j]);
				
				if (tile.equals(Tile.empty)) continue;
				
				Point tp = tile.getTexturePos(this.x * Chunk.SIZE + i, this.y * Chunk.SIZE + j);
				if (tp.x < 3) // convex
				{
					g.drawImage(Game.getImage("tile/" + tile.getTileset()), x, y, x + Tile.SIZE, y + Tile.SIZE, tp.x * Tile.SIZE, tp.y * Tile.SIZE, tp.x * Tile.SIZE + Tile.SIZE, tp.y * Tile.SIZE + Tile.SIZE, null);
				}
				else
				{
					// absolute sizes, not Tile.SIZE, easier to read.
					g.drawImage(Game.getImage("tile/" + tile.getTileset()), x, y, x + Tile.SIZE, y + Tile.SIZE, 32, 64, 64, 96, null);
					
					if (tp.y == 0) g.drawImage(Game.getImage("tile/" + tile.getTileset()), x, y, x + 16, y + 16, 64, 0, 80, 16, null);
					if (tp.y == 1) g.drawImage(Game.getImage("tile/" + tile.getTileset()), x + 16, y, x + 32, y + 16, 80, 0, 96, 16, null);
					if (tp.y == 2) g.drawImage(Game.getImage("tile/" + tile.getTileset()), x, y + 16, x + 16, y + 32, 64, 16, 80, 32, null);
					if (tp.y == 3) g.drawImage(Game.getImage("tile/" + tile.getTileset()), x + 16, y + 16, x + 32, y + 32, 80, 16, 96, 32, null);
				}
			}
		}
	}
	
	public byte getTileId(int x, int y)
	{
		if (x < 0 || y < 0) return Tile.empty.getId();
		
		if (x >= SIZE) x -= this.x * SIZE;
		if (y >= SIZE) y -= this.y * SIZE;
		
		return data[x][y];
	}
	
	public void draw(Graphics2D g)
	{
		g.drawImage(image, x * SIZE * Tile.SIZE, y * SIZE * Tile.SIZE, Game.w);
	}
	
	public void setTileId(int x, int y, byte d)
	{
		data[x][y] = d;
	}
}
