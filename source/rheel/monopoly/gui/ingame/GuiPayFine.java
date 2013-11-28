package rheel.monopoly.gui.ingame;

import java.awt.Color;

import rheel.monopoly.game.Monopoly;
import rheel.monopoly.game.Player;
import rheel.monopoly.gui.GuiButton;
import rheel.monopoly.gui.GuiGame;
import rheel.monopoly.renderer.StringRenderer;

public class GuiPayFine extends GuiIngame implements IInventoryVisibleGui
{
	public GuiPayFine(GuiGame parent, Player player)
	{
		super(parent, player);
	}

	@Override
	protected void initGui()
	{
		this.addButton(new GuiButton(0, this, this.xSize / 2 - 150, 80, 300, 30, 16, "Pay " + StringRenderer.getMoneyString(50)));
	}

	@Override
	public void draw()
	{
		StringRenderer.drawCenteredString("Pay " + StringRenderer.getMoneyString(50) + " to get out of jail next time. ", this.xSize / 2, 30, 20, true, Color.BLACK);
	}

	@Override
	protected void onButtonReleased(GuiButton button)
	{
		if (!this.player.inventory.subtractMoney(this.parent, 50, true).isEmpty())
		{
			this.player.leaveJail();
		}
		Monopoly.getInstance().setNextPlayer();
	}
}
