package rheel.monopoly.place;

import java.util.ArrayList;
import java.util.List;

import rheel.monopoly.game.Gameboard;
import rheel.monopoly.game.Player;

public class PlaceJail extends PlaceCorner
{
	public PlaceJail(Gameboard board, int id, Position pos)
	{
		super(board, id, pos);
	}

	@Override
	public String getTexure()
	{
		return "/textures/board/jail.png";
	}

	public List<Player> getPlayersInJail(List<Player> choices)
	{
		final List<Player> result = new ArrayList<Player>();

		for (final Player player : choices)
		{
			if (player.isInJail())
			{
				result.add(player);
			}
		}

		return result;
	}

	public List<Player> getPlayersVisiting(List<Player> choices)
	{
		final List<Player> result = new ArrayList<Player>();

		for (final Player player : choices)
		{
			if (!player.isInJail() && player.position == 10)
			{
				result.add(player);
			}
		}

		return result;
	}
}
