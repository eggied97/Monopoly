package rheel.monopoly.gui;

import rheel.monopoly.game.Monopoly;
import rheel.monopoly.renderer.DisplayBase;

public class GuiBackground extends Gui
{
	@Override
	public void drawGui()
	{
		Monopoly.getInstance().getEngine().bindTexture("/textures/back.png");
		
		for (int x = 0; x < Math.ceil(DisplayBase.screenSize.width / 512.0F); x++)
		{
			for (int y = 0; y < Math.ceil(DisplayBase.screenSize.height / 512.0F); y++)
			{
				Monopoly.getInstance().getEngine().drawTexturedRectangle(x * 512, y * 512, 0, 0, 512, 512);
			}
		}
	}
}
