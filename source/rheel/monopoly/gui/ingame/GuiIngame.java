package rheel.monopoly.gui.ingame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.opengl.GL11;

import rheel.monopoly.game.MouseLoc;
import rheel.monopoly.game.Player;
import rheel.monopoly.gui.Gui;
import rheel.monopoly.gui.GuiButton;
import rheel.monopoly.gui.GuiGame;
import rheel.monopoly.renderer.DisplayBase;

public abstract class GuiIngame extends Gui
{
	private final List<GuiButton> buttonList = new ArrayList<GuiButton>();
	protected final GuiGame parent;
	protected final Player player;

	protected final int xSize;
	protected final int ySize;
	protected int xStart;
	protected int yStart;

	public GuiIngame(GuiGame parent, Player player)
	{
		this(parent, player, 765, 510);
	}

	public GuiIngame(GuiGame parent, Player player, int xSize, int ySize)
	{
		this.parent = parent;
		this.player = player;

		this.xSize = xSize;
		this.ySize = ySize;
		this.xStart = (DisplayBase.screenSize.width - xSize) / 2;
		this.yStart = 384;

		this.initGui();
	}

	protected List<GuiButton> getButtons()
	{
		return Collections.unmodifiableList(this.buttonList);
	}

	protected void initGui()
	{
	}

	protected void draw()
	{
	}

	protected void addButton(GuiButton button)
	{
		final GuiButton gb = button.translate(this.xStart, this.yStart);
		this.buttonList.add(gb);
	}

	@Override
	public void drawGui()
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(this.xStart, this.yStart, 0);
		this.draw();
		GL11.glPopMatrix();

		for (final GuiButton button : this.buttonList)
		{
			button.drawGui();
		}
	}

	public int getMouseX()
	{
		return MouseLoc.getX() - this.xStart;
	}

	public int getMouseY()
	{
		return MouseLoc.getY() - this.yStart;
	}
}
