package rheel.monopoly.game;

import java.util.HashMap;
import java.util.Map;

public final class Pot
{
	private static Map<Money, Integer> contents = new HashMap<Money, Integer>();

	static
	{
		Pot.contents.clear();

		for (final Money money : Money.values())
		{
			Pot.contents.put(money, 0);
		}
	}

	public static void addToPot(Object... values)
	{
		if (values.length % 2 != 0)
		{
			throw new RuntimeException();
		}

		if (Monopoly.getInstance().getBoard().gameValues.free_parking_pot)
		{
			for (int i = 0; i < values.length; i += 2)
			{
				final Money type = (Money) values[i];
				System.out.println(type);
				final int amount = (Integer) values[i + 1] + Pot.contents.get(type);

				Pot.contents.put(type, amount);
			}
		}
	}

	public static void addPotToPlayer(Player player)
	{
		final Object[] values = new Object[Pot.contents.size() * 2];
		int index = 0;

		for (final Money money : Pot.contents.keySet())
		{
			values[index++] = money;
			values[index++] = Pot.contents.get(money);
		}

		player.inventory.addMoney(values);
		Pot.contents.clear();
	}
}
