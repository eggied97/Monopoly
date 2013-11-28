package rheel.monopoly.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import rheel.monopoly.game.GameValues.MutableGameValues;
import rheel.monopoly.street.StreetColor;

public class GameParser
{
	public static GameValues parse(File file)
	{
		if (!file.getName().endsWith(".mpb"))
		{
			throw new ParseException("File extension must be .mpb");
		}

		final MutableGameValues gv = new MutableGameValues(file);

		try
		{
			final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			final List<String> lines = new ArrayList<String>();
			String line;

			while ((line = reader.readLine()) != null)
			{
				lines.add(line);
			}

			reader.close();
			GameParser.parse(gv, lines);
		}
		catch (final Exception e)
		{
			if (e instanceof ParseException)
			{
				throw (ParseException) e;
			}

			e.printStackTrace();
			throw new ParseException("An error occured during parsing: " + e.getMessage());
		}

		return gv.toImmutable();
	}

	private static void parse(MutableGameValues gv, List<String> lines)
	{
		ParseState state = ParseState.DEFAULT;
		String lastLine = "";

		for (String s : lines)
		{
			s = s.trim();

			if (!(s.startsWith("##") || s.isEmpty()))
			{
				if (s.equals("}"))
				{
					state = ParseState.DEFAULT;
				}

				switch (state)
				{
					case BOARD_COLORS:
						gv.streets.put(StreetColor.valueOf(s.split("=")[0]), GameParser.parseString(s.split("=")[1]));
						break;
					case BOARD_NAMES:
						gv.names.put(StreetColor.valueOf(s.split("=")[0]), GameParser.parseStrings(s.split("=")[1]));
						break;
					case DEFAULT:
						if (s.equals("{"))
						{
							state = ParseState.valueOf(lastLine.toUpperCase().replace('.', '_').substring(0, lastLine.length() - 1));
						}
						else
						{
							final String[] split = s.split("=");

							if (split[0].equals("name"))
							{
								gv.name = GameParser.parseString(split[1]);
							}
							else if (split[0].equals("currency"))
							{
								gv.currency = GameParser.parseString(split[1]);
								if (gv.currency.startsWith("\\u"))
								{
									final char c = (char) (int) Integer.valueOf(gv.currency.substring(2));
									gv.currency = String.valueOf(c);
								}
							}
							else if (split[0].equals("multiplier"))
							{
								gv.multiplier = Integer.valueOf(split[1]);
							}
							else if (split[0].equals("double_if_on_go"))
							{
								gv.double_go = Boolean.parseBoolean(split[1]);
							}
							else if (split[0].equals("free_parking_money"))
							{
								gv.free_parking_pot = Boolean.parseBoolean(split[1]);
							}
							else if (split[0].equals("buy_before_roll"))
							{
								gv.buy_before_roll = Boolean.parseBoolean(split[1]);
							}
							else if (split[0].equals("money"))
							{
								final int[] array = GameParser.parseIntegers(split[1]);

								if (array.length != 7)
								{
									throw new ParseException("Invalid money array lenght");
								}

								gv.moneyStart = array;
							}
						}
						break;
				}
			}

			lastLine = s;
		}
	}

	private static int[] parseIntegers(String s)
	{
		if (!(s.startsWith("[") && s.endsWith("]")))
		{
			throw new ParseException("Invalid Array");
		}

		final List<Integer> list = new ArrayList<Integer>();
		final String[] array = s.substring(1, s.length() - 1).split(",");

		for (String str : array)
		{
			str = str.trim();
			list.add(Integer.parseInt(str));
		}

		final int[] result = new int[list.size()];

		for (int i = 0; i < list.size(); i++)
		{
			result[i] = list.get(i);
		}

		return result;
	}

	private static List<String> parseStrings(String s)
	{
		if (!(s.startsWith("[") && s.endsWith("]")))
		{
			throw new ParseException("Invalid Array");
		}

		final List<String> list = new ArrayList<String>();
		final String[] array = s.substring(1, s.length() - 1).split(",");

		for (String str : array)
		{
			str = str.trim();
			list.add(GameParser.parseString(str));
		}

		return list;
	}

	private static String parseString(String s)
	{
		final StringBuilder sb = new StringBuilder();
		boolean add = false;

		for (int i = 0; i < s.length(); i++)
		{
			if (s.charAt(i) == '"')
			{
				if (i > 0 && s.charAt(i - 1) != '\\' || i == 0)
				{
					add = !add;
				}
				else
				{
					sb.append(s.charAt(i));
				}
			}
			else
			{
				sb.append(s.charAt(i));
			}
		}

		if (add)
		{
			throw new ParseException("Invalid String: " + s);
		}

		return sb.toString();
	}

	private static enum ParseState
	{
		BOARD_COLORS, BOARD_NAMES, DEFAULT;
	}
}
