package rheel.monopoly.game;

import rheel.monopoly.gui.GuiScreen;
import rheel.monopoly.renderer.DisplayBase;

public final class MouseLoc
{
	private MouseLoc()
	{
		throw new AssertionError();
	}

	public static int getX()
	{
		return (int) (org.lwjgl.input.Mouse.getX() * (1 / GuiScreen.scale));
	}

	public static int getY()
	{
		return (int) ((DisplayBase.screenSize.height - org.lwjgl.input.Mouse.getY()) * (1 / GuiScreen.scale));
	}
}
