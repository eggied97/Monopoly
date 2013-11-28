package rheel.monopoly.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rheel.monopoly.game.GameParser;
import rheel.monopoly.game.Gameboard;
import rheel.monopoly.game.ParseException;
import rheel.monopoly.renderer.DisplayBase;

public class GuiChoseBoard extends GuiScreen
{
	private final GuiMenu parent;

	private Gameboard board;
	private GuiGameboard gui;
	private int page;
	private final List<Gameboard> boards;

	GuiButton button_done = new GuiButton(0, this, (DisplayBase.screenSize.width - 400) / 2, 600 + 100, 400, 24, "DONE");
	GuiButton button_left = new GuiButton(1, this, (DisplayBase.screenSize.width - 400) / 2, 570 + 100, 190, 24, "<----");
	GuiButton button_right = new GuiButton(2, this, (DisplayBase.screenSize.width - 400) / 2 + 210, 570 + 100, 190, 24, "---->");

	List<GuiButton> options = new ArrayList<GuiButton>();

	public GuiChoseBoard(GuiMenu guiMenu, Gameboard initialBoard)
	{
		this.parent = guiMenu;

		this.board = initialBoard;
		this.gui = new GuiGameboard(GuiGame.board_x, GuiGame.board_y, initialBoard);

		final File[] files = new File("C:/Users/Levi van Rheenen/Eclipse projects/mono/Monopoly/resources/boards/").listFiles();
		final List<Gameboard> boards = new ArrayList<Gameboard>();

		for (final File file : files)
		{
			try
			{
				final Gameboard board = new Gameboard(GameParser.parse(file));
				boards.add(board);
			}
			catch (final ParseException e)
			{
				// nothing
			}
		}

		this.boards = boards;

		this.selectPage(1);
	}

	@Override
	protected void draw()
	{
		this.gui.drawGui();
		this.button_done.drawGui();
		this.button_left.drawGui();
		this.button_right.drawGui();

		for (final GuiButton button : this.options)
		{
			button.drawGui();
		}
	}

	public void selectPage(int page)
	{
		this.page = page;
		this.options.clear();

		for (int i = 0; i < 20; i++)
		{
			final int id = i + (this.page - 1) * 20;

			if (id < this.boards.size())
			{
				this.options.add(new GuiButton(id + 3, this, (DisplayBase.screenSize.width - 400) / 2 + (i / 10 == 0 ? 0 : 210), 350 + i % 10 * 30, 190, 24, this.boards.get(i).gameValues.name));
			}
		}
		
		this.button_right.disabled = this.boards.size() <= 20 * this.page;
		this.button_left.disabled = this.page == 1;
	}

	private void select(int id)
	{
		this.board = this.boards.get(id);
		this.gui = new GuiGameboard(GuiGame.board_x, GuiGame.board_y, this.boards.get(id));
	}

	@Override
	protected void onButtonReleased(GuiButton button)
	{
		if (button.id == 0)
		{
			this.parent.setBoard(this.board);
			GuiScreen.setGui(this.parent);
		}
		else if (button.id == 1)
		{
			this.selectPage(this.page - 1);
		}
		else if (button.id == 2)
		{
			this.selectPage(this.page + 1);
		}
		else
		{
			this.select(button.id - 3);
		}
	}
}
