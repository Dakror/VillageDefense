package de.dakror.villagedefense.game.entity.struct.tower;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import de.dakror.gamesetup.util.Helper;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.struct.Struct;
import de.dakror.villagedefense.game.projectile.Arrow;
import de.dakror.villagedefense.game.projectile.Projectile;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public abstract class Tower extends Struct {
	/**
	 * -1 = none<br>
	 * 0 = red<br>
	 * 1 = blue<br>
	 * 2 = green<br>
	 * 3 = purple<br>
	 */
	protected int color;
	protected int spheres;
	
	public Tower(int x, int y) {
		super(x, y, 1, 3);
		
		tx = 3;
		ty = 11;
		color = -1;
		spheres = 0;
		placeGround = false;
		
		setBump(new Rectangle2D.Float(0, 2.5f, 1, 0.5f));
	}
	
	@Override
	protected void onMinedUp() {}
	
	@Override
	protected void tick(int tick) {
		super.tick(tick);
		
		if ((tick + randomOffset) % attributes.get(Attribute.ATTACK_SPEED) == 0) {
			shoot(0);
			
			if (has(Researches.TOWER_DOUBLESHOT)) shoot(1);
		}
	}
	
	@Override
	public Projectile getProjectile(Entity target) {
		return new Arrow(getCenter(), target, 10f, (int) attributes.get(Attribute.DAMAGE_CREATURE));
	}
	
	@Override
	public Shape getAttackArea() {
		int rad = (int) attributes.get(Attribute.ATTACK_RANGE);
		Vector c = getCenter();
		return new Arc2D.Float(c.x - rad, c.y - rad, rad * 2, rad * 2, 0, 360, Arc2D.OPEN);
	}
	
	@Override
	protected void onDeath() {}
	
	@Override
	public void initGUI() {}
	
	@Override
	protected BufferedImage createImage() {
		BufferedImage tower = super.createImage();
		BufferedImage image = new BufferedImage(Tile.SIZE, 3 * Tile.SIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.drawImage(tower, 0, Tile.SIZE, null);
		
		Point spheresP = new Point(13, 8);
		
		if (spheres == 1) {
			Helper.drawImage(Game.getImage("structs.png"), 0, Tile.SIZE - 8, Tile.SIZE, Tile.SIZE, spheresP.x * Tile.SIZE, (spheresP.y + color) * Tile.SIZE, Tile.SIZE, Tile.SIZE, g);
		} else if (spheres == 2) {
			Helper.drawImage(Game.getImage("structs.png"), -6, Tile.SIZE - 8, Tile.SIZE, Tile.SIZE, spheresP.x * Tile.SIZE, (spheresP.y + color) * Tile.SIZE, Tile.SIZE, Tile.SIZE, g);
			Helper.drawImage(Game.getImage("structs.png"), 6, Tile.SIZE - 8, Tile.SIZE, Tile.SIZE, spheresP.x * Tile.SIZE, (spheresP.y + color) * Tile.SIZE, Tile.SIZE, Tile.SIZE, g);
		}
		return image;
	}
	
	@Override
	public void onUpgrade(Researches research, boolean inititial) {
		if (research == Researches.TOWER_DOUBLESHOT) {
			image = null;
			spheres = 2;
		}
	}
}
