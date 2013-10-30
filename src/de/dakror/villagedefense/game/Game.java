package de.dakror.villagedefense.game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import de.dakror.villagedefense.game.world.World;
import de.dakror.villagedefense.settings.Resources;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.util.Assistant;
import de.dakror.villagedefense.util.EventListener;

/**
 * @author Dakror
 */
public class Game extends EventListener
{
	public static Game currentGame;
	public static JFrame w;
	
	static HashMap<String, BufferedImage> imageCache = new HashMap<>();
	
	int frames = 0;
	long start = 0;
	
	boolean debug = false;
	
	/**
	 * 0 = playing<br>
	 * 1 = won<br>
	 * 2 = lost<br>
	 */
	public int state;
	
	public static World world;
	public Resources resources;
	
	public UpdateThread updateThread;
	
	public long nextWave; // UNIX timestamp
	
	public Game()
	{
		currentGame = this;
		
		init();
	}
	
	public void init()
	{
		w = new JFrame("Village Defense");
		w.addKeyListener(this);
		w.addMouseListener(this);
		w.addMouseMotionListener(this);
		w.addMouseWheelListener(this);
		w.setBackground(Color.black);
		w.setForeground(Color.white);
		w.getContentPane().setBackground(Color.black);
		w.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(getImage("cursor.png"), new Point(0, 0), "cursor"));
		
		w.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		// w.setExtendedState(JFrame.MAXIMIZED_BOTH); makes game superlaggy somehow
		w.setMinimumSize(new Dimension(1280, 720));
		w.setUndecorated(true);
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		world = new World();
		world.init();
		state = 0;
		nextWave = System.currentTimeMillis() + (1000 * 300); // nextwave in 5 minutes
		resources = new Resources();
		resources.set(Resource.GOLD, 1000);
		w.setVisible(true);
		w.getContentPane().setIgnoreRepaint(true);
		try
		{
			w.setFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/MorrisRomanBlack.ttf")));
			w.createBufferStrategy(2);
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
		
		drawGUI(g);
		
		// TODO: DEBUG
		// Color oldColor = g.getColor();
		// Font oldFont = g.getFont();
		// g.setColor(Color.green);
		// g.setFont(oldFont.deriveFont(25f));
		// g.drawString(Math.round(frames / ((System.currentTimeMillis() - start) / 1000f)) + " FPS", 0, 20);
		// g.drawString(Math.round(updateThread.tick / ((System.currentTimeMillis() - start) / 1000f)) + " UPS", 0, 40);
		// g.setColor(oldColor);
		// g.setFont(oldFont);
		
		drawState(g);
		
		g.dispose();
		
		if (!s.contentsLost()) s.show();
		
		frames++;
	}
	
	public void drawGUI(Graphics2D g)
	{
		Assistant.drawContainer(getWidth() / 2 - 175, 70, 350, 60, false, false, g);
		if (world.selectedEntity != null)
		{
			Assistant.drawHorizontallyCenteredString(world.selectedEntity.getName(), getWidth(), 115, g, 40);
			
			if (world.selectedEntity.getResources().size() > 0)
			{
				ArrayList<Resource> resources = world.selectedEntity.getResources().getFilled();
				Assistant.drawShadow(0, 80, 160, resources.size() * 24 + 40, g);
				for (int i = 0; i < resources.size(); i++)
				{
					Assistant.drawResource(world.selectedEntity.getResources(), resources.get(i), 16, 100 + i * 24, 26, 30, g);
				}
			}
		}
		
		Assistant.drawContainer(0, 0, getWidth(), 80, false, false, g);
		Assistant.drawContainer(0, getHeight() - 100, getWidth(), 100, false, false, g);
		
		for (int i = 0; i < Resource.values().length; i++)
		{
			int w = (getWidth() / 2 - 100) / 4;
			
			Assistant.drawResource(resources, Resource.values()[i], 25 + i * w, 30, 30, 25, g);
		}
		
		// -- time panel -- //
		Assistant.drawContainer(getWidth() / 2 - 150, 0, 300, 80, true, true, g);
		Assistant.drawHorizontallyCenteredString(new SimpleDateFormat("mm:ss").format(new Date((nextWave >= System.currentTimeMillis()) ? nextWave - System.currentTimeMillis() : 0)), getWidth(), 60, g, 70);
	}
	
	public void drawState(Graphics2D g)
	{
		if (state != 0)
		{
			Composite composite = g.getComposite();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
			g.setColor(Color.darkGray);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.white);
			g.setComposite(composite);
			Assistant.drawHorizontallyCenteredString(state == 1 ? "Gewonnen!" : "Niederlage!", getWidth(), getHeight() / 2, g, 100);
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_F11)
		{
			w.dispose();
			w.setUndecorated(!w.isUndecorated());
			w.setVisible(true);
			w.createBufferStrategy(2);
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		if (state == 0) world.mouseMoved(e);
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		if (state == 0) world.mousePressed(e);
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
