package rheel.monopoly.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rheel.monopoly.game.Card.CardType;
import rheel.monopoly.gui.GuiGame;
import rheel.monopoly.gui.GuiScreen;
import rheel.monopoly.gui.ingame.GuiNotEnoughMoney;
import rheel.monopoly.place.PlaceOwnable;
import rheel.monopoly.street.Street;

public class Inventory
{
	private Map<Money, Integer> ownedMoney = new HashMap<Money, Integer>();
	private final List<PlaceOwnable> properties = new ArrayList<PlaceOwnable>();

	private boolean jailLeaveCC = false;
	private boolean jailLeaveC = false;

	public final Player player;

	public Inventory(Player player, GameValues values)
	{
		this.player = player;
		this.fillInitialMoney(values);
	}

	public void fillInitialMoney(GameValues values)
	{
		this.ownedMoney.clear();
		this.ownedMoney.put(Money.ONE, values.moneyStart[0]);
		this.ownedMoney.put(Money.FIVE, values.moneyStart[1]);
		this.ownedMoney.put(Money.TEN, values.moneyStart[2]);
		this.ownedMoney.put(Money.TWENTY, values.moneyStart[3]);
		this.ownedMoney.put(Money.FIFTY, values.moneyStart[4]);
		this.ownedMoney.put(Money.ONE_HUNDRED, values.moneyStart[5]);
		this.ownedMoney.put(Money.FIVE_HUNDRED, values.moneyStart[6]);
	}

	/**
	 * @param values
	 *            Money, int, Money, int, Money, int, etc
	 */
	public void addMoney(Object... values)
	{
		if (values.length % 2 != 0)
		{
			throw new RuntimeException();
		}

		for (int i = 0; i < values.length; i += 2)
		{
			final Money type = (Money) values[i];
			final int amount = (Integer) values[i + 1] + this.ownedMoney.get(type);
			
			for(int j = 0; j < amount; j++)
			{
				GuiScreen.addMoney(type);
			}

			this.ownedMoney.put(type, amount);
		}
	}

	public void addMoney(int amount)
	{
		for (int i = Money.values().length - 1; i >= 0; i--)
		{
			final Money money = Money.values()[i];

			while (money.amount <= amount)
			{
				final int newAmount = this.ownedMoney.get(money) + 1;
				this.ownedMoney.put(money, newAmount);
				amount -= money.amount;
				GuiScreen.addMoney(money);
			}
		}
	}

	public Map<Money, Integer> subtractMoney(GuiGame gui, int amount)
	{
		return this.subtractMoney(gui, amount, false);
	}

	public Map<Money, Integer> subtractMoney(GuiGame gui, int amount, boolean force)
	{
		final Map<Money, Integer> subtracted = new HashMap<Money, Integer>();

		if (amount > this.getMoneyAmount())
		{
			gui.setIngameGui(new GuiNotEnoughMoney(gui, this.player, amount));

			if (force)
			{
				this.player.setBankrupt();
			}

			return Collections.emptyMap();
		}
		else
		{
			final Map<Money, Integer> copy = new HashMap<Money, Integer>(this.ownedMoney);

			for (int i = 6; i >= 0; i--)
			{
				final Money type = Money.values()[i];

				while (this.ownedMoney.get(type) != 0 && type.amount <= amount && amount / (float) type.amount > 0.1 && amount != 0)
				{
					final int value = this.ownedMoney.get(type) - 1;
					this.ownedMoney.put(type, value);
					amount -= type.amount;

					if (!subtracted.containsKey(type))
					{
						subtracted.put(type, 1);
					}
					else
					{
						final int j = subtracted.get(type);
						subtracted.put(type, j + 1);
					}
				}

				if (amount == 0)
				{
					break;
				}
			}

			if (amount != 0)
			{
				this.ownedMoney = copy;
				this.subtractMoneyNext(amount, 1);
			}

			if (amount != 0)
			{
				if (force)
				{
					this.player.setBankrupt();
				}

				return Collections.emptyMap();
			}
			else
			{
				return subtracted;
			}
		}
	}

