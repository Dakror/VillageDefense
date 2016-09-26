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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import de.dakror.gamesetup.ui.Component;
import de.dakror.gamesetup.util.Helper;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.creature.Creature;
import de.dakror.villagedefense.game.entity.creature.Villager;
import de.dakror.villagedefense.game.projectile.Projectile;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.settings.StructPoints;
import de.dakror.villagedefense.ui.button.ResearchButton;
import de.dakror.villagedefense.util.TowerTargetComparator;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public abstract class Struct extends Entity {
	public int tx;
	public int ty;
	protected boolean placeGround;
	protected boolean canPlaceOnWay;
	protected StructPoints structPoints;
	protected Resources buildingCosts;
	protected BufferedImage image;
	public Point guiPoint;
	public Dimension guiSize;
	public CopyOnWriteArrayList<Component> components = new CopyOnWriteArrayList<>();
	protected ArrayList<Researches> researches = new ArrayList<>();
	protected Class<?> researchClass;
	protected boolean working;
	
	public Struct(int x, int y, int width, int height) {
		super(x * Tile.SIZE, y * Tile.SIZE, width * Tile.SIZE, height * Tile.SIZE);
		
		structPoints = new StructPoints();
		buildingCosts = new Resources();
		
		canPlaceOnWay = false;
		researchClass = getClass();
		
		guiSize = new Dimension(250, 250);
		working = true;
		canHunger = false;
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(getImage(), (int) x, (int) y, Game.w);
		
		if (getAttackArea().getBounds().width > 0 && (clicked || hovered)) {
			Color oldColor = g.getColor();
			g.setColor(Color.darkGray);
			
			g.draw(getAttackArea());
			g.setColor(oldColor);
		}
		
		// TODO: DEBUG
		// for (Vector p : structPoints.entries)
		// {
		// Vector v = p.clone();
		// v.mul(Tile.SIZE);
		// v.add(new Vector(x, y));
		//
		// Color old = g.getColor();
		// g.setColor(Color.green);
		// g.fillRect((int) v.x, (int) v.y, 4, 4);
		// g.setColor(old);
		// }
	}
	
	@Override
	protected void tick(int tick) {
		for (Component c : components)
			c.update(tick);
	}
	
	protected void drawComponents(int x, int y, Graphics2D g) {
		g.translate(x, y);
		
		for (Component c : components)
			c.draw(g);
		
		g.translate(-x, -y);
	}
	
	public abstract void initGUI();
	
	public void destroyGUI() {
		guiPoint = null;
		components.clear();
	}
	
	public void drawGUI(Graphics2D g) {
		drawUpgrades(g);
	}
	
	public void drawUpgrades(Graphics2D g) {
		if (Game.currentGame.getResearches(researchClass).length == 0) return;
		
		if (components.size() < Researches.values(researchClass).length) initUpgrades();
		try {
			Helper.drawContainer(guiPoint.x - 125, guiPoint.y - 125, 250, 250, false, false, g);
			Helper.drawHorizontallyCenteredString("Verbesserungen", guiPoint.x - 125, 250, guiPoint.y - 85, g, 40);
			
			drawComponents(guiPoint.x - 125, guiPoint.y - 125, g);
			for (Component c : components) {
				if ((c instanceof ResearchButton)) {
					ResearchButton n = (ResearchButton) c;
					if (n.state != 2) continue;
					
					((ResearchButton) c).drawTooltip(Game.currentGame.mouse.x, Game.currentGame.mouse.y, g);
					break;
				}
			}
		} catch (NullPointerException e) {}
	}
	
	public void initUpgrades() {
		int width = guiSize.width - 20;
		
		int size = 32;
		int gap = 24;
		
		int proRow = width / (size + gap);
		
		Researches[] res = Researches.values(researchClass);
		
		int index = 0;
		for (int i = 0; i < res.length; i++) {
			Researches research = res[i];
			if (Game.currentGame.researches.contains(research)) {
				components.add(new ResearchButton(20 + ((index % proRow) * (size + gap)), 55 + ((index / proRow) * (size + gap)), research, researches, this));
				index++;
			}
		}
	}
	
	public void setBump(Rectangle2D r) {
		super.setBump(new Rectangle((int) Math.round(r.getX() * Tile.SIZE), (int) Math.round(r.getY() * Tile.SIZE), (int) Math.round(r.getWidth() * Tile.SIZE), (int) Math.round(r.getHeight() * Tile.SIZE)));
	}
	
	public void placeGround() {
		if (!placeGround) return;
		
		int x = (int) Math.round(bump.getX() / Tile.SIZE) - 1;
		int y = (int) Math.round(bump.getY() / Tile.SIZE) - 1;
		int width = (int) Math.round(bump.getWidth() / Tile.SIZE) + 2;
		int height = (int) Math.round(bump.getHeight() / Tile.SIZE) + 2;
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Game.world.setTileId((int) this.x / Tile.SIZE + x + i, (int) this.y / Tile.SIZE + y + j, Tile.ground.getId());
			}
		}
	}
	
	public boolean isPlaceGround() {
		return placeGround;
	}
	
	public StructPoints getStructPoints() {
		return structPoints;
	}
	
	@Override
	public void onSpawn(boolean initial) {
		if (initial) return;
		placeGround();
		requestVillagersToCome(buildingCosts.get(Resource.PEOPLE));
	}
	
	public void mineAllResources(int amount, Resources target) {
		if (resources.size() == 0) return;
		
		ArrayList<Resource> filled = resources.getFilled();
		for (Resource r : filled) {
			if (!r.isUsable()) continue;
			
			int get = resources.get(r);
			int newVal = get - amount > -1 ? get - amount : 0;
			target.add(r, get - newVal);
			resources.set(r, newVal);
		}
		
		if (resources.size() == 0) onMinedUp();
	}
	
	public ArrayList<Vector> getSurroundingTiles(boolean onlyFree) {
		ArrayList<Vector> tiles = new ArrayList<>();
		
		// -- bump rectangle -- //
		int x = (int) Math.round(bump.getX() / Tile.SIZE);
		int y = (int) Math.round(bump.getY() / Tile.SIZE);
		int w = (int) Math.round(bump.getWidth() / Tile.SIZE);
		int h = (int) Math.round(bump.getHeight() / Tile.SIZE);
		
		w = w == 0 ? 1 : w;
		h = h == 0 ? 1 : h;
		
		for (int i = 0; i < w + 2; i++) {
			for (int j = 0; j < h + 2; j++) {
				if ((i == 0 || i == w + 1) && (j == 0 || j == h + 1)) continue;
				if (i > 0 && j > 0 && i < w + 1 && j < h + 1) continue;
				
				if (!onlyFree || (onlyFree && Game.world.isFreeTile((x + i - 1) * Tile.SIZE, (y + j - 1) * Tile.SIZE))) tiles.add(new Vector(x + i - 1, y + j - 1));
			}
		}
		
		return tiles;
	}
	
	public int getTargetedCarriers() {
		int count = 0;
		
		for (Entity e : Game.world.entities) {
			if (e instanceof Villager) {
				Villager v = (Villager) e;
				if (v.getTargetEntity() == null || !v.isTargetingToCarry()) continue;
				
				if (v.getTargetEntity().equals(this)) count++;
			}
		}
		
		return count;
	}
	
	public Resources getBuildingCosts() {
		return buildingCosts;
	}
	
	public BufferedImage getImage() {
		if (image != null) return image;
		image = createImage();
		return image;
	}
	
	protected BufferedImage createImage() {
		return Game.getImage("structs.png").getSubimage(tx * Tile.SIZE, ty * Tile.SIZE, width, height);
	}
	
	public boolean requestVillagersToCome(int amount) {
		if (Game.currentGame.resources.get(Resource.PEOPLE) < amount || amount == 0) return false;
		
		int count = 0;
		for (Entity e : Game.world.entities) {
			if (count == amount) break;
			
			if (e instanceof Villager && e.alpha > 0) {
				Villager v = (Villager) e;
				v.setTarget(this, false);
				count++;
			}
		}
		
		return true;
	}
	
	public boolean canPlaceOnWay() {
		return canPlaceOnWay;
	}
	
	@Override
	public boolean mousePressed(MouseEvent e) {
		if (guiPoint != null && guiSize != null) e.translatePoint(-(guiPoint.x - guiSize.width / 2), -(guiPoint.y - guiSize.height / 2));
		
		for (Component c : components)
			c.mousePressed(e);
		
		if (guiPoint != null && guiSize != null) e.translatePoint(guiPoint.x - guiSize.width / 2, guiPoint.y - guiSize.height / 2);
		
		boolean pressed = super.mousePressed(e);
		
		if (pressed && guiPoint == null && guiSize != null) {
			guiPoint = e.getPoint();
			guiPoint.translate(Game.world.x, Game.world.y);
			if (guiPoint.x - guiSize.width / 2 < 0) guiPoint.x = guiSize.width / 2;
			if (guiPoint.y - guiSize.height / 2 < 0) guiPoint.y = guiSize.height / 2;
			if (guiPoint.x + guiSize.width / 2 > Game.getWidth()) guiPoint.x = Game.getWidth() - guiSize.width / 2;
			if (guiPoint.y + guiSize.height / 2 > Game.getHeight()) guiPoint.y = Game.getHeight() - guiSize.height / 2;
		} else {
			if (guiPoint != null && guiSize != null && !new Rectangle(guiPoint.x - guiSize.width / 2, guiPoint.y - guiSize.height / 2, guiSize.width, guiSize.height).contains(e.getPoint())) {
				destroyGUI();
			}
		}
		
		return pressed;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (guiPoint != null && guiSize != null) e.translatePoint(-(guiPoint.x - guiSize.width / 2), -(guiPoint.y - guiSize.height / 2));
		
		for (Component c : components)
			c.mouseReleased(e);
		
		if (guiPoint != null && guiSize != null) e.translatePoint(guiPoint.x - guiSize.width / 2, guiPoint.y - guiSize.height / 2);
	}
	
	@Override
	public boolean mouseMoved(MouseEvent e) {
		if (guiPoint != null && guiSize != null) e.translatePoint(-(guiPoint.x - guiSize.width / 2), -(guiPoint.y - guiSize.height / 2));
		
		for (Component c : components)
			c.mouseMoved(e);
		
		if (guiPoint != null && guiSize != null) e.translatePoint(guiPoint.x - guiSize.width / 2, guiPoint.y - guiSize.height / 2);
		
		return super.mouseMoved(e);
	}
	
	public boolean has(Researches res) {
		return researches.contains(res);
	}
	
	public void add(Researches res) {
		researches.add(res);
		onUpgrade(res, true);
	}
	
	public void clearResearches() {
		researches.clear();
	}
	
	public Resources getResourcesPerSecond() {
		return new Resources();
	}
	
	public Shape getAttackArea() {
		return new Rectangle();
	}
	
	public Projectile getProjectile(Entity target) {
		return null;
	}
	
	protected ArrayList<Creature> getTargetableCreatures() {
		ArrayList<Creature> targetable = new ArrayList<>();
		for (Entity e : Game.world.entities) {
			if (!(e instanceof Creature)) continue;
			
			Creature c = (Creature) e;
			if (!c.isHostile()) continue;
			
			if (getAttackArea().intersects(e.getArea(false))) targetable.add(c);
		}
		
		Collections.sort(targetable, new TowerTargetComparator());
		
		return targetable;
	}
	
	public boolean isWorking() {
		return working;
	}
	
	public void setWorking(boolean working) {
		this.working = working;
	}
	
	public void shoot(int targetIndex) {
		ArrayList<Creature> t = getTargetableCreatures();
		
		if (t.size() == 0) return;
		
		targetIndex = targetIndex >= t.size() ? t.size() - 1 : targetIndex;
		
		Game.world.addProjectile(getProjectile(t.get(targetIndex)));
	}
	
	@Override
	public JSONObject getData() {
		JSONObject o = new JSONObject();
		try {
			o.put("x", x / Tile.SIZE);
			o.put("y", y / Tile.SIZE);
			o.put("tx", tx);
			o.put("ty", ty);
			o.put("class", getClass().getName());
			o.put("researches", researches);
			o.put("attributes", attributes.getData());
			o.put("resources", resources.getData());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return o;
	}
	
	public boolean isCanHunger() {
		return canHunger;
	}
	
	protected abstract void onMinedUp();
	
	public abstract void onUpgrade(Researches research, boolean initial);
}
