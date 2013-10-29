package de.dakror.villagedefense.game.entity;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import de.dakror.villagedefense.game.Game;

/**
 * @author Dakror
 */
public class Creature extends Entity
{
	Image image;
	
	public Creature(int x, int y, String img)
	{
		super(x, y, Game.getImage("char/" + img + ".png").getWidth() / 4, Game.getImage("char/" + img + ".png").getHeight() / 4);
		image = Game.getImage("char/" + img + ".png");
		
		setBump(new Rectangle((int) (width * 0.25f), (int) (height * 0.75f), (int) (width * 0.5f), (int) (height * 0.25f)));
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		drawBump(g, false);
		
		g.drawImage(image, x, y, x + width, y + height, 0, 0, width, height, Game.w);
		
		drawBump(g, true);
	}
	
	@Override
	public void update()
	{}
}
