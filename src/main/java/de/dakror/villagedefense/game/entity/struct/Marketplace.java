package de.dakror.villagedefense.game.entity.struct;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import de.dakror.gamesetup.util.Helper;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.ui.ClickEvent;
import de.dakror.villagedefense.ui.button.CountButton;
import de.dakror.villagedefense.ui.button.TextButton;
import de.dakror.villagedefense.util.Assistant;

/**
 * @author Dakror
 */
public class Marketplace extends Struct {
	public Marketplace(int x, int y) {
		super(x, y, 5, 5);
		tx = 8;
		ty = 8;
		placeGround = true;
		setBump(new Rectangle2D.Float(0, 0, 5, 5));
		
		name = "Marktplatz";
		description = "Hier kannst mit deinen Ressourcen Handel treiben.";
		buildingCosts.set(Resource.GOLD, 200);
		buildingCosts.set(Resource.WOOD, 80);
		buildingCosts.set(Resource.STONE, 150);
		buildingCosts.set(Resource.PEOPLE, 1);
		
		guiSize.height = 250 - 90 + Resource.usable().length * 30;
		guiSize.width = 300;
	}
	
	@Override
	protected void tick(int tick) {
		super.tick(tick);
		
		if (components.size() == 0) return;
		
		Resource[] res = Resource.usableNoGold();
		for (int i = 0; i < res.length; i++) {
			((CountButton) components.get(i)).max = Game.currentGame.resources.get(res[i]);
		}
	}
	
	@Override
	public void drawGUI(Graphics2D g) {
		if (components.size() == 0) initGUI();
		try {
			Helper.drawContainer(guiPoint.x - guiSize.width / 2, guiPoint.y - guiSize.height / 2, guiSize.width, guiSize.height, false, false, g);
			Helper.drawHorizontallyCenteredString("Verkauf", guiPoint.x - guiSize.width / 2, guiSize.width, guiPoint.y - guiSize.height / 2 + 40, g, 40);
			
			Resource[] res = Resource.usableNoGold();
			int sum = 0;
			for (int i = 0; i < res.length; i++) {
				CountButton cb = (CountButton) components.get(i);
				sum += cb.value * res[i].getGoldValue();
				
				Helper.drawString(res[i].getName(), guiPoint.x - guiSize.width / 2 + 15, guiPoint.y - guiSize.height / 2 + 80 + 30 * i, g, 30);
				Assistant.drawResource(Game.currentGame.resources, Resource.GOLD, res[i].getGoldValue(), guiPoint.x - 20, guiPoint.y - guiSize.height / 2 + 60 + 30 * i, 30, 30, g);
			}
			
			Helper.drawString("Gesamt:", guiPoint.x - guiSize.width / 2 + 15, guiPoint.y + guiSize.height / 2 - 65, g, 30);
			Assistant.drawResource(Game.currentGame.resources, Resource.GOLD, sum, guiPoint.x, guiPoint.y + guiSize.height / 2 - 85, 30, 30, g);
			
			drawComponents(guiPoint.x - guiSize.width / 2, guiPoint.y - guiSize.height / 2, g);
		} catch (Exception e) {}
	}
	
	@Override
	protected void onMinedUp() {}
	
	@Override
	public Entity clone() {
		return new Marketplace((int) x / Tile.SIZE, (int) y / Tile.SIZE);
	}
	
	@Override
	protected void onDeath() {}
	
	@Override
	public void initGUI() {
		components.clear();
		
		Resource[] res = Resource.usableNoGold();
		for (int i = 0; i < res.length; i++) {
			components.add(new CountButton(guiSize.width / 2 + 35, 60 + 30 * i, 100, 0, 99, 1, 0));
		}
		TextButton sell = new TextButton((guiSize.width - 230) / 2, guiSize.height - 60, 230, "Verkaufen", 30);
		sell.addClickEvent(new ClickEvent() {
			@Override
			public void trigger() {
				int sum = 0;
				Resource[] res = Resource.usableNoGold();
				for (int i = 0; i < res.length; i++) {
					CountButton cb = (CountButton) components.get(i);
					sum += cb.value * res[i].getGoldValue();
					Game.currentGame.resources.add(res[i], -cb.value);
				}
				
				Game.currentGame.resources.add(Resource.GOLD, sum);
				
				destroyGUI();
			}
		});
		components.add(sell);
	}
	
	@Override
	public void onUpgrade(Researches research, boolean inititial) {}
}
