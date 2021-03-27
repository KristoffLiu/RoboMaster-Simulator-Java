package com.kristoff.robomaster_simulator.core;

import com.badlogic.gdx.*;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.teams.RoboMasters;
import com.kristoff.robomaster_simulator.view.renderers.EnvRenderer;
import com.kristoff.robomaster_simulator.view.renderers.Renderer;
import com.kristoff.robomaster_simulator.view.renderers.RendererConfiguration;

public class Simulator extends Game {

	public static Simulator current;

	public SimulatorConfiguration config = new SimulatorConfiguration();

	public RendererConfiguration rendererConfig = new RendererConfiguration();

	public static final float VIEW_WIDTH = 1920;
	public static final float VIEW_HEIGHT = 1080;

	public Systems systems;
	public RoboMasters roboMasters;
	public Renderer renderer;
	public EnvRenderer envRenderer;

	boolean isLoaded = false;

	public Simulator(SimulatorConfiguration config){
		this.current = this;
		this.config = config;
		configurateRenderer();
	}

	public void init(){

	}

	public void launch(){
		this.renderer = new Renderer(this, this.rendererConfig);
	}

	public void configurateRenderer(){
		rendererConfig.title = "RoboMaster Simulator Platform - [Real-time Remoting Mode]";
		rendererConfig.width = (int) (this.config.width * this.config.scaleFactor);
		rendererConfig.height = (int) (this.config.height * this.config.scaleFactor);
		rendererConfig.foregroundFPS = this.config.renderedFrameRate;
		rendererConfig.backgroundFPS = 60;
		rendererConfig.useGL30 = true;
	}

	public void reset(){

	}

	/**
	 * Called when the game is first created.
	 */
	@Override
	public void create() {
		systems = new Systems(config);
		roboMasters = new RoboMasters(config);
		systems.start();
		roboMasters.start();
		envRenderer = new EnvRenderer(this.config);
		setScreen(envRenderer);
		isLoaded = true;
	}

	/**
	 * Changes the current screen to the one passed in
	 */
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
		if (envRenderer != null) {
			envRenderer.dispose();
			envRenderer = null;
		}
	}

	@Override
	public void render () {
		if(isLoaded){
			super.render();
		}
	}

}
