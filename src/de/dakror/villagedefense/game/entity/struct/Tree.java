package de.dakror.villagedefense.game.entity.struct;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class Tree extends Struct
{
	int startTick;
	
	public Tree(int x, int y, boolean sapling)
	{
		super(x, y, sapling ? 1 : 5, sapling ? 2 : 5);
		
		if (sapling)
		{
			tx = 6;
			ty = 5;
			setBump(new Rectangle2D.Float(0.3f, 1.7f, 0.3f, 0.3f));
			placeGround = false;
			resources.set(Resource.WOOD, 4);
			attributes.set(Attribute.ATTACK_SPEED, 30 * 60 * 4); // 4 minutes (30 ticks * 60 seconds * 4)
			name = "Setzling";
		}
		else
		{
			tx = 0;
			ty = 0;
			setBump(new Rectangle2D.Float(2, 4.3f, 1, 0.6f));
			placeGround = false;
			resources.set(Resource.WOOD, 100);
			name = "Baum";
		}
	}
	
	@Override
	protected void tick(int tick)
	{
		super.tick(tick);
		
		if (isSapling())
		{
			if (startTick == 0) startTick = tick;
			
			if ((tick - startTick) % attributes.get(Attribute.ATTACK_SPEED) == 0 && startTick < tick)
			{
				setGrown();
			}
		}
	}
	
	@Override
	protected void onDeath()
	{
		if (isStump() || isSapling())
		{
			dead = true;
			return;
		}
		
		setStump();
	}
	
	@Override
	protected void onMinedUp()
	{
		onDeath();
	}
	
	@Override
	public Entity clone()
	{
		return new Tree((int) x, (int) y, isSapling());
	}
	
	public boolean isSapling()
	{
		return tx == 6 && ty == 5;
	}
	
	public void setStump()
	{
		width = Tile.SIZE;
		height = Tile.SIZE;
		tx = 7;
		ty = 5;
		x += 2 * Tile.SIZE;
		y += 4 * Tile.SIZE;
		name = "Baumstumpf";
		image = null;
		setBump(new Rectangle2D.Float(0, 0.3f, 1, 0.6f));
		attributes.set(Attribute.MINE_SPEED, 500);
		resources.set(Resource.WOOD, 1);
	}
	
	public void setGrown()
	{
		width = 5 * Tile.SIZE;
		height = 5 * Tile.SIZE;
		tx = 0;
		ty = 0;
		x -= 2 * Tile.SIZE;
		y -= 3 * Tile.SIZE;
		image = null;
		setBump(new Rectangle2D.Float(2, 4.3f, 1, 0.6f));
		placeGround = false;
		resources.set(Resource.WOOD, 100);
		name = "Baum";
	}
	
	public boolean isStump()
	{
		return tx == 7 && ty == 5;
	}
	
	@Override
	public void drawGUI(Graphics2D g)
	{}
	
	@Override
	public void initGUI()
	{}
}
