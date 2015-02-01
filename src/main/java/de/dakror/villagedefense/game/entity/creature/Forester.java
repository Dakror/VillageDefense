package de.dakror.villagedefense.game.entity.creature;

import de.dakror.gamesetup.util.Helper;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.struct.Tree;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public class Forester extends Creature {
	public Forester(int x, int y) {
		super(x, y, "forester");
		setHostile(false);
		name = "Förster";
		
		attributes.set(Attribute.ATTACK_SPEED, 30 * 45); // 45 second plant cooldown
		attributes.set(Attribute.ATTACK_RANGE, 6 * Tile.SIZE); // 6 fields plant radius
		
		description = "Pflanzt Setzlinge um sein Haus herum.";
		canHunger = true;
	}
	
	@Override
	public void onSpawn(boolean initial) {
		Game.currentGame.resources.add(Resource.PEOPLE, 1);
	}
	
	@Override
	public void onDeath() {
		super.onDeath();
		Game.currentGame.resources.add(Resource.PEOPLE, -1);
	}
	
	@Override
	public void tick(int tick) {
		super.tick(tick);
		
		if (target == null && path == null) {
			if ((tick + randomOffset) % attributes.get(Attribute.ATTACK_SPEED) == 0) {
				if (!getPos().equals(new Vector(spawnPoint)))
					Game.world.addEntity2(new Tree(Helper.round(Math.round(getPos().x), Tile.SIZE) / Tile.SIZE, Helper.round(Math.round(getPos().y), Tile.SIZE) / Tile.SIZE, true), false);
				setTarget(lookupPlantTarget(), false);
			}
		}
	}
	
	public Vector lookupPlantTarget() {
		float rad = (float) Math.toRadians(Math.random() * 360);
		float hyp = (float) Math.random() * attributes.get(Attribute.ATTACK_RANGE);
		
		int x = Helper.round(Math.round(spawnPoint.x + (float) Math.cos(rad) * hyp), Tile.SIZE);
		int y = Helper.round(Math.round(spawnPoint.y + (float) Math.sin(rad) * hyp), Tile.SIZE);
		int my = Helper.round(Game.world.height / 2, Tile.SIZE);
		
		if (x < 0 || y < 100 || x >= Game.world.width || y >= Game.world.height - 120 || y == my || y == my + Tile.SIZE) return lookupPlantTarget();
		
		if (Game.world.isFreeTile(x, y)) return new Vector(x, y);
		else return lookupPlantTarget();
	}
	
	@Override
	protected boolean onArrivalAtEntity(int tick) {
		return false;
	}
	
	@Override
	public Entity clone() {
		return new Forester((int) x, (int) y);
	}
}
