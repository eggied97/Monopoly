package rheel.monopoly.renderer;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import rheel.monopoly.gui.GuiScreen;

public class DisplayBase
{
	public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	public static void initialize()
	{
		try
		{
			Display.setTitle("Monopoly");
			Display.setFullscreen(true);
			Display.setResizable(false);
			Display.create();

			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glOrtho(0, DisplayBase.screenSize.width, DisplayBase.screenSize.height, 0, 1, -1);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}
		catch (final LWJGLException e)
		{
			e.printStackTrace();
		}
	}

	public static void startRenderLoop()
	{
		while (!Display.isCloseRequested())
		{
			GL11.glLoadIdentity();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

			GuiScreen.currentGui.drawGui();

			Display.sync(60);
			Display.update();

			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			{
				System.exit(0);
			}
		}

		Display.destroy();
	}
}
