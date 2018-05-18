package com.simulator.bindings;

import java.util.Enumeration;
import java.util.Hashtable;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class KeyBinds
{
	protected static Hashtable<Integer, Boolean> clicks = new Hashtable<Integer, Boolean>();
	protected static Hashtable<Integer, Boolean> unpresses = new Hashtable<Integer, Boolean>();
	public static boolean leftClick = false;
	public static boolean leftUnpress = true;
	public static boolean rightClick = false;
	public static boolean rightUnpress = true;

	public static final int MOUSE_LEFT = 777;
	public static final int MOUSE_RIGHT = 778;
	public static final int MOUSE_MIDDLE = 779;

	public static void register()
	{
		registerKey(Keyboard.KEY_LSHIFT);
		registerKey(Keyboard.KEY_ESCAPE);
	}

	public static void update()
	{
		Enumeration<Integer> en = clicks.keys();
		while (en.hasMoreElements())
		{
			Integer key = en.nextElement();
			if (key != MOUSE_LEFT && key != MOUSE_MIDDLE && key != MOUSE_RIGHT)
			{
				if (Keyboard.isKeyDown(key))
				{
					clicks.put(key, unpresses.get(key));
					unpresses.put(key, !Keyboard.isKeyDown(key));
				}
				else
				{
					clicks.put(key, false);
					unpresses.put(key, true);
				}
			}
		}

		if (Mouse.isButtonDown(0))
		{
			leftClick = leftUnpress;
			leftUnpress = !Mouse.isButtonDown(0);
		}
		else
		{
			leftClick = false;
			leftUnpress = true;
		}

		if (Mouse.isButtonDown(1))
		{
			rightClick = rightUnpress;
			rightUnpress = !Mouse.isButtonDown(1);
		}
		else
		{
			rightClick = false;
			rightUnpress = true;
		}
	}

	public static void registerKey(int key)
	{
		clicks.put(key, false);
		unpresses.put(key, true);
	}

	public static boolean keyClick(int key)
	{
		return key == MOUSE_LEFT ? leftClick : (key == MOUSE_RIGHT ? rightClick : clicks.get(key));
	}

	public static boolean keyUnpressed(int key)
	{
		return key == MOUSE_LEFT ? leftUnpress : (key == MOUSE_RIGHT ? rightUnpress : unpresses.get(key));
	}

	public static boolean keyPressed(int key)
	{
		return !keyUnpressed(key);
	}
}
