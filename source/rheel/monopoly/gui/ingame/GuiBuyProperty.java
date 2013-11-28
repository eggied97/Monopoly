package rheel.monopoly.gui.ingame;

import org.lwjgl.opengl.GL11;

import rheel.monopoly.game.Monopoly;
import rheel.monopoly.game.Player;
import rheel.monopoly.gui.GuiButton;
import rheel.monopoly.gui.GuiGame;
import rheel.monopoly.gui.GuiPropertyCard;
import rheel.monopoly.place.PlaceOwnable;
import rheel.monopoly.renderer.StringRenderer;

public class GuiBuyProperty extends GuiIngame implements IInventoryVisibleGui
{
	private final GuiButton BUY_PROPERTY = new GuiButton(0, this, this.xStart + this.xSize / 2 - 250, 268 + this.yStart, 500, 30, 18, "");
	private final GuiButton DONT_BUY_PROPERTY = new GuiButton(1, this, this.xStart + this.xSize / 2 - 250, 303 + this.yStart, 500, 30, 18, "Don't Buy");
	private final GuiPropertyCard CARD;

	private final PlaceOwnable place;

	private final boolean doubles;

	public GuiBuyProperty(PlaceOwnable place, Player player, GuiGame parent, boolean doubles)
	{
		super(parent, player);
		this.place = place;
		this.BUY_PROPERTY.text = "Buy " + place.getName() + " for " + StringRenderer.getMoneyString(place.getPrice());
		this.CARD = new GuiPropertyCard(place);
		this.doubles = doubles;
	}

	@Override
	protected void draw()
	{
		super.draw();
		GL11.glPushMatrix();
		GL11.glTranslatef(-this.xStart, -this.yStart, 0);

		this.BUY_PROPERTY.drawGui();
		this.DONT_BUY_PROPERTY.drawGui();

		GL11.glPopMatrix();

		GL11.glTranslatef((this.xSize - 170) / 2, 0, 0);
		this.CARD.drawGui();
	}

	@Override
	protected void onButtonReleased(GuiButton button)
	{
		super.onButtonReleased(button);
		switch (button.id)
		{
			case 0:
				if (!this.player.inventory.subtractMoney(this.parent, this.place.getPrice()).isEmpty())
				{
					this.place.setOwned(this.player);
				}
				break;
			case 1:
				break;
		}

		if (this.doubles)
		{
			this.parent.setIngameGui(new GuiTurn(this.parent, this.player, true));
		}
		else
		{
			Monopoly.getInstance().setNextPlayer();
		}
	}
}
