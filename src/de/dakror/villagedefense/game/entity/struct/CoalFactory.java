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
public class CoalFactory extends Struct
{
	public CoalFactory(int x, int y)
	{
		super(x, y, 6, 7);
		tx = 0;
		ty = 23;
		placeGround = true;
		name = "Köhlerei";
		setBump(new Rectangle2D.Float(0.9f, 3.5f, 4.8f, 3.4f));
		attributes.set(Attribute.MINE_SPEED, 300);
		attributes.set(Attribute.MINE_AMOUNT, 3); // use 3 get 1
		
		buildingCosts.set(Resource.GOLD, 350);
		buildingCosts.set(Resource.WOOD, 150);
		buildingCosts.set(Resource.STONE, 225);
		buildingCosts.set(Resource.PEOPLE, 2);
	}
	
	@Override
	public Resources getResourcesPerSecond()
	{
		Resources res = new Resources();
		
		if (Game.currentGame.resources.get(Resource.WOOD) == 0) return res;
		
		res.set(Resource.WOOD, Game.currentGame.getUPS2() / attributes.get(Attribute.MINE_SPEED) * (-attributes.get(Attribute.MINE_AMOUNT)));
		res.set(Resource.COAL, Game.currentGame.getUPS2() / attributes.get(Attribute.MINE_SPEED) * 1);
		
		return res;
	}
	
	@Override
	protected void tick(int tick)
	{
		super.tick(tick);
		
		if (tick % attributes.get(Attribute.MINE_SPEED) == 0 && Game.currentGame.resources.get(Resource.WOOD) >= attributes.get(Attribute.MINE_AMOUNT))
		{
			Game.currentGame.resources.add(Resource.WOOD, (int) -attributes.get(Attribute.MINE_AMOUNT));
			Game.currentGame.resources.add(Resource.COAL, 1);
		}
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
		return new CoalFactory((int) x, (int) y);
	}
	
	@Override
	protected void onDeath()
	{}
}
