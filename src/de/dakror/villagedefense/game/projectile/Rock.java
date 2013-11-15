package de.dakror.villagedefense.game.projectile;

import java.awt.Shape;
import java.awt.geom.Arc2D;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.animation.Animation;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.creature.Creature;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public class Rock extends Projectile
{
	Vector t;
	int damage;
	int radius;
	
	public Rock(Vector pos, Vector target, float speed, int damage, int radius)
	{
		super(pos, "rock", speed);
		t = target;
		this.radius = radius;
		this.damage = damage;
	}
	
	@Override
	protected Vector getTargetVector()
	{
		return t;
	}
	
	@Override
	protected void onImpact()
	{
		Game.world.addAnimation(new Animation(Math.round(getPos().x - radius), Math.round(getPos().y - radius), Math.round(radius * 2), 2, "rock_impact"));
		Shape s = new Arc2D.Float(getPos().x - radius, getPos().y - radius, radius * 2, radius * 2, 0, 360, Arc2D.OPEN);
		for (Entity e : Game.world.entities)
		{
			if (e instanceof Creature)
			{
				Creature c = (Creature) e;
				if (c.isHostile() && s.intersects(c.getBump(true))) c.dealDamage(damage, this);
			}
		}
	}
}
