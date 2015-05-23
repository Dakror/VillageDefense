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

import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class Warehouse extends Struct {
	public Warehouse(int x, int y) {
		super(x, y, 6, 5);
		tx = 0;
		ty = 33;
		name = "Lagerhaus";
		
		setBump(new Rectangle2D.Float(0.2f, 2.6f, 5.52f, 2.3f));
		
		placeGround = true;
		buildingCosts.set(Resource.GOLD, 350);
		buildingCosts.set(Resource.WOOD, 100);
		buildingCosts.set(Resource.STONE, 65);
		description = "Lagert Resourcen.";
	}
	
	@Override
	public void initGUI() {}
	
	@Override
	protected void onMinedUp() {}
	
	@Override
	public void onUpgrade(Researches research, boolean initial) {}
	
	@Override
	public Entity clone() {
		return new Warehouse((int) x / Tile.SIZE, (int) y / Tile.SIZE);
	}
	
	@Override
	protected void onDeath() {}
}
