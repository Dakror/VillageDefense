package de.dakror.villagedefense.game.entity.creature;

import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class Troll extends Creature {
	public Troll(int x, int y) {
		super(x, y, "troll");
		
		name = "Troll";
		attributes.set(Attribute.HEALTH, 500);
		attributes.set(Attribute.HEALTH_MAX, 500);
		attributes.set(Attribute.DAMAGE_STRUCT, 25);
		attributes.set(Attribute.SPEED, 0.6f);
		attributes.set(Attribute.ATTACK_RANGE, Tile.SIZE);
		
		setHostile(true);
		
		resources.set(Resource.GOLD, 150);
		
		description = "Ein Troll.";
	}
	
	@Override
	protected boolean onArrivalAtEntity(int tick) {
		return false;
	}
	
	@Override
	public Entity clone() {
		return new Troll((int) x, (int) y);
	}
}