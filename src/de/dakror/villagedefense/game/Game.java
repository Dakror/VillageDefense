package de.dakror.villagedefense.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import de.dakror.villagedefense.game.world.World;
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
	
	World world;
	
	public Game()
	{
		currentGame = this;
		
		w = new JFrame("Village Defense");
		w.addKeyListener(this);
		w.addMouseListener(this);
		w.addMouseMotionListener(this);
		w.addMouseWheelListener(this);
		
		w.setBackground(Color.black);
		w.setForeground(Color.white);
		w.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		w.setUndecorated(true);
		w.setResizable(false);
		w.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		world = new World();
		
		w.setVisible(true);
		w.createBufferStrategy(2);
		
		new UpdateThread();
	}
	
	public void draw()
	{
		if (start == 0) start = System.currentTimeMillis();
		
		BufferStrategy s = w.getBufferStrategy();
		Graphics2D g = (Graphics2D) s.getDrawGraphics();
		
		g.clearRect(0, 0, w.getWidth(), w.getHeight());
		
		// draw content
		world.draw(g);
		
		g.dispose();
		
		if (!s.contentsLost()) s.show();
		
		frames++;
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
			catch (IOException e)
			{
				e.printStackTrace();
				return null;
			}
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		world.mouseMoved(e);
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		world.mousePressed(e);
	}
}
