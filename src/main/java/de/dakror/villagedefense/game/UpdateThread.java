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


package de.dakror.villagedefense.game;

import de.dakror.gamesetup.Updater;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.settings.WaveManager;

/**
 * @author Dakror
 */
public class UpdateThread extends Updater {
	long time2 = 0;
	
	public UpdateThread() {
		super();
	}
	
	@Override
	public void update() {
		if (time2 == 0) time2 = System.currentTimeMillis();
		
		if (System.currentTimeMillis() - time2 > 1000 / speed && Game.currentGame.state == 0) {
			if (WaveManager.nextWave > 0) WaveManager.nextWave--;
			time2 = System.currentTimeMillis();
		}
		
		WaveManager.update();
		
		if (tick % 30 == 0 && Game.currentGame.resources.get(Resource.BREAD) > 0 && Game.currentGame.state == 0) {
			float amount = Game.hungerPerUnitPerSecond * Game.currentGame.resources.get(Resource.PEOPLE);
			float sub = Game.currentGame.resources.get(Resource.BREAD) < amount ? Game.currentGame.resources.get(Resource.BREAD) : amount;
			Game.currentGame.resources.add(Resource.BREAD, -sub);
		}
		
		if (Game.world != null && Game.currentGame.state == 0) Game.world.update(tick);
	}
}
