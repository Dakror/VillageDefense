package de.dakror.villagedefense.game.world;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.Struct;
import de.dakror.villagedefense.game.entity.Structs;
import de.dakror.villagedefense.util.Drawable;
import de.dakror.villagedefense.util.EventListener;

/**
 * @author Dakror
 */
public class World extends EventListener implements Drawable
{
	public int width, height;
	
	Chunk[][] chunks;
	
	public ArrayList<Entity> entities = new ArrayList<>();
	
	public World()
	{
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
		entities.add(new Struct(x, y + 4, Structs.TREE));
	}
	
	@Override
	public void update()
	{}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		for (Entity entity : entities)
			entity.setHovered(false);
		for (Entity entity : entities)
			if (entity.mouseMoved(e)) break;
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		for (Entity entity : entities)
			entity.setClicked(false);
		for (Entity entity : entities)
			if (entity.mousePressed(e)) break;
	}
}
