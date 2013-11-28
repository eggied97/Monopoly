package rheel.monopoly.gui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import rheel.monopoly.game.Monopoly;
import rheel.monopoly.place.PlaceOwnable;
import rheel.monopoly.place.PlaceProperty;
import rheel.monopoly.place.PlaceProperty.RentAmount;
import rheel.monopoly.place.PlaceStation;
import rheel.monopoly.place.PlaceUtility;
import rheel.monopoly.renderer.StringRenderer;

public class GuiPropertyCard extends Gui
{
	private final PlaceOwnable place;

	public GuiPropertyCard(PlaceOwnable place)
	{
		this.place = place;
	}

	@Override
	public void drawGui()
	{
		GL11.glPushMatrix();

		Monopoly.getInstance().getEngine().bindTexture("/textures/card_0.png");
		Monopoly.getInstance().getEngine().drawTexturedRectangle(0, 0, 0, 0, 170, 256);
		Monopoly.getInstance().getEngine().bindTexture("/textures/card_1.png");

		if (this.place instanceof PlaceProperty)
		{
			Monopoly.getInstance().getEngine().drawTexturedRectangle(12, 12, 0, 0, 145, 48);

			final float[] color = ((PlaceProperty) this.place).getColor().getColor();

			GL11.glColor4f(color[0], color[1], color[2], 1.0F);
			Monopoly.getInstance().getEngine().drawTexturedRectangle(14, 14, 0, 48, 141, 44);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			StringRenderer.drawCenteredString(((PlaceProperty) this.place).getSubName(), 85, 16, 12, false, Color.BLACK);
			StringRenderer.drawCenteredSplitString(this.place.getName(), 12, 158, 30, 17, 0, true, Color.BLACK);

			StringRenderer.drawString("Rent:", 12, 70, 13, false, Color.BLACK);
			StringRenderer.drawString("With 1 house: ", 12, 86, 13, false, Color.BLACK);
			StringRenderer.drawString("With 2 houses: ", 12, 102, 13, false, Color.BLACK);
			StringRenderer.drawString("With 3 houses: ", 12, 118, 13, false, Color.BLACK);
			StringRenderer.drawString("With 4 houses: ", 12, 134, 13, false, Color.BLACK);
			StringRenderer.drawString("With hotel: ", 12, 150, 13, false, Color.BLACK);

			final RentAmount rent = ((PlaceProperty) this.place).getRent();

			StringRenderer.drawString(StringRenderer.getMoneyString(rent.rent), 158 - StringRenderer.getStringWidth(StringRenderer.getMoneyString(rent.rent), 13, false), 70, 13, false, Color.BLACK);
			StringRenderer.drawString(StringRenderer.getMoneyString(rent.rent_h1), 158 - StringRenderer.getStringWidth(StringRenderer.getMoneyString(rent.rent_h1), 13, false), 86, 13, false, Color.BLACK);
			StringRenderer.drawString(StringRenderer.getMoneyString(rent.rent_h2), 158 - StringRenderer.getStringWidth(StringRenderer.getMoneyString(rent.rent_h2), 13, false), 102, 13, false, Color.BLACK);
			StringRenderer.drawString(StringRenderer.getMoneyString(rent.rent_h3), 158 - StringRenderer.getStringWidth(StringRenderer.getMoneyString(rent.rent_h3), 13, false), 118, 13, false, Color.BLACK);
			StringRenderer.drawString(StringRenderer.getMoneyString(rent.rent_h4), 158 - StringRenderer.getStringWidth(StringRenderer.getMoneyString(rent.rent_h4), 13, false), 134, 13, false, Color.BLACK);
			StringRenderer.drawString(StringRenderer.getMoneyString(rent.rent_hotel), 158 - StringRenderer.getStringWidth(StringRenderer.getMoneyString(rent.rent_hotel), 13, false), 150, 13, false, Color.BLACK);

			final int i = 2 + 14 * StringRenderer.drawCenteredSplitString("Mortage value: " + StringRenderer.getMoneyString(this.place.getPrice() / 2), 12, 158, 168, 13, 2, false, Color.BLACK);
			final String housesString = StringRenderer.getMoneyString((((PlaceProperty) this.place).getColor().ordinal() / 2 + 1) * 50);
			StringRenderer.drawCenteredSplitString("Houses cost " + housesString + " each. An hotel costs " + housesString + " plus 4 houses.", 12, 158, 170 + i, 13, 2, false, Color.BLACK);

			StringRenderer.drawCenteredSplitString("Rent without houses doubles whenever a player owns the whole colorgroup", 6, 164, 256 - 24, 8, 0, false, Color.BLACK);
		}
		else if (this.place instanceof PlaceStation)
		{
			Monopoly.getInstance().getEngine().drawTexturedRectangle(26, 16, 0, 92, 118, 89);

			StringRenderer.drawCenteredString(this.place.getName(), 85, 115 - 7, 15, true, Color.BLACK);

			StringRenderer.drawString("Rent:", 12, 134 - 7, 13, false, Color.BLACK);
			StringRenderer.drawSplitString("Player ownes 2 Railroads:", 12, 149 - 7, 13, 1, false, Color.BLACK, 158 - (StringRenderer.getStringWidth(StringRenderer.getMoneyString(200), 13, false) + 4));
			StringRenderer.drawSplitString("Player ownes 3 Railroads:", 12, 178 - 7, 13, 1, false, Color.BLACK, 158 - (StringRenderer.getStringWidth(StringRenderer.getMoneyString(200), 13, false) + 4));
			StringRenderer.drawSplitString("Player ownes 4 Railroads:", 12, 207 - 7, 13, 1, false, Color.BLACK, 158 - (StringRenderer.getStringWidth(StringRenderer.getMoneyString(200), 13, false) + 4));

			StringRenderer.drawString(StringRenderer.getMoneyString(25), 158 - StringRenderer.getStringWidth(StringRenderer.getMoneyString(25), 13, false), 134 - 7, 13, false, Color.BLACK);
			StringRenderer.drawString(StringRenderer.getMoneyString(50), 158 - StringRenderer.getStringWidth(StringRenderer.getMoneyString(50), 13, false), 149 + 7, 13, false, Color.BLACK);
			StringRenderer.drawString(StringRenderer.getMoneyString(100), 158 - StringRenderer.getStringWidth(StringRenderer.getMoneyString(100), 13, false), 178 + 7, 13, false, Color.BLACK);
			StringRenderer.drawString(StringRenderer.getMoneyString(200), 158 - StringRenderer.getStringWidth(StringRenderer.getMoneyString(200), 13, false), 207 + 7, 13, false, Color.BLACK);

			StringRenderer.drawCenteredString("Mortage value: " + StringRenderer.getMoneyString(this.place.getPrice() / 2), 85, 234, 13, false, Color.BLACK);
		}
		else if (this.place instanceof PlaceUtility)
		{
			if (((PlaceUtility) this.place).id == 0)
			{
				Monopoly.getInstance().getEngine().drawTexturedRectangle(50, 6, 145, 0, 72, 88);
			}
			else if (((PlaceUtility) this.place).id == 1)
			{
				Monopoly.getInstance().getEngine().drawTexturedRectangle(48, 13, 141, 92, 66, 55);
			}

			StringRenderer.drawCenteredString(this.place.getName(), 85, 88, 15, true, Color.BLACK);
			StringRenderer.drawSplitString("If one utility is owned, the rent is 4 times the total dice count.", 12, 122, 13, 2, false, Color.BLACK, 146);
			StringRenderer.drawSplitString("If two utilities are owned, the rent is 10 times the total dice count.", 12, 167, 13, 2, false, Color.BLACK, 146);
			
			StringRenderer.drawCenteredString("Mortage value: " + StringRenderer.getMoneyString(this.place.getPrice() / 2), 85, 234, 13, false, Color.BLACK);
		}

		GL11.glPopMatrix();
	}
}
