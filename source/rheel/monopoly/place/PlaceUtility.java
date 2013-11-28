package rheel.monopoly.place;

import rheel.monopoly.game.Gameboard;
import rheel.monopoly.street.StreetColor;

public class PlaceUtility extends PlaceOwnable
{
	public final int id;

	public PlaceUtility(Gameboard board, String name, int id, Position pos)
	{
		super(board, id == 0 ? 12 : 28, name, StreetColor.utilities, pos, 150);
		this.id = id;
	}
}
