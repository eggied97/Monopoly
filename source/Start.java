import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import rheel.monopoly.game.Monopoly;

public class Start
{
	public static final void main(String... args)
	{
		for (final String arg : args)
		{
			final String[] sSplt = arg.split("=");
			if (sSplt[0].equals("dellib"))
			{
				final File file = new File(sSplt[1]);
				file.delete();
			}
			else if (sSplt[0].equals("natives"))
			{
				try
				{
					final String natives = sSplt[1].replace("@space@", " ");
					final File file = new File("start.bat");
					file.delete();
					file.createNewFile();
					final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
					writer.write("java -Djava.library.path=\"" + natives + "\" -jar Monopoly.jar");
					writer.close();
				}
				catch (final Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		Monopoly.initInstance(Start.class);
	}
}
