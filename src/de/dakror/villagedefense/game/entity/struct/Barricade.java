package de.dakror.villagedefense.game.entity.struct;

import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.creature.Creature;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public class Barricade extends Struct
{
	public Barricade(int x, int y)
	{
		super(x, y, 1, 3);
		name = "Barrikade";
		tx = 2;
		ty = 10;
		attributes.set(Attribute.HEALTH, 21);
		attributes.set(Attribute.HEALTH_MAX, 21);
		
		buildingCosts.set(Resource.STONE, 5);
		buildingCosts.set(Resource.GOLD, 10);
		canPlaceOnWay = true;
		setBump(new Rectangle2D.Float(0.2f, 1, 0.4f, 2));
		
		structPoints.addAttacks(new Vector(-0.8f, 2), new Vector(0.6f, 2));
	}
	
	@Override
	protected void onMinedUp()
	{}
	
	@Override
	public Entity clone()
	{
		return new Barricade((int) x, (int) y);
	}
	
	@Override
	protected void onDeath()
	{
		dead = true;
	}
	
	@Override
	public void onSpawn()
	{
		super.onSpawn();
		for (Entity e : Game.world.entities)
		{
			if (e instanceof Creature) ((Creature) e).lookupTargetEntity();
		}
	}
	
	@Override
	public void initGUI()
	{}
	
	@Override
	public void onUpgrade(Researches research)
	{}
}
