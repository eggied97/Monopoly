package rheel.monopoly.gui.ingame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import rheel.monopoly.game.Monopoly;
import rheel.monopoly.game.Player;
import rheel.monopoly.gui.GuiButton;
import rheel.monopoly.gui.GuiGame;
import rheel.monopoly.gui.GuiPropertyCard;
import rheel.monopoly.place.PlaceProperty;
import rheel.monopoly.renderer.StringRenderer;
import rheel.monopoly.street.Street;
import rheel.monopoly.street.StreetColor;

public class GuiBuyHouses extends GuiIngame implements IInventoryVisibleGui
{
	private int streetSelected = -1;
	private int cost = 0;
	private int[] prevHouses;

	public GuiBuyHouses(GuiGame parent, Player player)
	{
		super(parent, player);
	}

	@Override
	protected void initGui()
	{
		for (int i = 0; i < this.player.inventory.getStreets().size(); i++)
		{
			this.addButton(new GuiButton(i, this, this.xSize / 2 - 83, 58 + i * 52, 166, 52, "").setDrawButton(false));
		}

		this.addButton(new GuiButton(100, this, this.xSize / 2 - 200, this.ySize - 40, 400, 20, "CANCEL"));
		this.addButton(new GuiButton(101, this, this.xSize / 2 - 200, this.ySize - 40, 195, 20, "OK").setDisabled(true).setDrawButton(false));
		this.addButton(new GuiButton(102, this, this.xSize / 2 + 5, this.ySize - 40, 195, 20, "CANCEL").setDisabled(true).setDrawButton(false));

		this.addButton(new GuiButton(111, this, this.xSize / 2 - 170, 325, 81, 20, "-1").setDisabled(true).setDrawButton(false));
		this.addButton(new GuiButton(112, this, this.xSize / 2 - 85, 325, 81, 20, "+1").setDisabled(true).setDrawButton(false));
		this.addButton(new GuiButton(113, this, this.xSize / 2 + 4, 325, 81, 20, "-1").setDisabled(true).setDrawButton(false));
		this.addButton(new GuiButton(114, this, this.xSize / 2 + 89, 325, 81, 20, "+1").setDisabled(true).setDrawButton(false));

		this.addButton(new GuiButton(121, this, this.xSize / 2 - 257, 325, 81, 20, "-1").setDisabled(true).setDrawButton(false));
		this.addButton(new GuiButton(122, this, this.xSize / 2 - 172, 325, 81, 20, "+1").setDisabled(true).setDrawButton(false));
		this.addButton(new GuiButton(123, this, this.xSize / 2 - 83, 325, 81, 20, "-1").setDisabled(true).setDrawButton(false));
		this.addButton(new GuiButton(124, this, this.xSize / 2 + 2, 325, 81, 20, "+1").setDisabled(true).setDrawButton(false));
		this.addButton(new GuiButton(125, this, this.xSize / 2 + 91, 325, 81, 20, "-1").setDisabled(true).setDrawButton(false));
		this.addButton(new GuiButton(126, this, this.xSize / 2 + 176, 325, 81, 20, "+1").setDisabled(true).setDrawButton(false));
	}

