package de.dakror.villagedefense.game.entity.struct;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.projectile.Projectile;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.util.Assistant;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public class Catapult extends Struct
{
	int frame;
	boolean downwards;
	boolean shooting;
	
	public Catapult(int x, int y)
	{
		super(x, y, 2, 2);
		downwards = true;
		shooting = false;
		frame = 0;
		
		name = "Katapult";
		placeGround = false;
		
		attributes.set(Attribute.ATTACK_RANGE, 20 * Tile.SIZE);
		attributes.set(Attribute.ATTACK_SPEED, 250);
		attributes.add(Attribute.DAMAGE_CREATURE, 30);
		
		
		setBump(new Rectangle2D.Float(0.29f, 1.4f, 1.40f, 1.3f));
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		int y = 0;
		if (shooting) y += 64;
		if (!downwards) y += 64 * 2;
		
		Assistant.drawImage(Game.getImage("creature/catapult.png"), (int) x, (int) this.y + Tile.SIZE / 4 * 3, width, height, frame * 64, y, 64, 64, g);
		
		if (hovered || clicked)
		{
			Color oldColor = g.getColor();
			g.setColor(Color.darkGray);
			
			g.draw(getAttackArea());
			g.setColor(oldColor);
		}
	}
	
	@Override
	protected void tick(int tick)
	{
		super.tick(tick);
		
		if ((tick + randomOffset) % attributes.get(Attribute.ATTACK_SPEED) == 0)
		{
			shoot(0);
		}
	}
	
	@Override
	public Projectile getProjectile(Entity target)
	{
		return new Projectile(getCenter(), target, "rock", 6f, (int) attributes.get(Attribute.DAMAGE_CREATURE));
	}
	
	@Override
	protected BufferedImage createImage()
	{
		return Game.getImage("creature/catapult.png").getSubimage(0, 0, 64, 64); // default image
	}
	
	@Override
	public Shape getAttackArea()
	{
		int rad = (int) attributes.get(Attribute.ATTACK_RANGE);
		Vector c = getCenter();
		return new Arc2D.Float(c.x - rad, c.y - rad, rad * 2, rad * 2, -120, 60, Arc2D.PIE);
	}
	
	@Override
	public void initGUI()
	{}
	
	@Override
	public void drawGUI(Graphics2D g)
	{}
	
	@Override
	protected void onMinedUp()
	{}
	
	@Override
	public void onUpgrade(Researches research)
	{}
	
	@Override
	public Entity clone()
	{
		return new Catapult((int) x, (int) y);
	}
	
	@Override
	protected void onDeath()
	{}
	
}
