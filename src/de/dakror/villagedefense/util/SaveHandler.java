package de.dakror.villagedefense.util;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.creature.Creature;
import de.dakror.villagedefense.game.entity.creature.Forester;
import de.dakror.villagedefense.game.entity.creature.Woodsman;
import de.dakror.villagedefense.game.entity.struct.Struct;
import de.dakror.villagedefense.game.world.Chunk;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes;
import de.dakror.villagedefense.settings.CFG;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources;
import de.dakror.villagedefense.settings.WaveManager;

/**
 * @author Dakror
 */
public class SaveHandler
{
	public static void saveGame()
	{
		try
		{
			File save = new File(CFG.DIR, "saves/" + new SimpleDateFormat("'Spielstand' dd.MM.yyyy HH-mm-ss").format(new Date()) + ".save");
			save.createNewFile();
			
			JSONObject o = new JSONObject();
			
			o.put("version", CFG.VERSION);
			o.put("created", Game.currentGame.worldCreated);
			o.put("width", Game.world.width);
			o.put("height", Game.world.height);
			o.put("tile", new BASE64Encoder().encode(Compressor.compressRow(Game.world.getData())));
			o.put("resources", Game.currentGame.resources.getData());
			o.put("researches", Game.currentGame.researches);
			o.put("wave", WaveManager.wave);
			o.put("time", WaveManager.nextWave);
			
			JSONArray entities = new JSONArray();
			for (Entity e : Game.world.entities)
			{
				if ((e instanceof Forester) || (e instanceof Woodsman)) continue; // don't save them, because they get spawned by the house upgrades
				
				entities.put(e.getData());
			}
			o.put("entities", entities);
			
			Compressor.compressFile(save, o.toString());
			// Assistant.setFileContent(new File(save.getPath() + ".debug"), o.toString());
			Game.currentGame.state = 3;
			JOptionPane.showMessageDialog(Game.w, "Spielstand erfolgreich gespeichert.", "Speichern erfolgreich", JOptionPane.INFORMATION_MESSAGE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void loadSave(File f)
	{
		try
		{
			JSONObject o = new JSONObject(Compressor.decompressFile(f));
			Game.world.init(o.getInt("width"), o.getInt("height"));
			Game.world.setData((int) Math.ceil(o.getInt("width") / (float) (Chunk.SIZE * Tile.SIZE)), (int) Math.ceil(o.getInt("height") / (float) (Chunk.SIZE * Tile.SIZE)), Compressor.decompressRow(new BASE64Decoder().decodeBuffer(o.getString("tile"))));
			Game.currentGame.resources = new Resources(o.getJSONObject("resources"));
			
			if (o.has("created")) Game.currentGame.worldCreated = o.getInt("created");
			
			JSONArray researches = o.getJSONArray("researches");
			Game.currentGame.researches = new ArrayList<>();
			for (int i = 0; i < researches.length(); i++)
				Game.currentGame.researches.add(Researches.valueOf(researches.getString(i)));
			
			WaveManager.wave = o.getInt("wave");
			WaveManager.nextWave = o.getInt("time");
			
			JSONArray entities = o.getJSONArray("entities");
			
			HashMap<Integer, Creature> creaturesWithTargets = new HashMap<>();
			
			for (int i = 0; i < entities.length(); i++)
			{
				JSONObject e = entities.getJSONObject(i);
				Entity entity = (Entity) Class.forName(e.getString("class")).getConstructor(int.class, int.class).newInstance(e.getInt("x"), e.getInt("y"));
				entity.setAttributes(new Attributes(e.getJSONObject("attributes")));
				entity.setResources(new Resources(e.getJSONObject("resources")));
				
				if (entity instanceof Creature)
				{
					Creature c = (Creature) entity;
					c.alpha = (float) e.getDouble("alpha");
					c.setSpawnPoint(new Point(e.getInt("spawnX"), e.getInt("spawnY")));
					
					if (!e.isNull("targetX") || !e.isNull("targetEntity"))
					{
						creaturesWithTargets.put(i, c);
						continue;
					}
				}
				else if (entity instanceof Struct)
				{
					JSONArray researches2 = e.getJSONArray("researches");
					
					((Struct) entity).clearResearches();
					for (int j = 0; j < researches2.length(); j++)
						((Struct) entity).add(Researches.valueOf(researches2.getString(j)));
				}
				
				Game.world.addEntity2(entity, true);
			}
			
			// -- set creatures' targets
			for (Iterator<Integer> iterator = creaturesWithTargets.keySet().iterator(); iterator.hasNext();)
			{
				int index = iterator.next();
				JSONObject e = entities.getJSONObject(index);
				
				Entity entity = creaturesWithTargets.get(index);
				
				if (!e.isNull("targetEntity"))
				{
					JSONObject tE = e.getJSONObject("targetEntity");
					for (Entity e1 : Game.world.entities)
					{
						int x = (int) (e1 instanceof Creature ? e1.getX() : e1.getX() / Tile.SIZE);
						int y = (int) (e1 instanceof Creature ? e1.getY() : e1.getY() / Tile.SIZE);
						if (e1.getClass().getName().equals(tE.getString("class")) && tE.getInt("x") == x && tE.getInt("y") == y)
						{
							((Creature) entity).setTarget(e1, false);
							continue;
						}
					}
				}
				if (!e.isNull("targetX"))
				{
					((Creature) entity).setTarget(e.getInt("targetX"), e.getInt("targetY"), false);
				}
				
				Game.world.addEntity2(entity, true);
			}
			
			Game.currentGame.state = 3;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static File[] getSaves()
	{
		return new File(CFG.DIR, "saves").listFiles(new FileFilter()
		{
			@Override
			public boolean accept(File pathname)
			{
				return pathname.getName().endsWith(".save");
			}
		});
	}
	
	public static boolean isWorldScorePosted(int worldCreated) throws Exception
	{
		File f = new File(CFG.DIR, "scores");
		try
		{
			if (!f.exists()) return false;
			
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = "";
			while ((line = br.readLine()) != null)
			{
				if (Integer.parseInt(line) == worldCreated)
				{
					br.close();
					return true;
				}
			}
			br.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public static void addWorldScorePosted(int worldCreated)
	{
		File f = new File(CFG.DIR, "scores");
		try
		{
			f.createNewFile();
			Assistant.setFileContent(f, Assistant.getFileContent(f) + worldCreated + "\r\n");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void sendScore()
	{
		try
		{
			if (isWorldScorePosted(Game.currentGame.worldCreated))
			{
				JOptionPane.showMessageDialog(null, "Du hast deinen Punktestand auf dieser Karte bereits in der Bestenliste platziert!", "Bereits platziert!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			String response = Assistant.getURLContent(new URL("http://dakror.de/villagedefense/api/scores.php?USERNAME=" + CFG.USERNAME + "&SCORE=" + Game.currentGame.getPlayerScore()));
			if (!response.equals("false"))
			{
				JOptionPane.showMessageDialog(null, "Dein Punktestand wurde erfolgreich in der Bestenliste platziert.", "Platzieren erfolgreich!", JOptionPane.INFORMATION_MESSAGE);
				addWorldScorePosted(Game.currentGame.worldCreated);
				Game.currentGame.scoreSent = true;
			}
			else JOptionPane.showMessageDialog(null, "Dein Punktestand konnte nicht in der Bestenliste platziert werden!", "Platzieren fehlgeschlagen!", JOptionPane.ERROR_MESSAGE);
		}
		catch (Exception e1)
		{
			JOptionPane.showMessageDialog(null, "Dein Punktestand konnte nicht in der Bestenliste platziert werden!\nMöglicherweise bist du nicht mit dem Internet verbunden.", "Platzieren fehlgeschlagen!", JOptionPane.ERROR_MESSAGE);
		}
	}
}
