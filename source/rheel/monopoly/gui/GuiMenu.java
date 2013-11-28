package rheel.monopoly.gui;

import java.awt.Color;
import java.io.File;

import rheel.monopoly.game.GameOptions;
import rheel.monopoly.game.GameParser;
import rheel.monopoly.game.Gameboard;
import rheel.monopoly.game.Monopoly;
import rheel.monopoly.renderer.DisplayBase;
import rheel.monopoly.renderer.StringRenderer;
import rheel.monopoly.save.SaveSettings;

public class GuiMenu extends GuiScreen
{
	private final GameOptions options = new GameOptions();
	private GuiGameboard boardGui;

	public GuiMenu()
	{
		super();
		Monopoly.getInstance().clearPlayers();
		SaveSettings.load();
		if (SaveSettings.latestGameValues != null)
		{
			this.setBoard(new Gameboard(SaveSettings.latestGameValues));
		}
		else
		{
			this.setBoard(new Gameboard(GameParser.parse(new File("C:/Users/Levi van Rheenen/Eclipse projects/mono/Monopoly/resources/boards/dutch.mpb"))));
		}

		this.options.playerCount = 2;
	}

	@Override
	protected void initGui()
	{
		super.initGui();

		this.buttonList.add(new GuiButton(0, this, DisplayBase.screenSize.width / 2 - 200, 600, 400, 50, 20, "PLAY MONOPOLY"));
		this.buttonList.add(new GuiButton(1, this, DisplayBase.screenSize.width / 2 - 200, 460, 190, 20, "Change board"));
		this.buttonList.add(new GuiButton(2, this, DisplayBase.screenSize.width / 2 - 200, 490, 190, 20, "Add player"));
		this.buttonList.add(new GuiButton(3, this, DisplayBase.screenSize.width / 2 + 010, 490, 190, 20, "Remove player"));
		this.buttonList.add(new GuiButton(4, this, DisplayBase.screenSize.width / 2 + 010, 460, 190, 20, "Close Game"));

		this.buttonList.get(3).disabled = true;
	}

	@Override
	protected void draw()
	{
		this.boardGui.drawGui();
		StringRenderer.drawString("Players: " + this.options.playerCount, DisplayBase.screenSize.width / 2 - 200 + 420, 490, 13, true, Color.BLACK);
	}

	@Override
	protected void onButtonReleased(GuiButton button)
	{
		if (button.id == 0)
		{
			Monopoly.getInstance().startGame(this.options);
		}
		else if (button.id == 1)
		{
			GuiScreen.setGui(new GuiChoseBoard(this, this.options.board));
		}
		else if (button.id == 2)
		{
			this.options.playerCount++;

			if (this.options.playerCount == 8)
			{
				button.disabled = true;
			}

			if (this.options.playerCount >= 3)
			{
				this.buttonList.get(3).disabled = false;
			}
		}
		else if (button.id == 3)
		{
			this.options.playerCount--;

			if (this.options.playerCount == 2)
			{
				this.buttonList.get(3).disabled = true;
			}

			this.buttonList.get(2).disabled = false;
		}
		else if (button.id == 4)
		{
			Monopoly.getInstance().closeGame();
		}
	}

	void setBoard(Gameboard game)
	{
		this.options.board = game;
		this.boardGui = new GuiGameboard(GuiGame.board_x, GuiGame.board_y, game);
	}
}
