package de.dakror.villagedefense.game.entity.creature;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.struct.Struct;
import de.dakror.villagedefense.game.entity.struct.Warehouse;
import de.dakror.villagedefense.game.entity.struct.WheatField;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.game.world.Way;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.util.path.AStar;
import de.dakror.villagedefense.util.path.Path;

/**
 * @author Dakror
 */
public class Villager extends Creature
{
	public Villager(int x, int y)
	{
		super(x, y, "villager" + (int) Math.round(Math.random()));
		setHostile(false);
		name = "Einwohner";
		attributes.set(Attribute.HEALTH, 15);
		attributes.set(Attribute.HEALTH_MAX, 15);
		attributes.set(Attribute.MINE_AMOUNT, 10); // transport capacity
		
		description = "Kann Rohstoffe sammeln und in Gebäuden arbeiten.";
		canHunger = true;
	}
	
	@Override
	public void tick(int tick)
	{
		super.tick(tick);
		
		if (getTileIdBelow() == Tile.way.getId()) attributes.set(Attribute.SPEED, Way.speed);
		else attributes.set(Attribute.SPEED, Attribute.SPEED.getDefaultValue());
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
	public Entity clone()
	{
		return new Villager((int) x, (int) y);
	}
	
	@Override
	protected boolean onArrivalAtEntity(int tick)
	{
		if (targetEntity instanceof Struct)
		{
			if (((Struct) targetEntity).getBuildingCosts().get(Resource.PEOPLE) > 0 && !targetByUser)
			{
				alpha = 0;
			}
			else if ((tick + randomOffset) % targetEntity.getAttributes().get(Attribute.MINE_SPEED) == 0 && targetEntity.getResources().size() > 0)
			{
				if (!((Struct) targetEntity).isPlaceGround())
				{
					if (targetEntity instanceof WheatField) return false; // can't mine those
					
					if (frame % 2 == 0) ((Struct) targetEntity).mineAllResources(1, Game.currentGame.resources);
				}
				else
				{
					((Struct) targetEntity).mineAllResources((int) attributes.get(Attribute.MINE_AMOUNT), resources);
					setTarget(getNearestWarehouse(), false);
				}
				frame++;
			}
			else if (targetEntity instanceof Warehouse)
			{
				Game.currentGame.resources.add(resources);
				resources = new Resources();
				frame++;
				targetEntity = null;
			}
			
			return true;
		}
		return false;
	}
	
	public Struct getMostImportantStructToClear()
	{
		Struct struct = null;
		float F = 0;
		
		for (Entity e : Game.world.entities)
		{
			if (e instanceof Struct && ((Struct) e).isPlaceGround() && e.getResources().size() > 0)
			{	
				
			}
		}
		
		return struct;
	}
	
	public Struct getNearestWarehouse()
	{
		Struct nearest = null;
		float distance = 0;
		
		for (Entity e : Game.world.entities)
		{
			if (e instanceof Warehouse)
			{
				Path path = AStar.getPath(getTile(), Game.world.getTile(getTargetForStruct((Struct) e)));
				if (path == null) continue;
				
				path.mul(Tile.SIZE);
				path.translate(0, -bump.y + bump.height);
				
				if (distance == 0 || path.getLength() < distance)
				{
					distance = path.getLength();
					nearest = (Struct) e;
				}
			}
		}
		
		return nearest;
	}
}
