package rheel.monopoly.gui.ingame;

import java.util.ArrayList;
import java.util.List;

import rheel.monopoly.game.ITradable;
import rheel.monopoly.game.ITrader;
import rheel.monopoly.game.Trade.TradeType;
import rheel.monopoly.gui.GuiGame;

public class GuiTrade extends GuiIngame implements IInventoryVisibleGui
{
	private final List<ITradable> side1 = new ArrayList<ITradable>();
	private final List<ITradable> side2 = new ArrayList<ITradable>();

	public GuiTrade(GuiGame parent, ITrader trader1, ITrader trader2, TradeType type)
	{
		super(parent, null);

		for (final ITradable tradable : trader1.getTradables())
		{
			if (type.contains(tradable))
			{
				this.side1.add(tradable);
			}
		}

		for (final ITradable tradable : trader2.getTradables())
		{
			if (type.contains(tradable))
			{
				this.side2.add(tradable);
			}
		}
	}
}
