package com.kristoff.robomaster_simulator.core;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kristoff.robomaster_simulator.maps.Map;
import com.kristoff.robomaster_simulator.physicalsimulation.PhysicalSimulation;
import com.kristoff.robomaster_simulator.view.Renderer;

public class Simulator extends Game {

	public static Simulator current;

	public final float VIEW_WIDTH = 1920;
	public final float VIEW_HEIGHT = 1080;

	Renderer renderer;
	PhysicalSimulation physicalSimulation;
	Map map;



	public Simulator(){

	}

	/**
	 * Called when the game is first created.
	 */
	@Override
	public void create() {
		physicalSimulation = new PhysicalSimulation();
		renderer = new Renderer(this);
		map = ;
		setScreen(renderer);
	}

	//Changes the current screen to the one passed in
	@Override
	public void setScreen(Screen nextScreen) {
		super.setScreen(nextScreen);
	}

	@Override
	public void dispose() {
		// super.dispose() I think you can't remove this line,
		// because there's some essential action in superclass's method.
		super.dispose();

		// When the game finished, dispose everything
		if (renderer != null) {
			renderer.dispose();
			renderer = null;
		}
	}

	@Override
	public void render () {

	}

	public void launch(){
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "RoboMaster Simulator - Java Platform";

		float scaleFactor = 0.6f;
		config.width = (int) (1920 * scaleFactor);
		config.height = (int) (1080 * scaleFactor);
		config.useGL30 = true;
		config.foregroundFPS = 60;
		new LwjglApplication(new Simulator(), config);
	}
}
