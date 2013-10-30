package de.dakror.villagedefense.game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import de.dakror.villagedefense.game.world.World;
import de.dakror.villagedefense.settings.CFG;
import de.dakror.villagedefense.settings.Resources;
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
		w.setUndecorated(true);
		w.setResizable(false);
		w.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		world = new World();
		world.init();
		state = 0;
		nextWave = System.currentTimeMillis() + (1000 * 300); // nextwave in 5 minutes
		w.setVisible(true);
		
		try
		{
			w.setFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/MorrisRomanBlack.ttf")));
			w.createBufferStrategy(2);
		}
		catch (Exception e)
		{
			CFG.p("retry");
			w.dispose();
			init();
			return;
		}
		
		new UpdateThread();
	}
	
	public void draw()
	{
		if (start == 0) start = System.currentTimeMillis();
		if (frames == Integer.MAX_VALUE) frames = 0;
		
		BufferStrategy s = w.getBufferStrategy();
		Graphics2D g = (Graphics2D) s.getDrawGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		g.clearRect(0, 0, w.getWidth(), w.getHeight());
		
		// draw content
		world.draw(g);
		
		drawGUI(g);
		
		drawState(g);
		
		g.dispose();
		
		if (!s.contentsLost()) s.show();
		
		frames++;
	}
	
	public void drawGUI(Graphics2D g)
	{
		Assistant.drawContainer(0, 0, w.getWidth(), 80, false, false, g);
		Assistant.drawContainer(0, w.getHeight() - 100, w.getWidth(), 100, false, false, g);
		
		// -- time panel -- //
		Assistant.drawContainer(w.getWidth() / 2 - 150, 0, 300, 80, true, true, g);
		Assistant.drawHorizontallyCenteredString(new SimpleDateFormat("mm:ss").format(new Date((nextWave >= System.currentTimeMillis()) ? nextWave - System.currentTimeMillis() : 0)), w.getWidth(), 60, g, 70);
	}
	
	public void drawState(Graphics2D g)
	{
		if (state != 0)
		{
			Composite composite = g.getComposite();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
			g.setColor(Color.darkGray);
			g.fillRect(0, 0, w.getWidth(), w.getHeight());
			g.setColor(Color.white);
			g.setComposite(composite);
			Assistant.drawHorizontallyCenteredString(state == 1 ? "Gewonnen!" : "Niederlage!", w.getWidth(), w.getHeight() / 2, g, 100);
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
