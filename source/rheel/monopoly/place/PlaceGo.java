package rheel.monopoly.place;

import rheel.monopoly.game.Gameboard;
import rheel.monopoly.game.Money;
import rheel.monopoly.game.Player;
import rheel.monopoly.gui.GuiGame;

public class PlaceGo extends PlaceCorner
{
	public PlaceGo(Gameboard board, int id, Position pos)
	{
		super(board, id, pos);
	}

	@Override
	public void onPlayerLand(Player player, GuiGame gui, boolean doubles)
	{
		player.inventory.addMoney(Money.ONE_HUNDRED, this.board.gameValues.double_go ? 4 : 2);
		super.onPlayerLand(player, gui, doubles);
	}

	@Override
	public void onPlayerPass(Player player, GuiGame gui)
	{
		player.inventory.addMoney(Money.ONE_HUNDRED, 2);
	}

	@Override
	public String getTexure()
	{
		return "/textures/board/go.png";
	}
}
