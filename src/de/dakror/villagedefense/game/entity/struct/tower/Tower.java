package de.dakror.villagedefense.game.entity.struct.tower;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.creature.Creature;
import de.dakror.villagedefense.game.entity.struct.Struct;
import de.dakror.villagedefense.game.projectile.Projectile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.util.TowerTargetComparator;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public abstract class Tower extends Struct
{
	public Tower(int x, int y)
	{
		super(x, y, 1, 3);
		placeGround = false;
		setBump(new Rectangle2D.Float(0, 2.5f, 1, 0.5f));
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		super.draw(g);
		
		if (hovered || clicked)
		{
			Color oldColor = g.getColor();
			g.setColor(Color.darkGray);
			
			int rad = (int) attributes.get(Attribute.ATTACK_RANGE);
			Vector c = getCenter();
			
			g.drawArc((int) c.x - rad, (int) c.y - rad, rad * 2, rad * 2, 0, 360);
			g.setColor(oldColor);
		}
	}
	
	@Override
	protected void onMinedUp()
	{}
	
	@Override
	protected void tick(int tick)
	{
		super.tick(tick);
		
		if ((tick + randomOffset) % attributes.get(Attribute.ATTACK_SPEED) == 0)
		{
			ArrayList<Creature> t = getTargetableCreatures();
			if (t.size() > 0)
			{
				for (int i = 0; i < t.size(); i++)
				{
					if (t.get(i).willDieFromTargetedProjectiles()) continue;
					
					Game.world.addProjectile(new Projectile(getCenter(), t.get(0), "arrow", 10f, (int) attributes.get(Attribute.DAMAGE_CREATURE)));
					break;
				}
			}
		}
	}
	
	protected ArrayList<Creature> getTargetableCreatures()
	{
		ArrayList<Creature> targetable = new ArrayList<>();
		for (Entity e : Game.world.entities)
		{
			if (!(e instanceof Creature)) continue;
			
			Creature c = (Creature) e;
			if (!c.isHostile()) continue;
			
			if (e.getCenter().getDistance(getCenter()) <= attributes.get(Attribute.ATTACK_RANGE)) targetable.add(c);
		}
		
		Collections.sort(targetable, new TowerTargetComparator());
		
		return targetable;
	}
	
	@Override
	protected void onDeath()
	{}
}
