package de.dakror.villagedefense.game.entity.struct;

import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public class WheatField extends Struct
{
	int startTick;
	
	public WheatField(int x, int y)
	{
		super(x, y, 1, 1);
		
		tx = 5;
		ty = 5;
		name = "Weizenfeld";
		
		attributes.set(Attribute.ATTACK_SPEED, 30 * 30); // 2 minutes 30 (30 ticks * 60 seconds * 2 + 30)
		
		description = "Ein Weizenfeld.";
		startTick = 0;
		
		massive = false;
		
		setBump(new Rectangle2D.Float(0.25f, 0.25f, 0.5f, 0.5f));
		
		structPoints.addEntries(new Vector(0.5f, 0.5f));
	}
	
	@Override
	protected void tick(int tick)
	{
		super.tick(tick);
		if (startTick == 0)
		{
			startTick = tick;
			return;
		}
		
		
		if ((tick - startTick) % attributes.get(Attribute.ATTACK_SPEED) / 4 == 0)
		{
			if (ty != 8)
			{
				ty++;
				image = null;
				if (ty == 8)
				{
					resources.set(Resource.WHEAT, 5);
				}
			}
		}
	}
	
	@Override
	public void initGUI()
	{}
	
	@Override
	protected void onMinedUp()
	{
		onDeath();
	}
	
	@Override
	public void onUpgrade(Researches research, boolean initial)
	{}
	
	public int getPhase()
	{
		return ty - 5;
	}
	
	@Override
	public Entity clone()
	{
		return new WheatField((int) x / Tile.SIZE, (int) y / Tile.SIZE);
	}
	
	@Override
	protected void onDeath()
	{
		dead = true;
	}
}