	@Override
	protected void draw()
	{
		if (this.streetSelected == -1)
		{
			StringRenderer.drawCenteredString("For which street would you like to buy houses?", this.xSize / 2, 25, 14, true, Color.BLACK);

			for (int i = 0; i < this.player.inventory.getStreets().size(); i++)
			{
				final Street street = this.player.inventory.getStreets().get(i);
				final StreetColor color = street.color;
				Monopoly.getInstance().getEngine().bindTexture("/textures/board/place.png");
				Monopoly.getInstance().getEngine().drawTexturedRectangle(this.xSize / 2 - 81, 60 + i * 52, 85, color.ordinal() * 24, 162, 48, 81, 24);
				Monopoly.getInstance().getEngine().bindTexture("/textures/houses_lines.png");
				Monopoly.getInstance().getEngine().drawTexturedRectangle(this.xSize / 2 - 83, 58 + i * 52, 0, this.getMouseX() >= this.xSize / 2 - 83 && this.getMouseX() <= this.xSize / 2 + 83 && this.getMouseY() >= 58 + i * 52 && this.getMouseY() <= 57 + (i + 1) * 52 ? 52 : 0, 166, 52);
			}
		}
		else
		{
			StringRenderer.drawCenteredString("Buy Houses", this.xSize / 2, 25, 14, true, Color.BLACK);
			StringRenderer.drawString((this.cost < 0 ? "Profit: " : "Cost: ") + StringRenderer.getMoneyString(Math.abs(this.cost)), this.xSize - (StringRenderer.getStringWidth((this.cost < 0 ? "Profit: " : "Cost: ") + StringRenderer.getMoneyString(Math.abs(this.cost)), 11, true) + 10), this.ySize - 50, 11, true, this.cost <= 0 ? new Color(19, 167, 88) : new Color(218, 36, 40));

			final List<String> bottomText = new ArrayList<String>();

			if (this.cost / (float) this.player.inventory.getMoneyAmount() * 100 >= 50)
			{
				bottomText.add("This purchage will take " + (int) (this.cost / (float) this.player.inventory.getMoneyAmount() * 100) + "% of your cash.");
			}

			if (!this.canBuyHouses())
			{
				bottomText.add("The maximum house difference per street can be 1");
			}

			final int start = this.ySize - (70 + bottomText.size() * 14);

			for (int i = 0; i < bottomText.size(); i++)
			{
				StringRenderer.drawCenteredString(bottomText.get(i), this.xSize / 2, start + i * 14, 13, true, Color.RED);
			}

			final PlaceProperty[] array = this.player.inventory.getStreets().get(this.streetSelected).properties;
			final int nr = this.player.inventory.getStreets().get(this.streetSelected).properties.length;

			if (nr == 2)
			{
				GL11.glTranslatef(this.xSize / 2, 65, 0);
				GuiPropertyCard gui = new GuiPropertyCard(array[0]);
				GL11.glTranslatef(-172, 0, 0);
				gui.drawGui();
				gui = new GuiPropertyCard(array[1]);
				GL11.glTranslatef(174, 0, 0);
				gui.drawGui();
				GL11.glTranslatef(-2, 0, 0);
				GL11.glTranslatef(-this.xSize / 2, -65, 0);
			}
			else if (nr == 3)
			{
				GL11.glTranslatef(this.xSize / 2, 65, 0);
				GuiPropertyCard gui = new GuiPropertyCard(array[0]);
				GL11.glTranslatef(-259, 0, 0);
				gui.drawGui();
				gui = new GuiPropertyCard(array[1]);
				GL11.glTranslatef(174, 0, 0);
				gui.drawGui();
				gui = new GuiPropertyCard(array[2]);
				GL11.glTranslatef(174, 0, 0);
				gui.drawGui();
				GL11.glTranslatef(-89, 0, 0);
				GL11.glTranslatef(-this.xSize / 2, -65, 0);
			}
		}
	}

	private boolean canBuyHouses()
	{
		final PlaceProperty[] array = this.player.inventory.getStreets().get(this.streetSelected).properties;

		int lowest = 5;
		int highest = 0;

		for (final PlaceProperty place : array)
		{
			final int houses = place.getHouses();

			if (houses < lowest)
			{
				lowest = houses;
			}

			if (houses > highest)
			{
				highest = houses;
			}
		}

		return highest - lowest <= 1;
	}

