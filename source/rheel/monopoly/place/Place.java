package rheel.monopoly.place;

import java.util.ArrayList;
import java.util.List;

import rheel.monopoly.game.Gameboard;
import rheel.monopoly.game.Monopoly;
import rheel.monopoly.game.Player;
import rheel.monopoly.gui.GuiGame;
import rheel.monopoly.gui.ingame.GuiTurn;

public abstract class Place
{
	public final Position position;
	protected final Gameboard board;
	protected final int placeID;

	public Place(Gameboard board, int id, Position pos)
	{
		this.board = board;
		this.position = pos;
		this.placeID = id;
	}

	public void onPlayerLand(Player player, GuiGame gui, boolean doubles)
	{
		if (doubles)
		{
			gui.setIngameGui(new GuiTurn(gui, player, true));
		}
		else
		{
			Monopoly.getInstance().setNextPlayer();
		}
	}

	public void onPlayerPass(Player player, GuiGame gui)
	{

	}

	public int getIMGPos()
	{
		return this.placeID;
	}

	public List<Player> getPlayersOnPlace()
	{
		final List<Player> players = new ArrayList<Player>();

		for (final Player player : Monopoly.getInstance().getPlayers())
		{
			if (player.position == this.placeID)
			{
				players.add(player);
			}
		}

		return players;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof Place && this.placeID == ((Place) obj).placeID;
	}

	@Override
	public int hashCode()
	{
		return this.placeID;
	}

	public static class Position
	{
		public final int x;
		public final int y;
		public final int rot;

		public Position(int x, int y, int rotation)
		{
			this.x = x;
			this.y = y;
			this.rot = rotation;
		}
	}
}
