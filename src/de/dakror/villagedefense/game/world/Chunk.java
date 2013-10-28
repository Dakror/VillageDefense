package de.dakror.villagedefense.game.world;

import java.awt.Graphics2D;
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
		
		render();
	}
	
	public void render()
	{
		image.flush();
		Graphics2D g = (Graphics2D) image.getGraphics();
		BufferedImage texture = Game.getImage("tileset.png");
		
		for (int i = 0; i < SIZE; i++)
		{
			for (int j = 0; j < SIZE; j++)
			{
				int x = i * Tile.SIZE;
				int y = j * Tile.SIZE;
				
				Tile tile = Tile.getTileForId(data[i][j]);
				int tx = tile.getTexturePos().x * Tile.SIZE;
				int ty = tile.getTexturePos().y * Tile.SIZE;
				
				g.drawImage(texture, x, y, x + Tile.SIZE, y + Tile.SIZE, tx, ty, tx + Tile.SIZE, ty + Tile.SIZE, null);
			}
		}
	}
	
	public void draw(Graphics2D g)
	{
		g.drawImage(image, x * SIZE * Tile.SIZE, y * SIZE * Tile.SIZE, Game.w);
	}
}
