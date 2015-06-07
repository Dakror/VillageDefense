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
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import de.dakror.gamesetup.util.Helper;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.creature.Creature;
import de.dakror.villagedefense.game.projectile.Projectile;
import de.dakror.villagedefense.game.projectile.Rock;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public class Catapult extends Struct {
	int frame;
	boolean shooting;
	
	public Catapult(int x, int y) {
		super(x, y, 2, 2);
		shooting = false;
		frame = 0;
		
		name = "Katapult";
		placeGround = false;
		
		attributes.set(Attribute.ATTACK_RANGE, 20 * Tile.SIZE);
		attributes.set(Attribute.ATTACK_SPEED, 250);
		attributes.add(Attribute.DAMAGE_CREATURE, 30);
		
		buildingCosts.set(Resource.GOLD, 150);
		buildingCosts.set(Resource.PLANKS, 20);
		buildingCosts.set(Resource.IRONINGOT, 5);
		
		setBump(new Rectangle2D.Float(0.29f, 1.4f, 1.40f, 1.3f));
		
		setDownwards(true);
		
		description = "Schleudert Steine auf Monster.";
	}
	
	@Override
	public void draw(Graphics2D g) {
		int y = 0;
		if (shooting) y += 64;
		if (!isDownwards()) y += 64 * 2;
		
		Helper.drawImage(Game.getImage("creature/catapult.png"), (int) x, (int) this.y + Tile.SIZE / 4 * 3, width, height, frame * 64, y, 64, 64, g);
		
		if (hovered || clicked) {
			Color oldColor = g.getColor();
			g.setColor(Color.darkGray);
			
			g.draw(getAttackArea());
			g.setColor(oldColor);
		}
	}
	
	@Override
	protected void tick(int tick) {
		super.tick(tick);
		
		if ((tick + randomOffset) % attributes.get(Attribute.ATTACK_SPEED) == 0) {
			shoot(0);
		}
	}
	
	@Override
	public Projectile getProjectile(Entity target) {
		float rockSpeed = 6f;
		Vector targetVector = target.getCenter2();
		
		if (target instanceof Creature) {
			// -- pre aim -- //
			
			Creature c = (Creature) target;
			Vector dif = c.getCenter2().clone().sub(getCenter()).normalize();
			
			Vector u = c.getVelocityVector();
			Vector uj = dif.clone().mul(dif.dot(u));
			Vector vi = u.clone().sub(uj);
			
			float viLen = vi.getLength();
			float vjLen = (float) Math.sqrt(rockSpeed * rockSpeed - viLen * viLen);
			
			Vector v = vi.add(new Vector(dif.x * vjLen, dif.y * vjLen));
			float angle = (float) Math.toRadians(90 - v.getAngleOnXAxis());
			float yDif = Math.abs(c.getCenter2().y - getCenter().y);
			
			float tan = (float) Math.tan(angle) * yDif;
			
			targetVector = new Vector(getCenter().x + (isDownwards() ? tan : -tan), c.getCenter().y);
		}
		
		return new Rock(getCenter(), targetVector, rockSpeed, (int) attributes.get(Attribute.DAMAGE_CREATURE), Tile.SIZE * 3);
	}
	
	@Override
	protected BufferedImage createImage() {
		return Game.getImage("creature/catapult.png").getSubimage(0, 0, 64, 64); // default image
	}
	
	@Override
	public Shape getAttackArea() {
		int rad = (int) attributes.get(Attribute.ATTACK_RANGE);
		Vector c = getCenter();
		return new Arc2D.Float(c.x - rad, c.y - rad, rad * 2, rad * 2, isDownwards() ? -120 : 60, 60, Arc2D.PIE);
	}
	
	@Override
	public void initGUI() {}
	
	@Override
	protected void onMinedUp() {}
	
	@Override
	public void onUpgrade(Researches research, boolean inititial) {}
	
	@Override
	public Entity clone() {
		Catapult c = new Catapult((int) x / Tile.SIZE, (int) y / Tile.SIZE);
		c.setDownwards(isDownwards());
		return c;
	}
	
	public void setDownwards(boolean b) {
		attributes.set(Attribute.MINE_SPEED, b ? Attribute.MINE_SPEED.getDefaultValue() : 1);
	}
	
	public boolean isDownwards() {
		return attributes.get(Attribute.MINE_SPEED) == Attribute.MINE_SPEED.getDefaultValue();
	}
	
	@Override
	protected void onDeath() {}
}
