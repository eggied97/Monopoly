package rheel.monopoly.street;

public enum StreetColor
{
	purple(78, 61, 113), 
	light_blue(172, 220, 239), 
	pink(197, 56, 131), 
	orange(236, 139, 44), 
	red(218, 36, 40), 
	yellow(255, 240, 4), 
	green(19, 167, 88), 
	dark_blue(0, 103, 164), 
	stations(), 
	utilities();

	public final boolean isStreet;
	private final float[] color;

	private StreetColor(int... color)
	{
		this.isStreet = this.ordinal() <= 7;
		this.color = this.calcColor(color);
	}

	private float[] calcColor(int[] values)
	{
		return values.length == 3 ? new float[]{values[0] / 255.0F, values[1] / 255.0F, values[2] / 255.0F} : new float[]{1.0F, 1.0F, 1.0F};
	}

	public float[] getColor()
	{
		return new float[]{this.color[0], this.color[1], this.color[2]};
	}
}
