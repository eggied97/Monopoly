package rheel.monopoly.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import rheel.monopoly.game.Money;
import rheel.monopoly.game.Monopoly;

public abstract class GuiScreen extends Gui
{
	public static final float scale = 1.0F;

	public static GuiScreen currentGui;
	public final GuiBackground background = new GuiBackground();
	protected final List<GuiButton> buttonList = new ArrayList<GuiButton>();
	private static final List<GuiAddMoney> gamList = new ArrayList<GuiAddMoney>();
	private static final List<GuiSubtractMoney> gsmList = new ArrayList<GuiSubtractMoney>();
	private static final List<GuiAddMoney> gamQueue = new ArrayList<GuiAddMoney>();
	private static final List<GuiSubtractMoney> gsmQueue = new ArrayList<GuiSubtractMoney>();

	private int tick;

	public GuiScreen()
	{
		this.initGui();
	}

	protected void initGui()
	{

	}

	@Override
	public final void drawGui()
	{
		if (this.tick++ >= 30)
		{
			this.tick = 0;
			if (!GuiScreen.gamQueue.isEmpty())
			{
				GuiScreen.gamList.add(GuiScreen.gamQueue.get(0));
				GuiScreen.gamQueue.remove(0);
			}

			if (!GuiScreen.gsmQueue.isEmpty())
			{
				GuiScreen.gsmList.add(GuiScreen.gsmQueue.get(0));
				GuiScreen.gsmQueue.remove(0);
			}
		}

		this.background.drawGui();

		GL11.glScalef(GuiScreen.scale, GuiScreen.scale, GuiScreen.scale);
		GL11.glPushMatrix();
		this.draw();
		GL11.glPopMatrix();

		for (final GuiButton button : this.buttonList)
		{
			if (button != null)
			{
				button.drawGui();
			}
		}

		GL11.glPushMatrix();
		GL11.glTranslatef(GuiGame.board_x, GuiGame.board_y, 0);
		GL11.glTranslatef(142, 213, 0);

//		for (int i = 0; i < GuiScreen.gamList.size(); i++)
//		{
//			final GuiAddMoney gam = GuiScreen.gamList.get(i);
//			if (gam.isDead())
//			{
//				GuiScreen.gamList.remove(i);
//			}
//			else
//			{
//				gam.drawGui();
//			}
//		}

		GL11.glTranslatef(609, 0, 0);

//		for (int i = 0; i < GuiScreen.gsmList.size(); i++)
//		{
//			final GuiSubtractMoney gsm = GuiScreen.gsmList.get(i);
//			if (gsm.isDead())
//			{
//				GuiScreen.gamList.remove(i);
//			}
//			else
//			{
//				gsm.drawGui();
//			}
//		}

		GL11.glPopMatrix();
		GL11.glScalef(1 / GuiScreen.scale, 1 / GuiScreen.scale, 1 / GuiScreen.scale);
	}

	protected abstract void draw();

	protected void onGuiClose()
	{

	}

	public static void addMoney(Money m)
	{
		GuiScreen.gamQueue.add(new GuiAddMoney(Monopoly.getInstance().getBoard().gameValues, m));
	}

	public static void subtractMoney(Money m)
	{
		GuiScreen.gsmQueue.add(new GuiSubtractMoney(Monopoly.getInstance().getBoard().gameValues, m));
	}

	public static void setGui(GuiScreen gui)
	{
		if (GuiScreen.currentGui != null)
		{
			GuiScreen.currentGui.onGuiClose();
		}

		GuiScreen.currentGui = gui;
	}
}
