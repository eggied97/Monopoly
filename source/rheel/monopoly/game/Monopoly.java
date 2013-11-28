package rheel.monopoly.game;

import java.util.Arrays;

import rheel.monopoly.gui.GuiGame;
import rheel.monopoly.gui.GuiMenu;
import rheel.monopoly.gui.GuiScreen;
import rheel.monopoly.gui.ingame.GuiTurn;
import rheel.monopoly.renderer.DisplayBase;
import rheel.monopoly.renderer.RenderEngine;

public class Monopoly
{
	private final Class<?> mainClass;
	private static Monopoly instance;

	private final RenderEngine renderEngine;

	private Gameboard board;
	private Player[] players = new Player[0];
	private Player activePlayer;

	private int doubleCount = 0;

	public Monopoly(Class<?> main)
	{
		this.mainClass = main;
		this.renderEngine = new RenderEngine(this.mainClass);
	}

	public static final void initInstance(Class<?> c)
	{
		Monopoly.instance = new Monopoly(c);
		DisplayBase.initialize();
		GuiScreen.setGui(new GuiMenu());
		DisplayBase.startRenderLoop();
	}

	public static final Monopoly getInstance()
	{
		return Monopoly.instance;
	}

	public RenderEngine getEngine()
	{
		return this.renderEngine;
	}

	public void clearPlayers()
	{
		this.players = new Player[0];
	}

	public void addPlayer(Player player)
	{
		this.players = Arrays.copyOf(this.players, this.players.length + 1);
		this.players[this.players.length - 1] = player;
	}

	public void setPlayer(Player player)
	{
		this.activePlayer = player;
		GuiGame.getInstance().setIngameGui(new GuiTurn(GuiGame.getInstance(), player, false));
	}

	public Player[] getPlayers()
	{
		return this.players;
	}

	public void setNextPlayer()
	{
		int id = 0;
		for (int i = 0; i < this.players.length; i++)
		{
			if (this.players[i].equals(this.activePlayer))
			{
				id = i;
			}
		}

		id++;
		id %= this.players.length;
		this.setPlayer(this.players[id]);
	}

	public boolean checkDoubles()
	{
		if (Dice.hasDoubles())
		{
			this.doubleCount++;

			if (this.doubleCount == 3)
			{
				this.activePlayer.setInJail();
				return false;
			}

			return true;
		}
		else
		{
			this.doubleCount = 0;
			return false;
		}
	}

	public void setPlayerPosition(int pos)
	{
		if (pos >= 0 && pos < 40)
		{
			this.activePlayer.position = pos;
		}
	}

	public void startGame(GameOptions options)
	{
		for (int i = 0; i < options.playerCount; i++)
		{
			this.addPlayer(Player.defaultPlayer(options.board.gameValues, i));
		}

		this.board = options.board;
		GuiScreen.setGui(new GuiGame(this.board));
	}

	public Player getActivePlayer()
	{
		return this.activePlayer;
	}

	public Gameboard getBoard()
	{
		return this.board;
	}

	public void closeGame()
	{
		System.exit(0);
	}
}
