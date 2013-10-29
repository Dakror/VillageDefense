package de.dakror.villagedefense.game.entity;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public class Creature extends Entity
{
	Image image;
	Vector target;
	float speed;
	boolean frozen;
	
	public Creature(int x, int y, String img)
	{
		super(x, y, Game.getImage("char/" + img + ".png").getWidth() / 4, Game.getImage("char/" + img + ".png").getHeight() / 4);
		image = Game.getImage("char/" + img + ".png");
		
		setBump(new Rectangle((int) (width * 0.25f), (int) (height * 0.75f), (int) (width * 0.5f), (int) (height * 0.25f)));
		frozen = false;
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		drawBump(g, false);
		
		g.drawImage(image, (int) x, (int) y, (int) x + width, (int) y + height, 0, 0, width, height, Game.w);
		
		drawBump(g, true);
	}
	
	@Override
	public void update()
	{
		if (!frozen && speed > 0 && target != null)
		{
			Vector pos = getPos();
			Vector dif = target.clone().sub(pos);
			
			if (dif.getLength() < speed) target = null;
			
			dif.setLength(speed);
			
			setPos(pos.add(dif));
		}
	}
	
	public void setTarget(int x, int y)
	{
		setTarget(new Vector(x, y));
	}
	
	public void setTarget(Vector target)
	{
		this.target = target;
	}
	
	public Vector getTarget()
	{
		return target;
	}
	
	public void setSpeed(float speed)
	{
		this.speed = speed;
	}
	
	public float getSpeed()
	{
		return speed;
	}
	
	public void setFrozen(boolean frozen)
	{
		this.frozen = frozen;
	}
	
	public boolean isFrozen()
	{
		return frozen;
	}
}
