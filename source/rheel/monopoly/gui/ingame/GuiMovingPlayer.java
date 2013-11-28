package rheel.monopoly.gui.ingame;

import rheel.monopoly.game.Monopoly;
import rheel.monopoly.game.Player;
import rheel.monopoly.gui.GuiDice;
import rheel.monopoly.gui.GuiGame;

public class GuiMovingPlayer extends GuiIngame implements IInventoryVisibleGui
{
	private final int count;
	private final GuiDice dice;
	private final boolean doubles;
	private int stepsTaken = 0;

	private int countdown = 0;

	public GuiMovingPlayer(GuiGame parent, GuiDice dice, Player player, int count, boolean doubles)
	{
		super(parent, player);
		this.dice = dice;
		this.count = count;
		this.doubles = doubles;
	}

	@Override
	public void drawGui()
	{
		super.drawGui();

		if (this.dice != null)
		{
			this.dice.button.disabled = true;
			this.dice.drawGui();
			this.dice.button.disabled = false;
		}

		if (this.countdown > 0)
		{
			if (this.countdown == 1)
			{
				if (this.count > 0)
				{
					this.player.position++;
					this.player.position %= 40;
					this.stepsTaken++;

					if (this.stepsTaken == this.count)
					{
						Monopoly.getInstance().getBoard().places[this.player.position].onPlayerLand(this.player, this.parent, this.doubles);
					}
					else
					{
						Monopoly.getInstance().getBoard().places[this.player.position].onPlayerPass(this.player, this.parent);
					}
				}
				else if (this.count < 0)
				{
					this.player.position += 39;
					this.player.position %= 40;
					this.stepsTaken++;

					if (this.stepsTaken == Math.abs(this.count))
					{
						Monopoly.getInstance().getBoard().places[this.player.position].onPlayerLand(this.player, this.parent, this.doubles);
					}
					else
					{
						Monopoly.getInstance().getBoard().places[this.player.position].onPlayerPass(this.player, this.parent);
					}
				}
			}

			this.countdown--;
		}
		else
		{
			this.countdown = 15;
		}
	}
}
