package com.robomaster_java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.robomaster_java.simulator.Simulator;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "RoboMaster Java Simulator Platform";

		float scaleFactor = 0.6f;
		config.width = (int) (1920 * scaleFactor);
		config.height = (int) (1080 * scaleFactor);
		config.useGL30 = true;
		config.foregroundFPS = 60;

		new LwjglApplication(new Simulator(), config);
	}
}
