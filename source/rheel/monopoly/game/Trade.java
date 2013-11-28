package rheel.monopoly.game;

public final class Trade
{
	public static enum TradeType
	{
		MONEY, PROPERTY, ALL
		{
			@Override
			public boolean contains(ITradable tradable)
			{
				return true;
			}
		};

		public boolean contains(ITradable tradable)
		{
			return tradable.getType() == this;
		}
	}
}
