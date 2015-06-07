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
import de.dakror.villagedefense.game.entity.creature.Farmer;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class Farm extends Struct {
	public Farm(int x, int y) {
		super(x, y, 5, 8);
		tx = 9;
		ty = 20;
		setBump(new Rectangle2D.Float(0, 4, 5, 4));
		name = "Bauernhof";
		placeGround = true;
		
		buildingCosts.set(Resource.GOLD, 175);
		buildingCosts.set(Resource.WOOD, 50);
		buildingCosts.set(Resource.STONE, 125);
		
		description = "Bietet Platz für einen Bauern, der Weizen anbaut.";
	}
	
	@Override
	public void onSpawn(boolean initial) {
		if (initial) return;
		super.onSpawn(initial);
		Game.world.addEntity2(new Farmer((int) x + 2 * Tile.SIZE, (int) y + 7 * Tile.SIZE).setOrigin(this), false);
	}
	
	@Override
	public void initGUI() {}
	
	@Override
	protected void onMinedUp() {}
	
	@Override
	public void onUpgrade(Researches research, boolean initial) {}
	
	@Override
	public Entity clone() {
		return new Farm((int) x / Tile.SIZE, (int) y / Tile.SIZE);
	}
	
	@Override
	protected void onDeath() {}
}
