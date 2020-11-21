package com.robomaster_libgdx.simulator.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.robomaster_libgdx.Simulator;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "RoboMaster Simulator - libGDX Java Platform";

		float scaleFactor = 0.6f;
		config.width = (int) (2320 * scaleFactor);
		config.height = (int) (1080 * scaleFactor);

		new LwjglApplication(new Simulator(), config);
	}
}
