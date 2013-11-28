package rheel.monopoly.gui.ingame;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import rheel.monopoly.game.Card;
import rheel.monopoly.game.Money;
import rheel.monopoly.game.Monopoly;
import rheel.monopoly.game.Player;
import rheel.monopoly.gui.GuiButton;
import rheel.monopoly.gui.GuiCard;
import rheel.monopoly.gui.GuiGame;
import rheel.monopoly.gui.GuiMoney;
import rheel.monopoly.gui.GuiPropertyCard;
import rheel.monopoly.place.PlaceOwnable;
import rheel.monopoly.renderer.DisplayBase;
import rheel.monopoly.renderer.StringRenderer;
import rheel.monopoly.street.StreetColor;

public class GuiInventory extends GuiIngame
{
	private InventoryTab tab = InventoryTab.MONEY;
	private int subTab = 0;
	private int numSubTabs = 1;
	private GuiIngame previousGui;

	public GuiInventory(GuiGame parent, Player player)
	{
		super(parent, player);
	}

	public GuiInventory(GuiGame parent, Player player, GuiIngame prevGui)
	{
		this(parent, player);
		this.previousGui = prevGui;
	}

	@Override
	protected void initGui()
	{
		this.addButton(new GuiButton(0, this, (this.xSize - 400) / 2, this.ySize - 30, 195, 25, "Back"));
		this.addButton(new GuiButton(1, this, this.xSize / 2 + 5, this.ySize - 30, 195, 25, "Other Player"));
		this.addButton(new GuiButton(2, this, 20, 397, 40, 20, "<--"));
		this.addButton(new GuiButton(3, this, 705, 397, 40, 20, "-->"));

		for (final GuiButton button : this.getButtons())
		{
			if (button.id == 2 || button.id == 3)
			{
				button.drawButton = false;
				button.disabled = true;
			}
		}
	}

	@Override
	protected void draw()
	{
		StringRenderer.drawString("Inventory (" + this.player.name + ")", 20, -20, 14, true, Color.BLACK);

		Monopoly.getInstance().getEngine().bindTexture("/textures/inventory.png");
		Monopoly.getInstance().getEngine().drawTexturedRectangle(0, 0, 0, 0, this.xSize, this.ySize);

		for (int i = 0; i < InventoryTab.values().length; i++)
		{
			final InventoryTab t = InventoryTab.values()[i];
			Monopoly.getInstance().getEngine().bindTexture("/textures/inventory.png");

			final int u;
			final int h;

			if (t == this.tab)
			{
				h = 22;
				u = 0;
			}
			else if (this.isTabSelected(i))
			{
				h = 19;
				u = 149;
			}
			else
			{
				h = 19;
				u = 0;
			}

			Monopoly.getInstance().getEngine().drawTexturedRectangle(40 + 149 * i, 1, u, 510, 149, h);
			StringRenderer.drawCenteredString(t.tabName, 115 + 149 * i, 2, 14, true, java.awt.Color.WHITE);
		}

		switch (this.tab)
		{
			case MONEY:
				final Map<Money, Integer> available = this.player.inventory.getOwnedMoney();

				GL11.glPushMatrix();
				GL11.glScalef(0.73F, 0.73F, 0.73F);
				GL11.glRotatef(90, 0, 0, 1);
				GL11.glTranslatef(50, -45, 0);

				for (final Money money : Money.values())
				{
					GL11.glTranslatef(0, -136, 0);

					if (money == Money.FIVE_HUNDRED)
					{
						GL11.glTranslatef(0, -1, 0);
					}

					final GuiMoney gui = new GuiMoney(Monopoly.getInstance().getBoard().gameValues, money);

					GL11.glPushMatrix();
					GL11.glTranslatef(-25, 0, 0);

					for (int i = 0; i < Math.min(8, available.get(money)); i++)
					{
						GL11.glTranslatef(25, 0, 0);
						gui.drawGui();
					}

					GL11.glPopMatrix();

					if (available.get(money) > 8)
					{
						GL11.glPushMatrix();
						GL11.glRotatef(90, 0, 0, -1);
						GL11.glTranslatef(-130, 0, 0);
						StringRenderer.drawString("+ " + (available.get(money) - 8) + " more", 0, 440, 16, true, java.awt.Color.BLACK);
						GL11.glPopMatrix();
					}
				}

				GL11.glPopMatrix();

				StringRenderer.drawString("Total Amount: " + StringRenderer.getMoneyString(this.player.inventory.getMoneyAmount()), 725 - StringRenderer.getStringWidth("Total amount: " + StringRenderer.getMoneyString(this.player.inventory.getMoneyAmount()), 14, true), 369, 14, true, java.awt.Color.WHITE);
				break;
			case PROPERTIES:
				final List<PlaceOwnable> properties = this.player.inventory.getProperties();
				final PropertiesSortResult sort = this.sortProperties(properties);

				final List<Map<PlaceOwnable, Point2D.Float>> result1 = sort.placeOwnablePos;
				final List<List<PlaceOwnable>> result2 = sort.tabs;

				this.numSubTabs = result2.size();

				GL11.glPushMatrix();
				GL11.glTranslatef(36, 36, 0);
				GL11.glScalef(136.2F / 170.0F, 136.2F / 170.0F, 136.2F / 170.0F);

				for (final PlaceOwnable place : result2.get(this.subTab))
				{
					final GuiPropertyCard gui = new GuiPropertyCard(place);
					GL11.glPushMatrix();
					GL11.glTranslatef(result1.get(this.subTab).get(place).x, result1.get(this.subTab).get(place).y, 0);
					gui.drawGui();
					GL11.glPopMatrix();
				}

				GL11.glPopMatrix();

				break;
			case OTHER:
				final List<Card> jailCards = this.player.inventory.getJailCards();

				GL11.glPushMatrix();
				GL11.glTranslatef(62, 62, 0);

				for (int i = 0; i < jailCards.size(); i++)
				{
					new GuiCard(jailCards.get(i), 322 * i, 0, false)
					{
						@Override
						protected void onOK()
						{
						}
					}.drawGui();
				}

				GL11.glPopMatrix();
				break;
		}

		for (int i = 0; i < InventoryTab.values().length; i++)
		{
			final InventoryTab t = InventoryTab.values()[i];
			if (this.tab != t && Mouse.isButtonDown(0) && this.isTabSelected(i))
			{
				this.tab = t;
			}
		}

		for (final GuiButton button : this.getButtons())
		{
			if (button.id == 2)
			{
				button.drawButton = this.tab == InventoryTab.PROPERTIES;
				button.disabled = this.subTab == 0;
			}
			else if (button.id == 3)
			{
				button.drawButton = this.tab == InventoryTab.PROPERTIES;
				button.disabled = this.subTab >= this.numSubTabs - 1;
			}
		}
	}

