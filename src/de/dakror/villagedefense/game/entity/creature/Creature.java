package de.dakror.villagedefense.game.entity.creature;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.struct.Barricade;
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
	protected Point spawnPoint;
	
	public Creature(int x, int y, String img)
	{
		super(x, y, Game.getImage("creature/" + img + ".png").getWidth() / 4, Game.getImage("creature/" + img + ".png").getHeight() / 4);
		
		spawnPoint = new Point(x, y);
		
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
					if (frame % 2 == 0) targetEntity.dealDamage((int) (targetEntity instanceof Struct ? attributes.get(Attribute.DAMAGE_STRUCT) : attributes.get(Attribute.DAMAGE_CREATURE)), this);
					frame++;
				}
			}
			else if (!onArrivalAtEntity(tick)) frame = 0;
		}
		
		if (targetEntity == null && target == null) // killed everything
		{
			lookupTargetEntity();
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
			
			float angle = Math.round(dif.getAngleOnXAxis());
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
		targetEntity = null;
		
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
			
			targetEntity = entity;
			
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
	
	public void setTargetEntity(Entity target)
	{
		targetEntity = target;
	}
	
	public Entity getTargetEntity()
	{
		return targetEntity;
	}
	
	public void setHostile(boolean hostile)
	{
		this.hostile = hostile;
		lookupTargetEntity();
	}
	
	public void lookupTargetEntity()
	{
		if (!hostile) return;
		
		Barricade closestBarricade = null;
		
		for (Entity e : Game.world.entities)
		{
			if (e instanceof Barricade)
			{
				boolean nearer = (closestBarricade == null) ? true : e.getPos().getDistance(getPos()) < closestBarricade.getPos().getDistance(getPos());
				
				boolean inDirection = (closestBarricade == null) ? true : spawnPoint.x < Game.getWidth() / 2 ? /* left side */e.getX() > x : /* right side */e.getX() < x;
				
				if (closestBarricade == null || (nearer && inDirection))
				{
					closestBarricade = (Barricade) e;
				}
			}
		}
		
		if (closestBarricade == null || closestBarricade.getPos().getDistance(getPos()) > Game.world.core.getPos().getDistance(getPos())) setTarget(Game.world.core);
		else setTarget(closestBarricade);
	}
	
	@Override
	public void onDeath()
	{
		if (hostile) Game.currentGame.resources.add(resources);
		dead = true;
	}
	
	@Override
	public void onSpawn()
	{}
	
	protected abstract boolean onArrivalAtEntity(int tick);
}
