package rheel.monopoly.gui;

import rheel.monopoly.game.Gameboard;
import rheel.monopoly.game.Monopoly;
import rheel.monopoly.game.Player;
import rheel.monopoly.gui.ingame.GuiCloseGame;
import rheel.monopoly.gui.ingame.GuiIngame;
import rheel.monopoly.gui.ingame.GuiInventory;
import rheel.monopoly.gui.ingame.GuiPlayerName;
import rheel.monopoly.gui.ingame.IInventoryVisibleGui;
import rheel.monopoly.place.Place.Position;
import rheel.monopoly.renderer.DisplayBase;

public class GuiGame extends GuiScreen
{
	private static GuiGame INSTANCE;

	private final static int boardsize = 1021;
	public static final int board_x = (DisplayBase.screenSize.width - GuiGame.boardsize) / 2;
	public static final int board_y = (DisplayBase.screenSize.height - GuiGame.boardsize) / 2;
	private final GuiGameboard boardGui;
	private final Gameboard board;

	private GuiIngame ingameGui = null;
	public GuiIngame prevIngameGui = null;

	public GuiGame(Gameboard board)
	{
		GuiGame.INSTANCE = this;
		this.board = board;
		this.boardGui = new GuiGameboard(GuiGame.board_x, GuiGame.board_y, board);
		this.setIngameGui(new GuiPlayerName(this, Monopoly.getInstance().getPlayers()));
	}

	public static GuiGame getInstance()
	{
		return GuiGame.INSTANCE;
	}

	@Override
	protected void initGui()
	{
		super.initGui();
		this.buttonList.add(new GuiButton(0, this, 5, 5, 85, 20, "Exit game"));
		this.buttonList.add(new GuiButton(1, this, GuiGame.board_x + GuiGame.boardsize - 282, GuiGame.board_y + 389, 150, 22, "View Inventory"));
		this.setButtonInventoryVisible(false);
	}

	@Override
	protected void draw()
	{
		this.setButtonInventoryVisible(this.ingameGui instanceof IInventoryVisibleGui);
		this.boardGui.drawGui();

		Monopoly.getInstance().getEngine().bindTexture("/textures/players.png");

//		for (int i = 0; i < Monopoly.getInstance().getPlayers().length; i++)
//		{
//			final Player player = Monopoly.getInstance().getPlayers()[i];
//			final Position pos = this.board.places[player.position].position;
//			Monopoly.getInstance().getEngine().drawTexturedRectangle(GuiGame.board_x + pos.x, GuiGame.board_y + pos.y, player.texture.x, player.texture.y, 32, 32);
//		}

		if (this.ingameGui == null)
		{
			Monopoly.getInstance().setNextPlayer();
		}

		this.ingameGui.drawGui();
	}

	public void setButtonInventoryVisible(boolean visible)
	{
		for (final GuiButton button : this.buttonList)
		{
			if (button.id == 1)
			{
				button.drawButton = visible;
			}
		}
	}

	@Override
	protected void onButtonReleased(GuiButton button)
	{
		super.onButtonReleased(button);
		if (button.id == 0)
		{
			this.setIngameGui(new GuiCloseGame(this));
		}
		else if (button.id == 1)
		{
			this.setIngameGui(new GuiInventory(this, Monopoly.getInstance().getActivePlayer()));
		}
	}

	public void setIngameGui(GuiIngame gui)
	{
		this.prevIngameGui = this.ingameGui;
		this.ingameGui = gui;
	}

	public void setPreviousGui()
	{
		this.setIngameGui(this.prevIngameGui);
	}
}
