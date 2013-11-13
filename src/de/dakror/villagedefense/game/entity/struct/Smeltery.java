package de.dakror.villagedefense.game.entity.struct;

import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class Smeltery extends Struct
{
	public Smeltery(int x, int y)
	{
		super(x, y, 7, 7);
		tx = 0;
		ty = 16;
		name = "Schmelze";
		placeGround = true;
		setBump(new Rectangle2D.Float(0, 2, 6, 4.7f));
		
		attributes.set(Attribute.MINE_SPEED, 300);
		attributes.set(Attribute.MINE_AMOUNT, 2); // use 3 ore + 4 coal -> 2 ingot
		
		buildingCosts.set(Resource.GOLD, 450);
		buildingCosts.set(Resource.WOOD, 200);
		buildingCosts.set(Resource.STONE, 275);
		buildingCosts.set(Resource.COAL, 25);
		buildingCosts.set(Resource.PEOPLE, 2);
	}
	
	@Override
	protected void tick(int tick)
	{
		super.tick(tick);
		
		if (tick % attributes.get(Attribute.MINE_SPEED) == 0 && Game.currentGame.resources.get(Resource.IRONORE) >= 3 && Game.currentGame.resources.get(Resource.COAL) >= 4)
		{
			Game.currentGame.resources.add(Resource.IRONORE, -3);
			Game.currentGame.resources.add(Resource.IRONINGOT, (int) attributes.get(Attribute.MINE_AMOUNT));
			Game.currentGame.resources.add(Resource.COAL, -4);
		}
	}
	
	@Override
	public Resources getResourcesPerSecond()
	{
		Resources res = new Resources();
		res.set(Resource.IRONORE, Game.currentGame.getUPS2() / attributes.get(Attribute.MINE_SPEED) * -3);
		res.set(Resource.COAL, Game.currentGame.getUPS2() / attributes.get(Attribute.MINE_SPEED) * -4);
		res.set(Resource.IRONINGOT, Game.currentGame.getUPS2() / attributes.get(Attribute.MINE_SPEED) * attributes.get(Attribute.MINE_AMOUNT));
		
		return res;
	}
	
	@Override
	public void initGUI()
	{}
	
	@Override
	protected void onMinedUp()
	{}
	
	@Override
	public void onUpgrade(Researches research)
	{}
	
	@Override
	public Entity clone()
	{
		return new Smeltery((int) x, (int) y);
	}
	
	@Override
	protected void onDeath()
	{}
	
}
