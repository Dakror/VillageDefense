package de.dakror.villagedefense.game.entity.creature;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class Zombie extends Creature {
	public Zombie(int x, int y) {
		super(x, y, "zombie");
		
		setHostile(true);
		attributes.set(Attribute.DAMAGE_STRUCT, 5);
		attributes.set(Attribute.HEALTH, 20);
		attributes.set(Attribute.HEALTH_MAX, 20);
		
		resources.add(Resource.GOLD, 6);
		
		name = "Zombie";
		
		description = "Ein Zombie.";
	}
	
	@Override
	public void onDeath() {
		if (Math.random() <= 0.35) // 35% prob. spawning ghost
		{
			boolean left = Math.random() < 0.5;
			Game.world.addEntity(new Ghost(left ? 0 : Game.world.width, Game.world.height / 2), false);
		}
		super.onDeath();
	}
	
	@Override
	protected boolean onArrivalAtEntity(int tick) {
		return false;
	}
	
	@Override
	public Entity clone() {
		return new Zombie((int) x, (int) y);
	}
}
