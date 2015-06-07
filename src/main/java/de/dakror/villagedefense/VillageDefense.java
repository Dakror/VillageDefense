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


package de.dakror.villagedefense;

import javax.swing.UIManager;

import de.dakror.dakrorbin.DakrorBin;
import de.dakror.dakrorbin.Launch;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.UpdateThread;
import de.dakror.villagedefense.settings.CFG;

/**
 * @author Dakror
 */
public class VillageDefense {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Launch.init(args);
		
		new Game();
		Game.currentFrame.init("Village Defense");
		
		DakrorBin.init(Game.w, "VillageDefense");
		
		Game.currentFrame.setFullscreen();
		Game.currentFrame.updater = new UpdateThread();
		
		CFG.init();
		
		while (true)
			Game.currentFrame.main();
	}
}
