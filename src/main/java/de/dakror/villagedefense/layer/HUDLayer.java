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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import de.dakror.gamesetup.layer.Layer;
import de.dakror.gamesetup.util.Helper;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.struct.Rock;
import de.dakror.villagedefense.game.entity.struct.Struct;
import de.dakror.villagedefense.game.entity.struct.Tree;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.settings.WaveManager;
import de.dakror.villagedefense.settings.WaveManager.Monster;
import de.dakror.villagedefense.ui.BuildBar;
import de.dakror.villagedefense.util.Assistant;
import de.dakror.villagedefense.util.SaveHandler;

/**
 * @author Dakror
 */
public class HUDLayer extends Layer {
	public static HUDLayer currentHudLayer;
	
	Resources rps;
	boolean spaceDown;
	
	public HUDLayer() {
		super();
		
		currentHudLayer = this;
	}
	
	@Override
	public void init() {
		rps = new Resources();
		components.add(new BuildBar());
	}
	
	@Override
	public void draw(Graphics2D g) {
		try {
			// -- wave monster faces -- //
			int selectedMonster = -1;
			if (WaveManager.monsters.size() > 0) {
				int cSize = 70;
				ArrayList<Monster> keys = new ArrayList<>(WaveManager.monsters.keySet());
				if (keys.contains(Monster.ZOMBIE)) keys.add(Monster.GHOST);
				
				for (int i = 0; i < keys.size(); i++) {
					Monster m = keys.get(i);
					
					int x = Game.getWidth() / 2 + 200 + i * cSize;
					Helper.drawShadow(x, 72, cSize, cSize, g);
					Helper.drawOutline(x, 72, cSize, cSize, false, g);
					g.drawImage(Game.getImage("creature/" + m.getImage() + "_face.png"), Game.getWidth() / 2 + 200 + i * cSize + (cSize - 48) / 2, 72 + (cSize - 48) / 2, 48, 48, Game.w);
					
					if (m != Monster.GHOST) Helper.drawString(WaveManager.monsters.get(m) + "", x + 6, 72 + cSize - 6, g, 22);
					else Helper.drawString("?", x + 6, 72 + cSize - 6, g, 22);
					
					if (new Rectangle(x, 72, cSize, cSize).contains(Game.currentGame.mouse)) selectedMonster = Arrays.asList(Monster.values()).indexOf(m);
				}
			}
			
			// -- selected entity stuff -- //
			Helper.drawContainer(Game.getWidth() / 2 - 175, 70, 350, 60, false, false, g);
			if (Game.world.selectedEntity != null) {
				Helper.drawHorizontallyCenteredString(Game.world.selectedEntity.getName(), Game.getWidth(), 111, g, 40);
				if (Game.world.selectedEntity.getAttributes().get(Attribute.HEALTH_MAX) > Attribute.HEALTH_MAX.getDefaultValue()) {
					Helper.drawProgressBar(Game.getWidth() / 2 - 179, 111, 358, Game.world.selectedEntity.getAttributes().get(Attribute.HEALTH)
							/ Game.world.selectedEntity.getAttributes().get(Attribute.HEALTH_MAX), "ff3232", g);
					Color oldColor = g.getColor();
					g.setColor(Color.white);
					Helper.drawHorizontallyCenteredString((int) Game.world.selectedEntity.getAttributes().get(Attribute.HEALTH) + " / "
							+ (int) Game.world.selectedEntity.getAttributes().get(Attribute.HEALTH_MAX), Game.getWidth(), 126, g, 15);
					g.setColor(oldColor);
				}
				
				if (Game.world.selectedEntity.getResources().size() > 0) {
					ArrayList<Resource> resources = Game.world.selectedEntity.getResources().getFilled();
					Helper.drawShadow(0, 80, 160, resources.size() * 24 + 40, g);
					for (int i = 0; i < resources.size(); i++) {
						Assistant.drawResource(Game.world.selectedEntity.getResources(), resources.get(i), 16, 100 + i * 24, 26, 30, g);
					}
				}
				
				if (Game.world.selectedEntity instanceof Struct) {
					Struct s = (Struct) Game.world.selectedEntity;
					if (!(s instanceof Tree) && !(s instanceof Rock)) {
						Helper.drawShadow(Game.getWidth() / 2 - 260, 72, 70, 70, g);
						
						if (new Rectangle(Game.getWidth() / 2 - 260, 72, 70, 70).contains(Game.currentGame.mouse)) Helper.drawContainer(Game.getWidth() / 2 - 260, 72, 70, 70, false, false, g);
						else Helper.drawOutline(Game.getWidth() / 2 - 260, 72, 70, 70, false, g);
						
						g.drawImage(Game.getImage("icon/bomb.png"), Game.getWidth() / 2 - 250, 82, 50, 50, Game.w);
					}
					
					if (s.getResourcesPerSecond().size() > 0 || !s.isWorking()) {
						Helper.drawShadow(Game.getWidth() / 2 - 330, 72, 70, 70, g);
						
						if (new Rectangle(Game.getWidth() / 2 - 330, 72, 70, 70).contains(Game.currentGame.mouse)) Helper.drawContainer(Game.getWidth() / 2 - 330, 72, 70, 70, s.isWorking(),
																																																														false, g);
						else Helper.drawOutline(Game.getWidth() / 2 - 330, 72, 70, 70, s.isWorking(), g);
						
						g.drawImage(Game.getImage("icon/working.png"), Game.getWidth() / 2 - 320, 82, 50, 50, Game.w);
					}
				}
			}
			
			// -- top bar -- //
			int total = Game.getWidth() / 2 - 150;
			boolean hover = (new Rectangle(0, 0, total + 48, 80).contains(Game.currentGame.mouse) || spaceDown) && Game.currentGame.state == 0;
			
			Helper.drawContainer(0, 0, Game.getWidth(), 80, false, false, g);
			
			int w = 300;
			
			if (hover) {
				Helper.drawShadow(0, 0, w, Resource.values().length * 30 + 20, g);
				Helper.drawOutline(0, 0, w, Resource.values().length * 30 + 20, false, g);
			}
			
			for (int i = 0; i < Resource.values().length; i++) {
				Resource r = Resource.values()[i];
				float dif = Math.round(rps.getF(r) * 100) / 100f;
				
				String delta = (dif != 0 ? " (" + (dif < 0 ? "" : "+") + dif + ")" : "");
				if (!hover) delta = "";
				
				int y = 13 + 30 * i;
				
				if (!hover && y > 70) continue;
				
				if (r == Resource.PEOPLE) {
					int free = Game.currentGame.getPeople();
					Assistant.drawLabelWithIcon(25, y, 30, new Point(r.getIconX(), r.getIconY()), (free != Game.currentGame.resources.get(r) ? Game.currentGame.getPeople() + " / " : "")
							+ Game.currentGame.resources.get(r) + delta, 30, g);
				} else {
					Assistant.drawLabelWithIcon(25, y, 30, new Point(r.getIconX(), r.getIconY()), Game.currentGame.resources.get(r) + delta, 30, g);
				}
			}
			
			// -- wave info -- //
			Helper.drawString("Welle: " + WaveManager.wave, Game.getWidth() / 2 + 170, 55, g, 45);
			Helper.drawString(Game.currentGame.getPlayerScore() + " Punkte", Game.getWidth() / 4 * 3, 50, g, 25);
			
			// -- time panel -- //
			Helper.drawContainer(Game.getWidth() / 2 - 150, 0, 300, 80, true, true, g);
			String minutes = ((int) Math.floor(WaveManager.nextWave / 60f)) + "";
			String seconds = (WaveManager.nextWave % 60) + "";
			
			while (minutes.length() < 2)
				minutes = "0" + minutes;
			while (seconds.length() < 2)
				seconds = "0" + seconds;
			
			Helper.drawHorizontallyCenteredString(minutes + ":" + seconds, Game.getWidth(), 60, g, 70);
			
			// -- forward -- //
			int x = Game.getWidth() / 2 + 100;
			int y = 30;
			
			boolean hover2 = new Rectangle(x, y, 60, 60).contains(Game.currentGame.mouse);
			
			if (hover2 || Game.currentGame.updater.speed != 1) Helper.drawShadow(x, y, 60, 60, g);
			Helper.drawOutline(x, y, 60, 60, false, g);
			g.drawImage(Game.getImage("icon/fwd.png"), x + 10, y + 10, 40, 40, Game.w);
			
			// -- save -- //
			if (!new Rectangle(Game.getWidth() - 155, 5, 70, 70).contains(Game.currentGame.mouse)) Helper.drawContainer(Game.getWidth() - 155, 5, 70, 70, false, false, g);
			else Helper.drawContainer(Game.getWidth() - 160, 0, 80, 80, false, true, g);
			
			g.drawImage(Game.getImage("icon/save.png"), Game.getWidth() - 145, 15, 50, 50, Game.w);
			
			if (WaveManager.nextWave == 0) Helper.drawShadow(Game.getWidth() - 155, 5, 70, 70, g);
			
			// -- pause -- //
			if (!new Rectangle(Game.getWidth() - 75, 5, 70, 70).contains(Game.currentGame.mouse)) Helper.drawContainer(Game.getWidth() - 75, 5, 70, 70, false, false, g);
			else Helper.drawContainer(Game.getWidth() - 80, 0, 80, 80, false, true, g);
			
			g.drawImage(Game.getImage("gui/pause.png"), Game.getWidth() - 75, 5, 70, 70, Game.w);
			
			drawComponents(g);
			
			// -- monster tooltip -- //
			if (selectedMonster != -1) {
				String text = Monster.values()[selectedMonster].getDescription();
				String name = text.substring(0, text.indexOf(":"));
				text = text.substring(text.indexOf(":") + 1);
				String[] lines = text.split("\\. ");
				int width = 0;
				for (String s : lines) {
					int myW = g.getFontMetrics(g.getFont().deriveFont(24f)).stringWidth(s);
					if (myW > width) width = myW;
				}
				
				width += 32;
				int height = 64 + text.split("\\. ").length * 30;
				
				int x1 = Game.currentGame.mouse.x;
				int y1 = Game.currentGame.mouse.y;
				
				if (x1 + width + 20 > Game.getWidth()) x1 -= (x1 + width + 20) - Game.getWidth();
				
				Helper.drawShadow(x1 + 12, y1 + 24, width, height, g);
				Helper.drawOutline(x1 + 12, y1 + 24, width, height, false, g);
				
				Helper.drawHorizontallyCenteredString(name, x1 + 12, width, y1 + 24 + 40, g, 30);
				for (int i = 0; i < lines.length; i++) {
					Helper.drawString(lines[i] + ".", x1 + 25, y1 + 24 + 70 + i * 30, g, 24);
				}
			}
			
			// -- UI components -- //
			drawComponents(g);
		} catch (NullPointerException e) {} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(int tick) {
		updateComponents(tick);
		
		rps = new Resources();
		
		for (Entity e : Game.world.entities) {
			if (e instanceof Struct && !e.isHungry()) rps.add(((Struct) e).getResourcesPerSecond());
		}
		
		rps.add(Resource.BREAD, -Game.hungerPerUnitPerSecond * Game.currentGame.resources.get(Resource.PEOPLE));
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		if (Game.currentGame.state == 0) {
			if (new Rectangle(Game.getWidth() / 2 + 100, 30, 60, 60).contains(e.getPoint())) {
				Game.currentGame.updater.ticks = 0;
				Game.currentGame.frames = 0;
				Game.currentGame.start = System.currentTimeMillis();
				Game.currentGame.updater.speed = Game.currentGame.updater.speed == 1 ? Game.forwardFactor : 1;
			}
			
			if (Game.world.selectedEntity != null && Game.world.selectedEntity instanceof Struct) {
				if (new Rectangle(Game.getWidth() / 2 - 260, 72, 70, 70).contains(e.getPoint())) // destroy
				{
					Resources res = ((Struct) Game.world.selectedEntity).getBuildingCosts();
					
					for (Resource r : res.getFilled()) {
						if (r.isUsable()) Game.currentGame.resources.add(r, Math.round(res.get(r) / 4f)); // give 25% back
					}
					
					Game.world.selectedEntity.kill();
					Game.currentGame.killedCoreHouse = true; // hack to prevent StateLayer from funk around
				} else if (new Rectangle(Game.getWidth() / 2 - 330, 72, 70, 70).contains(e.getPoint())) // change working state
				{
					((Struct) Game.world.selectedEntity).setWorking(!((Struct) Game.world.selectedEntity).isWorking());
				}
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);
		if (new Rectangle(Game.getWidth() - 155, 5, 70, 70).contains(e.getPoint()) && WaveManager.nextWave != 0) // save
		{
			SaveHandler.saveGame();
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		super.keyPressed(e);
		spaceDown = e.getKeyCode() == KeyEvent.VK_SPACE;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		super.keyReleased(e);
		
		spaceDown = false;
	}
}
