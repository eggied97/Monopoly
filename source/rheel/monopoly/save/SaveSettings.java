package rheel.monopoly.save;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import rheel.monopoly.game.GameParser;
import rheel.monopoly.game.GameValues;
import rheel.monopoly.game.ParseException;

public class SaveSettings
{
	private static final File SAVEFILE = new File("settings.mps");
	public static GameValues latestGameValues;

	static
	{
		if (!SaveSettings.SAVEFILE.exists())
		{
			try
			{
				SaveSettings.SAVEFILE.createNewFile();
			}
			catch (final IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void save()
	{
		try
		{
			final DataOutputStream data = new DataOutputStream(new FileOutputStream(SaveSettings.SAVEFILE));

			if (SaveSettings.latestGameValues != null)
			{
				data.writeByte(1);
				data.writeUTF(SaveSettings.latestGameValues.file.getAbsolutePath());
			}
			else
			{
				data.writeByte(0);
			}

			data.close();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void load()
	{
		try
		{
			final DataInputStream data = new DataInputStream(new FileInputStream(SaveSettings.SAVEFILE));

			if (data.available() > 0)
			{
				final byte b = data.readByte();
				if (b == 1)
				{
					final String s = data.readUTF();
					final File f = new File(s);

					try
					{
						SaveSettings.latestGameValues = GameParser.parse(f);
					}
					catch (final ParseException e)
					{
						SaveSettings.latestGameValues = null;
					}
					catch (final Exception e)
					{
						// nothing
					}
				}
			}

			data.close();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}
}
