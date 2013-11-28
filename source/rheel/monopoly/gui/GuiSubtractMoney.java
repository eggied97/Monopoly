package rheel.monopoly.gui;

import org.lwjgl.opengl.GL11;

import rheel.monopoly.game.GameValues;
import rheel.monopoly.game.Money;
import rheel.monopoly.game.Monopoly;

public class GuiSubtractMoney extends Gui
{
	private final GuiMoney money;
	private int tick = 0;

	public GuiSubtractMoney(GameValues values, Money type)
	{
		this.money = new GuiMoney(values, type);
	}

	@Override
	public void drawGui()
	{
		if (++this.tick <= 70)
		{
			GL11.glTranslatef(0.0F, this.tick / 5.0F, 0.0F);
			GL11.glScalef(0.5F, 0.5F, 0.5F);

			this.money.drawGui();

			Monopoly.getInstance().getEngine().bindTexture("/textures/money.png");
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.25F + this.tick / 200.0F);
			Monopoly.getInstance().getEngine().drawTexturedRectangle(0, 0, 6, 254, 256, 132, 1, 1);

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glScalef(2.0F, 2.0F, 2.0F);
			GL11.glTranslatef(0.0F, -this.tick / 5.0F, 0.0F);
		}
	}

	public boolean isDead()
	{
		return this.tick >= 70;
	}
}
