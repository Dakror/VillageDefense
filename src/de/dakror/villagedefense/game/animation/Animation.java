package de.dakror.villagedefense.game.animation;

import java.awt.Graphics2D;
import java.awt.Image;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.util.Assistant;
import de.dakror.villagedefense.util.Drawable;

/**
 * @author Dakror
 */
public class Animation implements Drawable
{
	boolean dead;
	int x;
	int y;
	int size;
	int speed;
	int frame;
	int startFrame;
	String imgName;
	Image image;
	
	public Animation(int x, int y, int size, int speed, String anim)
	{
		this.x = x;
		this.y = y;
		this.size = size;
		this.speed = speed;
		image = Game.getImage("anim/" + anim + ".png");
		imgName = anim;
		frame = 0;
		startFrame = 0;
		dead = false;
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		if (dead) return;
		
		Assistant.drawImage(image, x, y, size, size, frame * 192, 0, 192, 192, g);
	}
	
	@Override
	public void update(int tick)
	{
		if (startFrame == 0) startFrame = tick;
		
		if (dead) return;
		
		if ((tick - startFrame) > 0 && (tick - startFrame) % speed == 0)
		{
			if (frame * 192 >= image.getWidth(null)) dead = true;
			else frame++;
		}
	}
	
	public boolean isDead()
	{
		return dead;
	}
}
