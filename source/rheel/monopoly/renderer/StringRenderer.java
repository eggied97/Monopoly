package rheel.monopoly.renderer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rheel.monopoly.game.GameValues;
import rheel.monopoly.game.Monopoly;

public final class StringRenderer
{
	public static final String getMoneyString(GameValues values, int value)
	{
		return values.currency + "<n>" + String.valueOf(value * values.multiplier);
	}

	public static final String getMoneyString(int value)
	{
		return StringRenderer.getMoneyString(Monopoly.getInstance().getBoard().gameValues, value);
	}

	public static final void drawString(String s, int x, int y, int size, boolean bold, Color color)
	{
		s = s.replace("<s>", "").replace("<n>", " ");
		Monopoly.getInstance().getEngine().drawString(s, x, y, size, bold, color);
	}

	public static final void drawSplitString(String s, int x, int y, int size, int distance, boolean bold, Color color, int maxLenght)
	{
		if (maxLenght <= 0)
		{
			return;
		}

		final List<String> str = StringRenderer.getSplitString(s, size, bold, maxLenght);

		for (int i = 0; i < str.size(); i++)
		{
			StringRenderer.drawString(str.get(i), x, y + size * i, size, bold, color);
		}
	}

	public static final void drawCenteredString(String s, int xCenter, int y, int size, boolean bold, Color color)
	{
		StringRenderer.drawString(s, xCenter - StringRenderer.getStringWidth(s, size, bold) / 2, y, size, bold, color);
	}

	public static final int drawCenteredSplitString(String s, int xMin, int xMax, int y, int size, int distance, boolean bold, Color color)
	{
		final int maxLenght = xMax - xMin;

		if (maxLenght <= 0)
		{
			return 0;
		}

		final List<String> str = StringRenderer.getSplitString(s, size, bold, maxLenght);

		for (int i = 0; i < str.size(); i++)
		{
			StringRenderer.drawCenteredString(str.get(i), (xMax - xMin) / 2 + xMin, y + (size + distance) * i, size, bold, color);
		}

		return str.size();
	}

	public static final List<String> getSplitString(String s, int size, boolean bold, int maxLenght)
	{
		final List<String> list = new ArrayList<String>();

		if (StringRenderer.getStringWidth(s, size, bold) <= maxLenght)
		{
			list.add(s);
			return list;
		}

		final String[] split = StringRenderer.getSplitString(s);
		StringBuilder[] sb = new StringBuilder[]{new StringBuilder(split[0])};
		int index = 0;

		for (int i = 1; i < split.length; i++)
		{
			final String str = split[i];

			if (StringRenderer.getStringWidth(sb[index].toString() + " " + str, size, bold) > maxLenght)
			{
				sb = Arrays.copyOf(sb, index++ + 2);
				sb[index] = new StringBuilder(str);
			}
			else
			{
				sb[index].append(str.startsWith("<s>") ? str.substring(3) : " " + str);
			}
		}

		for (final StringBuilder strBuilder : sb)
		{
			list.add(strBuilder.toString());
		}

		return list;
	}

	private static final String[] getSplitString(String s)
	{
		final String[] s1 = s.split(" ");
		final List<String[]> s2 = new ArrayList<String[]>();
		int lenght = 0;

		for (final String str : s1)
		{
			final String[] s3 = str.split("<s>");
			s2.add(s3);
			lenght += s3.length;
		}

		final String[] s4 = new String[lenght];
		int index = 0;

		for (final String[] s5 : s2)
		{
			for (int i = 0; i < s5.length; i++)
			{
				final String str = s5[i];
				s4[index++] = i > 0 ? "<s>" + str : str;
			}
		}

		return s4;
	}

	public static final int getCharWidth(char c, int size, boolean bold)
	{
		return Monopoly.getInstance().getEngine().getStrRenderer(size, bold).getFontMetrics().charWidth(c);
	}

	public static final int getStringWidth(String s, int size, boolean bold)
	{
		s = s.replace("<s>", "").replace("<n>", " ");
		return Monopoly.getInstance().getEngine().getStrRenderer(size, bold).getFontMetrics().stringWidth(s);
	}

	public static final int getCenteredStringPos(String string, int xCentre, int size, boolean bold)
	{
		return xCentre - StringRenderer.getStringWidth(string, size, bold) / 2;
	}
}
