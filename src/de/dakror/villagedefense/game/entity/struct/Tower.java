package de.dakror.villagedefense.game.entity.struct;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.creature.Creature;
import de.dakror.villagedefense.game.tile.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.util.Assistant;
import de.dakror.villagedefense.util.TowerTargetComparator;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public abstract class Tower extends Struct
{
	int color;
	
	public Tower(int x, int y, int color)
	{
		super(x, y, 1, 2);
		tx = 6;
		ty = 5;
		placeGround = false;
		this.color = color;
		setBump(new Rectangle2D.Float(0, 1.5f, 1, 0.5f));
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		super.draw(g);
		Assistant.drawImage(Game.getImage("structs.png"), (int) x, (int) y - Tile.SIZE / 4, Tile.SIZE, Tile.SIZE, 5 * Tile.SIZE, (5 + color) * Tile.SIZE, Tile.SIZE, Tile.SIZE, g);
		
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
		if (tick % attributes.get(Attribute.ATTACK_SPEED) == 0)
		{
			ArrayList<Creature> t = getTargetableCreatures();
			if (t.size() > 0) t.get(0).dealDamage((int) attributes.get(Attribute.DAMAGE_CREATURE));
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
			
			if (e.getCenter().getDistance(getCenter()) < attributes.get(Attribute.ATTACK_RANGE)) targetable.add(c);
		}
		
		Collections.sort(targetable, new TowerTargetComparator());
		
		return targetable;
	}
	
	@Override
	protected void onDeath()
	{}
}
