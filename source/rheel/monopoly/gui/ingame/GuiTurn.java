package rheel.monopoly.gui.ingame;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import rheel.monopoly.game.Dice;
import rheel.monopoly.game.Monopoly;
import rheel.monopoly.game.Player;
import rheel.monopoly.gui.GuiButton;
import rheel.monopoly.gui.GuiDice;
import rheel.monopoly.gui.GuiGame;
import rheel.monopoly.renderer.StringRenderer;

public class GuiTurn extends GuiIngame implements IInventoryVisibleGui
{
	private final Player player;
	private TurnState state = TurnState.START;

	private final GuiButton BUY_HOUSES = new GuiButton(0, this, this.xStart + this.xSize / 2 - 200, 50 + this.yStart, 400, 30, 18, "Buy Houses");
	private final GuiButton TRADE = new GuiButton(1, this, this.xStart + this.xSize / 2 - 200, 90 + this.yStart, 400, 30, 18, "Trade");
	private final GuiButton ROLL_DICE = new GuiButton(2, this, this.xStart + this.xSize / 2 - 200, 130 + this.yStart, 400, 30, 18, "Roll the Dice");
	private final GuiButton VIEW_INVENTORY_BIG = new GuiButton(3, this, this.xStart + this.xSize / 2 - 200, 170 + this.yStart, 400, 30, 18, "View Inventory");
	private final GuiButton PAY_FINE = new GuiButton(4, this, this.xStart + this.xSize / 2 - 200, 210 + this.yStart, 400, 30, 18, "Pay " + StringRenderer.getMoneyString(50) + " to get out of jail");
	private final GuiButton PLAY_CARD = new GuiButton(5, this, this.xStart + this.xSize / 2 - 200, 250 + this.yStart, 400, 30, 18, "Play your \"get out of jail for free\" card");

	private final GuiDice DICE = new GuiDice(this.xStart + (this.xSize - 83) / 2, this.yStart + 124)
	{
		@Override
		protected void onDiceRoll()
		{
			GuiTurn.this.state = TurnState.STOP;

			if (GuiTurn.this.player.isInJail())
			{
				if (Dice.hasDoubles())
				{
					GuiTurn.this.player.leaveJail();
				}
				else
				{
					GuiTurn.this.player.addDoubleCountInJail();

					if (GuiTurn.this.player.getDoubleInJail() == 3)
					{
						GuiTurn.this.parent.setIngameGui(new GuiPayFine(GuiTurn.this.parent, GuiTurn.this.player));
						return;
					}

					Monopoly.getInstance().setNextPlayer();
					return;
				}
			}

			final boolean d = Monopoly.getInstance().checkDoubles();

			if (d)
			{
				GuiTurn.this.parent.setIngameGui(new GuiMovingPlayer(GuiTurn.this.parent, this, GuiTurn.this.player, Dice.getRollTotal(), true));
			}
			else if (!d && !Dice.hasDoubles())
			{
				GuiTurn.this.parent.setIngameGui(new GuiMovingPlayer(GuiTurn.this.parent, this, GuiTurn.this.player, Dice.getRollTotal(), false));
			}
			else
			{
				Monopoly.getInstance().setNextPlayer();
			}
		}
	};

	public GuiTurn(GuiGame parent, Player player, boolean doubles)
	{
		super(parent, player);
		this.player = player;
		this.BUY_HOUSES.disabled = !this.player.canBuyHouses();
		this.TRADE.disabled = true;

		if (doubles)
		{
			this.state = TurnState.ROLL;
		}
	}

	@Override
	public void draw()
	{
		if (this.player.isInJail())
		{
			StringRenderer.drawCenteredString("You are in jail!", this.xSize / 2, 20, 15, true, new Color(218, 36, 40));
		}

		if (this.state == TurnState.START)
		{
			StringRenderer.drawString(this.player.name + ", It's your turn!", 25, 0, 16, true, Color.BLACK);
		}

		if (this.state == TurnState.START || this.state == TurnState.BUTTONS)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(-this.xStart, -this.yStart, 0);

			this.BUY_HOUSES.drawGui();
			this.TRADE.drawGui();
			this.ROLL_DICE.drawGui();
			this.VIEW_INVENTORY_BIG.drawGui();

			if (this.player.isInJail())
			{
				this.PAY_FINE.drawGui();

				if (this.player.inventory.getFreeJailLeaveCardAmount() > 0)
				{
					this.PLAY_CARD.drawGui();
				}
			}

			GL11.glPopMatrix();
		}

		if (this.state == TurnState.ROLL)
		{
			if (this.player.isInJail())
			{
				StringRenderer.drawCenteredString("Roll #" + (this.player.getDoubleInJail() + 1), this.xSize / 2, 100, 16, true, Color.BLACK);
			}

			GL11.glPushMatrix();
			GL11.glTranslatef(-this.xStart, -this.yStart, 0);

			this.DICE.drawGui();

			GL11.glPopMatrix();
		}
	}

	@Override
	protected void onButtonReleased(GuiButton button)
	{
		super.onButtonReleased(button);

		if (button.id == 0)
		{
			this.parent.setIngameGui(new GuiBuyHouses(this.parent, this.player));
		}
		else if (button.id == 1)
		{

		}
		else if (button.id == 2)
		{
			this.state = TurnState.ROLL;
		}
		else if (button.id == 3)
		{
			this.parent.setIngameGui(new GuiInventory(this.parent, this.player));
		}
		else if (button.id == 4)
		{
			if (!this.player.inventory.subtractMoney(this.parent, 50).isEmpty())
			{
				this.player.leaveJail();
			}
		}
		else if (button.id == 5)
		{
			this.player.leaveJail();
			this.player.inventory.removeFreeJailLeaveCard();
		}
	}

	public static enum TurnState
	{
		START, ROLL, TRADE, HOUSES, BUTTONS, STOP;
	}
}
