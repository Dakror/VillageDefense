package de.dakror.villagedefense.game.entity;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.tile.Tile;
import de.dakror.villagedefense.settings.Attributes.Types;
import de.dakror.villagedefense.settings.CFG;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public class Creature extends Entity
{
	Image image;
	Vector target;
	boolean frozen;
	boolean hostile;
	
	public Creature(int x, int y, String img)
	{
		super(x, y, Game.getImage("char/" + img + ".png").getWidth() / 4, Game.getImage("char/" + img + ".png").getHeight() / 4);
		image = Game.getImage("char/" + img + ".png");
		
		setBump(new Rectangle((int) (width * 0.25f), (int) (height * 0.75f), (int) (width * 0.5f), (int) (height * 0.25f)));
		frozen = false;
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		drawBump(g, false);
		
		g.drawImage(image, (int) x, (int) y, (int) x + width, (int) y + height, 0, 0, width, height, Game.w);
		
		drawBump(g, true);
	}
	
	@Override
	public void update()
	{
		if (!frozen && attributes.get(Types.SPEED) > 0 && target != null)
		{
			Vector pos = getPos();
			Vector dif = target.clone().sub(pos);
			
			if (dif.getLength() < attributes.get(Types.SPEED)) target = null;
			
			dif.setLength(attributes.get(Types.SPEED));
			
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
			
			Vector nearestPoint = null;
			
			ArrayList<Vector> points = hostile ? s.getType().getStructPoints().attacks : s.getType().getStructPoints().entries;
			
			if (points.size() == 0)
			{
				CFG.p("Target has no StructPoints!");
				return;
			}
			
			Vector pos = getPos();
			for (Vector p : points)
			{
				Vector v = p.clone();
				v.mul(Tile.SIZE);
				v.add(s.getPos());
				if (nearestPoint == null || v.getDistance(pos) < nearestPoint.getDistance(pos)) nearestPoint = v;
			}
			
			nearestPoint.y -= height * 0.75f;
			
			setTarget(nearestPoint);
		}
	}
	
	public Vector getTarget()
	{
		return target;
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
	}
}
