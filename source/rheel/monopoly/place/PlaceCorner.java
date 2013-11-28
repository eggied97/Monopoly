package rheel.monopoly.place;

import rheel.monopoly.game.Gameboard;

public abstract class PlaceCorner extends Place
{
	public PlaceCorner(Gameboard board, int id, Position pos)
	{
		super(board, id, pos);
	}

	public abstract String getTexure();
}
