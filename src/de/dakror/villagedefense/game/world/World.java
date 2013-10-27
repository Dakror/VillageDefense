package de.dakror.villagedefense.game.world;

import java.awt.Graphics2D;
import java.util.ArrayList;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.block.Tile;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.util.Drawable;

/**
 * @author Dakror
 */
public class World implements Drawable
{
	public static final int FILED_SIZE = 32;
	
	public int x, y, width, height;
	
	Chunk[][] chunks;
	
	ArrayList<Entity> entities = new ArrayList<>();
	
	public World()
	{
		x = y = 0;
		width = Game.w.getWidth();
		height = Game.w.getHeight();
		
		chunks = new Chunk[(int) Math.ceil(width / (float) (Chunk.SIZE * Tile.SIZE))][(int) Math.ceil(height / (float) (Chunk.SIZE * Tile.SIZE))];
		for (int i = 0; i < chunks.length; i++)
			for (int j = 0; j < chunks[0].length; j++)
				chunks[i][j] = new Chunk(i, j);
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		g.translate(x, y);
		for (int i = 0; i < chunks.length; i++)
			for (int j = 0; j < chunks[0].length; j++)
				chunks[i][j].draw(g);
	}
	
	@Override
	public void update()
	{}
}
