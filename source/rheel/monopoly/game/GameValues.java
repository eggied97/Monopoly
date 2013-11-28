package rheel.monopoly.game;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rheel.monopoly.street.StreetColor;

public class GameValues
{
	public final Map<StreetColor, String> streets;
	public final Map<StreetColor, List<String>> names;

	public final String name;
	public final String currency;
	public final int multiplier;
	public final boolean double_go;
	public final boolean free_parking_pot;
	public final boolean buy_before_roll;
	public final int[] moneyStart;

	public final File file;

	private GameValues(Map<StreetColor, String> streets, Map<StreetColor, List<String>> names, String name, String currency, int multiplier, boolean double_go, boolean pot, boolean roll, int[] moneyStart, File file)
	{
		this.streets = streets;
		this.names = names;
		this.name = name;
		this.currency = currency;
		this.multiplier = multiplier;
		this.double_go = double_go;
		this.free_parking_pot = pot;
		this.buy_before_roll = roll;
		this.moneyStart = moneyStart;

		this.file = file;
	}

	public static class MutableGameValues
	{
		public Map<StreetColor, String> streets = new HashMap<StreetColor, String>();
		public Map<StreetColor, List<String>> names = new HashMap<StreetColor, List<String>>();

		public String name = "Unnamed";
		public String currency = "$";
		public int multiplier = 1;
		public boolean double_go = true;
		public boolean free_parking_pot = true;
		public boolean buy_before_roll = true;
		public int[] moneyStart = new int[]{5, 5, 5, 6, 2, 2, 2};

		private final File file;

		public MutableGameValues(File file)
		{
			this.file = file;
		}

		public GameValues toImmutable()
		{
			return new GameValues(this.streets, this.names, this.name, this.currency, this.multiplier, this.double_go, this.free_parking_pot, this.buy_before_roll, this.moneyStart, this.file);
		}
	}
}