	private boolean subtractMoneyNext(int amount, int tryNr)
	{
		if (tryNr == 15)
		{
			return false;
		}

		final Map<Money, Integer> copy = new HashMap<Money, Integer>(this.ownedMoney);

		for (int i = 6; i >= 0; i--)
		{
			final Money type = Money.values()[i];

			if (type.amount <= amount && type != Money.FIVE_HUNDRED)
			{
				this.tradeMoneyIntern(type);
				break;
			}
		}

		for (int i = 6; i >= 0; i--)
		{
			final Money type = Money.values()[i];

			while (this.ownedMoney.get(type) != 0 && type.amount <= amount && amount != 0)
			{
				final int value = this.ownedMoney.get(type) - 1;
				this.ownedMoney.put(type, value);
				amount -= type.amount;
			}

			if (amount == 0)
			{
				break;
			}
		}

		if (amount != 0)
		{
			this.ownedMoney = copy;
			return this.subtractMoneyNext(amount, tryNr + 1);
		}
		else
		{
			return true;
		}
	}

	private boolean tradeMoneyIntern(Money wantedType)
	{
		if (wantedType == Money.FIVE_HUNDRED)
		{
			return false;
		}

		for (int i = 0; i < 7; i++)
		{
			if (Money.values()[i] == wantedType)
			{
				final Money from = Money.values()[i + 1];
				final Money to = Money.values()[i];

				if (this.ownedMoney.get(from) == 0)
				{
					if (!this.tradeMoneyIntern(from))
					{
						return false;
					}
				}

				if (from == Money.FIFTY)
				{
					this.removeMoney(Money.FIFTY, 1);
					this.addMoney(Money.TWENTY, 2, Money.TEN, 1);
				}
				else
				{
					final int rate = from.amount / to.amount;
					this.removeMoney(from, 1);
					this.addMoney(to, rate);
				}
			}
		}

		return true;
	}

	private int removeMoney(Money type, int amount)
	{
		final int remove = Math.min(amount, this.ownedMoney.get(type));
		final int remain = this.ownedMoney.get(type) - remove;
		this.ownedMoney.put(type, remain);
		return remove;
	}

	public int getMoneyAmount()
	{
		int amount = 0;
		for (final Money ownedMoney : this.ownedMoney.keySet())
		{
			amount += ownedMoney.amount * this.ownedMoney.get(ownedMoney);
		}
		return amount;
	}

	public int getMoneyAmount(Money ownedMoney)
	{
		if (this.ownedMoney.containsKey(ownedMoney))
		{
			return this.ownedMoney.get(ownedMoney);
		}
		return 0;
	}

	public void addProperty(PlaceOwnable place)
	{
		this.properties.add(place);
	}

	public void removeProperty(PlaceOwnable place)
	{
		if (this.properties.contains(place))
		{
			this.properties.remove(place);
		}
	}

	public Map<Money, Integer> getOwnedMoney()
	{
		return Collections.unmodifiableMap(this.ownedMoney);
	}

	public List<PlaceOwnable> getProperties()
	{
		return Collections.unmodifiableList(this.properties);
	}

	public void getTradables(List<ITradable> list)
	{
		for (final Money money : Money.values())
		{
			for (int i = 0; i < this.ownedMoney.get(money); i++)
			{
				list.add(money);
			}
		}

		for (final PlaceOwnable place : this.properties)
		{
			list.add(place);
		}
	}

	public int getFreeJailLeaveCardAmount()
	{
		return (this.jailLeaveCC ? 1 : 0) + (this.jailLeaveC ? 1 : 0);
	}

	public boolean removeFreeJailLeaveCard()
	{
		if (this.jailLeaveCC)
		{
			this.jailLeaveCC = false;
			return true;
		}

		if (this.jailLeaveC)
		{
			this.jailLeaveC = false;
			return true;
		}

		return false;
	}

	public void addFreeJailLeaveCard(CardType type)
	{
		switch (type)
		{
			case CHANCE:
				this.jailLeaveC = true;
				break;
			case COMMUNITY_CHEST:
				this.jailLeaveCC = true;
				break;
		}
	}

	public List<Card> getJailCards()
	{
		final List<Card> cards = new ArrayList<Card>();

		if (this.jailLeaveC)
		{
			cards.add(Card.C_JAIL_FREE);
		}

		if (this.jailLeaveCC)
		{
			cards.add(Card.CC_JAIL_FREE);
		}

		return cards;
	}

	public List<Street> getStreets()
	{
		final List<Street> ret = new ArrayList<Street>();

		for (final Street street : Street.array())
		{
			if (street.hasPlayerStreet(this.player))
			{
				ret.add(street);
			}
		}

		return ret;
	}
}
