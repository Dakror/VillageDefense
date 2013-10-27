package de.dakror.villagedefense.game.entity;

import java.awt.Graphics2D;
import java.awt.Point;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.block.Tile;

/**
 * @author Dakror
 */
public class Struct extends Entity
{
	Point texturePoint;
	
	public Struct(int x, int y, int width, int height, Point texturePoint)
	{
		super(x * Tile.SIZE, y * Tile.SIZE, width * Tile.SIZE, height * Tile.SIZE);
		this.texturePoint = texturePoint;
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		g.drawImage(Game.getImage("structs.png"), x, y, x + width, y + height, texturePoint.x, texturePoint.y, texturePoint.x + width, texturePoint.y + height, Game.w);
	}
	
	@Override
	public void update()
	{}
	
}
