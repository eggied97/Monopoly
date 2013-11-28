package rheel.monopoly.place;

import rheel.monopoly.game.Gameboard;

public abstract class PlaceSpecial extends Place
{
	public PlaceSpecial(Gameboard board, int id, Position pos)
	{
		super(board, id, pos);
	}
}
