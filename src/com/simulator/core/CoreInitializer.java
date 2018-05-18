package com.simulator.core;

import org.lwjgl.input.Keyboard;

import com.simulator.bindings.KeyBinds;
import com.simulator.player.Player;
import com.simulator.settings.Settings;

import Utilities.Configuration;

public class CoreInitializer
{
	public static final Configuration cfg = new Configuration();
	public static final Settings SETTINGS = new Settings();
	public static Player p;

	public static void init(String[] args)
	{
		cfg.readConfig();
		SETTINGS.load();
		p = new Player();
		Keyboard.enableRepeatEvents(true);
	}

	public static void update()
	{
		KeyBinds.update();
	}

	public static void updateMovement()
	{
		if (KeyBinds.keyPressed(SETTINGS.keyForward))
		{
			p.vel.addChanged(p.look);
		}
		if (KeyBinds.keyPressed(SETTINGS.keyBack))
		{
			p.vel.subChanged(p.look);
		}
	}
}
