package de.dakror.villagedefense.game.projectile;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.util.Drawable;
import de.dakror.villagedefense.util.Vector;


/**
 * @author Dakror
 */
public class Projectile implements Drawable
{
	Vector pos;
	Entity target;
	Image image;
	float speed;
	float angle;
	int damage;
	boolean dead;
	
	public Projectile(Vector pos, Entity target, String image, float speed, int damage)
	{
		this.pos = pos;
		this.target = target;
		this.image = Game.getImage("particle/" + image + ".png");
		this.speed = speed;
		this.damage = damage;
		dead = false;
		angle = 0;
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		if (dead) return;
		
		AffineTransform old = g.getTransform();
		AffineTransform at = AffineTransform.getRotateInstance(angle, pos.x + Game.w.getInsets().left, pos.y + Game.w.getInsets().top);
		g.setTransform(at);
		g.drawImage(image, (int) pos.x + Game.w.getInsets().left, (int) pos.y + Game.w.getInsets().top, Game.w);
		g.setTransform(old);
	}
	
	@Override
	public void update(int tick)
	{
		Vector dif = target.getCenter2().sub(pos);
		if (dif.getLength() > speed) dif.setLength(speed);
		
		angle = (float) Math.atan2(dif.y, dif.x);
		
		pos.add(dif);
		
		if (pos.equals(target.getCenter2()))
		{
			target.dealDamage(damage);
			dead = true;
		}
	}
	
	public boolean isDead()
	{
		return dead;
	}
	
	public Vector getPos()
	{
		return pos;
	}
	
	public Entity getTarget()
	{
		return target;
	}
	
	public Image getImage()
	{
		return image;
	}
	
	public float getSpeed()
	{
		return speed;
	}
	
	public float getAngle()
	{
		return angle;
	}
	
	public int getDamage()
	{
		return damage;
	}
}
