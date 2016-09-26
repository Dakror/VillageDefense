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
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import de.dakror.gamesetup.util.Helper;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class Windmill extends Struct {
	int tick;
	
	public Windmill(int x, int y) {
		super(x, y, 7, 10);
		tx = 7;
		ty = 30;
		placeGround = true;
		
		name = "Windmühle";
		setBump(new Rectangle2D.Float(1.2f, 7, 4.7f, 3));
		
		attributes.set(Attribute.MINE_SPEED, 200);
		attributes.set(Attribute.MINE_AMOUNT, 2); // use 2 get 3
		
		buildingCosts.set(Resource.GOLD, 275);
		buildingCosts.set(Resource.WOOD, 150);
		buildingCosts.set(Resource.PLANKS, 200);
		buildingCosts.set(Resource.STONE, 85);
		buildingCosts.set(Resource.PEOPLE, 1);
		
		description = "Mahlt Weizen zu Mehl.";
	}
	
	@Override
	public void draw(Graphics2D g) {
		float angle = tick / 25f;
		
		Helper.drawImage(Game.getImage("structs.png"), (int) x, (int) y, width, height, tx * Tile.SIZE, ty * Tile.SIZE, width, height, g);
		
		AffineTransform old = g.getTransform();
		AffineTransform at = g.getTransform();
		at.rotate(angle, x - 32 + 144, y - 85 + 144);
		g.setTransform(at);
		g.drawImage(Game.getImage("creature/windmill.png"), (int) x - 32, (int) y - 85, Game.w);
		g.setTransform(old);
		
		if (getAttackArea().getBounds().width > 0 && (clicked || hovered)) {
			Color oldColor = g.getColor();
			g.setColor(Color.darkGray);
			
			g.draw(getAttackArea());
			g.setColor(oldColor);
		}
	}
	
	@Override
	protected BufferedImage createImage() {
		BufferedImage bi = new BufferedImage(288, height + 85, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) bi.getGraphics();
		Helper.drawImage2(Game.getImage("structs.png"), (288 - width) / 2, 85, width, height, tx * Tile.SIZE, ty * Tile.SIZE, width, height, g);
		g.drawImage(Game.getImage("creature/windmill.png"), 0, 0, null);
		
		return bi;
	}
	
	@Override
	protected void tick(int tick) {
		super.tick(tick);
		
		if (working) this.tick++;
		
		if (tick % attributes.get(Attribute.MINE_SPEED) == 0 && Game.currentGame.resources.get(Resource.WHEAT) >= attributes.get(Attribute.MINE_AMOUNT) && working) {
			Game.currentGame.resources.add(Resource.WHEAT, (int) -attributes.get(Attribute.MINE_AMOUNT));
			resources.add(Resource.FLOUR, 3);
		}
	}
	
	@Override
	public Resources getResourcesPerSecond() {
		Resources res = new Resources();
		
		if (!working) return res;
		
		res.set(Resource.WHEAT, Game.currentGame.getUPS2() / attributes.get(Attribute.MINE_SPEED) * (-attributes.get(Attribute.MINE_AMOUNT)));
		res.set(Resource.FLOUR, Game.currentGame.getUPS2() / attributes.get(Attribute.MINE_SPEED) * 3);
		
		return res;
	}
	
	@Override
	public void initGUI() {}
	
	@Override
	protected void onMinedUp() {}
	
	@Override
	public void onUpgrade(Researches research, boolean initial) {}
	
	@Override
	public Entity clone() {
		return new Windmill((int) x / Tile.SIZE, (int) y / Tile.SIZE);
	}
	
	@Override
	protected void onDeath() {}
}
