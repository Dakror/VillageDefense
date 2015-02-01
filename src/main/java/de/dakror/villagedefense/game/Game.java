package de.dakror.villagedefense.game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import de.dakror.gamesetup.GameFrame;
import de.dakror.gamesetup.layer.Layer;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.creature.Villager;
import de.dakror.villagedefense.game.entity.struct.Bakery;
import de.dakror.villagedefense.game.entity.struct.Barricade;
import de.dakror.villagedefense.game.entity.struct.Catapult;
import de.dakror.villagedefense.game.entity.struct.CoalFactory;
import de.dakror.villagedefense.game.entity.struct.Farm;
import de.dakror.villagedefense.game.entity.struct.House;
import de.dakror.villagedefense.game.entity.struct.Marketplace;
import de.dakror.villagedefense.game.entity.struct.Mine;
import de.dakror.villagedefense.game.entity.struct.Sawmill;
import de.dakror.villagedefense.game.entity.struct.School;
import de.dakror.villagedefense.game.entity.struct.Smeltery;
import de.dakror.villagedefense.game.entity.struct.Struct;
import de.dakror.villagedefense.game.entity.struct.Torch;
import de.dakror.villagedefense.game.entity.struct.Warehouse;
import de.dakror.villagedefense.game.entity.struct.Way;
import de.dakror.villagedefense.game.entity.struct.Windmill;
import de.dakror.villagedefense.game.entity.struct.tower.ArrowTower;
import de.dakror.villagedefense.game.world.World;
import de.dakror.villagedefense.layer.BuildStructLayer;
import de.dakror.villagedefense.layer.HUDLayer;
import de.dakror.villagedefense.layer.MenuLayer;
import de.dakror.villagedefense.layer.StateLayer;
import de.dakror.villagedefense.layer.StructGUILayer;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.settings.WaveManager;

/**
 * @author Dakror
 */
public class Game extends GameFrame {
	public static final float hungerPerUnitPerSecond = 0.005f;
	public static final int forwardFactor = 5;
	
	public static Game currentGame;
	public static World world;
	public static Struct[] buildableStructs = { new Way(0, 0), new House(0, 0), new Farm(0, 0), new Windmill(0, 0), new Bakery(0, 0), new Mine(0, 0), new Sawmill(0, 0),
			new CoalFactory(0, 0), new Smeltery(0, 0), new Marketplace(0, 0), new School(0, 0), new Warehouse(0, 0), new Barricade(0, 0), new ArrowTower(0, 0), new Catapult(0, 0),
			new Torch(0, 0) };
	
	public int worldCreated; // in seconds
	
	boolean debug;
	
	boolean started;
	public boolean scoreSent;
	public boolean skipDraw = false;
	
	/**
	 * 0 = playing<br>
	 * 1 = won<br>
	 * 2 = lost<br>
	 * 3 = pause<br>
	 */
	public int state;
	
	public Resources resources;
	public ArrayList<Researches> researches = new ArrayList<>();
	
	public Point mouseDown, mouseDownWorld, mouseDrag;
	
	public Struct activeStruct;
	
	// -- layer hacks -- //
	public boolean killedCoreHouse;
	public boolean placedStruct;
	
	public Game() {
		currentGame = this;
	}
	
	@Override
	public void initGame() {
		started = false;
		debug = false;
		scoreSent = false;
		killedCoreHouse = false;
		frames = 0;
		start = 0;
		
		w.setIconImage(getImage("icon/castle.png"));
		w.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(getImage("cursor.png"), new Point(0, 0), "cursor"));
		
		w.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		w.setMinimumSize(new Dimension(1280, 720));
		w.setUndecorated(true);
		
		resources = new Resources();
		
		world = new World();
		world.init(20, 20);
		worldCreated = (int) (System.currentTimeMillis() / 1000);
		state = 0;
		
		addLayer(new BuildStructLayer());
		addLayer(new HUDLayer());
		addLayer(new StructGUILayer());
		addLayer(new StateLayer());
		addLayer(new MenuLayer());
		
		WaveManager.init();
		for (Researches r : Researches.values()) {
			if (r.getCosts(false).size() == 0) researches.add(r);
		}
		
