package de.dakror.villagedefense.game.entity.struct;

import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.ui.ClickEvent;
import de.dakror.villagedefense.ui.Component;
import de.dakror.villagedefense.ui.button.ResearchButton;

/**
 * @author Dakror
 */
public class Mine extends Struct
{
	public Mine(int x, int y)
	{
		super(x, y, 2, 3);
		tx = 0;
		ty = 10;
		name = "Mine";
		placeGround = true;
		setBump(new Rectangle2D.Double(0.1f, 2, 1.8f, 1));
		
		attributes.set(Attribute.MINE_SPEED, 40);
		attributes.set(Attribute.MINE_AMOUNT, 2);
		
		buildingCosts.set(Resource.GOLD, 125);
		buildingCosts.set(Resource.WOOD, 65);
		buildingCosts.set(Resource.STONE, 30);
		buildingCosts.set(Resource.PEOPLE, 2);
		
		researches.add(Researches.MINE_STONE);
		
		description = "Baut untertage Metal oder Stein ab.";
		canHunger = true;
	}
	
	@Override
	protected void onMinedUp()
	{}
	
	@Override
	protected void tick(int tick)
	{
		super.tick(tick);
		
		if (tick % attributes.get(Attribute.MINE_SPEED) == 0 && working)
		{
			if (has(Researches.MINE_STONE)) resources.add(Resource.STONE, (int) attributes.get(Attribute.MINE_AMOUNT));
			else if (has(Researches.MINE_IRON)) resources.add(Resource.IRONORE, (int) attributes.get(Attribute.MINE_AMOUNT));
		}
	}
	
	@Override
	public Resources getResourcesPerSecond()
	{
		Resources res = new Resources();
		
		if (!working) return res;
		
		if (has(Researches.MINE_STONE)) res.set(Resource.STONE, Game.currentGame.getUPS2() / attributes.get(Attribute.MINE_SPEED) * attributes.get(Attribute.MINE_AMOUNT));
		if (has(Researches.MINE_IRON)) res.set(Resource.IRONORE, Game.currentGame.getUPS2() / attributes.get(Attribute.MINE_SPEED) * attributes.get(Attribute.MINE_AMOUNT));
		
		return res;
	}
	
	@Override
	public Entity clone()
	{
		return new Mine((int) x / Tile.SIZE, (int) y / Tile.SIZE);
	}
	
	@Override
	protected void onDeath()
	{}
	
	@Override
	public void initGUI()
	{}
	
	@Override
	public void initUpgrades()
	{
		super.initUpgrades();
		
		for (Component c : components)
		{
			if (c instanceof ResearchButton)
			{
				final ResearchButton rb = (ResearchButton) c;
				rb.addClickEvent(new ClickEvent()
				{
					@Override
					public void trigger()
					{
						researches.remove(Researches.MINE_STONE);
						researches.remove(Researches.MINE_IRON);
						
						for (Component c : components)
							if (c instanceof ResearchButton) ((ResearchButton) c).setContains(false);
						
						rb.setContains(true);
						researches.add(rb.research);
					}
				});
			}
		}
	}
	
	@Override
	public void onUpgrade(Researches research, boolean inititial)
	{}
}
