package de.dakror.villagedefense.game.world;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.animation.Animation;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.creature.Creature;
import de.dakror.villagedefense.game.entity.creature.Villager;
import de.dakror.villagedefense.game.entity.struct.CoreHouse;
import de.dakror.villagedefense.game.entity.struct.House;
import de.dakror.villagedefense.game.entity.struct.Rock;
import de.dakror.villagedefense.game.entity.struct.School;
import de.dakror.villagedefense.game.entity.struct.Struct;
import de.dakror.villagedefense.game.entity.struct.Tree;
import de.dakror.villagedefense.game.entity.struct.tower.ArrowTower;
import de.dakror.villagedefense.game.projectile.Projectile;
import de.dakror.villagedefense.util.Drawable;
import de.dakror.villagedefense.util.EventListener;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public class World extends EventListener implements Drawable
{
	public int x, y, width, height;
	
	Chunk[][] chunks;
	
	public Struct core;
	
	public Entity selectedEntity;
	
	public CopyOnWriteArrayList<Entity> entities = new CopyOnWriteArrayList<>();
	public CopyOnWriteArrayList<Projectile> projectiles = new CopyOnWriteArrayList<>();
	public CopyOnWriteArrayList<Animation> animations = new CopyOnWriteArrayList<>();
	
	public World()
	{
		x = y = 0;
		width = Game.getWidth();
		height = Game.getHeight();
		// width = (int) Math.ceil(Game.getWidth() / (float) (Chunk.SIZE * Tile.SIZE)) * Chunk.SIZE * Tile.SIZE;
		// height = (int) Math.ceil(Game.getHeight() / (float) (Chunk.SIZE * Tile.SIZE)) * Chunk.SIZE * Tile.SIZE;
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
		
		if (index.x < 0 || index.y < 0 || index.x >= chunks.length || index.y >= chunks[index.x].length) return Tile.empty.getId();
		
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
		g.translate(x, y);
		
		for (int i = 0; i < chunks.length; i++)
			for (int j = 0; j < chunks[0].length; j++)
				chunks[i][j].draw(g);
		
		for (Entity e : entities)
			e.drawEntity(g);
		
		for (Projectile p : projectiles)
			p.draw(g);
		
		for (Animation a : animations)
			a.draw(g);
		
		g.translate(-x, -y);
	}
	
	public void sortEntities()
	{
		@SuppressWarnings("unchecked")
		ArrayList<Entity> sorted = new ArrayList<>((List<Entity>) entities.clone());
		try
		{
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
		}
		catch (IllegalArgumentException e)
		{}
		entities = new CopyOnWriteArrayList<>(sorted);
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
		
		addEntity(new House(x - 7, y - 8));
		addEntity(new School(x + 1, y - 12));
		
		addEntity(new ArrowTower(x - 3, y));
		addEntity(new ArrowTower(x - 3, y - 3));
		addEntity(new ArrowTower(x + 1, y));
		addEntity(new ArrowTower(x + 1, y - 3));
		
		int heightMalus = 3;
		
		int rocks = (int) (Math.random() * 10) + 10;
		for (int i = 0; i < rocks; i++)
		{
			int x1 = (int) (Math.random() * width / Tile.SIZE);
			if ((width / Tile.SIZE) - x1 < 4) continue;
			
			int y1 = (int) (Math.random() * (height / Tile.SIZE - heightMalus * 4)) + heightMalus;
			if (Math.abs(y1 - y) < 2) continue;
			addEntity(new Rock(x1, y1));
		}
		
		int trees = (int) (Math.random() * 10) + 10;
		for (int i = 0; i < trees; i++)
		{
			int x1 = (int) (Math.random() * width / Tile.SIZE);
			if ((width / Tile.SIZE) - x1 < 4) continue;
			
			int y1 = (int) (Math.random() * (height / Tile.SIZE - heightMalus * 4)) + heightMalus;
			if (Math.abs(y1 - y + 2) < 3) continue;
			addEntity(new Tree(x1, y1, false));
		}
	}
	
	@Override
	public void update(int tick)
	{
		sortEntities();
		
		@SuppressWarnings("unchecked")
		List<Entity> sorted = (List<Entity>) entities.clone();
		Collections.reverse(sorted);
		for (Entity entity : sorted)
		{
			entity.update(tick);
			if (entity.isDead())
			{
				if (entity.equals(selectedEntity)) selectedEntity = null;
				entities.remove(entity);
			}
		}
		
		for (Projectile p : projectiles)
		{
			p.update(tick);
			if (p.isDead()) projectiles.remove(p);
		}
		
		for (Animation a : animations)
		{
			a.update(tick);
			if (a.isDead()) animations.remove(a);
		}
	}
	
	public void render()
	{
		for (int i = 0; i < chunks.length; i++)
			for (int j = 0; j < chunks[0].length; j++)
				chunks[i][j].render();
	}
	
	public boolean addEntity(Entity e)
	{
		for (Entity entity : entities)
		{
			if (e.getBump(true).intersects(entity.getBump(true))) return false;
		}
		entities.add(e);
		e.onSpawn();
		
		render();
		
		return true;
	}
	
	public boolean isFreeTile(int x, int y)
	{
		for (Entity entity : entities)
		{
			if (entity.getBump(true).intersects(new Rectangle(x, y, Tile.SIZE, Tile.SIZE))) return false;
		}
		
		return true;
	}
	
	public void addEntity2(Entity e)
	{
		e.onSpawn();
		entities.add(e);
		
		render();
	}
	
	public void addProjectile(Projectile p)
	{
		projectiles.add(p);
	}
	
	public void addAnimation(Animation a)
	{
		animations.add(a);
	}
	
	public byte[] getData()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			for (Chunk[] c1 : chunks)
			{
				for (Chunk c : c1)
				{
					baos.write(c.getData());
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return baos.toByteArray();
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		e.translatePoint(-x, -y);
		for (Entity entity : entities)
			entity.setHovered(false);
		for (Entity entity : entities)
			if (entity.mouseMoved(e)) break;
		
		e.translatePoint(x, y);
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		if (Game.currentGame.placedStruct) return;
		
		e.translatePoint(-x, -y);
		if (e.getButton() == MouseEvent.BUTTON1) // LMB
		{
			if (selectedEntity != null && selectedEntity instanceof Struct && ((Struct) selectedEntity).guiPoint != null && ((Struct) selectedEntity).components.size() > 0) return;
			
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
			else
			{
				((Creature) selectedEntity).setTargetEntity(null);
				((Creature) selectedEntity).setTarget(new Vector(e.getX(), e.getY()));
			}
		}
		else if (e.getButton() == MouseEvent.BUTTON3) // after Drag
		{
			Entity target = null;
			for (Entity entity : entities)
			{
				if (entity.isClicked() && entity instanceof Villager) continue;
				
				if (entity.mousePressed(e))
				{
					target = entity;
					break;
				}
			}
			
			for (Entity entity : entities)
			{
				if (entity.isClicked() && entity instanceof Villager)
				{
					if (target != null) ((Villager) entity).setTarget(target);
					else
					{
						((Villager) entity).setTargetEntity(null);
						((Villager) entity).setTarget(new Vector(e.getX(), e.getY()));
					}
				}
			}
		}
		
		e.translatePoint(x, y);
	}
}
