package de.dakror.villagedefense.game.entity.struct;

import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.util.Assistant;

/**
 * @author Dakror
 */
public class Way extends Struct
{
	public Way(int x, int y)
	{
		super(x, y, 1, 1);
		tx = 6;
		ty = 7;
		
		name = "Weg";
		
		buildingCosts.set(Resource.GOLD, 5);
		canDragBuild = true;
		
		description = "Weg, auf dem Einwohner und Träger schneller laufen.";
		setBump(new Rectangle2D.Float(0, 0, 1, 1));
	}
	
	@Override
	public void onSpawn(boolean initial)
	{
		if (!initial) Game.world.setTileId(Assistant.round((int) x, Tile.SIZE) / Tile.SIZE, Assistant.round((int) y, Tile.SIZE) / Tile.SIZE, Tile.way.getId());
		dead = true;
	}
	
	@Override
	public void initGUI()
	{}
	
	@Override
	protected void onMinedUp()
	{}
	
	@Override
	public void onUpgrade(Researches research, boolean initial)
	{}
	
	@Override
	public Entity clone()
	{
		return new Way((int) x / Tile.SIZE, (int) y / Tile.SIZE);
	}
	
	@Override
	protected void onDeath()
	{}
	
}
