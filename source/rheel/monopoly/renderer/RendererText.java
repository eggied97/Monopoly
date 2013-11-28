package rheel.monopoly.renderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

class RendererText
{
	private final Font font;
	private final FontMetrics fontMetrics;
	private final RenderEngine engine;

	private final Map<String, StringTexture> stringIDs = new HashMap<String, StringTexture>();

	RendererText(RenderEngine engine, Font font)
	{
		this.engine = engine;
		this.font = font;
		final BufferedImage graphicsBase = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
		final Graphics2D g2 = graphicsBase.createGraphics();
		this.fontMetrics = g2.getFontMetrics(font);
	}

	void drawString(String s, int x, int y, Color color)
	{
		if (s == null || s.equals(""))
		{
			return;
		}

		if (!this.stringIDs.containsKey(s))
		{
			final BufferedImage bi = new BufferedImage(this.getSavePow2(this.fontMetrics.stringWidth(s)), this.getSavePow2(this.font.getSize()), BufferedImage.TYPE_4BYTE_ABGR);
			final Graphics2D g2 = bi.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(color);
			g2.setFont(this.font);
			g2.drawString(s, 0, this.font.getSize());
			this.stringIDs.put(s, new StringTexture(this.engine.getTexture(bi), bi.getWidth(), bi.getHeight()));
		}

		this.engine.bind(this.stringIDs.get(s).id);
		this.engine.drawCompleteTexture(x, y, this.stringIDs.get(s).width, this.stringIDs.get(s).height);
	}

	public FontMetrics getFontMetrics()
	{
		return this.fontMetrics;
	}

	private int getSavePow2(int n)
	{
		return (int) Math.pow(2, Math.ceil(Math.log10(n * 2) / Math.log10(2)));
	}

	private static class StringTexture
	{
		private final int id;
		private final int width;
		private final int height;

		public StringTexture(int id, int width, int height)
		{
			this.id = id;
			this.width = width;
			this.height = height;
		}
	}
}
