package de.dakror.villagedefense.game.entity.creature;

import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class Skeleton extends Creature {
	public Skeleton(int x, int y) {
		super(x, y, "skeleton");
		
		name = "Skelett";
		setHostile(true);
		attributes.set(Attribute.SPEED, 1.5f);
		attributes.set(Attribute.DAMAGE_STRUCT, 15);
		attributes.set(Attribute.HEALTH, 50);
		attributes.set(Attribute.HEALTH_MAX, 50);
		
		resources.set(Resource.GOLD, 13);
		
		description = "Ein Skelett.";
	}
	
	@Override
	protected boolean onArrivalAtEntity(int tick) {
		return false;
	}
	
	@Override
	public Entity clone() {
		return new Skeleton((int) x, (int) y);
	}
}
