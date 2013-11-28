package rheel.monopoly.game;

import java.lang.reflect.Field;

import rheel.monopoly.game.Trade.TradeType;

public class Money implements ITradable
{
	public static final Money ONE = new Money(1, new int[]{235, 245, 247});
	public static final Money FIVE = new Money(5, new int[]{223, 197, 196});
	public static final Money TEN = new Money(10, new int[]{153, 213, 223});
	public static final Money TWENTY = new Money(20, new int[]{215, 231, 202});
	public static final Money FIFTY = new Money(50, new int[]{187, 172, 213});
	public static final Money ONE_HUNDRED = new Money(100, new int[]{238, 208, 158});
	public static final Money FIVE_HUNDRED = new Money(500, new int[]{223, 164, 63});

	public final int amount;
	public final int[] color;

	private Money(int amount, int[] color)
	{
		this.amount = amount;
		this.color = color;
	}

	public static Money[] values()
	{
		return new Money[]{Money.ONE, Money.FIVE, Money.TEN, Money.TWENTY, Money.FIFTY, Money.ONE_HUNDRED, Money.FIVE_HUNDRED};
	}
	
	@Override
	public String toString()
	{
		for(Field field : this.getClass().getFields())
		{
			try
			{
				if(field.get(null).equals(this))
				{
					return field.getName();
				}
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		
		return "null";
	}

	@Override
	public TradeType getType()
	{
		return TradeType.MONEY;
	}
}