		w.setVisible(true);
		w.getContentPane().setIgnoreRepaint(true);
		try {
			w.setFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/MorrisRomanBlack.ttf")));
			w.createBufferStrategy(2);
		} catch (Exception e) {
			w.dispose();
			return;
		}
	}
	
	public void startGame(int width, int height) {
		resources = new Resources();
		resources.set(Resource.GOLD, 1000);
		resources.set(Resource.BREAD, 500);
		
		// for (Resource r : Resource.values())
		// resources.set(r, 10000);
		// resources.set(Resource.PEOPLE, 0);
		
		Game.world.init(width, height);
	}
	
	@Override
	public void draw(Graphics2D g) {
		if (!skipDraw) world.draw(g);
		else skipDraw = false;
		
		if (mouseDown != null && mouseDrag != null) {
			Rectangle r = getDragRectangle();
			
			Composite c = g.getComposite();
			Color o = g.getColor();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
			g.setColor(Color.decode("#234af3"));
			g.fillRect(r.x, r.y, r.width, r.height);
			g.setComposite(c);
			g.setColor(Color.decode("#09c3f1"));
			g.drawRect(r.x, r.y, r.width, r.height);
			g.setColor(o);
		}
		
		drawLayers(g);
		
		// TODO: DEBUG
		Color oldColor = g.getColor();
		Font oldFont = g.getFont();
		g.setColor(Color.green);
		g.setFont(new Font("Arial", Font.PLAIN, 18));
		g.drawString(getFPS() + " FPS", 0, 14);
		g.drawString(getUPS() + " UPS", 100, 14);
		g.setColor(oldColor);
		g.setFont(oldFont);
		
		if (!started) {
			state = 3;
			started = true;
		}
	}
	
	public Rectangle getDragRectangle() {
		if (mouseDown == null || mouseDrag == null) return new Rectangle();
		
		int x = mouseDown.x < mouseDrag.x ? mouseDown.x : mouseDrag.x;
		int y = mouseDown.y < mouseDrag.y ? mouseDown.y : mouseDrag.y;
		int w = mouseDown.x < mouseDrag.x ? mouseDrag.x - mouseDown.x : mouseDown.x - mouseDrag.x;
		int h = mouseDown.y < mouseDrag.y ? mouseDrag.y - mouseDown.y : mouseDown.y - mouseDrag.y;
		return new Rectangle(x, y, w, h);
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		super.keyReleased(e);
		
		switch (e.getKeyCode()) {
			case KeyEvent.VK_F11: {
				if (w.isUndecorated()) setWindowed();
				else setFullscreen();
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		for (Layer l : layers) {
			l.keyPressed(e);
			if (l.isModal() && l.isEnabled()) return;
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		super.mouseDragged(e);
		
		if ((world.width > getWidth() || world.height > getHeight()) && mouseDown != null && e.getModifiers() == MouseEvent.BUTTON2_MASK) {
			int x = mouseDown.x - e.getX() - mouseDownWorld.x;
			int y = mouseDown.y - e.getY() - mouseDownWorld.y;
			
			if (x < 0) x = 0;
			if (x > world.width - getWidth()) x = world.width - getWidth();
			if (y < 0) y = 0;
			if (y > world.height - getHeight()) y = world.height - getHeight();
			
			world.x = -x;
			world.y = -y;
		}
		
		
		if (e.getModifiers() == MouseEvent.BUTTON1_MASK/* && e.getY() > 80 && e.getY() < Game.getHeight() - 100 */) {
			if (activeStruct == null) mouseDrag = e.getPoint();
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
		
		if (state == 0) {
			world.mouseMoved(e);
			
			if (world.selectedEntity != null && world.selectedEntity instanceof Struct && ((Struct) world.selectedEntity).guiPoint != null) world.selectedEntity.mouseMoved(e);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);
		
		// mouseDownWorld = null;
		if (world.selectedEntity != null && world.selectedEntity instanceof Struct && ((Struct) world.selectedEntity).guiPoint != null) world.selectedEntity.mouseReleased(e);
		
		placedStruct = false;
		
		if (mouseDown != null && mouseDrag != null) {
			
			Rectangle r = getDragRectangle();
			for (Entity e1 : world.entities) {
				if (e1 instanceof Villager && e1.getArea(true).intersects(r) && e1.alpha > 0) e1.setClicked(true);
			}
		}
		
		mouseDown = null;
		mouseDownWorld = null;
		mouseDrag = null;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		
		mouseDown = e.getPoint();
		mouseDownWorld = new Point(world.x, world.y);
		
		if (state == 0) world.mousePressed(e);
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public int getPeople() {
		int people = 0;
		
		for (Entity e : world.entities) {
			if (e instanceof Villager && e.alpha > 0) {
				Villager v = (Villager) e;
				if (v.getTargetEntity() != null && v.getTargetEntity() instanceof Struct) {
					Struct s = (Struct) v.getTargetEntity();
					if (s.getBuildingCosts().get(Resource.PEOPLE) > 0 && !v.isTargetByUser()) continue;
				}
				
				people++;
			}
		}
		
		return people;
	}
	
	public int getPlayerScore() {
		int score = 0;
		
		for (Resource r : resources.getFilled())
			score += resources.get(r);
		
		for (Entity e : world.entities) {
			if (e instanceof Struct && e.getResources().size() == 0) score += 50;
		}
		
		score += WaveManager.wave * 250;
		
		score -= (world.core.getAttributes().get(Attribute.HEALTH_MAX) - world.core.getAttributes().get(Attribute.HEALTH)) * 10;
		
		score -= 2151;
		
		score = score < 0 ? 0 : score;
		
		return score;
	}
	
	public Researches[] getResearches(Class<?> targetClass) {
		ArrayList<Researches> res = new ArrayList<>();
		for (Researches r : researches) {
			if (r.isTarget(targetClass)) res.add(r);
		}
		
		return res.toArray(new Researches[] {});
	}
	
}
