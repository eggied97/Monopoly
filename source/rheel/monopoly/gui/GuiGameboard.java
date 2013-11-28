package rheel.monopoly.gui;

import java.awt.Color;
import java.util.List;

import org.lwjgl.opengl.GL11;

import rheel.monopoly.game.Gameboard;
import rheel.monopoly.game.Monopoly;
import rheel.monopoly.game.Player;
import rheel.monopoly.place.Place;
import rheel.monopoly.place.PlaceChance;
import rheel.monopoly.place.PlaceCommunityChest;
import rheel.monopoly.place.PlaceCorner;
import rheel.monopoly.place.PlaceJail;
import rheel.monopoly.place.PlaceOwnable;
import rheel.monopoly.place.PlaceProperty;
import rheel.monopoly.place.PlaceSpecial;
import rheel.monopoly.place.PlaceStation;
import rheel.monopoly.place.PlaceTax;
import rheel.monopoly.place.PlaceUtility;
import rheel.monopoly.renderer.StringRenderer;

public class GuiGameboard extends Gui
{
	private final int x;
	private final int y;

	private final Gameboard board;

	public GuiGameboard(int x, int y, Gameboard board)
	{
		this.x = x;
		this.y = y;
		this.board = board;
	}

	@Override
	public void drawGui()
	{
		for (final Place place : this.board.places)
		{
			if (place instanceof PlaceProperty)
			{
				this.drawProperty((PlaceProperty) place);
			}
			else if (place instanceof PlaceStation)
			{
				this.drawStation((PlaceStation) place);
			}
			else if (place instanceof PlaceUtility)
			{
				this.drawUtility((PlaceUtility) place);
			}
			else if (place instanceof PlaceSpecial)
			{
				this.drawSpecialPlace((PlaceSpecial) place);
			}
			else if (place instanceof PlaceCorner)
			{
				this.drawCorner((PlaceCorner) place);
			}

			if (place instanceof PlaceOwnable)
			{
				this.drawPrice((PlaceOwnable) place);
			}
		}

		this.drawRestBoard();
	}

	private void drawProperty(PlaceProperty place)
	{
		this.rotateAndPosition(place.position.x, place.position.y, place.position.rot);

		Monopoly.getInstance().getEngine().bindTexture("/textures/board/place.png");
		Monopoly.getInstance().getEngine().drawTexturedRectangle(0, 0, 0, 0, 85, 128);
		Monopoly.getInstance().getEngine().drawTexturedRectangle(2, 2, 85, place.getColor().ordinal() * 24, 81, 24);
		Monopoly.getInstance().getEngine().drawTexturedRectangle(2, 26, 0, 0, 81, 2);

		if (place.getHouses() == 5)
		{
			Monopoly.getInstance().getEngine().drawTexturedRectangle(31, 3, 233, 117, 20, 20);
		}
		else if (place.getHouses() < 5)
		{
			int x = (81 - 20 * place.getHouses()) / 2 + place.getHouses();

			for (int i = 0; i < place.getHouses(); i++)
			{
				Monopoly.getInstance().getEngine().drawTexturedRectangle(x, 3, 233, 96, 20, 20);
				x += 20;
			}
		}

		final int i = StringRenderer.drawCenteredSplitString(place.getName(), 3, 82, 30, 11, 0, true, java.awt.Color.BLACK);
		StringRenderer.drawCenteredSplitString(place.getSubName(), 3, 82, 30 + 11 * i, 12, 0, true, java.awt.Color.BLACK);

		this.drawPlayersOnPlace(place);
		this.reset();
	}

	private void drawUtility(PlaceUtility place)
	{
		this.rotateAndPosition(place.position.x, place.position.y, place.position.rot);

		Monopoly.getInstance().getEngine().bindTexture("/textures/board/place.png");
		Monopoly.getInstance().getEngine().drawTexturedRectangle(0, 0, 0, 0, 85, 128);

		final int srcX = place.id == 0 ? 174 : place.id == 1 ? 1 : 255;
		final int srcY = place.id == 0 ? 162 : place.id == 1 ? 192 : 255;
		final int x = place.id == 0 ? 13 : place.id == 1 ? 9 : 255;
		final int y = place.id == 0 ? 34 : place.id == 1 ? 35 : 255;
		final int width = place.id == 0 ? 63 : place.id == 1 ? 65 : 0;
		final int height = place.id == 0 ? 73 : place.id == 1 ? 55 : 0;

		Monopoly.getInstance().getEngine().drawTexturedRectangle(x, y, srcX, srcY, width, height);
		StringRenderer.drawCenteredSplitString(place.getName(), 4, 81, 3, 12, 0, true, Color.BLACK);

		this.drawPlayersOnPlace(place);
		this.reset();
	}

