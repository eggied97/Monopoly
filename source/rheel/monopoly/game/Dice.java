package rheel.monopoly.game;

import java.util.Random;

public class Dice
{
	private static int dice1 = 1;
	private static int dice2 = 1;

	public static int[] getRoll()
	{
		return new int[]{Dice.dice1, Dice.dice2};
	}

	private static void setRoll(int dice1, int dice2)
	{
		Dice.dice1 = dice1;
		Dice.dice2 = dice2;
	}

	public static void roll()
	{
		final Random rand = new Random();
		final int roll1 = rand.nextInt(6) + 1;
		final int roll2 = rand.nextInt(6) + 1;
		Dice.setRoll(roll1, roll2);
	}

	public static int getRollTotal()
	{
		return Dice.dice1 + Dice.dice2;
	}

	public static boolean hasDoubles()
	{
		return Dice.dice1 == Dice.dice2;
	}
}
