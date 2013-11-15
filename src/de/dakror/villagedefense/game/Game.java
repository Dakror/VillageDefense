package de.dakror.villagedefense.game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.creature.Villager;
import de.dakror.villagedefense.game.entity.struct.Barricade;
import de.dakror.villagedefense.game.entity.struct.Catapult;
import de.dakror.villagedefense.game.entity.struct.CoalFactory;
import de.dakror.villagedefense.game.entity.struct.House;
import de.dakror.villagedefense.game.entity.struct.Marketplace;
import de.dakror.villagedefense.game.entity.struct.Mine;
import de.dakror.villagedefense.game.entity.struct.Sawmill;
import de.dakror.villagedefense.game.entity.struct.School;
import de.dakror.villagedefense.game.entity.struct.Smeltery;
import de.dakror.villagedefense.game.entity.struct.Struct;
import de.dakror.villagedefense.game.entity.struct.tower.ArrowTower;
import de.dakror.villagedefense.game.world.World;
import de.dakror.villagedefense.layer.BuildStructLayer;
import de.dakror.villagedefense.layer.HUDLayer;
import de.dakror.villagedefense.layer.Layer;
import de.dakror.villagedefense.layer.MenuLayer;
import de.dakror.villagedefense.layer.StateLayer;
import de.dakror.villagedefense.layer.StructGUILayer;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.settings.WaveManager;
import de.dakror.villagedefense.util.Assistant;
import de.dakror.villagedefense.util.EventListener;

/**
 * @author Dakror
 */
public class Game extends EventListener
{
	public static Game currentGame;
	public static JFrame w;
	public static World world;
	public static Struct[] buildableStructs = { new House(0, 0), new Mine(0, 0), new Sawmill(0, 0), new CoalFactory(0, 0), new Smeltery(0, 0), new Marketplace(0, 0), new School(0, 0), new ArrowTower(0, 0), new Catapult(0, 0), new Barricade(0, 0) };
	
	static HashMap<String, BufferedImage> imageCache = new HashMap<>();
	
	public int frames;
	public long start;
	
	boolean debug;
	
	boolean started;
	public boolean scoreSent;
	
	/**
	 * 0 = playing<br>
	 * 1 = won<br>
	 * 2 = lost<br>
	 * 3 = pause<br>
	 */
	public int state;
	
	public float alpha = 0;
	float speed = 0;
	float fadeTo = 0;
	boolean fade = false;
	
	public Resources resources;
	public UpdateThread updateThread;
	public ArrayList<Researches> researches = new ArrayList<>();
	
	public Point mouse = new Point(0, 0);
	public Point mouseDown, mouseDrag;
	
	public Struct activeStruct;
	
	// -- layer hacks -- //
	public boolean killedCoreHouse;
	public boolean placedStruct;
	
	public CopyOnWriteArrayList<Layer> layers;
	
	public Game()
	{
		currentGame = this;
		
		init();
	}
	
	public void init()
	{
		started = false;
		debug = false;
		scoreSent = false;
		killedCoreHouse = false;
		frames = 0;
		start = 0;
		layers = new CopyOnWriteArrayList<>();
		
		w = new JFrame("Village Defense");
		w.addKeyListener(this);
		w.addMouseListener(this);
		w.addMouseMotionListener(this);
		w.addMouseWheelListener(this);
		w.setBackground(Color.black);
		w.setForeground(Color.white);
		w.setIconImage(getImage("icon/castle.png"));
		w.getContentPane().setBackground(Color.black);
		w.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(getImage("cursor.png"), new Point(0, 0), "cursor"));
		
		w.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		// w.setExtendedState(JFrame.MAXIMIZED_BOTH); makes game superlaggy somehow
		w.setMinimumSize(new Dimension(1280, 720));
		w.setUndecorated(true);
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		resources = new Resources();
		resources.set(Resource.GOLD, 1000);
		
		world = new World();
		world.init();
		state = 0;
		