	private void drawStation(PlaceStation place)
	{
		this.rotateAndPosition(place.position.x, place.position.y, place.position.rot);

		Monopoly.getInstance().getEngine().bindTexture("/textures/board/place.png");
		Monopoly.getInstance().getEngine().drawTexturedRectangle(0, 0, 0, 0, 85, 128);
		Monopoly.getInstance().getEngine().drawTexturedRectangle(11, 40, 0, 132, 63, 48);

		StringRenderer.drawCenteredSplitString(place.getName(), 3, 82, 6, 14, 0, true, Color.BLACK);

		this.drawPlayersOnPlace(place);
		this.reset();
	}

	private void drawSpecialPlace(PlaceSpecial place)
	{
		this.rotateAndPosition(place.position.x, place.position.y, place.position.rot);

		Monopoly.getInstance().getEngine().bindTexture("/textures/board/place.png");
		Monopoly.getInstance().getEngine().drawTexturedRectangle(0, 0, 0, 0, 85, 128);

		if (place instanceof PlaceChance)
		{
			Monopoly.getInstance().getEngine().drawTexturedRectangle(18, 12, 170, 46, 51, 106);
		}
		else if (place instanceof PlaceCommunityChest)
		{
			Monopoly.getInstance().getEngine().drawTexturedRectangle(10, 45, 79, 197, 63, 51);
		}
		else if (place instanceof PlaceTax)
		{
			if (((PlaceTax) place).id == 0)
			{
				Monopoly.getInstance().getEngine().drawTexturedRectangle(36, 61, 234, 77, 15, 14);
			}
			else if (((PlaceTax) place).id == 1)
			{
				Monopoly.getInstance().getEngine().drawTexturedRectangle(18, 42, 172, 0, 51, 44);
			}
		}

		this.drawPlayersOnPlace(place);
		this.reset();
	}

	private void drawCorner(PlaceCorner corner)
	{
		final int posX = corner.position.x;
		final int posY = corner.position.y;
		final int angle = corner.position.rot;

		if (angle != 0)
		{
			throw new RuntimeException();
		}

		GL11.glPushMatrix();
		GL11.glTranslatef(this.x + posX, this.y + posY, 0);
		GL11.glRotatef(angle, 0, 0, 1);

		Monopoly.getInstance().getEngine().bindTexture(corner.getTexure());
		Monopoly.getInstance().getEngine().drawTexturedRectangle(0, 0, 0, 0, 128, 128);

		drawPlayersOnPlace(corner);
		GL11.glPopMatrix();
	}

	private void drawPrice(PlaceOwnable place)
	{
		this.rotateAndPosition(place.position.x, place.position.y, place.position.rot);
		StringRenderer.drawCenteredString(StringRenderer.getMoneyString(this.board.gameValues, place.getPrice()), 43, 113, 11, false, java.awt.Color.BLACK);
		this.reset();
	}

