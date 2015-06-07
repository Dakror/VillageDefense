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

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;

import de.dakror.gamesetup.util.Helper;
import de.dakror.villagedefense.game.Game;

/**
 * @author Dakror
 */
public class MenuButton extends Button {
	String image;
	int y1;
	Dimension size;
	float alpha;
	float speed = 0.075f;
	
	public MenuButton(String image, int y) {
		super(0, 0, 0, 100);
		this.image = image;
		y1 = y;
		size = new Dimension(0, 0);
		alpha = 0.3f;
	}
	
	@Override
	public void draw(Graphics2D g) {
		Image img = Game.getImage("menu/" + image + ".png");
		int height = 100;
		int width = (height * img.getWidth(null)) / img.getHeight(null);
		Dimension s = Helper.getRelativeScaled(new Dimension(width, height), new Dimension(1920, 1080), new Dimension(Game.getWidth(), Game.getHeight()));
		this.width = s.width;
		this.height = s.height;
		x = (Game.getWidth() - this.width) / 2;
		y = Game.getHeight() / 5 * 2 + y1 * (s.height + 20);
		Composite c = g.getComposite();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		Helper.drawImageCenteredRelativeScaled(img, y, width, height, 1920, 1080, Game.getWidth(), Game.getHeight(), g);
		g.setComposite(c);
	}
	
	@Override
	public void update(int tick) {
		if (state == 2 || state == 1) {
			alpha += speed;
		} else if (alpha > 0.3f + speed) {
			alpha -= speed;
		}
		
		if (alpha > 1) alpha = 1;
		if (alpha < 0.3f) alpha = 0.3f;
	}
}
