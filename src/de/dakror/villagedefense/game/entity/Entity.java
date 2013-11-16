package de.dakror.villagedefense.game.entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.JSONObject;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.creature.Creature;
import de.dakror.villagedefense.game.entity.struct.Struct;
import de.dakror.villagedefense.game.projectile.Projectile;
import de.dakror.villagedefense.settings.Attributes;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources;
import de.dakror.villagedefense.util.Drawable;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public abstract class Entity implements Drawable
{
	protected float x, y;
	protected int width, height;
	protected boolean hovered, clicked, dead;
	protected String name;
	protected Rectangle bump;
	protected Attributes attributes;
	protected Resources resources;
	public String description;
	
	protected int randomOffset = (int) (Math.random() * 100);
	
	public CopyOnWriteArrayList<Projectile> targetedProjectiles = new CopyOnWriteArrayList<>();
	
	public float alpha;
	
	public Entity(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		bump = new Rectangle();
		attributes = new Attributes();
		resources = new Resources();
		
		alpha = 1;
	}
	
	public void drawBump(Graphics2D g, boolean above)
	{
		if (bump == null || (!hovered && !clicked)) return;
		
		Color oldColor = g.getColor();
		g.setColor(clicked ? Color.black : Color.darkGray);
		if (above)
		{
			g.drawLine((int) x + bump.x, (int) y + bump.y, (int) x + bump.x, (int) y + bump.y + bump.height); // left
			g.drawLine((int) x + bump.x, (int) y + bump.y + bump.height, (int) x + bump.x + bump.width, (int) y + bump.y + bump.height); // bottom
			g.drawLine((int) x + bump.x + bump.width, (int) y + bump.y, (int) x + bump.x + bump.width, (int) y + bump.y + bump.height); // right
		}
		else
		{
			g.drawLine((int) x + bump.x, (int) y + bump.y, (int) x + bump.x + bump.width, (int) y + bump.y); // top
		}
		g.setColor(oldColor);
	}
	
	public float getX()
	{
		return x;
	}
	
	public void setX(float x)
	{
		this.x = x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}
	
	@Override
	public void update(int tick)
	{
		if (alpha > 0)
		{
			alpha = 1;
			
			for (Entity e : Game.world.entities)
			{
				if (e.equals(this)) continue;
				
				if (intersects(e) && e.alpha == 1 && e.y + e.height < y + height)
				{
					alpha = 0.6f;
					break;
				}
			}
		}
		
		if (new Double(x).isNaN()) // glitched
		{
			if ((this instanceof Creature) && ((Creature) this).isHostile()) kill();
			return;
		}
		
		if (attributes.get(Attribute.HEALTH) < 1) kill();
		
		tick(tick);
	}
	
	public void drawEntity(Graphics2D g)
	{
		if (alpha == 0) return;
		
		drawBump(g, false);
		Composite c = g.getComposite();
		if (this instanceof Struct) g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		draw(g);
		g.setComposite(c);
		
		drawBump(g, true);
	}
	
	protected abstract void tick(int tick);
	
	@Override
	public abstract Entity clone();
	
	public int getWidth()
	{
		return width;
	}
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	public void translate(int x, int y)
	{
		this.x += x;
		this.y += y;
	}
	
	public Rectangle getBump(boolean includePos)
	{
		if (!includePos) return bump;
		else
		{
			Rectangle rect = (Rectangle) bump.clone();
			rect.translate((int) x, (int) y);
			return rect;
		}
	}
	
	public void setBump(Rectangle r)
	{
		bump = r;
	}
	
	public boolean contains(float x, float y)
	{
		return x >= this.x && y >= this.y && x <= this.x + width && y <= this.y + height;
	}
	
	public boolean mouseMoved(MouseEvent e)
	{
		if (alpha == 0) return false;
		
		return hovered = contains(e.getX(), e.getY());
	}
	
	public boolean mousePressed(MouseEvent e)
	{
		if (alpha == 0) return false;
		
		if (alpha != 1 && this instanceof Struct) return clicked = getBump(true).contains(e.getPoint());
		
		return clicked = contains(e.getX(), e.getY());
	}
	
	public void mouseReleased(MouseEvent e)
	{}
	
	public boolean intersects(Entity o)
	{
		return getArea(false).intersects(o.getArea(false));
	}
	
	public Rectangle getArea(boolean world)
	{
		return new Rectangle((int) x + (world ? Game.world.x : 0), (int) y + (world ? Game.world.y : 0), width, height);
	}
	
	public void setClicked(boolean b)
	{
		clicked = b;
	}
	
	public boolean isClicked()
	{
		return clicked;
	}
	
	public void setHovered(boolean b)
	{
		hovered = b;
	}
	
	public Vector getPos()
	{
		return new Vector(x, y);
	}
	
	public Vector getCenter()
	{
		Vector v = getPos();
		v.add(new Vector((float) bump.getCenterX(), (float) bump.getCenterY()));
		return v;
	}
	
	public Vector getCenter2()
	{
		Vector v = getPos();
		v.add(new Vector(width / 2, height / 2));
		return v;
	}
	
	public void setPos(Vector v)
	{
		x = v.x;
		y = v.y;
	}
	
	public Attributes getAttributes()
	{
		return attributes;
	}
	
	public void setAttributes(Attributes attributes)
	{
		this.attributes = attributes;
	}
	
	public Resources getResources()
	{
		return resources;
	}
	
	public void setResources(Resources resources)
	{
		this.resources = resources;
	}
	
	public void dealDamage(int amount, Object source)
	{
		int newVal = (int) (attributes.get(Attribute.HEALTH) - amount);
		
		if (newVal < 0) newVal = 0;
		if (newVal > attributes.get(Attribute.HEALTH_MAX)) newVal = (int) attributes.get(Attribute.HEALTH_MAX);
		
		attributes.set(Attribute.HEALTH, newVal);
	}
	
	public boolean isDead()
	{
		return dead;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void addTargetedProjectile(Projectile p)
	{
		targetedProjectiles.add(p);
	}
	
	public void kill()
	{
		dead = true;
		onDeath();
	}
	
	public abstract JSONObject getData();
	
	// -- abstract event methods -- //
	public abstract void onSpawn(boolean initial);
	
	protected abstract void onDeath();
}
