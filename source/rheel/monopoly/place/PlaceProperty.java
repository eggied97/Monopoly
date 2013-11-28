package rheel.monopoly.place;

import rheel.monopoly.game.Gameboard;
import rheel.monopoly.street.StreetColor;

public class PlaceProperty extends PlaceOwnable
{
	private final String subName;
	private final RentAmount rent;

	private int houses = 0;

	public PlaceProperty(String name, int id, String subName, StreetColor color, int posInColor, Position pos, Gameboard gameboard, int price)
	{
		super(gameboard, id, name, color, pos, price);
		this.subName = subName;
		this.rent = RentAmount.getRentAmount(color, posInColor);
	}

	public String getSubName()
	{
		return this.subName;
	}

	public int getHouses()
	{
		return this.houses;
	}

	public void addHouse()
	{
		this.houses++;
	}

	public void removeHouse()
	{
		this.houses--;
	}

	public void setHouses(int i)
	{
		this.houses = i;
	}

	public RentAmount getRent()
	{
		return this.rent;
	}

	public static class RentAmount
	{
		private static final RentAmount PURPLE = new RentAmount(2, 10, 30, 90, 160, 250);
		private static final RentAmount PURPLE_LAST = new RentAmount(4, 20, 60, 180, 320, 450);
		private static final RentAmount LIGHT_BLUE = new RentAmount(6, 30, 90, 270, 400, 550);
		private static final RentAmount LIGHT_BLUE_LAST = new RentAmount(8, 40, 100, 300, 450, 600);
		private static final RentAmount PINK = new RentAmount(10, 50, 150, 450, 625, 570);
		private static final RentAmount PINK_LAST = new RentAmount(12, 60, 180, 500, 700, 900);
		private static final RentAmount ORANGE = new RentAmount(14, 70, 200, 550, 750, 950);
		private static final RentAmount ORANGE_LAST = new RentAmount(16, 80, 220, 600, 800, 1000);
		private static final RentAmount RED = new RentAmount(18, 90, 250, 700, 875, 1050);
		private static final RentAmount RED_LAST = new RentAmount(20, 100, 300, 750, 925, 1100);
		private static final RentAmount YELLOW = new RentAmount(22, 110, 330, 800, 975, 1150);
		private static final RentAmount YELLOW_LAST = new RentAmount(24, 120, 360, 850, 1025, 1200);
		private static final RentAmount GREEN = new RentAmount(26, 130, 390, 900, 1100, 1275);
		private static final RentAmount GREEN_LAST = new RentAmount(28, 150, 450, 1000, 1200, 1400);
		private static final RentAmount DARK_BLUE = new RentAmount(35, 175, 500, 1100, 1300, 1500);
		private static final RentAmount DARK_BLUE_LAST = new RentAmount(50, 200, 600, 1400, 1700, 2000);

		public final int rent;
		public final int rent_h1;
		public final int rent_h2;
		public final int rent_h3;
		public final int rent_h4;
		public final int rent_hotel;

		private RentAmount(int... rent)
		{
			this.rent = rent[0];
			this.rent_h1 = rent[1];
			this.rent_h2 = rent[2];
			this.rent_h3 = rent[3];
			this.rent_h4 = rent[4];
			this.rent_hotel = rent[5];
		}

		private static RentAmount getRentAmount(StreetColor color, int pos)
		{
			switch (color)
			{
				case purple:
					return pos == 1 ? RentAmount.PURPLE_LAST : RentAmount.PURPLE;
				case light_blue:
					return pos == 2 ? RentAmount.LIGHT_BLUE_LAST : RentAmount.LIGHT_BLUE;
				case pink:
					return pos == 2 ? RentAmount.PINK_LAST : RentAmount.PINK;
				case orange:
					return pos == 2 ? RentAmount.ORANGE_LAST : RentAmount.ORANGE;
				case red:
					return pos == 2 ? RentAmount.RED_LAST : RentAmount.RED;
				case yellow:
					return pos == 2 ? RentAmount.YELLOW_LAST : RentAmount.YELLOW;
				case green:
					return pos == 2 ? RentAmount.GREEN_LAST : RentAmount.GREEN;
				case dark_blue:
					return pos == 1 ? RentAmount.DARK_BLUE_LAST : RentAmount.DARK_BLUE;
				default:
					return null;
			}
		}
	}
}
