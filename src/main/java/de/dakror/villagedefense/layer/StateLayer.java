package de.dakror.villagedefense.layer;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import de.dakror.gamesetup.layer.Layer;
import de.dakror.gamesetup.util.Helper;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.util.SaveHandler;

/**
 * @author Dakror
 */
public class StateLayer extends Layer {
	@Override
	public void draw(Graphics2D g) {
		if (Game.currentGame.state != 0) {
			Composite composite = g.getComposite();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
			g.setColor(Color.darkGray);
			g.fillRect(0, 0, Game.getWidth(), Game.getHeight());
			g.setColor(Color.white);
			g.setComposite(composite);
			
			Helper.drawHorizontallyCenteredString(Game.currentGame.state == 1 ? "Gewonnen!" : (Game.currentGame.state == 2) ? "Niederlage!" : "Spiel pausiert", Game.getWidth(),
																						Game.getHeight() / 2, g, 100);
			Helper.drawHorizontallyCenteredString(Game.currentGame.state != 3 ? "Punktestand: " + Game.currentGame.getPlayerScore() : "Mit Klicken fortsetzen", Game.getWidth(),
																						Game.getHeight() / 2 + 100, g, 60);
			if (Game.currentGame.state != 3 && Game.currentGame.getPlayerScore() > 0) {
				Helper.drawHorizontallyCenteredString("Mit Klicken ins Hauptmenü", Game.getWidth(), Game.getHeight() / 2 + 200, g, 60);
				Helper.drawContainer(	Game.getWidth() / 4 * 3, Game.getHeight() / 2 - 50, 200, 200, true,
															new Rectangle(Game.getWidth() / 4 * 3, Game.getHeight() / 2 - 50, 200, 200).contains(Game.currentGame.mouse), g);
				g.drawImage(Game.getImage("icon/ebook.png"), Game.getWidth() / 4 * 3 + 20, Game.getHeight() / 2 - 30, 160, 160, Game.w);
				if (Game.currentGame.scoreSent) Helper.drawShadow(Game.getWidth() / 4 * 3 - 10, Game.getHeight() / 2 - 60, 220, 220, g);
			}
			if (Game.currentGame.state == 3) {
				if (!new Rectangle(5, 5, 70, 70).contains(Game.currentGame.mouse)) Helper.drawContainer(5, 5, 70, 70, false, false, g);
				else Helper.drawContainer(0, 0, 80, 80, false, true, g);
				
				g.drawImage(Game.getImage("icon/back.png"), 15, 15, 50, 50, Game.w);
			}
		}
	}
	
	@Override
	public void update(int tick) {}
	
	@Override
	public void init() {}
	
	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		
		if (Game.currentGame.killedCoreHouse) {
			Game.currentGame.killedCoreHouse = false;
			return;
		}
		
		if (new Rectangle(Game.getWidth() - 75, 5, 70, 70).contains(e.getPoint())) // pause
		{
			Game.currentGame.state = 3;
			return;
		}
		
		if (new Rectangle(5, 5, 70, 70).contains(Game.currentGame.mouse) && Game.currentGame.state == 3) // back
		{
			if (JOptionPane.showConfirmDialog(Game.w, "Bist du sicher, dass du zum Hauptmenü zurückkehren willst?\nJeglicher ungespeicherter Fortschritt geht verloren!", "Sicher?",
																				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
				Game.currentGame.state = 3;
				Game.currentGame.addLayer(new MenuLayer());
			}
			return;
		}
		
		if (Game.currentGame.state == 3) Game.currentGame.state = 0;
		
		if (Game.currentGame.state == 1 || Game.currentGame.state == 2) {
			if (new Rectangle(Game.getWidth() / 4 * 3, Game.getHeight() / 2 - 50, 200, 200).contains(e.getPoint()) && e.getButton() == MouseEvent.BUTTON1) {
				if (Game.currentGame.scoreSent) return;
				
				SaveHandler.sendScore();
			} else {
				Game.currentGame.updater.closeRequested = true;
				Game.w.dispose();
				Game.currentFrame.init("Village Defense");
			}
		}
	}
}
