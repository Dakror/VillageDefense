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
 

package de.dakror.villagedefense.game.animation;

import java.awt.Graphics2D;
import java.awt.Image;

import de.dakror.gamesetup.util.Helper;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.util.Drawable;

/**
 * @author Dakror
 */
public class Animation implements Drawable {
	boolean dead;
	int x;
	int y;
	int size;
	int speed;
	int frame;
	int startFrame;
	String imgName;
	Image image;
	
	public Animation(int x, int y, int size, int speed, String anim) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.speed = speed;
		image = Game.getImage("anim/" + anim + ".png");
		imgName = anim;
		frame = 0;
		startFrame = 0;
		dead = false;
	}
	
	@Override
	public void draw(Graphics2D g) {
		if (dead) return;
		
		Helper.drawImage(image, x, y, size, size, frame * 192, 0, 192, 192, g);
	}
	
	@Override
	public void update(int tick) {
		if (startFrame == 0) startFrame = tick;
		
		if (dead) return;
		
		if ((tick - startFrame) > 0 && (tick - startFrame) % speed == 0) {
			if (frame * 192 >= image.getWidth(null)) dead = true;
			else frame++;
		}
	}
	
	public boolean isDead() {
		return dead;
	}
}
