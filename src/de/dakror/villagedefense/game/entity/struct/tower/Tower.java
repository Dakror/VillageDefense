package de.dakror.villagedefense.game.entity.struct.tower;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.creature.Creature;
import de.dakror.villagedefense.game.entity.struct.Struct;
import de.dakror.villagedefense.game.projectile.Projectile;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.util.Assistant;
import de.dakror.villagedefense.util.TowerTargetComparator;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public abstract class Tower extends Struct
{
	/**
	 * -1 = none<br>
	 * 0 = red<br>
	 * 1 = blue<br>
	 * 2 = green<br>
	 * 3 = purple<br>
	 */
	protected int color;
	protected int spheres;
	
	public Tower(int x, int y)
	{
		super(x, y, 1, 3);
		
		tx = 3;
		ty = 11;
		color = -1;
		spheres = 0;
		placeGround = false;
		
		setBump(new Rectangle2D.Float(0, 2.5f, 1, 0.5f));
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		super.draw(g);
		
		if (hovered || clicked)
		{
			Color oldColor = g.getColor();
			g.setColor(Color.darkGray);
			
			int rad = (int) attributes.get(Attribute.ATTACK_RANGE);
			Vector c = getCenter();
			
			g.drawArc((int) c.x - rad, (int) c.y - rad, rad * 2, rad * 2, 0, 360);
			g.setColor(oldColor);
		}
	}
	
	@Override
	protected void onMinedUp()
	{}
	
	@Override
	protected void tick(int tick)
	{
		super.tick(tick);
		
		if ((tick + randomOffset) % attributes.get(Attribute.ATTACK_SPEED) == 0)
		{
			shoot(0);
			
			if (has(Researches.TOWER_DOUBLESHOT)) shoot(1);
		}
	}
	
	public void shoot(int targetIndex)
	{
		ArrayList<Creature> t = getTargetableCreatures();
		
		if (t.size() == 0) return;
		
		targetIndex = targetIndex >= t.size() ? t.size() - 1 : targetIndex;
		
		for (int i = 0; i < t.size(); i++)
		{
			if (t.get(i).willDieFromTargetedProjectiles()) continue;
			
			Game.world.addProjectile(new Projectile(getCenter(), t.get(targetIndex), "arrow", 10f, (int) attributes.get(Attribute.DAMAGE_CREATURE)));
			break;
		}
	}
	
	protected ArrayList<Creature> getTargetableCreatures()
	{
		ArrayList<Creature> targetable = new ArrayList<>();
		for (Entity e : Game.world.entities)
		{
			if (!(e instanceof Creature)) continue;
			
			Creature c = (Creature) e;
			if (!c.isHostile()) continue;
			
			if (e.getCenter().getDistance(getCenter()) <= attributes.get(Attribute.ATTACK_RANGE)) targetable.add(c);
		}
		
		Collections.sort(targetable, new TowerTargetComparator());
		
		return targetable;
	}
	
	@Override
	protected void onDeath()
	{}
	
	@Override
	public void drawGUI(Graphics2D g)
	{
		drawUpgrades(g);
	}
	
	@Override
	public void initGUI()
	{}
	
	@Override
	protected BufferedImage createImage()
	{
		BufferedImage tower = super.createImage();
		BufferedImage image = new BufferedImage(Tile.SIZE, 3 * Tile.SIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.drawImage(tower, 0, Tile.SIZE, null);
		
		Point spheresP = new Point(13, 8);
		
		if (spheres == 1)
		{
			Assistant.drawImage(Game.getImage("structs.png"), 0, Tile.SIZE - 8, Tile.SIZE, Tile.SIZE, spheresP.x * Tile.SIZE, (spheresP.y + color) * Tile.SIZE, Tile.SIZE, Tile.SIZE, g);
		}
		else if (spheres == 2)
		{
			Assistant.drawImage(Game.getImage("structs.png"), -6, Tile.SIZE - 8, Tile.SIZE, Tile.SIZE, spheresP.x * Tile.SIZE, (spheresP.y + color) * Tile.SIZE, Tile.SIZE, Tile.SIZE, g);
			Assistant.drawImage(Game.getImage("structs.png"), 6, Tile.SIZE - 8, Tile.SIZE, Tile.SIZE, spheresP.x * Tile.SIZE, (spheresP.y + color) * Tile.SIZE, Tile.SIZE, Tile.SIZE, g);
		}
		return image;
	}
	
	@Override
	public void onUpgrade(Researches research)
	{
		if (research == Researches.TOWER_DOUBLESHOT)
		{
			image = null;
			spheres = 2;
		}
	}
}
