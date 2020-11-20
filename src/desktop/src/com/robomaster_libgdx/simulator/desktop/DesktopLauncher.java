package com.robomaster_libgdx.simulator.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.robomaster_libgdx.simulator.SimulatorEnvironment;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "RoboMaster Simulator - libGDX Platform, 2020";

		float scaleFactor = 0.6f;
		config.width = (int) (1920 * scaleFactor);
		config.height = (int) (1080 * scaleFactor);

		new LwjglApplication(new SimulatorEnvironment(), config);
	}
}