		addLayer(new BuildStructLayer());
		addLayer(new HUDLayer());
		addLayer(new StructGUILayer());
		addLayer(new StateLayer());
		addLayer(new MenuLayer());
		
		for (Researches r : Researches.values())
		{
			if (r.getCosts(false).size() == 0) researches.add(r);
		}
		
		WaveManager.init();
		w.setVisible(true);
		w.getContentPane().setIgnoreRepaint(true);
		try
		{
			w.setFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/MorrisRomanBlack.ttf")));
			w.createBufferStrategy(2);
		}
		catch (Exception e)
		{
			w.dispose();
			init();
			return;
		}
		updateThread = new UpdateThread();
	}
	
	public void draw()
	{
		if (start == 0) start = System.currentTimeMillis();
		if (frames == Integer.MAX_VALUE) frames = 0;
		
		if (!w.isVisible()) return;
		
		BufferStrategy s = w.getBufferStrategy();
		Graphics2D g = null;
		try
		{
			g = (Graphics2D) s.getDrawGraphics();
		}
		catch (Exception e)
		{
			return;
		}
		
		g.translate(w.getInsets().left, w.getInsets().top);
		
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		g.clearRect(0, 0, getWidth(), getHeight());
		
		world.draw(g);
		
		
		for (Layer l : layers)
			l.draw(g);
		
		// TODO: DEBUG
		Color oldColor = g.getColor();
		Font oldFont = g.getFont();
		g.setColor(Color.green);
		g.setFont(new Font("Arial", Font.PLAIN, 18));
		g.drawString(Math.round(frames / ((System.currentTimeMillis() - start) / 1000f)) + " FPS", 0, 14);
		g.drawString(getUPS() + " UPS", 100, 14);
		g.setColor(oldColor);
		g.setFont(oldFont);
		
		if (mouseDown != null && mouseDrag != null)
		{
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
		
		Composite c1 = g.getComposite();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setComposite(c1);
		g.dispose();
		
		try
		{
			if (!s.contentsLost()) s.show();
		}
		catch (Exception e)
		{
			return;
		}
		
		frames++;
		if (!started)
		{
			state = 3;
			started = true;
		}
	}
	
	public int getUPS()
	{
		return Math.round(updateThread.ticks / ((System.currentTimeMillis() - start) / 1000f));
	}
	
	public int getUPS2()
	{
		return Assistant.round(Math.round(updateThread.ticks / ((System.currentTimeMillis() - start) / 1000f)), 30);
	}
	
	public void addLayer(Layer l)
	{
		l.init();
		layers.add(l);
	}
	
	public void toggleLayer(Layer l)
	{
		for (Layer layer : layers)
		{
			if (layer.getClass().equals(l.getClass()))
			{
				layers.remove(layer);
				return;
			}
		}
		
		l.init();
		layers.add(l);
		
	}
	
	public Rectangle getDragRectangle()
	{
		if (mouseDown == null || mouseDrag == null) return new Rectangle();
		
		int x = mouseDown.x < mouseDrag.x ? mouseDown.x : mouseDrag.x;
		int y = mouseDown.y < mouseDrag.y ? mouseDown.y : mouseDrag.y;
		int w = mouseDown.x < mouseDrag.x ? mouseDrag.x - mouseDown.x : mouseDown.x - mouseDrag.x;
		int h = mouseDown.y < mouseDrag.y ? mouseDrag.y - mouseDown.y : mouseDown.y - mouseDrag.y;
		return new Rectangle(x, y, w, h);
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_F11:
			{
				w.dispose();
				w.setUndecorated(!w.isUndecorated());
				w.setVisible(true);
				w.createBufferStrategy(2);
				break;
			}
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (e.getModifiers() != MouseEvent.BUTTON1_MASK) return;
		
		e.translatePoint(-w.getInsets().left, -w.getInsets().top);
		
		mouseDrag = e.getPoint();
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		e.translatePoint(-w.getInsets().left, -w.getInsets().top);
		mouse = e.getPoint();
		
		for (Layer l : layers)
		{
			l.mouseMoved(e);
			if (l.isModal() && l.isEnabled()) break;
		}
		
		if (state == 0)
		{
			world.mouseMoved(e);
			
			
			if (world.selectedEntity != null && world.selectedEntity instanceof Struct && ((Struct) world.selectedEntity).guiPoint != null) world.selectedEntity.mouseMoved(e);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		// mouseDownWorld = null;
		if (world.selectedEntity != null && world.selectedEntity instanceof Struct && ((Struct) world.selectedEntity).guiPoint != null) world.selectedEntity.mouseReleased(e);
		
		placedStruct = false;
		
		for (Layer l : layers)
		{
			l.mouseReleased(e);
			if (l.isModal() && l.isEnabled()) break;
		}
		
		if (mouseDown != null && mouseDrag != null)
		{
			
			Rectangle r = getDragRectangle();
			for (Entity e1 : world.entities)
			{
				if (e1 instanceof Villager && e1.getArea().intersects(r) && e1.alpha > 0) e1.setClicked(true);
			}
		}
		
		mouseDown = null;
		mouseDrag = null;
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		e.translatePoint(-w.getInsets().left, -w.getInsets().top);
		
		mouseDown = e.getPoint();
		
		for (Layer l : layers)
		{
			l.mousePressed(e);
			if (l.isModal() && l.isEnabled()) break;
		}
		
		if (state == 0) world.mousePressed(e);
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		e.translatePoint(-w.getInsets().left, -w.getInsets().top);
		
		for (Layer l : layers)
		{
			l.mouseWheelMoved(e);
			if (l.isModal() && l.isEnabled()) break;
		}
	}
	
	public void setState(int state)
	{
		this.state = state;
	}
	
	public int getPeople()
	{
		int people = 0;
		
		for (Entity e : world.entities)
		{
			if (e instanceof Villager && e.alpha > 0)
			{
				Villager v = (Villager) e;
				if (v.getTargetEntity() != null && v.getTargetEntity() instanceof Struct)
				{
					Struct s = (Struct) v.getTargetEntity();
					if (s.getBuildingCosts().get(Resource.PEOPLE) > 0) continue;
				}
				
				people++;
			}
		}
		
		return people;
	}
	
	public int getPlayerScore()
	{
		int score = 0;
		
		for (Resource r : resources.getFilled())
			score += resources.get(r);
		
		for (Entity e : world.entities)
		{
			if (e instanceof Struct && e.getResources().size() == 0) score += 50;
		}
		
		score += WaveManager.wave * 250;
		
		score -= (world.core.getAttributes().get(Attribute.HEALTH_MAX) - world.core.getAttributes().get(Attribute.HEALTH)) * 10;
		
		score -= 1601;
		
		score = score < 0 ? 0 : score;
		
		return score;
	}
	
	public Researches[] getResearches(Class<?> targetClass)
	{
		ArrayList<Researches> res = new ArrayList<>();
		for (Researches r : researches)
		{
			if (r.isTarget(targetClass)) res.add(r);
		}
		
		return res.toArray(new Researches[] {});
	}
	
	public void fadeTo(float target, float speed)
	{
		fade = true;
		fadeTo = target;
		this.speed = speed;
	}
	
	public static int getWidth()
	{
		return w.getWidth() - (w.getInsets().left + w.getInsets().right);
	}
	
	public static int getHeight()
	{
		return w.getHeight() - (w.getInsets().top + w.getInsets().bottom);
	}
	
	public static BufferedImage getImage(String p)
	{
		if (imageCache.containsKey(p)) return imageCache.get(p);
		else
		{
			try
			{
				BufferedImage i = ImageIO.read(Game.class.getResource("/img/" + p));
				imageCache.put(p, i);
				
				return i;
			}
			catch (Exception e)
			{
				System.err.println("Missing image: " + p);
				return null;
			}
		}
	}
}
