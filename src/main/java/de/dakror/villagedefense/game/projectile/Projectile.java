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


package de.dakror.villagedefense.game.projectile;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import de.dakror.gamesetup.util.Helper;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.struct.Torch;
import de.dakror.villagedefense.util.Drawable;
import de.dakror.villagedefense.util.Vector;


/**
 * @author Dakror
 */
public abstract class Projectile implements Drawable {
	Vector pos;
	Vector target;
	Image image;
	String imgName;
	float speed;
	float angle;
	boolean dead;
	boolean rotate;
	boolean canSetOnFire;
	boolean onFire;
	int frame;
	
	public Projectile(Vector pos, String image, float speed) {
		this.pos = pos;
		this.image = Game.getImage("particle/" + image + ".png");
		imgName = image;
		this.speed = speed;
		dead = false;
		angle = 0;
		rotate = true;
	}
	
	@Override
	public void draw(Graphics2D g) {
		if (dead) return;
		
		AffineTransform old = g.getTransform();
		if (rotate) {
			AffineTransform at = g.getTransform();
			at.rotate(angle, pos.x, pos.y);
			g.setTransform(at);
		}
		
		g.drawImage(image, (int) pos.x, (int) pos.y, Game.w);
		
		g.setTransform(old);
		
		if (canSetOnFire && onFire) Helper.drawImage(Game.getImage("anim/fire.png"), (int) pos.x - 12, (int) pos.y - 12, 24, 24, 32 * frame, 0, 32, 29, g);
	}
	
	public Projectile setRotate(boolean rotate) {
		this.rotate = rotate;
		
		return this;
	}
	
	@Override
	public void update(int tick) {
		target = getTargetVector();
		
		if (onFire && tick % 3 == 0) frame = (frame + 1) % 4;
		
		Vector dif = target.clone().sub(pos);
		if (dif.getLength() > speed) dif.setLength(speed);
		
		angle = (float) Math.atan2(dif.y, dif.x);
		
		pos.add(dif);
		
		if (canSetOnFire && !onFire) {
			for (Entity e : Game.world.entities) {
				if (e instanceof Torch) {
					if (((Torch) e).getAttackArea().contains(pos.x, pos.y)) {
						onFire = true;
						break;
					}
				}
			}
		}
		
		if (pos.equals(target)) {
			onImpact();
			dead = true;
		}
	}
	
	protected abstract Vector getTargetVector();
	
	protected abstract void onImpact();
	
	public boolean isDead() {
		return dead;
	}
	
	public boolean isOnFire() {
		return onFire;
	}
	
	public Vector getPos() {
		return pos;
	}
	
	public Vector getTarget() {
		return target;
	}
	
	public Image getImage() {
		return image;
	}
	
	public String getImageName() {
		return imgName;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public float getAngle() {
		return angle;
	}
}
