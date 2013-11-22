package de.dakror.villagedefense.game.entity.struct.tower;

import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class ArrowTower extends Tower
{
	public ArrowTower(int x, int y)
	{
		super(x, y);
		
		name = "Pfeil-Turm";
		color = 0;
		spheres = 1;
		attributes.set(Attribute.ATTACK_RANGE, Tile.SIZE * 5);
		attributes.set(Attribute.ATTACK_SPEED, 30);
		attributes.set(Attribute.DAMAGE_CREATURE, 3);
		
		buildingCosts.set(Resource.GOLD, 75);
		buildingCosts.set(Resource.STONE, 15);
		
		researchClass = Tower.class;
		
		description = "Schießt mit Pfeilen auf Monster.";
	}
	
	@Override
	public Entity clone()
	{
		return new ArrowTower((int) x / Tile.SIZE, (int) y / Tile.SIZE);
	}
}
