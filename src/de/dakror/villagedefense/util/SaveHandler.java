package de.dakror.villagedefense.util;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
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
			
			o.put("width", Game.world.chunks.length);
			o.put("height", Game.world.chunks[0].length);
			o.put("tile", new BASE64Encoder().encode(Compressor.compressRow(Game.world.getData())));
			o.put("resources", Game.currentGame.resources.getData());
			o.put("researches", Game.currentGame.researches);
			o.put("wave", WaveManager.wave);
			o.put("time", WaveManager.nextWave);
			
			JSONArray entities = new JSONArray();
			for (Entity e : Game.world.entities)
				entities.put(e.getData());
			o.put("entities", entities);
			
			Compressor.compressFile(save, o.toString());
			Assistant.setFileContent(new File(save.getPath() + ".debug"), o.toString());
			Game.currentGame.state = 3;
			JOptionPane.showMessageDialog(Game.w, "Spielstand erfolgreich gespeichert.", "Speichern erflogreich", JOptionPane.INFORMATION_MESSAGE);
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
			Game.world.setData(o.getInt("width"), o.getInt("height"), Compressor.decompressRow(new BASE64Decoder().decodeBuffer(o.getString("tile"))));
			Game.currentGame.resources = new Resources(o.getJSONObject("resources"));
			
			JSONArray researches = o.getJSONArray("researches");
			Game.currentGame.researches = new ArrayList<>();
			
			WaveManager.wave = o.getInt("wave");
			WaveManager.nextWave = o.getInt("time");
			for (int i = 0; i < researches.length(); i++)
				Game.currentGame.researches.add(Researches.valueOf(researches.getString(i)));
			
			JSONArray entities = o.getJSONArray("entities");
			for (int i = 0; i < entities.length(); i++)
			{
				JSONObject e = entities.getJSONObject(i);
				Entity entity = (Entity) Class.forName(e.getString("class")).getConstructor(int.class, int.class).newInstance(e.getInt("x"), e.getInt("y"));
				
				Game.world.addEntity2(entity);
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
}
