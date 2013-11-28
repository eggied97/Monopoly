package rheel.monopoly.gui.ingame;

import org.lwjgl.opengl.GL11;

import rheel.monopoly.game.Card;
import rheel.monopoly.game.Card.CardType;
import rheel.monopoly.game.Player;
import rheel.monopoly.gui.GuiCard;
import rheel.monopoly.gui.GuiGame;

public class GuiShowCard extends GuiIngame implements IInventoryVisibleGui
{
	private final GuiCard gui;

	public GuiShowCard(final GuiGame parent, final Player player, CardType type, final boolean doubles)
	{
		super(parent, player);
		final Card card = Card.getCard(type);

		this.gui = new GuiCard(card, this.xStart + (this.xSize - 319) / 2, this.yStart + (this.ySize - 205) / 2, true)
		{
			@Override
			protected void onOK()
			{
				card.onCardUse(player, parent, doubles);
			}
		};
	}

	@Override
	protected void draw()
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(-this.xStart, -this.yStart, 0);
		this.gui.drawGui();
		GL11.glPopMatrix();
	}
}
