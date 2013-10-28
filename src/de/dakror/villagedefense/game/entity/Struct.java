package de.dakror.villagedefense.game.entity;

import java.awt.Graphics2D;

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
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		g.drawImage(Game.getImage("structs.png"), x, y, x + width, y + height, type.getX() * Tile.SIZE, type.getY() * Tile.SIZE, type.getX() * Tile.SIZE + width, type.getY() * Tile.SIZE + height, Game.w);
	}
	
	@Override
	public void update()
	{}
	
}
