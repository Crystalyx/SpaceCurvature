package com.simulator.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.simulator.client.Client;
import com.simulator.core.CoreInitializer;

public class Launcher
{
	public static LwjglApplicationConfiguration config;

	public static void main(String[] arg)
	{
		CoreInitializer.init(arg);
		config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Client(), config);
	}
}
