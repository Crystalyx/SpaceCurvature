package Utilities;

public class Graph
{
	public static Tess t = Tess.instance;

	public static void colorize(Color c)
	{
		if (c != null)
			t.setColor(c.red / 255f, c.green / 255f, c.blue / 255f, c.alpha / 255f);
	}

	public static void clearColor()
	{
		t.setColor(1, 1, 1, 1);
	}
}
