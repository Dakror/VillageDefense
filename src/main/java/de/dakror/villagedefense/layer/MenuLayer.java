package de.dakror.villagedefense.layer;

import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.net.URI;

import de.dakror.dakrorbin.DakrorBin;
import de.dakror.gamesetup.layer.Layer;
import de.dakror.gamesetup.util.Helper;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.ui.ClickEvent;
import de.dakror.villagedefense.ui.button.MenuButton;

/**
 * @author Dakror
 */
public class MenuLayer extends Layer {
	public MenuLayer() {
		modal = true;
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(Game.getImage("menu/menuBG.png"), 0, 0, Game.getWidth(), Game.getHeight(), Game.w);
		Helper.drawImageCenteredRelativeScaled(Game.getImage("menu/menuFG.png"), 80, 1920, 1080, Game.getWidth(), Game.getHeight(), g);
		
		int size = 60;
		int size2 = size - 20;
		
		Helper.drawContainer(Game.getWidth() - size - 10, Game.getHeight() - size - 10, size, size, false, new Rectangle(Game.getWidth() - size - 10, Game.getHeight() - size - 10, size, size).contains(Game.currentGame.mouse), g);
		g.drawImage(Game.getImage("icon/help.png"), Game.getWidth() - size, Game.getHeight() - size, size2, size2, Game.w);
		
		Helper.drawString("Version " + DakrorBin.buildDate, 10, Game.getHeight() - 10, g, 24);
		
		drawComponents(g);
	}
	
	@Override
	public void update(int tick) {
		if (Game.currentGame.alpha == 1) {
			Game.currentGame.layers.remove(Game.currentGame.layers.size() - 1);
			Game.currentGame.fadeTo(0, 0.05f);
			return;
		}
		
		updateComponents(tick);
	}
	
	@Override
	public void init() {
		MenuButton start = new MenuButton("newGame", 0);
		start.addClickEvent(new ClickEvent() {
			@Override
			public void trigger() {
				Game.currentGame.addLayer(new NewGameLayer(MenuLayer.this));
			}
		});
		components.add(start);
		
		MenuButton load = new MenuButton("loadGame", 1);
		load.addClickEvent(new ClickEvent() {
			@Override
			public void trigger() {
				Game.currentGame.addLayer(new LoadGameLayer(MenuLayer.this));
			}
		});
		components.add(load);
		
		MenuButton quit = new MenuButton("quit", 2);
		quit.addClickEvent(new ClickEvent() {
			@Override
			public void trigger() {
				System.exit(0);
			}
		});
		components.add(quit);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		
		int size = 60;
		if (new Rectangle(Game.getWidth() - size - 10, Game.getHeight() - size - 10, size, size).contains(e.getPoint())) {
			try {
				Desktop.getDesktop().browse(new URI("https://github.com/Dakror/Village-Defense/wiki"));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
