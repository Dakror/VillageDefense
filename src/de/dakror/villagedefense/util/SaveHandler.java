package de.dakror.villagedefense.util;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

import sun.misc.BASE64Encoder;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.settings.CFG;
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
			Game.currentGame.state = 3;
			JOptionPane.showMessageDialog(Game.w, "Spielstand erfolgreich gespeichert.", "Speichern erflogreich", JOptionPane.INFORMATION_MESSAGE);
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
