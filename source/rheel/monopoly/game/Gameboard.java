package rheel.monopoly.game;

import rheel.monopoly.place.Place;
import rheel.monopoly.place.Place.Position;
import rheel.monopoly.place.PlaceChance;
import rheel.monopoly.place.PlaceCommunityChest;
import rheel.monopoly.place.PlaceFreeParking;
import rheel.monopoly.place.PlaceGo;
import rheel.monopoly.place.PlaceGoToJail;
import rheel.monopoly.place.PlaceJail;
import rheel.monopoly.place.PlaceProperty;
import rheel.monopoly.place.PlaceStation;
import rheel.monopoly.place.PlaceTax;
import rheel.monopoly.place.PlaceUtility;
import rheel.monopoly.street.Street;
import rheel.monopoly.street.StreetColor;

public final class Gameboard
{
	public final Place[] places = new Place[40];
	public final GameValues gameValues;

	public Gameboard(GameValues values)
	{
		this.gameValues = values;

		this.addPlace(0, new PlaceGo(this, 0, new Position(893, 893, 0)));
		this.addPlace(1, this.getPlaceProperty(values, 1, StreetColor.purple, 0, 60, new Position(808, 893, 0)));
		this.addPlace(2, new PlaceCommunityChest(this, 2, new Position(723, 893, 0)));
		this.addPlace(3, this.getPlaceProperty(values, 3, StreetColor.purple, 1, 60, new Position(638, 893, 0)));
		this.addPlace(4, new PlaceTax(200, this, 0, new Position(553, 893, 0)));
		this.addPlace(5, this.getPlaceStation(values, 0, new Position(468, 893, 0)));
		this.addPlace(6, this.getPlaceProperty(values, 6, StreetColor.light_blue, 0, 100, new Position(383, 893, 0)));
		this.addPlace(7, new PlaceChance(this, 7, new Position(298, 893, 0)));
		this.addPlace(8, this.getPlaceProperty(values, 8, StreetColor.light_blue, 1, 100, new Position(213, 893, 0)));
		this.addPlace(9, this.getPlaceProperty(values, 9, StreetColor.light_blue, 2, 120, new Position(128, 893, 0)));
		this.addPlace(10, new PlaceJail(this, 10, new Position(0, 893, 0)));
		this.addPlace(11, this.getPlaceProperty(values, 11, StreetColor.pink, 0, 140, new Position(0, 808, 90)));
		this.addPlace(12, this.getPlaceUtility(values, 0, new Position(0, 723, 90)));
		this.addPlace(13, this.getPlaceProperty(values, 13, StreetColor.pink, 1, 140, new Position(0, 638, 90)));
		this.addPlace(14, this.getPlaceProperty(values, 14, StreetColor.pink, 2, 160, new Position(0, 553, 90)));
		this.addPlace(15, this.getPlaceStation(values, 1, new Position(0, 468, 90)));
		this.addPlace(16, this.getPlaceProperty(values, 16, StreetColor.orange, 0, 180, new Position(0, 383, 90)));
		this.addPlace(17, new PlaceCommunityChest(this, 1, new Position(0, 298, 90)));
		this.addPlace(18, this.getPlaceProperty(values, 18, StreetColor.orange, 1, 180, new Position(0, 213, 90)));
		this.addPlace(19, this.getPlaceProperty(values, 19, StreetColor.orange, 2, 200, new Position(0, 128, 90)));
		this.addPlace(20, new PlaceFreeParking(this, 20, new Position(0, 0, 0)));
		this.addPlace(21, this.getPlaceProperty(values, 21, StreetColor.red, 0, 220, new Position(128, 0, 180)));
		this.addPlace(22, new PlaceChance(this, 22, new Position(213, 0, 180)));
		this.addPlace(23, this.getPlaceProperty(values, 23, StreetColor.red, 1, 220, new Position(298, 0, 180)));
		this.addPlace(24, this.getPlaceProperty(values, 24, StreetColor.red, 2, 240, new Position(383, 0, 180)));
		this.addPlace(25, this.getPlaceStation(values, 2, new Position(468, 0, 180)));
		this.addPlace(26, this.getPlaceProperty(values, 26, StreetColor.yellow, 0, 260, new Position(553, 0, 180)));
		this.addPlace(27, this.getPlaceProperty(values, 27, StreetColor.yellow, 1, 260, new Position(638, 0, 180)));
		this.addPlace(28, this.getPlaceUtility(values, 1, new Position(723, 0, 180)));
		this.addPlace(29, this.getPlaceProperty(values, 29, StreetColor.yellow, 2, 280, new Position(808, 0, 180)));
		this.addPlace(30, new PlaceGoToJail(this, 30, new Position(893, 0, 0)));
		this.addPlace(31, this.getPlaceProperty(values, 31, StreetColor.green, 0, 300, new Position(893, 128, 270)));
		this.addPlace(32, this.getPlaceProperty(values, 32, StreetColor.green, 1, 300, new Position(893, 213, 270)));
		this.addPlace(33, new PlaceCommunityChest(this, 33, new Position(893, 298, 270)));
		this.addPlace(34, this.getPlaceProperty(values, 34, StreetColor.green, 2, 320, new Position(893, 383, 270)));
		this.addPlace(35, this.getPlaceStation(values, 3, new Position(893, 468, 270)));
		this.addPlace(36, new PlaceChance(this, 36, new Position(893, 553, 270)));
		this.addPlace(37, this.getPlaceProperty(values, 37, StreetColor.dark_blue, 0, 350, new Position(893, 638, 270)));
		this.addPlace(38, new PlaceTax(100, this, 1, new Position(893, 723, 270)));
		this.addPlace(39, this.getPlaceProperty(values, 39, StreetColor.dark_blue, 1, 400, new Position(893, 808, 270)));

		Street.initializeStreets(this);
	}

	private PlaceUtility getPlaceUtility(GameValues v, int id, Position pos)
	{
		return new PlaceUtility(this, v.names.get(StreetColor.utilities).get(id), id, pos);
	}

	private PlaceStation getPlaceStation(GameValues v, int id, Position pos)
	{
		return new PlaceStation(this, id, v.names.get(StreetColor.stations).get(id), pos);
	}

	private PlaceProperty getPlaceProperty(GameValues v, int placeID, StreetColor c, int idInColor, int cost, Position pos)
	{
		return new PlaceProperty(v.names.get(c).get(idInColor), placeID, v.streets.get(c), c, idInColor, pos, this, cost);
	}

	private void addPlace(int id, Place place)
	{
		this.places[id] = place;
	}
}
