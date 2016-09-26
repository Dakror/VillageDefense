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


package de.dakror.villagedefense.ui.button;

import java.awt.Graphics2D;

import de.dakror.gamesetup.util.Helper;
import de.dakror.villagedefense.game.Game;

/**
 * @author Dakror
 */
public class ArrowButton extends Button {
	public static int MARGIN = 52;
	
	int tx, ty;
	
	public ArrowButton(int x, int y, int tx, int ty) {
		super(x, y, 32, 32);
		
		this.tx = tx;
		this.ty = ty;
	}
	
	@Override
	public void draw(Graphics2D g) {
		Helper.drawImage(Game.getImage("gui/gui.png"), x, y, width, height, tx, ty + MARGIN * state, width, height, g);
	}
	
	@Override
	public void update(int tick) {}
}
