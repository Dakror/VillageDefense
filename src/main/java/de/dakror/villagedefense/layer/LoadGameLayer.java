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
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import de.dakror.gamesetup.layer.Layer;
import de.dakror.gamesetup.util.Helper;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.util.SaveHandler;

/**
 * @author Dakror
 */
public class LoadGameLayer extends Layer {
	MenuLayer ml;
	File[] saves;
	int y;
	int height = 90;
	int h;
	
	public LoadGameLayer(MenuLayer ml) {
		super();
		this.ml = ml;
		modal = true;
		ml.setEnabled(false);
		saves = SaveHandler.getSaves();
		Arrays.sort(saves, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				return o2.getName().compareTo(o1.getName());
			}
		});
		y = 0;
	}
	
	@Override
	public void draw(Graphics2D g) {
		Helper.drawContainer(Game.getWidth() / 8, Game.getHeight() / 8, Game.getWidth() / 8 * 6, Game.getHeight() / 8 * 6, true, false, g);
		Helper.drawHorizontallyCenteredString("Spiel laden", Game.getWidth(), Game.getHeight() / 8 + 70, g, 70);
		
		Shape c = g.getClip();
		
		h = Helper.round(Game.getHeight() / 8 * 6 - 140, height);
		g.setClip(new Rectangle(Game.getWidth() / 8 + 20, Game.getHeight() / 8 + 120, Game.getWidth() / 8 * 6 - 40, h));
		
		for (int i = 0; i < saves.length; i++) {
			Rectangle r = new Rectangle(Game.getWidth() / 4 + 100, Game.getHeight() / 8 + 120 + height * i + y, Game.getWidth() / 2 - 200, height - 10);
			File f = saves[i];
			Helper.drawShadow(r.x, r.y, r.width, r.height, g);
			Helper.drawOutline(r.x, r.y, r.width, r.height, r.contains(Game.currentGame.mouse), g);
			Helper.drawHorizontallyCenteredString(f.getName().replace(".save", ""), Game.getWidth(), r.y + 50, g, 35);
		}
		
		g.setClip(c);
	}
	
	@Override
	public void update(int tick) {}
	
	@Override
	public void init() {}
	
	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		if (!new Rectangle(Game.getWidth() / 8, Game.getHeight() / 8, Game.getWidth() / 8 * 6, Game.getHeight() / 8 * 6).contains(e.getPoint())) {
			ml.setEnabled(true);
			Game.currentGame.layers.remove(this);
		} else if (new Rectangle(Game.getWidth() / 4 + 100, Game.getHeight() / 8 + 120, Game.getWidth() / 2 - 200, h).contains(e.getPoint())) {
			int eY = Helper.round(e.getY() - Game.getHeight() / 8 + 120 - y, height) / height - 3;
			
			if (eY < saves.length) {
				SaveHandler.loadSave(saves[eY]);
				Game.currentGame.fadeTo(1, 0.05f);
				
				ml.setEnabled(true);
				Game.currentGame.layers.remove(this);
			}
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		super.mouseWheelMoved(e);
		int dif = saves.length * height - h;
		
		if (dif < 0) return;
		
		int delta = -(int) (e.getWheelRotation() * height * 0.25f);
		if (y + delta < -dif) y = -dif;
		else if (y + delta > 0) y = 0;
		else y += delta;
	}
}
