package rheel.monopoly.place;

import rheel.monopoly.game.Gameboard;
import rheel.monopoly.game.Player;
import rheel.monopoly.gui.GuiGame;

public class PlaceTax extends PlaceSpecial
{
	private final int amount;
	public final int id;

	public PlaceTax(int amount, Gameboard board, int id, Position pos)
	{
		super(board, id == 0 ? 4 : 38, pos);
		this.amount = amount;
		this.id = id;
	}

	@Override
	public void onPlayerLand(Player player, GuiGame gui, boolean doubles)
	{
		player.inventory.subtractMoney(gui, this.amount);
		super.onPlayerLand(player, gui, doubles);
	}
}
