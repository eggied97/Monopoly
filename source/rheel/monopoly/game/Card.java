package rheel.monopoly.game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import rheel.monopoly.gui.GuiGame;
import rheel.monopoly.gui.ingame.GuiMovingPlayer;
import rheel.monopoly.gui.ingame.GuiTurn;
import rheel.monopoly.place.PlaceOwnable;
import rheel.monopoly.place.PlaceProperty;
import rheel.monopoly.place.PlaceStation;
import rheel.monopoly.place.PlaceUtility;
import rheel.monopoly.renderer.StringRenderer;

public abstract class Card
{
	private static List<Card> pileChance = new ArrayList<Card>();
	private static List<Card> pileCommunityChest = new ArrayList<Card>();

	public static final int xSize = 319;
	public static final int yStart = 80;

	//=============================================================
	//                         CHANCE
	//=============================================================

	public static final Card C_MOVE_TO_START = new Card(CardType.CHANCE)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			gui.setIngameGui(new GuiMovingPlayer(gui, null, player, 40 - player.position, doubles));
			return false;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Advance to GO");
		}
	};

	public static final Card C_RED = new Card(CardType.CHANCE)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			gui.setIngameGui(new GuiMovingPlayer(gui, null, player, player.position < 24 ? 24 - player.position : 64 - player.position, doubles));
			return false;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Advance to " + ((PlaceOwnable) Monopoly.getInstance().getBoard().places[24]).getName());
			Card.drawSubTitle("If you pass GO, collect " + StringRenderer.getMoneyString(200));
		}
	};

	public static final Card C_PINK = new Card(CardType.CHANCE)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			gui.setIngameGui(new GuiMovingPlayer(gui, null, player, player.position < 11 ? 11 - player.position : 51 - player.position, doubles));
			return false;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Advance to " + ((PlaceOwnable) Monopoly.getInstance().getBoard().places[11]).getName());
			Card.drawSubTitle("If you pass GO, collect " + StringRenderer.getMoneyString(200));
		}
	};

	public static final Card C_UTILITY = new Card(CardType.CHANCE)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			int utility = player.position;

			while (!(Monopoly.getInstance().getBoard().places[utility] instanceof PlaceUtility))
			{
				utility++;
				utility %= 40;
			}

			gui.setIngameGui(new GuiMovingPlayer(gui, null, player, player.position < utility ? utility - player.position : 40 + utility - player.position, doubles));
			return false;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Advance to the nearest utility");
			Card.drawSubTitle("If you pass GO, collect " + StringRenderer.getMoneyString(200));
		}
	};

	public static final Card C_RAILROAD = new Card(CardType.CHANCE)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			int station = player.position;

			while (!(Monopoly.getInstance().getBoard().places[station] instanceof PlaceStation))
			{
				station++;
				station %= 40;
			}

			gui.setIngameGui(new GuiMovingPlayer(gui, null, player, player.position < station ? station - player.position : 40 + station - player.position, doubles));
			return false;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Advance token to the nearest Railroad");
			Card.drawSubTitle("If you pass GO, collect " + StringRenderer.getMoneyString(200));
		}
	};

	public static final Card C_DIVIDENT = new Card(CardType.CHANCE)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			player.inventory.addMoney(Money.FIFTY, 1);
			return true;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Bank pays you dividend of " + StringRenderer.getMoneyString(50));
		}
	};

	public static final Card C_JAIL_FREE = new Card(CardType.CHANCE)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			player.inventory.addFreeJailLeaveCard(CardType.CHANCE);
			return true;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Get out of jail, free");
			Card.drawSubTitle("This card may be kept until needed or sold");
		}
	};

	public static final Card C_BACK = new Card(CardType.CHANCE)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			gui.setIngameGui(new GuiMovingPlayer(gui, null, player, -3, doubles));
			return false;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Go back 3 spaces");
		}
	};

	public static final Card C_GOTO_JAIL = new Card(CardType.CHANCE)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			player.setInJail();
			return true;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Go to Jail");
			Card.drawSubTitle("Do not pass GO - Do not collect " + StringRenderer.getMoneyString(200));
		}
	};

	public static final Card C_HOUSE_REPAIRS = new Card(CardType.CHANCE)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			int houses = 0;
			int hotels = 0;

			for (final PlaceOwnable place : player.inventory.getProperties())
			{
				if (place instanceof PlaceProperty)
				{
					if (((PlaceProperty) place).getHouses() == 5)
					{
						hotels++;
					}
					else
					{
						houses += ((PlaceProperty) place).getHouses();
					}
				}
			}

			final Map<Money, Integer> map = player.inventory.subtractMoney(gui, houses * 25 + hotels * 100, true);
			final Object[] toPot = new Object[map.size() * 2];

			int index = 0;
			for (final Money money : map.keySet())
			{
				toPot[index++] = money;
				toPot[index++] = map.get(money);
			}

			Pot.addToPot(toPot);
			return true;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Make general repairs on all your property");
			Card.drawSubTitle("For each house pay " + StringRenderer.getMoneyString(25) + " - For each hotel " + StringRenderer.getMoneyString(100));
		}
	};

	public static final Card C_TAX = new Card(CardType.CHANCE)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			player.inventory.subtractMoney(gui, 15);
			return true;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Pay poor tax of " + StringRenderer.getMoneyString(15));
		}
	};

	public static final Card C_RAILROAD_2 = new Card(CardType.CHANCE)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			gui.setIngameGui(new GuiMovingPlayer(gui, null, player, player.position < 5 ? 5 - player.position : 45 - player.position, doubles));
			return false;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Take a trip to " + ((PlaceOwnable) Monopoly.getInstance().getBoard().places[5]).getName());
			Card.drawSubTitle("If you pass GO, collect " + StringRenderer.getMoneyString(200));
		}
	};

	public static final Card C_BLUE = new Card(CardType.CHANCE)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			gui.setIngameGui(new GuiMovingPlayer(gui, null, player, player.position < 39 ? 39 - player.position : 89 - player.position, doubles));
			return false;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Advance to " + ((PlaceOwnable) Monopoly.getInstance().getBoard().places[39]).getName());
			Card.drawSubTitle("If you pass GO, collect " + StringRenderer.getMoneyString(200));
		}
	};

