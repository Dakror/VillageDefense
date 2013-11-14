package de.dakror.villagedefense.game.projectile;

import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public class Arrow extends Projectile
{
	Entity targetEntity;
	int damage;
	
	public Arrow(Vector pos, Entity target, float speed, int damage)
	{
		super(pos, "arrow", speed);
		targetEntity = target;
		this.target = target.getCenter2();
		this.damage = damage;
	}
	
	@Override
	protected void onImpact()
	{
		targetEntity.dealDamage(damage, this);
	}
	
	@Override
	protected Vector getTargetVector()
	{
		return targetEntity.getCenter2();
	}
}
