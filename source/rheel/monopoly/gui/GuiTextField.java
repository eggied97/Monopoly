package rheel.monopoly.gui;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import rheel.monopoly.game.Monopoly;
import rheel.monopoly.game.MouseLoc;
import rheel.monopoly.renderer.StringRenderer;

public class GuiTextField extends Gui
{
	public final int id;

	private final int x;
	private final int y;
	private final int w;
	private final int h;
	private final int size;

	public String text;
	public boolean disabled = false;
	public boolean active;

	private boolean lastState;

	private int cursorCounter = 0;
	private final int cursorLenght = 30;
	private boolean cursor = false;

	public GuiTextField tabField;

	public GuiTextField(int id, int x, int y, int width, int height, String text)
	{
		this(id, x, y, width, height, 13, text);
	}

	public GuiTextField(int id, int x, int y, int width, int height, int size, String text)
	{
		this.id = id;
		this.x = x;
		this.y = y;
		this.w = width;
		this.h = height;
		this.text = text;
		this.size = size;
	}

	public GuiTextField translate(int x, int y)
	{
		return new GuiTextField(this.id, this.x + x, this.y + y, this.w, this.h, this.size, this.text);
	}

	@Override
	public void drawGui()
	{
		Monopoly.getInstance().getEngine().bindTexture("/textures/textfield.png");

		Monopoly.getInstance().getEngine().drawTexturedRectangle(this.x, this.y, 0, 0, 5, 5);
		Monopoly.getInstance().getEngine().drawTexturedRectangle(this.x + this.w - 5, this.y, 8, 0, 5, 5);
		Monopoly.getInstance().getEngine().drawTexturedRectangle(this.x + this.w - 5, this.y + this.h - 5, 8, 8, 5, 5);
		Monopoly.getInstance().getEngine().drawTexturedRectangle(this.x, this.y + this.h - 5, 0, 8, 5, 5);

		for (int i = this.x + 5; i < this.x + this.w - 5; i++)
		{
			Monopoly.getInstance().getEngine().drawTexturedRectangle(i, this.y, 6, 0, 1, 5);
			Monopoly.getInstance().getEngine().drawTexturedRectangle(i, this.y + this.h - 5, 6, 8, 1, 5);
		}

		for (int i = this.y + 5; i < this.y + this.h - 5; i++)
		{
			Monopoly.getInstance().getEngine().drawTexturedRectangle(this.x, i, 0, 6, 5, 1);
			Monopoly.getInstance().getEngine().drawTexturedRectangle(this.x + this.w - 5, i, 8, 6, 5, 1);
		}

		for (int i = this.x + 5; i < this.x + this.w - 5; i++)
		{
			for (int j = this.y + 5; j < this.y + this.h - 5; j++)
			{
				Monopoly.getInstance().getEngine().drawTexturedRectangle(i, j, 6, 6, 1, 1);
			}
		}

		if (!this.disabled)
		{
			if (Mouse.isButtonDown(0) && this.isMouseInArea())
			{
				this.lastState = true;
			}
			else if (this.isMouseInArea() && !Mouse.isButtonDown(0) && this.lastState)
			{
				this.lastState = false;
				this.active = !this.active;

				if (!this.active)
				{
					this.cursorCounter = 0;
					this.cursor = false;
				}
			}

			if (this.active)
			{
				this.cursorCounter++;
				if (this.cursorCounter == this.cursorLenght)
				{
					this.cursorCounter = 0;
					this.cursor = !this.cursor;
				}

				Keyboard.enableRepeatEvents(true);

				while (Keyboard.next())
				{
					if (Keyboard.getEventKeyState())
					{
						if (Keyboard.getEventKey() == Keyboard.KEY_RETURN)
						{
							this.active = false;
						}
						else if (Keyboard.getEventKey() == Keyboard.KEY_BACK)
						{
							if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
							{
								this.text = "";
							}
							else if (this.text.length() != 0)
							{
								this.text = this.text.substring(0, this.text.length() - 1);
							}
						}
						else if (Keyboard.getEventKey() == Keyboard.KEY_TAB)
						{
							if (this.tabField != null)
							{
								this.active = false;
								this.cursorCounter = 0;
								this.cursor = false;
								this.tabField.active = true;
							}
						}
						else if (Keyboard.getEventCharacter() != Keyboard.CHAR_NONE)
						{
							final char c = Keyboard.getEventCharacter();
							final int width = StringRenderer.getStringWidth(this.text + c, this.size, true);

							if (width + 12 <= this.w)
							{
								this.text += c;
							}
						}
					}
				}

				Keyboard.enableRepeatEvents(false);
			}

			if (Mouse.isButtonDown(0) && !this.isMouseInArea())
			{
				this.active = false;
				this.cursorCounter = 0;
				this.cursor = false;
			}
		}

		StringRenderer.drawString(this.text + (this.cursor ? "|" : ""), this.x + 6, this.y + 2, this.size, true, Color.BLACK);
	}

	public boolean isMouseInArea()
	{
		final int mouseX = MouseLoc.getX();
		final int mouseY = MouseLoc.getY();

		return mouseX >= this.x && mouseX <= this.x + this.w && mouseY >= this.y && mouseY <= this.y + this.h;
	}
}
