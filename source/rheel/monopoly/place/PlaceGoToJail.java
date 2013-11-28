package rheel.monopoly.place;

import rheel.monopoly.game.Gameboard;
import rheel.monopoly.game.Player;
import rheel.monopoly.gui.GuiGame;

public class PlaceGoToJail extends PlaceCorner
{
	public PlaceGoToJail(Gameboard board, int id, Position pos)
	{
		super(board, id, pos);
	}

	@Override
	public String getTexure()
	{
		return "/textures/board/go_to_jail.png";
	}

	@Override
	public void onPlayerLand(Player player, GuiGame gui, boolean doubles)
	{
		player.setInJail();
		super.onPlayerLand(player, gui, doubles);
	}
}
