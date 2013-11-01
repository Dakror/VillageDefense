package de.dakror.villagedefense.game.entity.struct.tower;

import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;

/**
 * @author Dakror
 */
public class ArrowTower extends Tower
{
	public ArrowTower(int x, int y)
	{
		super(x, y);
		tx = 5;
		ty = 7;
		name = "Pfeil-Turm";
		attributes.set(Attribute.ATTACK_RANGE, Tile.SIZE * 5);
		// attributes.set(Attribute.HEALTH, 50);
		// attributes.set(Attribute.HEALTH_MAX, 50);
		attributes.set(Attribute.ATTACK_SPEED, 35);
		attributes.set(Attribute.DAMAGE_CREATURE, 3);
		
		// buildingCosts.set(Resource.GOLD, 75);
		// buildingCosts.set(Resource.STONE, 15);
	}
	
	@Override
	public Entity clone()
	{
		return new ArrowTower((int) x, (int) y);
	}
}
