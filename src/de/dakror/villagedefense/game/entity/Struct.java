package de.dakror.villagedefense.game.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.world.Tile;

/**
 * @author Dakror
 */
public class Struct extends Entity
{
	Structs type;
	
	public Struct(int x, int y, Structs type)
	{
		super(x * Tile.SIZE, y * Tile.SIZE, type.getWidth() * Tile.SIZE, type.getHeight() * Tile.SIZE);
		this.type = type;
		setBump(type.getBump());
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		drawBump(g, false);
		
		g.drawImage(Game.getImage("structs.png"), x, y, x + width, y + height, type.getX() * Tile.SIZE, type.getY() * Tile.SIZE, type.getX() * Tile.SIZE + width, type.getY() * Tile.SIZE + height, Game.w);
		
		drawBump(g, true);
	}
	
	@Override
	public void update()
	{}
	
	@Override
	public void setBump(Rectangle r)
	{
		super.setBump(new Rectangle(r.x * Tile.SIZE, r.y * Tile.SIZE, r.width * Tile.SIZE, r.height * Tile.SIZE));
	}
}
