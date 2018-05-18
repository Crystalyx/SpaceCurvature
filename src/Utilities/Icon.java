package Utilities;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.graphics.Texture;

public class Icon
{
	public static Icon nil;
	public static Icon sqr;
	static
	{
		nil = new Icon("null.png");
		sqr = new Icon("sqr.png");
	}

	public static final HashMap<String, Icon> icons = new HashMap<String, Icon>();
	private Texture texture;
	private String path;

	public Icon(String texturename)
	{
		try
		{
			texture = new Texture(texturename);
			setPath(texturename);
		}
		catch (Exception e)
		{
			Logger.info("Couldn't find texture path: " + texturename);
			e.printStackTrace();
			texture = nil.texture;

		}
	}

	public Texture getTexture()
	{
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		return this.texture;
	}

	public static Icon getIcon(String path)
	{
		if (icons.containsKey(path))
		{
			return icons.get(path);
		}
		else
		{
			Icon s = new Icon(path);
			icons.put(path, s);
			return s;
		}
	}

	public static Icon getPng(String path)
	{
		String pathpng = path + ".png";
		if (icons.containsKey(pathpng))
		{
			return icons.get(pathpng);
		}
		else
		{
			Icon s = new Icon(pathpng);
			icons.put(pathpng, s);
			return s;
		}
	}

	public static Icon[] values()
	{
		Class<?> clazz = Icon.class;
		Field[] f = clazz.getFields();
		Icon[] ret = new Icon[f.length];
		for (int i = 0; i < ret.length; i++)
		{
			try
			{
				ret[i] = (Icon) f[i].get(null);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return ret;
	}

	public static Icon valueOf(String path)
	{
		Class<?> clazz = Icon.class;
		try
		{
			Field f = clazz.getField(path);

			return (Icon) f.get(null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public int width()
	{
		return getTexture().getWidth();
	}

	public int height()
	{
		return getTexture().getHeight();
	}

	public void bind()
	{
		Tess.instance.texture = this.getTexture();
	}
}
