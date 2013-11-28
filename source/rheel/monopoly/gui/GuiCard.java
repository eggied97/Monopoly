package rheel.monopoly.gui;

import org.lwjgl.opengl.GL11;

import rheel.monopoly.game.Card;
import rheel.monopoly.game.Card.CardType;
import rheel.monopoly.game.Monopoly;

public abstract class GuiCard extends Gui
{
	private final Card card;
	private final int x;
	private final int y;
	private final GuiButton button;

	public GuiCard(Card card, int x, int y, boolean showButton)
	{
		this.card = card;
		this.x = x;
		this.y = y;
		this.button = new GuiButton(0, this, x, y + 185, 319, 25, 16, "OK");
		this.button.drawButton = showButton;
	}

	@Override
	public void drawGui()
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(this.x, this.y, 0);
		Monopoly.getInstance().getEngine().bindTexture("/textures/cards.png");
		Monopoly.getInstance().getEngine().drawTexturedRectangle(0, 0, 0, this.card.type == CardType.COMMUNITY_CHEST ? 0 : this.card.type == CardType.CHANCE ? 180 : 255, 319, 180);
		this.card.render();
		GL11.glPopMatrix();

		this.button.drawGui();
	}

	@Override
	protected void onButtonReleased(GuiButton button)
	{
		this.onOK();
	}

	protected abstract void onOK();
}
