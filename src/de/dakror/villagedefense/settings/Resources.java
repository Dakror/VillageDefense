package de.dakror.villagedefense.settings;


/**
 * @author Dakror
 */
public class Resources
{
	
	public enum Types
	{
		GOLD("Gold", 13, 12),
		WOOD("Holz", 0, 12),
		STONE("Stein", 2, 9),
		PEOPLE("Einwohner", 9, 8),
		
		;
		
		private String name;
		private int iconX, iconY;
		
		private Types(String name, int iconX, int iconY)
		{
			this.name = name;
			this.iconX = iconX;
			this.iconY = iconY;
		}
		
		public String getName()
		{
			return name;
		}
		
		public int getIconX()
		{
			return iconX;
		}
		
		public int getIconY()
		{
			return iconY;
		}
	}
	
	public Resources()
	{}
	
}
