package rheel.monopoly.street;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import rheel.monopoly.game.Gameboard;
import rheel.monopoly.game.Player;
import rheel.monopoly.place.Place;
import rheel.monopoly.place.PlaceProperty;

public class Street
{
	public static Street PURPLE;
	public static Street LIGHT_BLUE;
	public static Street PINK;
	public static Street ORANGE;
	public static Street RED;
	public static Street YELLOW;
	public static Street GREEN;
	public static Street DARK_BLUE;

	public final PlaceProperty[] properties;
	public final StreetColor color;

	private Street(PlaceProperty[] properties, StreetColor color)
	{
		this.properties = properties;
		this.color = color;
	}

	public boolean hasPlayerStreet(Player player)
	{
		for (final PlaceProperty place : this.properties)
		{
			if (!player.inventory.getProperties().contains(place))
			{
				return false;
			}
		}

		return true;
	}

	public static void initializeStreets(Gameboard board)
	{
		try
		{
			for (final StreetColor color : StreetColor.values())
			{
				final List<PlaceProperty> places = new ArrayList<PlaceProperty>();

				for (final Place place : board.places)
				{
					if (place instanceof PlaceProperty && ((PlaceProperty) place).getColor().equals(color))
					{
						places.add((PlaceProperty) place);
					}
				}

				if (!places.isEmpty())
				{
					final PlaceProperty[] pps = new PlaceProperty[places.size()];

					for (int i = 0; i < places.size(); i++)
					{
						pps[i] = places.get(i);
					}

					final Field street = Street.class.getField(color.toString().toUpperCase());
					street.set(null, new Street(pps, color));
				}
			}
		}
		catch (final Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static Street[] array()
	{
		return new Street[]{Street.PURPLE, Street.LIGHT_BLUE, Street.PINK, Street.ORANGE, Street.RED, Street.YELLOW, Street.GREEN, Street.DARK_BLUE};
	}

	public static Street getStreet(PlaceProperty placeProperty)
	{
		for (final Street street : Street.array())
		{
			for (final PlaceProperty place : street.properties)
			{
				if (place.equals(placeProperty))
				{
					return street;
				}
			}
		}

		return null;
	}
}
