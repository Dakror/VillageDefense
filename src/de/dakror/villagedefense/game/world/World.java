package de.dakror.villagedefense.game.world;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Creature;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.Struct;
import de.dakror.villagedefense.game.entity.creature.Villager;
import de.dakror.villagedefense.game.entity.creature.Zombie;
import de.dakror.villagedefense.game.entity.struct.CoreHouse;
import de.dakror.villagedefense.game.entity.struct.House;
import de.dakror.villagedefense.game.entity.struct.Rock;
import de.dakror.villagedefense.game.entity.struct.Tree;
import de.dakror.villagedefense.game.entity.struct.tower.ArrowTower;
import de.dakror.villagedefense.game.tile.Tile;
import de.dakror.villagedefense.util.Drawable;
import de.dakror.villagedefense.util.EventListener;

/**
 * @author Dakror
 */
public class World extends EventListener implements Drawable
{
	public int width, height;
	
	Chunk[][] chunks;
	
	public Struct core;
	
	public Entity selectedEntity;
	
	public ArrayList<Entity> entities = new ArrayList<>();
	
	public World()
	{
		width = Game.getWidth();
		height = Game.getHeight();
	}
	
	public void init()
	{
		chunks = new Chunk[(int) Math.ceil(width / (float) (Chunk.SIZE * Tile.SIZE))][(int) Math.ceil(height / (float) (Chunk.SIZE * Tile.SIZE))];
		for (int i = 0; i < chunks.length; i++)
			for (int j = 0; j < chunks[0].length; j++)
				chunks[i][j] = new Chunk(i, j);
		
		generate();
		render();
	}
	
	public void setTileId(int x, int y, byte d)
	{
		Point index = getChunk(x, y);
		
		if (index.x < 0 || index.y < 0 || index.x >= chunks.length || index.y >= chunks[index.x].length) return;
		
		chunks[index.x][index.y].setTileId(x - index.x * Chunk.SIZE, y - index.y * Chunk.SIZE, d);
	}
	
	public byte getTileId(int x, int y)
	{
		Point index = getChunk(x, y);
		
		if (index.x < 0 || index.y < 0 || index.x >= chunks.length || index.y >= chunks[index.x].length) return Tile.emtpy.getId();
		
		return chunks[index.x][index.y].getTileId(x - index.x * Chunk.SIZE, y - index.y * Chunk.SIZE);
	}
	
	/**
	 * @return <table>
	 *         <tr>
	 *         <td>(0|0)</td>
	 *         <td>(1|0)</td>
	 *         <td>(2|0)</td>
	 *         </tr>
	 *         <tr>
	 *         <td>(0|1)</td>
	 *         <td>(1|1)</td>
	 *         <td>(2|1)</td>
	 *         </tr>
	 *         <tr>
	 *         <td>(0|2)</td>
	 *         <td>(1|2)</td>
	 *         <td>(2|2)</td>
	 *         </tr>
	 *         </table>
	 */
	public byte[][] getNeighbors(int x, int y)
	{
		byte[][] data = new byte[3][3];
		for (int i = -1; i < 2; i++)
		{
			for (int j = -1; j < 2; j++)
			{
				data[i + 1][j + 1] = getTileId(x + i, y + j);
			}
		}
		return data;
	}
	
	public Point getChunk(int x, int y)
	{
		return new Point((int) Math.floor(x / (float) Chunk.SIZE), (int) Math.floor(y / (float) Chunk.SIZE));
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
				float dif = (o1.getY() + o1.getHeight()) - (o2.getY() + o2.getHeight());
				if (dif < 0) return -1;
				else if (dif > 0) return 1;
				return 0;
			}
		});
		
		for (Entity e : sorted)
			e.draw(g);
	}
	
	public void generate()
	{
		int x = (int) Math.floor(width / 2f / Tile.SIZE);
		int y = (int) Math.floor(height / 2f / Tile.SIZE);
		
		for (int i = 0; i < width / Tile.SIZE; i++)
		{
			setTileId(i, y, Tile.ground.getId());
			setTileId(i, y + 1, Tile.ground.getId());
		}
		
		core = new CoreHouse(x - 2, y - 3);
		addEntity(core);
		
		addEntity(new Rock(x + 7, y + 8));
		addEntity(new House(x - 7, y - 8));
		addEntity(new Tree(x + 7, y - 8));
		addEntity(new ArrowTower(x - 7, y + 1));
		
		addEntity(new Zombie(0, 500));
		addEntity(new Zombie(-40, 500));
	}
	
	@Override
	public void update(int tick)
	{
		try
		{
			for (Entity entity : entities)
			{
				entity.update(tick);
				if (entity.isDead())
				{
					if (entity.equals(selectedEntity)) selectedEntity = null;
					entities.remove(entity);
				}
			}
		}
		catch (ConcurrentModificationException e)
		{}
	}
	
	public void render()
	{
		for (int i = 0; i < chunks.length; i++)
			for (int j = 0; j < chunks[0].length; j++)
				chunks[i][j].render();
	}
	
	public void addEntity(Entity e)
	{
		e.onSpawn();
		entities.add(e);
		
		render();
	}
	
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
		if (e.getButton() == MouseEvent.BUTTON1) // LMB
		{
			selectedEntity = null;
			for (Entity entity : entities)
				entity.setClicked(false);
			for (Entity entity : entities)
			{
				if (entity.mousePressed(e))
				{
					selectedEntity = entity;
					break;
				}
			}
		}
		else if (e.getButton() == MouseEvent.BUTTON3 && selectedEntity != null && selectedEntity instanceof Villager)
		{
			Entity target = null;
			for (Entity entity : entities)
			{
				if (entity.mousePressed(e))
				{
					target = entity;
					break;
				}
			}
			
			if (target != null) ((Creature) selectedEntity).setTarget(target);
		}
	}
}
