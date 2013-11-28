package rheel.monopoly.gui.ingame;

import rheel.monopoly.game.Monopoly;
import rheel.monopoly.game.Player;
import rheel.monopoly.gui.GuiButton;
import rheel.monopoly.gui.GuiGame;

public class GuiChosePlayer extends GuiIngame
{
	private final GuiIngame prev;
	
	public GuiChosePlayer(GuiGame parent, Player player, GuiIngame prev)
	{
		super(parent, player);
		this.prev = prev;
	}

	@Override
	protected void initGui()
	{
		for (int i = 0; i < Monopoly.getInstance().getPlayers().length; i++)
		{
			this.addButton(new GuiButton(i, this, this.xSize / 2 - 200, 60 + 30 * i, 400, 25, Monopoly.getInstance().getPlayers()[i].name));
		}
	}

	@Override
	protected void onButtonReleased(GuiButton button)
	{
		this.parent.setIngameGui(new GuiInventory(this.parent, Monopoly.getInstance().getPlayers()[button.id], prev));
	}
}