//  TODO players --> players paying 
//	public static final Card C_CHAIRMAN = new Card(CardType.CHANCE)
//	{
//		@Override
//		protected boolean use(Player player, GuiGame gui, boolean doubles)
//		{
//		}
//
//		@Override
//		public void render()
//		{
//			Card.drawTitle("You have been elected Chairman of the Board");
//			Card.drawSubTitle("Pay each player " + StringRenderer.getMoneyString(50));
//		}
//	};

	public static final Card C_LOAN = new Card(CardType.CHANCE)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			player.inventory.addMoney(Money.ONE_HUNDRED, 1, Money.FIFTY, 1);
			return true;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Your building loan matures");
			Card.drawSubTitle("Collect " + StringRenderer.getMoneyString(150));
		}
	};

	public static final Card C_CROSSWORD = new Card(CardType.CHANCE)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			player.inventory.addMoney(Money.ONE_HUNDRED, 1);
			return true;
		}

		@Override
		public void render()
		{
			Card.drawTitle("You have won a crossword competition");
			Card.drawSubTitle("Collect " + StringRenderer.getMoneyString(100));
		}
	};

	//=============================================================
	//                       COMMUNITY CHEST
	//=============================================================

	public static final Card CC_MOVE_TO_START = new Card(CardType.COMMUNITY_CHEST)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			gui.setIngameGui(new GuiMovingPlayer(gui, null, player, 40 - player.position, doubles));
			return false;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Advance to GO");
		}
	};

	public static final Card CC_BANK_ERROR = new Card(CardType.COMMUNITY_CHEST)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			player.inventory.addMoney(Money.ONE_HUNDRED, 2);
			return true;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Bank error in your favor");
			Card.drawSubTitle("Collect " + StringRenderer.getMoneyString(200));
		}
	};

	public static final Card CC_DOCTOR = new Card(CardType.COMMUNITY_CHEST)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			final Map<Money, Integer> map = player.inventory.subtractMoney(gui, 50, true);
			final Object[] toPot = new Object[map.size() * 2];

			int index = 0;
			for (final Money money : map.keySet())
			{
				toPot[index++] = money;
				toPot[index++] = map.get(money);
			}

			Pot.addToPot(toPot);
			return true;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Doctor's fees");
			Card.drawSubTitle("Pay " + StringRenderer.getMoneyString(50));
		}
	};

	public static final Card CC_STOCK = new Card(CardType.COMMUNITY_CHEST)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			player.inventory.addMoney(Money.FIFTY, 1);
			return true;
		}

		@Override
		public void render()
		{
			Card.drawTitle("From sale of stock you get " + StringRenderer.getMoneyString(50));
		}
	};

	public static final Card CC_JAIL_FREE = new Card(CardType.COMMUNITY_CHEST)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			player.inventory.addFreeJailLeaveCard(CardType.COMMUNITY_CHEST);
			return true;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Get out of jail, free");
			Card.drawSubTitle("This card may be kept until needed or sold");
		}
	};

	public static final Card CC_GO_TO_JAIL = new Card(CardType.COMMUNITY_CHEST)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			player.setInJail();
			return true;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Go to Jail");
			Card.drawSubTitle("Do not pass GO - Do not collect " + StringRenderer.getMoneyString(200));
		}
	};

