package rheel.monopoly.place;

import rheel.monopoly.game.Gameboard;
import rheel.monopoly.game.ITradable;
import rheel.monopoly.game.Player;
import rheel.monopoly.game.Trade.TradeType;
import rheel.monopoly.gui.GuiGame;
import rheel.monopoly.gui.ingame.GuiBuyProperty;
import rheel.monopoly.street.StreetColor;

public class PlaceOwnable extends Place implements ITradable
{
	protected Player owner;
	protected final int price;
	protected final String name;
	protected final StreetColor color;

	public PlaceOwnable(Gameboard board, int id, String name, StreetColor color, Position pos, int price)
	{
		super(board, id, pos);
		this.name = name;
		this.price = price;
		this.color = color;
	}

	public void setOwned(Player player)
	{
		this.owner = player;
		player.inventory.addProperty(this);
	}

	public boolean isOwnedBy(Player player)
	{
		return this.owner.equals(player);
	}

	public Player getOwner()
	{
		return this.owner;
	}

	public boolean canBeBought()
	{
		return this.owner == null;
	}

	public int getPrice()
	{
		return this.price;
	}

	@Override
	public TradeType getType()
	{
		return TradeType.PROPERTY;
	}

	public String getName()
	{
		return this.name;
	}

	public StreetColor getColor()
	{
		return this.color;
	}

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + "(" + this.name + ")";
	}

	@Override
	public void onPlayerLand(Player player, GuiGame gui, boolean doubles)
	{
		if (this.owner == null)
		{
			gui.setIngameGui(new GuiBuyProperty(this, player, gui, doubles));
		}
		else
		{
			super.onPlayerLand(player, gui, doubles);
		}
	}
}