	@Override
	protected void onButtonReleased(GuiButton button)
	{
		if (button.id == 100 || button.id == 102)
		{
			if (this.streetSelected != -1)
			{
				for (int i = 0; i < this.player.inventory.getStreets().get(this.streetSelected).properties.length; i++)
				{
					final PlaceProperty place = this.player.inventory.getStreets().get(this.streetSelected).properties[i];
					place.setHouses(this.prevHouses[i]);
				}

				for (final GuiButton b : this.getButtons())
				{
					if (b.id < 100)
					{
						b.disabled = false;
					}
					else if (b.id == 100 && button.id == 102)
					{
						b.disabled = false;
						b.drawButton = true;
					}
					else if (b.id == 101 || b.id == 102 && button.id == 102 || b.id > 110)
					{
						b.disabled = true;
						b.drawButton = false;
					}
				}

				this.streetSelected = -1;
				this.cost = 0;
			}
			else
			{
				this.parent.setPreviousGui();
			}
		}
		else if (button.id < 100)
		{
			this.streetSelected = button.id;
			final PlaceProperty[] array = this.player.inventory.getStreets().get(button.id).properties;

			if (array.length == 2)
			{
				this.prevHouses = new int[]{array[0].getHouses(), array[1].getHouses()};
			}
			else if (array.length == 3)
			{
				this.prevHouses = new int[]{array[0].getHouses(), array[1].getHouses(), array[2].getHouses()};
			}

			for (final GuiButton b : this.getButtons())
			{
				if (b.id < 100)
				{
					b.disabled = true;
				}
				else if (b.id == 100)
				{
					b.disabled = true;
					b.drawButton = false;
				}
				else if (b.id == 101 || b.id == 102)
				{
					b.disabled = false;
					b.drawButton = true;
				}
				else if (b.id > 110 && b.id < 130)
				{
					final int propIndex = b.id < 120 ? (b.id - 111) / 2 : (b.id - 121) / 2;

					if (array.length == 2)
					{
						if (b.id > 110 && b.id < 120)
						{
							final int houses = array[propIndex].getHouses();
							b.disabled = b.id % 2 == 1 && houses == 0 || b.id % 2 == 0 && houses == 5;
							b.drawButton = true;
						}
						else
						{
							b.disabled = true;
							b.drawButton = false;
						}
					}
					else if (array.length == 3)
					{
						if (b.id > 110 && b.id < 120)
						{
							b.disabled = true;
							b.drawButton = false;
						}
						else
						{
							final int houses = array[propIndex].getHouses();
							b.disabled = b.id % 2 == 1 && houses == 0 || b.id % 2 == 0 && houses == 5;
							b.drawButton = true;
						}
					}
				}
			}
		}
		else if (button.id == 101)
		{
			if (this.cost > 0)
			{
				this.player.inventory.subtractMoney(this.parent, this.cost);
			}

			if (this.cost < 0)
			{
				this.player.inventory.addMoney(Math.abs(this.cost));
			}

			for (final GuiButton b : this.getButtons())
			{
				if (b.id < 100)
				{
					b.disabled = false;
				}
				else if (b.id == 100)
				{
					b.disabled = false;
					b.drawButton = true;
				}
				else if (b.id == 101 || b.id == 102 || b.id > 110)
				{
					b.disabled = true;
					b.drawButton = false;
				}
			}

			this.streetSelected = -1;
			this.cost = 0;
		}
		else if (button.id > 110 && button.id < 130)
		{
			final int propIndex = button.id < 120 ? (button.id - 111) / 2 : (button.id - 121) / 2;
			final int add = button.id % 2 == 1 ? -1 : 1;
			final PlaceProperty place = this.player.inventory.getStreets().get(this.streetSelected).properties[propIndex];
			final int housePrice = (place.getColor().ordinal() / 2 + 1) * 50;

			if (add == 1)
			{
				place.addHouse();
				this.cost += housePrice;
			}
			else if (add == -1)
			{
				place.removeHouse();
				if (place.getHouses() < this.prevHouses[propIndex])
				{
					this.cost -= housePrice / 2;
				}
				else
				{
					this.cost -= housePrice;
				}
			}

			button.disabled = place.getHouses() == 5 || place.getHouses() == 0;

			for (final GuiButton b : this.getButtons())
			{
				if (add == 1 && b.id == button.id - 1 || add == -1 && b.id == button.id + 1)
				{
					b.disabled = false;
				}
				else if (b.id == 101)
				{
					b.disabled = !this.canBuyHouses();
				}
			}
		}
	}
}
