package rheel.monopoly.gui;

import java.awt.Color;

import org.lwjgl.input.Mouse;

import rheel.monopoly.game.Monopoly;
import rheel.monopoly.game.MouseLoc;
import rheel.monopoly.renderer.StringRenderer;

public class GuiButton extends Gui
{
	private final Gui parent;
	public final int id;

	private final int x;
	private final int y;
	private final int w;
	private final int h;
	private final int size;

	public String text;
	public boolean disabled = false;
	public boolean drawButton = true;

	private boolean lastState = false;

	public GuiButton(int id, Gui gui, int x, int y, int width, int height, String text)
	{
		this(id, gui, x, y, width, height, 13, text);
	}

	public GuiButton(int id, Gui gui, int x, int y, int width, int height, int size, String text)
	{
		this.parent = gui;
		this.id = id;

		this.x = x;
		this.y = y;
		this.w = width;
		this.h = height;
		this.text = text;
		this.size = size;
	}

	public GuiButton translate(int x, int y)
	{
		return new GuiButton(this.id, this.parent, this.x + x, this.y + y, this.w, this.h, this.size, this.text).setDisabled(this.disabled).setDrawButton(this.drawButton);
	}

	public GuiButton setDisabled(boolean disabled)
	{
		this.disabled = disabled;
		return this;
	}

	public GuiButton setDrawButton(boolean drawButton)
	{
		this.drawButton = drawButton;
		return this;
	}

	@Override
	public void drawGui()
	{
		if (this.drawButton)
		{
			final String type = this.disabled ? "disabled" : this.isMouseInArea() ? Mouse.isButtonDown(0) ? "in" : "glow" : "out";

			Monopoly.getInstance().getEngine().bindTexture("/textures/button_" + type + "_new.png");

			Monopoly.getInstance().getEngine().drawTexturedRectangle(this.x, this.y, 0, 0, 5, 5);
			Monopoly.getInstance().getEngine().drawTexturedRectangle(this.x + this.w - 5, this.y, 59, 0, 5, 5);
			Monopoly.getInstance().getEngine().drawTexturedRectangle(this.x, this.y + this.h - 5, 0, 59, 5, 5);
			Monopoly.getInstance().getEngine().drawTexturedRectangle(this.x + this.w - 5, this.y + this.h - 5, 59, 59, 5, 5);
			Monopoly.getInstance().getEngine().drawTexturedRectangle(this.x + 5, this.y, 6, 0, this.w - 10, 5, 52, 5);
			Monopoly.getInstance().getEngine().drawTexturedRectangle(this.x, this.y + 5, 0, 6, 5, this.h - 10);
			Monopoly.getInstance().getEngine().drawTexturedRectangle(this.x + this.w - 5, this.y + 5, 59, 5, 5, this.h - 10);
			Monopoly.getInstance().getEngine().drawTexturedRectangle(this.x + 5, this.y + this.h - 5, 5, 59, this.w - 10, 5, 52, 5);
			Monopoly.getInstance().getEngine().drawTexturedRectangle(this.x + 5, this.y + 5, 6, 6, this.w - 10, this.h - 10, 52, 52);

			StringRenderer.drawCenteredString(this.text, this.x + this.w - this.w / 2, this.y / 2 + (this.y + this.h) / 2 + (this.isMouseInArea() && Mouse.isButtonDown(0) && !this.disabled ? -1 : -2) - this.size / 2, this.size, true, Color.WHITE);
		}

		if (Mouse.isButtonDown(0) && this.isMouseInArea() && !this.disabled)
		{
			this.parent.onButtonPressed(this);
			this.lastState = true;
		}
		else if (this.lastState && this.isMouseInArea() && !this.disabled)
		{
			this.parent.onButtonReleased(this);
			this.lastState = false;
		}
	}

	public boolean isMouseInArea()
	{
		final int mouseX = MouseLoc.getX();
		final int mouseY = MouseLoc.getY();

		return mouseX >= this.x && mouseX <= this.x + this.w && mouseY >= this.y && mouseY <= this.y + this.h;
	}

	@Override
	public String toString()
	{
		return this.getClass().getName() + "{id=" + this.id + ", x=" + this.x + ", y=" + this.y + ", " + this.disabled + " : " + this.text + "}";
	}
}
