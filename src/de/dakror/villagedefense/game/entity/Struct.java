package de.dakror.villagedefense.game.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.tile.Tile;
import de.dakror.villagedefense.game.world.World;
import de.dakror.villagedefense.settings.StructPoints;

/**
 * @author Dakror
 */
public abstract class Struct extends Entity
{
	protected int tx, ty;
	protected boolean placeGround;
	protected StructPoints structPoints;
	
	public Struct(int x, int y, int width, int height)
	{
		super(x * Tile.SIZE, y * Tile.SIZE, width * Tile.SIZE, height * Tile.SIZE);
		
		structPoints = new StructPoints();
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		drawBump(g, false);
		
		g.drawImage(Game.getImage("structs.png"), (int) x, (int) y, (int) x + width, (int) y + height, tx * Tile.SIZE, ty * Tile.SIZE, tx * Tile.SIZE + width, ty * Tile.SIZE + height, Game.w);
		
		drawBump(g, true);
	}
	
	public void setBump(Rectangle2D r)
	{
		super.setBump(new Rectangle((int) Math.round(r.getX() * Tile.SIZE), (int) Math.round(r.getY() * Tile.SIZE), (int) Math.round(r.getWidth() * Tile.SIZE), (int) Math.round(r.getHeight() * Tile.SIZE)));
	}
	
	public void placeGround(World w)
	{
		if (!placeGround) return;
		
		int x = (int) Math.round(bump.getX() / Tile.SIZE) - 1;
		int y = (int) Math.round(bump.getY() / Tile.SIZE) - 1;
		int width = (int) Math.round(bump.getWidth() / Tile.SIZE) + 2;
		int height = (int) Math.round(bump.getHeight() / Tile.SIZE) + 2;
		
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				w.setTileId((int) this.x / Tile.SIZE + x + i, (int) this.y / Tile.SIZE + y + j, Tile.ground.getId());
			}
		}
	}
	
	public boolean isPlaceGround()
	{
		return placeGround;
	}
	
	public StructPoints getStructPoints()
	{
		return structPoints;
	}
}
