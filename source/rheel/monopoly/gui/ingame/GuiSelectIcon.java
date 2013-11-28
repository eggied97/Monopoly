package rheel.monopoly.gui.ingame;

import java.awt.Color;
import java.awt.Point;

import org.lwjgl.input.Mouse;

import rheel.monopoly.game.Monopoly;
import rheel.monopoly.game.MouseLoc;
import rheel.monopoly.game.Player;
import rheel.monopoly.gui.GuiGame;
import rheel.monopoly.renderer.StringRenderer;

public class GuiSelectIcon extends GuiIngame
{
	private int index;
	
	public GuiSelectIcon(GuiGame parent, Player player)
	{
		super(parent, player);
	}
	
	@Override
	protected void draw()
	{
		super.draw();
		StringRenderer.drawCenteredString("Select your icon:", this.xSize / 2, 42, 26, true, Color.BLACK);

		int xStart = (xSize / 2) - (34 * 4);
		
		boolean hasSelectedSomething = false;
		for(int i = 0; i < 8; i++)
		{
			Monopoly.getInstance().getEngine().bindTexture("/textures/players.png");
			Monopoly.getInstance().getEngine().drawTexturedRectangle((xStart + 1) + (i * 34), 120, i % 4 * 32, i / 4 * 32, 32, 32);
			Monopoly.getInstance().getEngine().bindTexture("/textures/player_border.png");
			Monopoly.getInstance().getEngine().drawTexturedRectangle(xStart + i * 34, 119, 0, i == index ? 34 : 0, 34, 34);
			
			final int x = MouseLoc.getX() - this.xStart;
			final int y = MouseLoc.getY() - this.yStart;

			if (x >= xStart + i * 34 && x <= (xStart + i * 34) + 34 && y >= 119 && y <= 119 + 34)
			{
				if (Mouse.isButtonDown(0))
				{
					player.texture = new Point(i % 4 * 32, i / 4 * 32);
					parent.setPreviousGui();
				}

				this.index = i;
				hasSelectedSomething = true;
			}
		}
		
		if (!hasSelectedSomething)
		{
			this.index = -1;
		}
	}
}
