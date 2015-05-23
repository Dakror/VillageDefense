/*******************************************************************************
 * Copyright 2015 Maximilian Stark | Dakror <mail@dakror.de>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
 

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
public class CoalFactory extends Struct {
	public CoalFactory(int x, int y) {
		super(x, y, 6, 7);
		tx = 8;
		ty = 13;
		placeGround = true;
		name = "Köhlerei";
		setBump(new Rectangle2D.Float(0.9f, 3.5f, 4.8f, 3.4f));
		attributes.set(Attribute.MINE_SPEED, 300);
		attributes.set(Attribute.MINE_AMOUNT, 3); // use 3 get 2
		
		buildingCosts.set(Resource.GOLD, 375);
		buildingCosts.set(Resource.WOOD, 150);
		buildingCosts.set(Resource.STONE, 225);
		buildingCosts.set(Resource.PEOPLE, 2);
		
		description = "Erzeugt Kohle mit Holz.";
	}
	
	@Override
	public Resources getResourcesPerSecond() {
		Resources res = new Resources();
		
		if (!working) return res;
		
		res.set(Resource.WOOD, Game.currentGame.getUPS2() / attributes.get(Attribute.MINE_SPEED) * (-attributes.get(Attribute.MINE_AMOUNT)));
		res.set(Resource.COAL, Game.currentGame.getUPS2() / attributes.get(Attribute.MINE_SPEED) * 2);
		
		return res;
	}
	
	@Override
	protected void tick(int tick) {
		super.tick(tick);
		
		if (tick % attributes.get(Attribute.MINE_SPEED) == 0 && Game.currentGame.resources.get(Resource.WOOD) >= attributes.get(Attribute.MINE_AMOUNT) && working) {
			Game.currentGame.resources.add(Resource.WOOD, (int) -attributes.get(Attribute.MINE_AMOUNT));
			resources.add(Resource.COAL, 2);
		}
	}
	
	@Override
	public void initGUI() {}
	
	@Override
	protected void onMinedUp() {}
	
	@Override
	public void onUpgrade(Researches research, boolean inititial) {}
	
	@Override
	public Entity clone() {
		return new CoalFactory((int) x / Tile.SIZE, (int) y / Tile.SIZE);
	}
	
	@Override
	protected void onDeath() {}
}
