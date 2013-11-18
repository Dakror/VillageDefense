package de.dakror.villagedefense.game.entity.creature;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.struct.Struct;
import de.dakror.villagedefense.game.entity.struct.WheatField;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.util.Assistant;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public class Farmer extends Creature
{
	int range;
	Vector spawn;
	
	public Farmer(int x, int y)
	{
		super(x, y, "farmer");
		
		setHostile(false);
		name = "Bauer";
		
		attributes.set(Attribute.ATTACK_SPEED, 30 * 45); // 45 second plant cooldown
		
		range = 5 * Tile.SIZE; // 5 fields plant radius
		
		spawn = new Vector(spawnPoint);
		spawn.y -= 1 * Tile.SIZE;
		
		attributes.set(Attribute.MINE_AMOUNT, 3);
		
		description = "Pflanzt und erntet Weizenfelder um sein Haus herum.";
	}
	
	@Override
	public void tick(int tick)
	{
		super.tick(tick);
		
		if (target == null && targetEntity == null)
		{
			if ((tick + randomOffset) % attributes.get(Attribute.ATTACK_SPEED) == 0)
			{
				if (!getPos().equals(new Vector(spawnPoint))) Game.world.addEntity(new WheatField(Assistant.round(Math.round(getPos().x), Tile.SIZE) / Tile.SIZE, Assistant.round(Math.round(getPos().y), Tile.SIZE) / Tile.SIZE), false);
				setTarget(lookupPlantTarget(), false);
			}
			else
			{
				setTarget(lookupHarvestTarget(), false);
			}
		}
	}
	
	public Entity lookupHarvestTarget()
	{
		for (Entity e : Game.world.entities)
		{
			if (e instanceof WheatField && e.getCenter().getDistance(spawn) <= range && ((WheatField) e).getPhase() == 3)
			{
				return e;
			}
		}
		
		return null;
	}
	
	public Vector lookupPlantTarget()
	{
		float rad = (float) Math.toRadians(Math.random() * 360);
		float hyp = (float) Math.random() * range - Tile.SIZE;
		
		int x = Assistant.round(Math.round(spawn.x + (float) Math.cos(rad) * hyp), Tile.SIZE);
		int y = Assistant.round(Math.round(spawn.y + (float) Math.sin(rad) * hyp), Tile.SIZE);
		int my = Assistant.round(Game.world.height / 2, Tile.SIZE);
		
		if (x < 0 || y < 100 || x >= Game.world.width || y >= Game.world.height - 120 || y == my || y == my - Tile.SIZE) return lookupPlantTarget();
		
		if (Game.world.isFreeTile(x, y)) return new Vector(x, y);
		else return lookupPlantTarget();
	}
	
	@Override
	public void onSpawn(boolean initial)
	{
		Game.currentGame.resources.add(Resource.PEOPLE, 1);
	}
	
	@Override
	public void onDeath()
	{
		super.onDeath();
		Game.currentGame.resources.add(Resource.PEOPLE, -1);
	}
	
	@Override
	protected boolean onArrivalAtEntity(int tick)
	{
		if (targetEntity instanceof WheatField)
		{
			if ((tick + randomOffset) % targetEntity.getAttributes().get(Attribute.MINE_SPEED) == 0 && targetEntity.getResources().size() > 0)
			{
				if (frame % 2 == 0) ((Struct) targetEntity).mineAllResources((int) attributes.get(Attribute.MINE_AMOUNT));
				frame++;
			}
			return true;
		}
		return false;
	}
	
	@Override
	public Entity clone()
	{
		return new Farmer((int) x, (int) y);
	}
}