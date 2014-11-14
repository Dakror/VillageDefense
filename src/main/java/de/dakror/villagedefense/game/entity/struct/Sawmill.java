package de.dakror.villagedefense.game.entity.struct;

import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class Sawmill extends Struct {
	public Sawmill(int x, int y) {
		super(x, y, 4, 3);
		
		name = "Sägewerk";
		placeGround = true;
		tx = 0;
		ty = 13;
		setBump(new Rectangle2D.Float(0.25f, 2, 3.5f, 1));
		
		buildingCosts.set(Resource.GOLD, 300);
		buildingCosts.set(Resource.WOOD, 120);
		buildingCosts.set(Resource.STONE, 50);
		buildingCosts.set(Resource.IRONINGOT, 40);
		buildingCosts.set(Resource.PEOPLE, 1);
		
		attributes.set(Attribute.MINE_SPEED, 300);
		attributes.set(Attribute.MINE_AMOUNT, 1); // use 1 get 2
		
		description = "Sägt Holz in Bretter.";
		canHunger = true;
	}
	
	@Override
	public Resources getResourcesPerSecond() {
		Resources res = new Resources();
		
		if (!working) return res;
		
		res.set(Resource.WOOD, Game.currentGame.getUPS2() / attributes.get(Attribute.MINE_SPEED) * (-attributes.get(Attribute.MINE_AMOUNT)));
		res.set(Resource.PLANKS, Game.currentGame.getUPS2() / attributes.get(Attribute.MINE_SPEED) * attributes.get(Attribute.MINE_AMOUNT) * 2);
		
		return res;
	}
	
	@Override
	public void initGUI() {}
	
	@Override
	protected void onMinedUp() {}
	
	@Override
	protected void tick(int tick) {
		super.tick(tick);
		
		if (tick % attributes.get(Attribute.MINE_SPEED) == 0 && Game.currentGame.resources.get(Resource.WOOD) > 0 && working) {
			Game.currentGame.resources.add(Resource.WOOD, (int) -attributes.get(Attribute.MINE_AMOUNT));
			resources.add(Resource.PLANKS, (int) attributes.get(Attribute.MINE_AMOUNT) * 2);
		}
	}
	
	@Override
	public void onUpgrade(Researches research, boolean inititial) {}
	
	@Override
	public Entity clone() {
		return new Sawmill((int) x / Tile.SIZE, (int) y / Tile.SIZE);
	}
	
	@Override
	protected void onDeath() {}
}
