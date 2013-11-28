package rheel.monopoly.place;

import rheel.monopoly.game.Gameboard;
import rheel.monopoly.game.Player;
import rheel.monopoly.game.Pot;
import rheel.monopoly.gui.GuiGame;

public class PlaceFreeParking extends PlaceCorner
{
	public PlaceFreeParking(Gameboard board, int id, Position pos)
	{
		super(board, id, pos);
	}

	@Override
	public String getTexure()
	{
		return "/textures/board/free_parking.png";
	}

	@Override
	public void onPlayerLand(Player player, GuiGame gui, boolean doubles)
	{
		Pot.addPotToPlayer(player);
		super.onPlayerLand(player, gui, doubles);
	}
}
