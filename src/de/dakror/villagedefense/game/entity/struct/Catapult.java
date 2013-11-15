package de.dakror.villagedefense.game.entity.struct;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.creature.Creature;
import de.dakror.villagedefense.game.projectile.Projectile;
import de.dakror.villagedefense.game.projectile.Rock;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.CFG;
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
		
		// buildingCosts.set(Resource.GOLD, 150);
		// buildingCosts.set(Resource.PLANKS, 20);
		// buildingCosts.set(Resource.IRONINGOT, 5);
		
		setBump(new Rectangle2D.Float(0.29f, 1.4f, 1.40f, 1.3f));
		
		description = "Schleudert Steine auf Monster.";
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
		Vector dif = target.getCenter().clone().sub(getCenter());
		CFG.p(dif.getAngleOnXAxis());
		if (target instanceof Creature)
		{
			Creature c = (Creature) target;
		}
		/*
		 * Notation: I write vectors in capital letters, scalars in lower case, and ∠V for the angle that the vector V makes with the x-axis. (Which you can compute with the function atan2 in many languages.)
		 * The simplest case is a stationary shooter which can rotate instantly.
		 * Let the target be at the position A and moving with velocity VA, and the shooter be stationary at the position B and can fire bullets with speed s. Let the shooter fire at time 0. The bullet hits at time t such that |A − B + t VA| = t s. This is a straightforward quadratic equation in t, which you should be easily able to solve (or determine that there is no solution). Having determined t, you can now work out the firing angle, which is just ∠(A − B + t VA).
		 * Now suppose that the shooter is not stationary but has constant velocity VB. (I'm supposing Newtonian relativity here, i.e. the bullet velocity is added to the shooter's velocity.)
		 * It's still a straightforward quadratic equation to work out the time to hit: |A − B + t(VA − VB)| = t s. In this case the firing angle is ∠(A − B + t (VA − VB)).
		 * What if the shooter waits until time u before firing? Then the bullet hits the target when |A − B + t(VA − VB)| = (t − u) s. The firing angle is still ∠(A − B + t(VA − VB)).
		 * Now for your problem. Suppose that the shooter can complete a half rotation in time r. Then it can certainly fire at time r. (Basically: work out the necessary firing angle, if any, for a shot at time r, as described above, rotate to that angle, stop, wait until time r, then fire.)
		 * But you probably want to know the earliest time at which the shooter can fire. Here's where you probably want to use successive approximation to find it. (Sketch of algorithm: Can you fire at time 0? No. Can you fire at time r? Yes. Can you fire at time ½ r? No. etc.)
		 */
		
		
		// else
		return new Rock(getCenter(), target.getCenter2(), 6f, (int) attributes.get(Attribute.DAMAGE_CREATURE), Tile.SIZE * 3);
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
