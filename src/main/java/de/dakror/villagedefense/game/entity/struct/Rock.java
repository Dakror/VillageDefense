package de.dakror.villagedefense.game.entity.struct;

import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class Rock extends Struct {
	public Rock(int x, int y) {
		super(x, y, 1, 1);
		tx = 7;
		ty = 6;
		setBump(new Rectangle2D.Float(0, 0.5f, 1, 0.5f));
		placeGround = false;
		resources.set(Resource.STONE, 50);
		name = "Stein";
		description = "Ein Stein.";
	}
	
	@Override
	protected void onDeath() {
		dead = true;
	}
	
	@Override
	protected void onMinedUp() {
		onDeath();
	}
	
	@Override
	public Entity clone() {
		return new Rock((int) x / Tile.SIZE, (int) y / Tile.SIZE);
	}
	
	@Override
	public void initGUI() {}
	
	@Override
	public void onUpgrade(Researches research, boolean inititial) {}
}
