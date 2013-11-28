package rheel.monopoly.gui.ingame;

import rheel.monopoly.gui.GuiButton;
import rheel.monopoly.gui.GuiGame;
import rheel.monopoly.gui.GuiMenu;
import rheel.monopoly.gui.GuiScreen;

public class GuiCloseGame extends GuiIngame
{
	public GuiCloseGame(GuiGame parent)
	{
		super(parent, null, 400, 80);
	}

	@Override
	protected void initGui()
	{
		super.initGui();
		this.addButton(new GuiButton(0, this, 0, 0, 190, 24, "Exit without saving"));
		this.addButton(new GuiButton(1, this, 210, 0, 190, 24, "Exit with saving"));
		this.addButton(new GuiButton(2, this, 0, 40, 400, 24, "Cancel"));
	}

	@Override
	protected void draw()
	{
	}

	@Override
	protected void onButtonReleased(GuiButton button)
	{
		super.onButtonReleased(button);

		if (button.id == 0)
		{
			GuiScreen.setGui(new GuiMenu());
		}
		else if (button.id == 1)
		{
			this.parent.setIngameGui(new GuiSaveGame(this.parent));
		}
		else if (button.id == 2)
		{
			this.parent.setPreviousGui();
		}
	}
}
