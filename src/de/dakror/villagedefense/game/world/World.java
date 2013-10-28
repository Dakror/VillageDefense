package de.dakror.villagedefense.game.world;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.Struct;
import de.dakror.villagedefense.game.entity.Structs;
import de.dakror.villagedefense.util.Drawable;

/**
 * @author Dakror
 */
public class World implements Drawable
{
	public int x, y, width, height;
	
	Chunk[][] chunks;
	
	public ArrayList<Entity> entities = new ArrayList<>();
	
	public World()
	{
		x = y = 0;
		width = Game.w.getWidth();
		height = Game.w.getHeight();
		
		chunks = new Chunk[(int) Math.ceil(width / (float) (Chunk.SIZE * Tile.SIZE))][(int) Math.ceil(height / (float) (Chunk.SIZE * Tile.SIZE))];
		for (int i = 0; i < chunks.length; i++)
			for (int j = 0; j < chunks[0].length; j++)
				chunks[i][j] = new Chunk(i, j);
		
		generate();
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		g.translate(x, y);
		for (int i = 0; i < chunks.length; i++)
			for (int j = 0; j < chunks[0].length; j++)
				chunks[i][j].draw(g);
		
		@SuppressWarnings("unchecked")
		ArrayList<Entity> sorted = (ArrayList<Entity>) entities.clone();
		Collections.sort(sorted, new Comparator<Entity>()
		{
			@Override
			public int compare(Entity o1, Entity o2)
			{
				return o1.getY() - o2.getY();
			}
		});
		
		for (Entity e : sorted)
			e.draw(g);
	}
	
	public void generate()
	{
		int x = (int) Math.floor(width / 2f / Tile.SIZE) - 2;
		int y = (int) Math.floor(height / 2f / Tile.SIZE) - 3;
		
		entities.add(new Struct(x, y, Structs.CORE_HOUSE));
	}
	
	@Override
	public void update()
	{}
}