	private boolean isTabSelected(int i)
	{
		final int mouseX = Mouse.getX() - this.xStart;
		final int mouseY = DisplayBase.screenSize.height - Mouse.getY() - this.yStart;

		return mouseX >= 40 + 149 * i && mouseX <= 40 + 149 * (i + 1) && mouseY >= 1 && mouseY <= 20;
	}

	@Override
	protected void onButtonReleased(GuiButton button)
	{
		if (button.id == 0)
		{
			if (this.previousGui == null)
			{
				this.parent.setPreviousGui();
			}
			else
			{
				this.parent.setIngameGui(this.previousGui);
			}
		}
		else if (button.id == 1)
		{
			this.parent.setIngameGui(new GuiChosePlayer(this.parent, this.player, this.previousGui == null ? this.parent.prevIngameGui : this.previousGui));
		}
		else if (button.id == 2)
		{
			this.subTab--;
		}
		else if (button.id == 3)
		{
			this.subTab++;
		}
	}

	private final Map<List<PlaceOwnable>, PropertiesSortResult> cache = new HashMap<List<PlaceOwnable>, PropertiesSortResult>();

	private PropertiesSortResult sortProperties(List<PlaceOwnable> properties)
	{
		if (!this.cache.containsKey(properties))
		{
			final List<Map<PlaceOwnable, Point2D.Float>> result1 = new ArrayList<Map<PlaceOwnable, Point2D.Float>>();
			final List<List<PlaceOwnable>> result2 = new ArrayList<List<PlaceOwnable>>();

			final Map<StreetColor, List<PlaceOwnable>> sortedByColor = new HashMap<StreetColor, List<PlaceOwnable>>();
			final List<StreetColor> presentColors = new ArrayList<StreetColor>();

			for (final PlaceOwnable place : properties)
			{
				if (sortedByColor.containsKey(place.getColor()))
				{
					sortedByColor.get(place.getColor()).add(place);
				}
				else
				{
					final List<PlaceOwnable> list = new ArrayList<PlaceOwnable>();
					list.add(place);
					sortedByColor.put(place.getColor(), list);
					presentColors.add(place.getColor());
				}
			}

			Collections.sort(presentColors);
			final int tabAmount = (int) Math.max(1, Math.ceil(presentColors.size() / 5.0));

			for (int tabNr = 0; tabNr < tabAmount; tabNr++)
			{
				final Map<PlaceOwnable, Point2D.Float> posInTab = new HashMap<PlaceOwnable, Point2D.Float>();
				final List<PlaceOwnable> placesInTab = new ArrayList<PlaceOwnable>();

				for (int colorNr = tabNr; colorNr < (tabNr + 1) * 5; colorNr++)
				{
					if (colorNr < presentColors.size())
					{
						final StreetColor color = presentColors.get(colorNr);
						final List<PlaceOwnable> placesInColor = sortedByColor.get(color);

						for (int placeNr = 0; placeNr < placesInColor.size(); placeNr++)
						{
							final PlaceOwnable place = placesInColor.get(placeNr);
							placesInTab.add(place);
							final Point2D.Float point = new Point2D.Float((170 + 3 / (136.2F / 170.0F)) * (colorNr % 5), placeNr * 47);
							posInTab.put(place, point);
						}
					}
				}

				result1.add(posInTab);
				result2.add(placesInTab);
			}

			final PropertiesSortResult result = new PropertiesSortResult();
			result.placeOwnablePos = result1;
			result.tabs = result2;

			this.cache.put(properties, result);
		}

		return this.cache.get(properties);
	}

	private static class PropertiesSortResult
	{
		public List<Map<PlaceOwnable, Point2D.Float>> placeOwnablePos;
		public List<List<PlaceOwnable>> tabs;

		@Override
		public String toString()
		{
			return this.getClass().getSimpleName() + "(" + this.placeOwnablePos.toString() + ", " + this.tabs.toString() + ")";
		}
	}

	public static enum InventoryTab
	{
		MONEY("Money"), PROPERTIES("Properties"), OTHER("Other");

		public final String tabName;

		private InventoryTab(String name)
		{
			this.tabName = name;
		}
	}
}
