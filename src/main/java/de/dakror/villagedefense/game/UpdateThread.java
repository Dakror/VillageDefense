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