	private void drawPlayersOnPlace(Place place)
	{
		final List<Player> playersOnPlace = place.getPlayersOnPlace();
		final int size = playersOnPlace.size();

		Monopoly.getInstance().getEngine().bindTexture("/textures/players.png");

		if (place instanceof PlaceCorner)
		{
//			if (place instanceof PlaceJail)
//			{
//				final List<Player> playersInJail = ((PlaceJail) place).getPlayersInJail(playersOnPlace);
//				final List<Player> playersVisiting = ((PlaceJail) place).getPlayersVisiting(playersOnPlace);
//				
//				for(int i = 0; i < playersInJail.size(); i++)
//				{
//					int x = 29 + ((i % 3) * 32);
//					int y = 2 + ((i / 3) * 21);
//					
//					Monopoly.getInstance().getEngine().drawTexturedRectangle(x, y, playersInJail.get(i).texture.x, playersInJail.get(i).texture.y, 32, 32);
//				}
//			}
//			else
//			{
				for(int i = 0; i < playersOnPlace.size(); i++)
				{
					int x = 7 + ((i % 3) * 42);
					int y = 7 + ((i / 3) * 42);
					
					Monopoly.getInstance().getEngine().drawTexturedRectangle(x, y, playersOnPlace.get(i).texture.x, playersOnPlace.get(i).texture.y, 32, 32);
				}
//			}
		}
		else
		{
			switch (size)
			{
				case 1:
					Monopoly.getInstance().getEngine().drawTexturedRectangle(27, 59, playersOnPlace.get(0).texture.x, playersOnPlace.get(0).texture.y, 32, 32);
					break;
				case 2:
					Monopoly.getInstance().getEngine().drawTexturedRectangle(7, 59, playersOnPlace.get(0).texture.x, playersOnPlace.get(0).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(46, 59, playersOnPlace.get(1).texture.x, playersOnPlace.get(1).texture.y, 32, 32);
					break;
				case 3:
					Monopoly.getInstance().getEngine().drawTexturedRectangle(7, 38, playersOnPlace.get(0).texture.x, playersOnPlace.get(0).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(46, 38, playersOnPlace.get(1).texture.x, playersOnPlace.get(1).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(27, 79, playersOnPlace.get(2).texture.x, playersOnPlace.get(2).texture.y, 32, 32);
					break;
				case 4:
					Monopoly.getInstance().getEngine().drawTexturedRectangle(7, 38, playersOnPlace.get(0).texture.x, playersOnPlace.get(0).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(46, 38, playersOnPlace.get(1).texture.x, playersOnPlace.get(1).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(7, 79, playersOnPlace.get(2).texture.x, playersOnPlace.get(2).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(46, 79, playersOnPlace.get(3).texture.x, playersOnPlace.get(3).texture.y, 32, 32);
					break;
				case 5:
					Monopoly.getInstance().getEngine().drawTexturedRectangle(7, 7, playersOnPlace.get(0).texture.x, playersOnPlace.get(0).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(46, 7, playersOnPlace.get(1).texture.x, playersOnPlace.get(1).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(7, 48, playersOnPlace.get(2).texture.x, playersOnPlace.get(2).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(46, 48, playersOnPlace.get(3).texture.x, playersOnPlace.get(3).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(27, 89, playersOnPlace.get(4).texture.x, playersOnPlace.get(4).texture.y, 32, 32);
					break;
				case 6:
					Monopoly.getInstance().getEngine().drawTexturedRectangle(7, 7, playersOnPlace.get(0).texture.x, playersOnPlace.get(0).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(46, 7, playersOnPlace.get(1).texture.x, playersOnPlace.get(1).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(7, 48, playersOnPlace.get(2).texture.x, playersOnPlace.get(2).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(46, 48, playersOnPlace.get(3).texture.x, playersOnPlace.get(3).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(7, 89, playersOnPlace.get(4).texture.x, playersOnPlace.get(4).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(46, 89, playersOnPlace.get(5).texture.x, playersOnPlace.get(5).texture.y, 32, 32);
					break;
				case 7:
					Monopoly.getInstance().getEngine().drawTexturedRectangle(7, 7, playersOnPlace.get(0).texture.x, playersOnPlace.get(0).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(46, 7, playersOnPlace.get(1).texture.x, playersOnPlace.get(1).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(7, 48, playersOnPlace.get(2).texture.x, playersOnPlace.get(2).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(46, 48, playersOnPlace.get(3).texture.x, playersOnPlace.get(3).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(7, 89, playersOnPlace.get(4).texture.x, playersOnPlace.get(4).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(46, 89, playersOnPlace.get(5).texture.x, playersOnPlace.get(5).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(27, 27, playersOnPlace.get(6).texture.x, playersOnPlace.get(6).texture.y, 32, 32);
					break;
				case 8:
					Monopoly.getInstance().getEngine().drawTexturedRectangle(7, 7, playersOnPlace.get(0).texture.x, playersOnPlace.get(0).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(46, 7, playersOnPlace.get(1).texture.x, playersOnPlace.get(1).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(7, 48, playersOnPlace.get(2).texture.x, playersOnPlace.get(2).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(46, 48, playersOnPlace.get(3).texture.x, playersOnPlace.get(3).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(7, 89, playersOnPlace.get(4).texture.x, playersOnPlace.get(4).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(46, 89, playersOnPlace.get(5).texture.x, playersOnPlace.get(5).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(27, 27, playersOnPlace.get(6).texture.x, playersOnPlace.get(6).texture.y, 32, 32);
					Monopoly.getInstance().getEngine().drawTexturedRectangle(27, 68, playersOnPlace.get(7).texture.x, playersOnPlace.get(7).texture.y, 32, 32);
					break;
			}
		}
	}

	private void drawRestBoard()
	{
		Monopoly.getInstance().getEngine().bindTexture("/textures/back_board.png");
		Monopoly.getInstance().getEngine().drawTexturedRectangle(this.x + 128, this.y + 128, 0, 0, 765, 765);
		Monopoly.getInstance().getEngine().bindTexture("/textures/logo.png");
		Monopoly.getInstance().getEngine().drawTexturedRectangle(this.x + 320, this.y + 185, 0, 0, 382, 142);
	}

	private void rotateAndPosition(int posX, int posY, int angle)
	{
		if (angle != 0 && angle != 90 && angle != 180 && angle != 270)
		{
			throw new RuntimeException();
		}

		posX += angle == 90 ? 128 : angle == 180 ? 85 : 0;
		posY += angle == 180 ? 128 : angle == 270 ? 85 : 0;

		GL11.glPushMatrix();
		GL11.glTranslatef(this.x + posX, this.y + posY, 0);
		GL11.glRotatef(angle, 0, 0, 1);
	}

	private void reset()
	{
		GL11.glPopMatrix();
	}
}
