package rheel.monopoly.gui;

import java.awt.Point;

import org.lwjgl.opengl.GL11;

import rheel.monopoly.game.Dice;
import rheel.monopoly.game.Monopoly;

public abstract class GuiDice extends Gui
{
	public final GuiButton button;

	private static boolean[][] dice = new boolean[][]{{false, false, false, false, true, false, false, false, false}, {true, false, false, false, false, false, false, false, true}, {true, false, false, false, true, false, false, false, true}, {true, false, true, false, false, false, true, false, true}, {true, false, true, false, true, false, true, false, true}, {true, false, true, true, false, true, true, false, true}};
	private static Point[] points = new Point[]{new Point(4, 4), null, new Point(26, 4), new Point(4, 15), new Point(15, 15), new Point(26, 15), new Point(4, 26), null, new Point(26, 26)};

	private final int x;
	private final int y;

	private int countdown = 0;

	public GuiDice(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.button = new GuiButton(0, this, x, y + 43, 83, 20, "ROLL");
	}

	@Override
	public void drawGui()
	{
		Monopoly.getInstance().getEngine().bindTexture("/textures/dice.png");

		GL11.glPushMatrix();
		GL11.glTranslatef(this.x, this.y, 0);
		this.drawDice(Dice.getRoll()[0]);
		GL11.glTranslatef(42, 0, 0);
		this.drawDice(Dice.getRoll()[1]);
		GL11.glPopMatrix();

		this.button.drawGui();

		if (this.countdown > 0)
		{
			if (this.countdown == 1)
			{
				this.onDiceRoll();
				this.button.disabled = false;
			}

			this.countdown--;
		}
	}

	@Override
	protected void onButtonPressed(GuiButton button)
	{
		Dice.roll();
	}

	@Override
	protected void onButtonReleased(GuiButton button)
	{
		button.disabled = true;
		this.countdown = 30;
	}

	private void drawDice(int roll)
	{
		Monopoly.getInstance().getEngine().drawTexturedRectangle(0, 0, 0, 0, 41, 41);

		for (final Point p : this.getPoints(roll))
		{
			Monopoly.getInstance().getEngine().drawTexturedRectangle(p.x, p.y, 42, 0, 11, 11);
		}
	}

	private Point[] getPoints(int roll)
	{
		final Point[] p = new Point[roll];
		final boolean[] present = GuiDice.dice[roll - 1];

		int index = 0;
		for (int x = 0; x < 3; x++)
		{
			for (int y = 0; y < 3; y++)
			{
				if (present[x + y * 3])
				{
					p[index++] = GuiDice.points[x + y * 3];
				}
			}
		}

		return p;
	}

	protected abstract void onDiceRoll();
}
