package rheel.monopoly.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import rheel.monopoly.place.PlaceProperty;
import rheel.monopoly.street.Street;

public class Player implements ITrader
{
	public int position;
	public Point texture;
	public String name;
	public final Inventory inventory;

	private boolean inJail = false;
	private int doubleInJail = 0;

	private Player(GameValues values)
	{
		this.inventory = new Inventory(this, values);
	}

	public static Player defaultPlayer(GameValues values, int id)
	{
		final Player player = new Player(values);
		player.position = 0;
		player.texture = new Point(id % 4 * 32, id / 4 * 32);
		return player;
	}

	@Override
	public List<ITradable> getTradables()
	{
		final List<ITradable> list = new ArrayList<ITradable>();
		this.inventory.getTradables(list);
		return list;
	}

	public boolean canBuyHouses()
	{
		for (final Street street : this.inventory.getStreets())
		{
			for (final PlaceProperty place : street.properties)
			{
				if (place.getHouses() < 5)
				{
					return true;
				}
			}
		}

		return false;
	}

	public void setInJail()
	{
		this.position = 10;
		this.inJail = true;
	}

	public void leaveJail()
	{
		this.inJail = false;
		this.doubleInJail = 0;
	}

	@Override
	public String toString()
	{
		return this.name;
	}

	public boolean isInJail()
	{
		return this.inJail;
	}

	public void addDoubleCountInJail()
	{
		this.doubleInJail++;
	}

	public int getDoubleInJail()
	{
		return this.doubleInJail;
	}

	public void setBankrupt()
	{
		this.name += " [Bankrupt]";
	}
}
