package rheel.monopoly.gui.ingame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import rheel.monopoly.game.Dice;
import rheel.monopoly.game.Monopoly;
import rheel.monopoly.game.Player;
import rheel.monopoly.gui.GuiDice;
import rheel.monopoly.gui.GuiGame;
import rheel.monopoly.renderer.StringRenderer;

public class GuiPlayerSet extends GuiIngame
{
	private final Player[] players;
	private final int size;
	private int i = 0;
	private final Map<Player, Integer> rolls = new HashMap<Player, Integer>();
	private final GuiDice dice = new GuiDice(this.xStart + (this.xSize - 83) / 2, this.yStart + 150)
	{
		@Override
		protected void onDiceRoll()
		{
			GuiPlayerSet.this.rolls.put(GuiPlayerSet.this.players[GuiPlayerSet.this.i], Dice.getRollTotal());

			if (++GuiPlayerSet.this.i == GuiPlayerSet.this.size)
			{
				Player heighest = GuiPlayerSet.this.players[0];

				for (int i = 1; i < GuiPlayerSet.this.players.length; i++)
				{
					if (GuiPlayerSet.this.rolls.get(GuiPlayerSet.this.players[i]) > GuiPlayerSet.this.rolls.get(heighest))
					{
						heighest = GuiPlayerSet.this.players[i];
					}
				}

				final List<Player> newPlayers = new ArrayList<Player>();

				for (final Player player : GuiPlayerSet.this.players)
				{
					if (GuiPlayerSet.this.rolls.get(player) == GuiPlayerSet.this.rolls.get(heighest))
					{
						newPlayers.add(player);
					}
				}

				if (newPlayers.size() == 1)
				{
					Monopoly.getInstance().setPlayer(newPlayers.get(0));
				}
				else
				{
					final Player[] newPlayerArray = new Player[newPlayers.size()];

					for (int i = 0; i < newPlayers.size(); i++)
					{
						newPlayerArray[i] = newPlayers.get(i);
					}

					GuiPlayerSet.this.parent.setIngameGui(new GuiPlayerSet(GuiPlayerSet.this.parent, newPlayerArray));
				}
			}
		}
	};

	public GuiPlayerSet(GuiGame parent, Player[] players)
	{
		super(parent, null);
		this.players = players;
		this.size = players.length;
	}

	@Override
	public void draw()
	{
		StringRenderer.drawCenteredString("The player who has the highest total roll may start.", this.xSize / 2, 60, 24, true, Color.BLACK);
		StringRenderer.drawCenteredString(this.players[this.i].name, this.xSize / 2, 92, 18, true, Color.BLACK);

		GL11.glPushMatrix();
		GL11.glTranslatef(-this.xStart, -this.yStart, 0);
		this.dice.drawGui();
		GL11.glPopMatrix();
	}
}
