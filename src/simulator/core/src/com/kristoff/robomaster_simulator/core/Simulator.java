package com.kristoff.robomaster_simulator.core;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kristoff.robomaster_simulator.maps.Map;
import com.kristoff.robomaster_simulator.simulations.MatrixSimulation;
import com.kristoff.robomaster_simulator.simulations.PhysicalSimulation;
import com.kristoff.robomaster_simulator.robomasters.RoboMasters;
import com.kristoff.robomaster_simulator.view.Renderer;

public class Simulator extends Game {

	public static Simulator current;

	public final float VIEW_WIDTH = 1920;
	public final float VIEW_HEIGHT = 1080;

	SimulatorConfiguration config = new SimulatorConfiguration();

	public Map map;
	public Renderer renderer;
	public PhysicalSimulation physicalSimulation;
	public MatrixSimulation matrixSimulation;
	public RoboMasters roboMasters;

	/**
	 * Called when the game is first created.
	 */
	@Override
	public void create() {
		map = new Map("CompetitionMap");
		roboMasters = new RoboMasters();
		matrixSimulation = new MatrixSimulation(this);
		physicalSimulation = new PhysicalSimulation(this);
		renderer = new Renderer(this);
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
		act(Gdx.graphics.getDeltaTime());
		super.render();
	}

	public void act(float delta){
		physicalSimulation.step(delta);
	}

	float accum = 0f;
	private void physicalSimulate(float delta){
//		accum += Gdx.graphics.getDeltaTime();
//		while (accum >= 1/60f) {
//			accum -= 1/60f;
//			runnable.run();
//		}
	}

	public void launch(){
		LwjglApplicationConfiguration rendererConfig = new LwjglApplicationConfiguration();

		rendererConfig.title = "RoboMaster Simulator - Java Platform";

		rendererConfig.width = (int) (this.config.width * this.config.scaleFactor);
		rendererConfig.height = (int) (this.config.height * this.config.scaleFactor);
		rendererConfig.foregroundFPS = this.config.renderedFrameRate;
		rendererConfig.backgroundFPS = 120;

		rendererConfig.useGL30 = false;
		new LwjglApplication(new Simulator(), rendererConfig);
	}
}
