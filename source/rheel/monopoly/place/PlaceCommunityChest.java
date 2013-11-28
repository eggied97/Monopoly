package rheel.monopoly.place;

import rheel.monopoly.game.Card.CardType;
import rheel.monopoly.game.Gameboard;
import rheel.monopoly.game.Player;
import rheel.monopoly.gui.GuiGame;
import rheel.monopoly.gui.ingame.GuiShowCard;

public class PlaceCommunityChest extends PlaceCard
{
	public PlaceCommunityChest(Gameboard board, int id, Position pos)
	{
		super(board, id, pos);
	}

	@Override
	public void onPlayerLand(final Player player, final GuiGame gui, final boolean doubles)
	{
		gui.setIngameGui(new GuiShowCard(gui, player, CardType.COMMUNITY_CHEST, doubles));
	}
}
