package rheel.monopoly.place;

import rheel.monopoly.game.Gameboard;

public abstract class PlaceCard extends PlaceSpecial
{
	public PlaceCard(Gameboard board, int id, Position pos)
	{
		super(board, id, pos);
	}
}
