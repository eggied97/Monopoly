package rheel.monopoly.place;

import rheel.monopoly.game.Gameboard;
import rheel.monopoly.street.StreetColor;

public class PlaceStation extends PlaceOwnable
{
	public PlaceStation(Gameboard board, int id, String name, Position pos)
	{
		super(board, 5 + id * 10, name, StreetColor.stations, pos, 200);
	}
}
