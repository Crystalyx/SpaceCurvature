package com.simulator.settings;

import java.lang.reflect.Field;

import org.lwjgl.input.Keyboard;

import com.simulator.bindings.KeyBinds;
import com.simulator.core.CoreInitializer;

public class Settings
{
	public int keyAttack = KeyBinds.MOUSE_LEFT;
	public int keyInterract = Keyboard.KEY_R;
	public int keyEquip = Keyboard.KEY_U;

	public int keyCycleR = Keyboard.KEY_E;
	public int keyCycleL = Keyboard.KEY_Q;

	public int keyLeft = Keyboard.KEY_A;
	public int keyRight = Keyboard.KEY_D;
	public int keyForward = Keyboard.KEY_W;
	public int keyBack = Keyboard.KEY_S;
	public int keyUp = Keyboard.KEY_SPACE;
	public int keyDown = Keyboard.KEY_LSHIFT;

	public int keyDebug = Keyboard.KEY_O;
	public int keyWave = Keyboard.KEY_J;
	public int keyReset = Keyboard.KEY_L;

	public void load()
	{
		loadKey("Attack", keyAttack);
		loadKey("Interract", keyInterract);
		loadKey("Equip", keyEquip);

		loadKey("CycleR", keyCycleR);
		loadKey("CycleL", keyCycleL);

		loadKey("Left", keyLeft);
		loadKey("Right", keyRight);
		loadKey("Forward", keyForward);
		loadKey("Back", keyBack);
		loadKey("Debug", keyDebug);
		loadKey("Wave", keyWave);
		loadKey("Reset", keyReset);

	}

	public void loadKey(String key, int word)
	{
		Class<?> clazz = this.getClass();
		try
		{
			Field k = clazz.getField("key" + key);

			CoreInitializer.cfg.setWarn(false);
			CoreInitializer.cfg.addProperty("Settings", key, word);
			CoreInitializer.cfg.setWarn(true);

			int b = CoreInitializer.cfg.getInt("Settings", key, word);
			k.set(this, b);
			KeyBinds.registerKey((int) k.get(this));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
