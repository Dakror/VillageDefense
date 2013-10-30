package de.dakror.villagedefense.game.entity.creature;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.struct.Struct;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public abstract class Creature extends Entity
{
	protected Image image;
	protected Vector target;
	protected Entity targetEntity;
	protected boolean frozen;
	private boolean hostile;
	/**
	 * 0 = down<br>
	 * 1 = left<br>
	 * 2 = right<br>
	 * 3 = up
	 */
	protected int dir;
	protected int frame;
	
	private int randomOffset = (int) (Math.random() * 100);
	
	public Creature(int x, int y, String img)
	{
		super(x, y, Game.getImage("creature/" + img + ".png").getWidth() / 4, Game.getImage("creature/" + img + ".png").getHeight() / 4);
		image = Game.getImage("creature/" + img + ".png");
		
		setBump(new Rectangle((int) (width * 0.25f), (int) (height * 0.75f), (int) (width * 0.5f), (int) (height * 0.25f)));
		frozen = false;
		
		dir = 0;
		frame = 0;
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		drawBump(g, false);
		
		g.drawImage(image, (int) x, (int) y, (int) x + width, (int) y + height, frame * width, dir * height, frame * width + width, dir * height + height, Game.w);
		
		drawBump(g, true);
	}
	
	@Override
	public void tick(int tick)
	{
		if (targetEntity != null && !Game.world.entities.contains(targetEntity)) targetEntity = null;
		
		move(tick);
		
		// -- attacks -- //
		if (targetEntity != null && //
		target == null // not moving = at destination or standing still
		)
		{
			if (hostile)
			{
				if ((tick + randomOffset) % attributes.get(Attribute.ATTACK_SPEED) == 0)
				{
					if (frame % 2 == 0) targetEntity.dealDamage((int) (targetEntity instanceof Struct ? attributes.get(Attribute.DAMAGE_STRUCT) : attributes.get(Attribute.DAMAGE_CREATURE)));
					frame++;
				}
			}
			else if (targetEntity instanceof Struct && targetEntity.getResources().size() > 0)
			{
				if ((tick + randomOffset) % attributes.get(Attribute.MINE_SPEED) == 0)
				{
					if (frame % 2 == 0) ((Struct) targetEntity).mineAllResources((int) attributes.get(Attribute.MINE_AMOUNT));
					frame++;
				}
			}
			else frame = 0;
		}
		
		frame = frame % 4;
	}
	
	public void move(int tick)
	{
		if (!frozen && attributes.get(Attribute.SPEED) > 0 && target != null)
		{
			Vector pos = getPos();
			Vector dif = target.clone().sub(pos);
			
			if (dif.getLength() < attributes.get(Attribute.SPEED))
			{
				target = null;
				frame = 0;
			}
			
			dif.setLength(attributes.get(Attribute.SPEED));
			
			float angle = dif.getAngleOnXAxis();
			if (angle <= 135 && angle >= 45) dir = 0;
			else if (angle <= 45 && angle >= -45) dir = 2;
			else if (angle <= -45 && angle >= -135) dir = 3;
			else dir = 1;
			
			if ((tick + randomOffset) % 10 == 0) frame++;
			
			setPos(pos.add(dif));
		}
	}
	
	public void setTarget(int x, int y)
	{
		setTarget(new Vector(x, y));
	}
	
	public void setTarget(Vector target)
	{
		this.target = target;
	}
	
	public void setTarget(Entity entity)
	{
		if (hostile) targetEntity = entity;
		
		if (frozen || attributes.get(Attribute.SPEED) == 0) return;
		
		if (entity instanceof Creature)
		{
			// TODO: target on creatures
			// Creature c = (Creature) entity;
			// if (hostile != c.isHostile())
			// {
			//
			// }
		}
		else if (entity instanceof Struct)
		{
			Struct s = (Struct) entity;
			
			if (s.getResources().size() > 0) targetEntity = entity;
			
			Vector nearestPoint = null;
			
			ArrayList<Vector> points = hostile ? s.getStructPoints().attacks : s.getStructPoints().entries;
			
			if (points.size() == 0) points = s.getSurroundingTiles();
			
			Vector pos = getPos();
			for (Vector p : points)
			{
				Vector v = p.clone();
				v.mul(Tile.SIZE);
				v.add(s.getPos());
				if (nearestPoint == null || v.getDistance(pos) < nearestPoint.getDistance(pos)) nearestPoint = v;
			}
			nearestPoint.setLength(nearestPoint.getLength() - attributes.get(Attribute.ATTACK_RANGE));
			
			nearestPoint.y -= height * 0.6f;
			
			setTarget(nearestPoint);
		}
	}
	
	public Vector getTarget()
	{
		return target;
	}
	
	public Vector getTarget2()
	{
		return target == null ? getPos() : target;
	}
	
	public void setFrozen(boolean frozen)
	{
		this.frozen = frozen;
	}
	
	public boolean isFrozen()
	{
		return frozen;
	}
	
	public boolean isHostile()
	{
		return hostile;
	}
	
	public void setHostile(boolean hostile)
	{
		this.hostile = hostile;
		if (hostile) setTarget(Game.world.core);
	}
	
	@Override
	public void onDeath()
	{
		dead = true;
	}
	
	@Override
	public void onSpawn()
	{}
}
