package de.dakror.villagedefense.game.entity.struct;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public class CoreHouse extends Struct
{
	public CoreHouse(int x, int y)
	{
		super(x, y, 3, 5);
		tx = 5;
		ty = 0;
		setBump(new Rectangle2D.Float(0.1f, 3, 2.8f, 2));
		structPoints.addAttacks(new Vector(-1, 3.75f), new Vector(3, 3.75f));
		attributes.set(Attribute.HEALTH, 100);
		attributes.set(Attribute.HEALTH_MAX, 100);
		placeGround = true;
		name = "Basis-Gebäude";
	}
	
	@Override
	protected void onDeath()
	{
		Game.currentGame.setState(2); // game lost
	}
	
	@Override
	protected void onMinedUp()
	{}
	
	@Override
	public Entity clone()
	{
		return new CoreHouse((int) x, (int) y);
	}
	
	@Override
	public void drawGUI(Graphics2D g)
	{}
}
