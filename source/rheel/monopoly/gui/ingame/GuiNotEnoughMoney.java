package rheel.monopoly.gui.ingame;

import java.awt.Color;

import rheel.monopoly.game.Player;
import rheel.monopoly.gui.GuiButton;
import rheel.monopoly.gui.GuiGame;
import rheel.monopoly.renderer.StringRenderer;

public class GuiNotEnoughMoney extends GuiIngame implements IInventoryVisibleGui
{
	private final int amount;
	
	public GuiNotEnoughMoney(GuiGame parent, Player player, int amount)
	{
		super(parent, player);
		System.out.println(getClass());
		this.amount = amount;
	}

	@Override
	protected void initGui()
	{
		this.addButton(new GuiButton(0, this, this.xSize / 2 - 150, 90, 300, 25, "Sell houses").setDisabled(true));
		this.addButton(new GuiButton(1, this, this.xSize / 2 - 150, 120, 300, 25, "Mortage properties").setDisabled(!this.player.inventory.getProperties().isEmpty()));
		this.addButton(new GuiButton(2, this, this.xSize / 2 - 150, 150, 300, 25, "Trade").setDisabled(true));
		this.addButton(new GuiButton(3, this, this.xSize / 2 - 150, 180, 300, 25, "I am bankrupt"));
		this.addButton(new GuiButton(4, this, this.xSize / 2 - 150, 220, 300, 25, "Cancel"));
	}

	@Override
	protected void draw()
	{
		StringRenderer.drawCenteredString("You do not have enough money for that! Needed: " + StringRenderer.getMoneyString(amount), this.xSize / 2, 25, 20, true, Color.BLACK);
		StringRenderer.drawCenteredString("What would you like to do? ", this.xSize / 2, 50, 17, true, Color.BLACK);
	}

	@Override
	protected void onButtonReleased(GuiButton button)
	{
		switch (button.id)
		{
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				this.parent.setPreviousGui();
				break;
		}
	}
}
