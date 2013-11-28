package rheel.monopoly.gui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import rheel.monopoly.game.GameValues;
import rheel.monopoly.game.Money;
import rheel.monopoly.game.Monopoly;
import rheel.monopoly.renderer.StringRenderer;

public class GuiMoney extends Gui
{
	private final Money type;
	private final GameValues values;
	private final int size1;
	private final int size2;

	public GuiMoney(GameValues values, Money type)
	{
		this.values = values;
		this.type = type;
		this.size1 = this.calcSize(80, true);
		this.size2 = this.calcSize(20, false);
	}

	@Override
	public void drawGui()
	{
		GL11.glColor4f(this.type.color[0] / 255.0F, this.type.color[1] / 255.0F, this.type.color[2] / 255.0F, 1.0F);
		Monopoly.getInstance().getEngine().bindTexture("/textures/money.png");
		Monopoly.getInstance().getEngine().drawTexturedRectangle(0, 0, 0, 0, 256, 132);

		StringRenderer.drawCenteredString(String.valueOf(this.type.amount * this.values.multiplier), 127, 63 - this.size1 / 2, this.size1, true, new Color(89, 89, 89));
		StringRenderer.drawCenteredString(String.valueOf(this.type.amount * this.values.multiplier), 43, 45 - this.size2 / 2, this.size2, false, new Color(89, 89, 89));
		StringRenderer.drawCenteredString(String.valueOf(this.type.amount * this.values.multiplier), 213, 88 - this.size2 / 2, this.size2, false, new Color(89, 89, 89));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private int calcSize(int maxWidth, boolean bold)
	{
		int size = 1;

		while (StringRenderer.getStringWidth(String.valueOf(Money.FIVE_HUNDRED.amount * this.values.multiplier), size, bold) < maxWidth && size < 34)
		{
			size++;
		}

		return size;
	}
}
