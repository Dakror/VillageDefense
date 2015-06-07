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


package de.dakror.villagedefense.layer;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import de.dakror.gamesetup.layer.Layer;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.struct.Struct;

/**
 * @author Dakror
 */
public class StructGUILayer extends Layer {
	@Override
	public void draw(Graphics2D g) {
		try {
			if (Game.world.selectedEntity != null && Game.world.selectedEntity instanceof Struct && ((Struct) Game.world.selectedEntity).guiPoint != null)
				((Struct) Game.world.selectedEntity).drawGUI(g);
		} catch (NullPointerException e) {}
	}
	
	@Override
	public void update(int tick) {}
	
	@Override
	public void init() {}
	
	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		
		if (Game.currentGame.placedStruct) return;
		
		if (Game.currentGame.state == 0 && Game.world.selectedEntity != null && Game.world.selectedEntity instanceof Struct)
			if (((Struct) Game.world.selectedEntity).guiPoint != null) Game.world.selectedEntity.mousePressed(e);
	}
}