//  TODO players --> players paying
//	public static final Card CC_OPERA = new Card(CardType.COMMUNITY_CHEST)
//	{
//		@Override
//		protected boolean use(Player player, GuiGame gui, boolean doubles)
//		{
//			
//		}
//
//		@Override
//		public void render()
//		{
//			Card.drawTitle("Grand Opera Night Opening");
//			Card.drawSubTitle("Collect " + StringRenderer.getMoneyString(50) + " from every player for opening night seats");
//		}
//	};

	public static final Card CC_Holiday = new Card(CardType.COMMUNITY_CHEST)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			player.inventory.addMoney(Money.ONE_HUNDRED, 1);
			return true;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Holiday fund matures");
			Card.drawSubTitle("Collect " + StringRenderer.getMoneyString(100));
		}
	};

	public static final Card CC_INCOME = new Card(CardType.COMMUNITY_CHEST)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			player.inventory.addMoney(Money.TWENTY, 1);
			return true;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Income tax refund");
			Card.drawSubTitle("Collect " + StringRenderer.getMoneyString(20));
		}
	};

//  TODO players --> players paying
//	public static final Card CC_BIRTHDAY = new Card(CardType.COMMUNITY_CHEST)
//	{
//		@Override
//		protected boolean use(Player player, GuiGame gui, boolean doubles)
//		{
//		}
//
//		@Override
//		public void render()
//		{
//			Card.drawTitle("It is your birthday");
//			Card.drawSubTitle("Collect " + StringRenderer.getMoneyString(10) + " from each player");
//		}
//	};

	public static final Card CC_INSURANCE = new Card(CardType.COMMUNITY_CHEST)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			player.inventory.addMoney(Money.ONE_HUNDRED, 1);
			return true;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Life insurance matures");
			Card.drawSubTitle("Collect " + StringRenderer.getMoneyString(100));
		}
	};

	public static final Card CC_HOSPITAL = new Card(CardType.COMMUNITY_CHEST)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			final Map<Money, Integer> map = player.inventory.subtractMoney(gui, 100, true);
			final Object[] toPot = new Object[map.size() * 2];

			int index = 0;
			for (final Money money : map.keySet())
			{
				toPot[index++] = money;
				toPot[index++] = map.get(money);
			}

			Pot.addToPot(toPot);
			return true;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Pay hospital fees of " + StringRenderer.getMoneyString(100));
		}
	};

	public static final Card CC_SCHOOL = new Card(CardType.COMMUNITY_CHEST)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			final Map<Money, Integer> map = player.inventory.subtractMoney(gui, 150, true);
			final Object[] toPot = new Object[map.size() * 2];

			int index = 0;
			for (final Money money : map.keySet())
			{
				toPot[index++] = money;
				toPot[index++] = map.get(money);
			}

			Pot.addToPot(toPot);
			return true;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Pay school fees of " + StringRenderer.getMoneyString(150));
		}
	};

	public static final Card CC_CONSULTANCY_FEE = new Card(CardType.COMMUNITY_CHEST)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			player.inventory.addMoney(Money.TWENTY, 1, Money.FIVE, 1);
			return true;
		}

		@Override
		public void render()
		{
			Card.drawTitle("Receive " + StringRenderer.getMoneyString(25) + " consultancy fee");
		}
	};

	public static final Card CC_STREET_REPAIRS = new Card(CardType.COMMUNITY_CHEST)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			int houses = 0;
			int hotels = 0;

			for (final PlaceOwnable place : player.inventory.getProperties())
			{
				if (place instanceof PlaceProperty)
				{
					if (((PlaceProperty) place).getHouses() == 5)
					{
						hotels++;
					}
					else
					{
						houses += ((PlaceProperty) place).getHouses();
					}
				}
			}

			final Map<Money, Integer> map = player.inventory.subtractMoney(gui, houses * 40 + hotels * 115, true);
			final Object[] toPot = new Object[map.size() * 2];

			int index = 0;
			for (final Money money : map.keySet())
			{
				toPot[index++] = money;
				toPot[index++] = map.get(money);
			}

			Pot.addToPot(toPot);
			return true;
		}

		@Override
		public void render()
		{
			Card.drawTitle("You are assessed for street repairs");
			Card.drawSubTitle(StringRenderer.getMoneyString(40) + " per house, " + StringRenderer.getMoneyString(115) + " per hotel.");
		}
	};

	public static final Card CC_BEAUTY = new Card(CardType.COMMUNITY_CHEST)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			player.inventory.addMoney(Money.TEN, 1);
			return true;
		}

		@Override
		public void render()
		{
			Card.drawTitle("You have won second prize in a beauty contest");
			Card.drawSubTitle("Collect " + StringRenderer.getMoneyString(10));
		}
	};

	public static final Card CC_INHERIT = new Card(CardType.COMMUNITY_CHEST)
	{
		@Override
		protected boolean use(Player player, GuiGame gui, boolean doubles)
		{
			player.inventory.addMoney(Money.ONE_HUNDRED, 1);
			return true;
		}

		@Override
		public void render()
		{
			Card.drawTitle("You inherit " + StringRenderer.getMoneyString(100));
		}
	};

	public final CardType type;

	private Card(CardType type)
	{
		this.type = type;

		switch (type)
		{
			case CHANCE:
				Card.pileChance.add(this);
				Collections.shuffle(Card.pileChance);
				break;
			case COMMUNITY_CHEST:
				Card.pileCommunityChest.add(this);
				Collections.shuffle(Card.pileCommunityChest);
		}
	}

	public final void onCardUse(Player player, GuiGame gui, boolean doubles)
	{
		if (this.use(player, gui, doubles))
		{
			if (doubles)
			{
				gui.setIngameGui(new GuiTurn(gui, player, true));
			}
			else
			{
				Monopoly.getInstance().setNextPlayer();
			}
		}
	}

	protected abstract boolean use(Player player, GuiGame gui, boolean doubles);

	public abstract void render();

	private static void drawTitle(String s)
	{
		StringRenderer.drawCenteredSplitString(s, 10, Card.xSize - 10, Card.yStart, 20, 2, true, Color.BLACK);
	}

	private static void drawSubTitle(String s)
	{
		StringRenderer.drawCenteredSplitString(s, 10, Card.xSize - 10, Card.yStart + 44, 16, 2, true, Color.BLACK);
	}

	public static Card getCard(CardType type)
	{
		List<Card> newList;
		Card returnCard;

		switch (type)
		{
			case CHANCE:
				newList = new ArrayList<Card>();
				for (int i = 0; i < Card.pileChance.size(); i++)
				{
					final int cardID = (i + Card.pileChance.size() - 1) % Card.pileChance.size();
					newList.add(Card.pileChance.get(cardID));
				}

				returnCard = Card.pileChance.get(0);
				Card.pileChance = newList;
				return returnCard;
			case COMMUNITY_CHEST:
				newList = new ArrayList<Card>();
				for (int i = 0; i < Card.pileCommunityChest.size(); i++)
				{
					final int cardID = (i + Card.pileCommunityChest.size() - 1) % Card.pileCommunityChest.size();
					newList.add(Card.pileCommunityChest.get(cardID));
				}

				returnCard = Card.pileCommunityChest.get(0);
				Card.pileCommunityChest = newList;
				return returnCard;
		}

		return null;
	}

	public static enum CardType
	{
		CHANCE, COMMUNITY_CHEST;
	}
}
