package rheel.monopoly.gui.ingame;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import rheel.monopoly.game.Monopoly;
import rheel.monopoly.game.MouseLoc;
import rheel.monopoly.game.Player;
import rheel.monopoly.gui.GuiButton;
import rheel.monopoly.gui.GuiGame;
import rheel.monopoly.gui.GuiTextField;
import rheel.monopoly.renderer.StringRenderer;

public class GuiPlayerName extends GuiIngame
{
	private final Player[] players;
	private final GuiTextField[] textfields;

	private int index = -1;

	public GuiPlayerName(GuiGame parent, Player[] players)
	{
		super(parent, null);
		this.players = players;
		this.textfields = new GuiTextField[players.length];

		for (int i = 0; i < players.length; i++)
		{
			this.textfields[i] = new GuiTextField(i, 195, 78 + 34 * i, 180, 23, 14, "Player " + (i + 1)).translate(this.xStart, this.yStart);
		}

		for (int i = 0; i < this.textfields.length; i++)
		{
			this.textfields[i].tabField = this.textfields[(i + 1) % this.textfields.length];
		}
	}

	@Override
	protected void initGui()
	{
		super.initGui();
		this.addButton(new GuiButton(0, this, this.xSize / 2 - 120, 450, 240, 24, "DONE"));
	}

	@Override
	protected void draw()
	{
		super.draw();
		StringRenderer.drawCenteredString("Enter your name:", this.xSize / 2, 42, 26, true, Color.BLACK);

		boolean hasSelectedSomething = false;
		for (int i = 0; i < this.players.length; i++)
		{
			StringRenderer.drawString("Player " + (i + 1) + ": ", 120, 80 + 34 * i, 14, true, Color.BLACK);
			StringRenderer.drawString("Icon: ", 520, 80 + 34 * i, 14, true, Color.BLACK);
			Monopoly.getInstance().getEngine().bindTexture("/textures/players.png");
			Monopoly.getInstance().getEngine().drawTexturedRectangle(570, 74 + 34 * i, this.players[i].texture.x, this.players[i].texture.y, 32, 32);
			Monopoly.getInstance().getEngine().bindTexture("/textures/player_border.png");
			Monopoly.getInstance().getEngine().drawTexturedRectangle(569, 73 + 34 * i, 0, this.index == i ? 34 : 0, 34, 34);

			final int x = MouseLoc.getX() - this.xStart;
			final int y = MouseLoc.getY() - this.yStart;

			if (x >= 569 && x <= 569 + 34 && y >= 73 + 34 * i && y <= 73 + 34 * (i + 1))
			{
				if (Mouse.isButtonDown(0))
				{
					this.parent.setIngameGui(new GuiSelectIcon(this.parent, this.players[i]));
				}

				this.index = i;
				hasSelectedSomething = true;
			}
		}

		if (!hasSelectedSomething)
		{
			this.index = -1;
		}

		GL11.glPushMatrix();
		GL11.glTranslatef(-this.xStart, -this.yStart, 0);

		for (final GuiTextField tf : this.textfields)
		{
			tf.drawGui();
		}

		final List<Point> points = new ArrayList<Point>();
		boolean disabled = false;

		for (final Player player : this.players)
		{
			if (points.contains(player.texture))
			{
				disabled = true;
				break;
			}
			else
			{
				points.add(player.texture);
			}
		}

		for (final GuiButton button : this.getButtons())
		{
			if (button.id == 0)
			{
				button.disabled = disabled;
			}
		}

		GL11.glPopMatrix();
	}

	@Override
	protected void onButtonReleased(GuiButton button)
	{
		super.onButtonReleased(button);

		if (button.id == 0)
		{
			for (int i = 0; i < this.players.length; i++)
			{
				this.players[i].name = this.textfields[i].text;
				this.parent.setIngameGui(new GuiPlayerSet(this.parent, this.players));
			}
		}
	}
}
