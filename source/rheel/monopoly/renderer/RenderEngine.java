package rheel.monopoly.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public final class RenderEngine
{
	private static final IntBuffer intbuf = BufferUtils.createIntBuffer(1);
	private static final ColorModel glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[]{8, 8, 8, 8}, true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
	private static final ColorModel glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[]{8, 8, 8, 0}, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);

	private final Class<?> baseClass;

	private final RendererText[] strRenderersBold = new RendererText[256];
	private final RendererText[] strRenderers = new RendererText[256];

	private final Map<String, Integer> textureMap = new HashMap<String, Integer>();
	private final Map<String, Dimension> sizesMap = new HashMap<String, Dimension>();

	private final Map<RectData, Integer> quadMapTexture = new HashMap<RectData, Integer>();
	private final Map<RectData, Integer> quadMap = new HashMap<RectData, Integer>();
	private final Map<ComplRectData, Integer> quadMapCompl = new HashMap<ComplRectData, Integer>();

	private String prevTex = "";

	public RenderEngine(Class<?> baseClass)
	{
		this.baseClass = baseClass;
	}

	public void drawTexturedRectangle(int x, int y, int u, int v, int width, int height)
	{
		this.drawTexturedRectangle(x, y, u, v, width, height, width, height);
	}

	public void drawTexturedRectangle(int x, int y, int u, int v, int width, int height, int uEnd, int vEnd)
	{
		final RectData rd = new RectData(x, y, u, v, width, height, uEnd, vEnd);

		if (!this.quadMapTexture.containsKey(rd))
		{
			final int id = GL11.glGenLists(1);
			final Dimension dim = this.sizesMap.get(this.prevTex);

			GL11.glNewList(id, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(u / (float) dim.width, v / (float) dim.height);
			GL11.glVertex2f(x, y);
			GL11.glTexCoord2f(u / (float) dim.width, (vEnd + v) / (float) dim.height);
			GL11.glVertex2f(x, y + height);
			GL11.glTexCoord2f((uEnd + u) / (float) dim.width, (vEnd + v) / (float) dim.height);
			GL11.glVertex2f(x + width, y + height);
			GL11.glTexCoord2f((uEnd + u) / (float) dim.width, v / (float) dim.height);
			GL11.glVertex2f(x + width, y);
			GL11.glEnd();
			GL11.glEndList();

			this.quadMapTexture.put(rd, id);
		}

		GL11.glCallList(this.quadMapTexture.get(rd));
	}

	void drawCompleteTexture(int x, int y, int width, int height)
	{
		final ComplRectData rd = new ComplRectData(x, y, width, height);

		if (!this.quadMapCompl.containsKey(rd))
		{
			final int id = GL11.glGenLists(1);

			GL11.glNewList(id, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(x, y);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex2f(x, y + height);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex2f(x + width, y + height);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex2f(x + width, y);
			GL11.glEnd();
			GL11.glEndList();

			this.quadMapCompl.put(rd, id);
		}

		GL11.glCallList(this.quadMapCompl.get(rd));
	}

	public void drawRectangle(int x, int y, int width, int height)
	{
		final RectData rd = new RectData(x, y, 0, 0, width, height, 0, 0);

		if (!this.quadMap.containsKey(rd))
		{
			final int id = GL11.glGenLists(1);

			GL11.glNewList(id, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(x, y);
			GL11.glVertex2f(x, y + height);
			GL11.glVertex2f(x + width, y + height);
			GL11.glVertex2f(x + width, y);
			GL11.glEnd();
			GL11.glEndList();

			this.quadMap.put(rd, id);
		}

		GL11.glCallList(this.quadMap.get(rd));
	}

	void drawString(String s, int x, int y, int size, boolean bold, Color color)
	{
		if (size == 0 || size > 255)
		{
			return;
		}

		GL11.glPushMatrix();
		this.getStrRenderer(size, bold).drawString(s, x, y, color);
		GL11.glPopMatrix();
	}

	public RendererText getStrRenderer(int size, boolean bold)
	{
		if (bold)
		{
			if (this.strRenderersBold[size] == null)
			{
				this.strRenderersBold[size] = new RendererText(this, new Font(Font.SANS_SERIF, Font.BOLD, size));
			}

			return this.strRenderersBold[size];
		}

		if (this.strRenderers[size] == null)
		{
			this.strRenderers[size] = new RendererText(this, new Font(Font.SANS_SERIF, Font.PLAIN, size));
		}

		return this.strRenderers[size];
	}

	public void bindTexture(String s)
	{
		if (!this.textureMap.containsKey(s))
		{
			try
			{
				final InputStream is = this.baseClass.getResourceAsStream(s);
				final BufferedImage bi = ImageIO.read(is);
				final int i = this.getTexture(bi);
				this.textureMap.put(s, i);
				this.sizesMap.put(s, new Dimension(bi.getWidth(), bi.getHeight()));
			}
			catch (final Exception e)
			{
				e.printStackTrace();
				return;
			}
		}

		final int bind = this.textureMap.get(s);

		if (this.prevTex != s)
		{
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, bind);
			this.prevTex = s;
		}
	}

	void bind(int bind)
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, bind);
		this.prevTex = "";
	}

	int getTexture(BufferedImage bi)
	{
		try
		{
			GL11.glGenTextures(RenderEngine.intbuf);
			final int id = RenderEngine.intbuf.get(0);

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

			final int format = bi.getColorModel().hasAlpha() ? GL11.GL_RGBA : GL11.GL_RGB;

			ByteBuffer texData;
			WritableRaster raster;
			BufferedImage texImage;

			if (Math.log10(bi.getWidth()) / Math.log10(2) % 1 != 0 || Math.log10(bi.getHeight()) / Math.log10(2) % 1 != 0)
			{
				throw new RuntimeException();
			}

			final int texWidth = bi.getWidth();
			final int texHeight = bi.getHeight();

			if (bi.getColorModel().hasAlpha())
			{
				raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 4, null);
				texImage = new BufferedImage(RenderEngine.glAlphaColorModel, raster, false, new Hashtable<String, Object>());
			}
			else
			{
				raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 3, null);
				texImage = new BufferedImage(RenderEngine.glColorModel, raster, false, new Hashtable<String, Object>());
			}

			final Graphics g = texImage.getGraphics();
			g.setColor(new Color(0f, 0f, 0f, 0f));
			g.fillRect(0, 0, texWidth, texHeight);
			g.drawImage(bi, 0, 0, null);

			final byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();

			texData = ByteBuffer.allocateDirect(data.length);
			texData.order(ByteOrder.nativeOrder());
			texData.put(data, 0, data.length);
			texData.flip();

			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, bi.getWidth(), bi.getHeight(), 0, format, GL11.GL_UNSIGNED_BYTE, texData);

			return id;
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			System.exit(0);
			return 0;
		}
	}

	private class ComplRectData
	{
		private final int x;
		private final int y;
		private final int w;
		private final int h;

		private ComplRectData(int x, int y, int w, int h)
		{
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}

		@Override
		public String toString()
		{
			return this.getClass().getName() + "{x=" + this.x + ",y=" + this.y + ",w=" + this.w + ",h=" + this.h + "}";
		}

		@Override
		public boolean equals(Object o)
		{
			return o instanceof ComplRectData ? this.x == ((ComplRectData) o).x && this.y == ((ComplRectData) o).y && this.w == ((ComplRectData) o).w && this.h == ((ComplRectData) o).h : false;
		}

		@Override
		public int hashCode()
		{
			int value = 17;
			value *= this.x * 37;
			value *= this.y * 37;
			value *= this.w * 37;
			value *= this.h * 37;

			return value;
		}
	}

	private class RectData
	{
		private final int x;
		private final int y;
		private final int u;
		private final int v;
		private final int w;
		private final int h;
		private final int u2;
		private final int v2;

		private RectData(int x, int y, int u, int v, int w, int h, int u2, int v2)
		{
			this.x = x;
			this.y = y;
			this.u = u;
			this.v = v;
			this.w = w;
			this.h = h;
			this.u2 = u2;
			this.v2 = v2;
		}

		@Override
		public boolean equals(Object o)
		{
			return o instanceof RectData ? this.x == ((RectData) o).x && this.y == ((RectData) o).y && this.u == ((RectData) o).u && this.v == ((RectData) o).v && this.w == ((RectData) o).w && this.h == ((RectData) o).h : false;
		}

		@Override
		public String toString()
		{
			return this.getClass().getName() + "{x=" + this.x + ",y=" + this.y + ",u=" + this.u + ",v=" + this.v + ",w=" + this.w + ",h=" + this.h + "u2=" + this.u2 + ",v2=" + this.v2 + "}";
		}

		@Override
		public int hashCode()
		{
			int value = 19;
			value *= this.x * 37;
			value *= this.y * 37;
			value *= this.u * 37;
			value *= this.v * 37;
			value *= this.w * 37;
			value *= this.h * 37;
			value *= this.u2 * 37;
			value *= this.v2 * 37;

			return value;
		}
	}
}
