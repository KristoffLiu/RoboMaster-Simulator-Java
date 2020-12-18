package com.kristoff.robomaster_simulator.envs;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.view.renderers.EnvRenderer;
import com.kristoff.robomaster_simulator.view.renderers.Renderer;
import com.kristoff.robomaster_simulator.view.renderers.RendererConfiguration;

public class Environment extends Game {

	public static Environment current;

	public static EnvironmentConfiguration config = new EnvironmentConfiguration();

	public static RendererConfiguration rendererConfig = new RendererConfiguration();

	public final float VIEW_WIDTH = 1920;
	public final float VIEW_HEIGHT = 1080;


	public Systems systems;
	public EnvRenderer envRenderer;

	boolean isLoaded = false;

	public Environment(EnvironmentConfiguration config){
		this.config = config;
		configurateRenderer();
	}

	public void launch(){
		new Renderer(this, this.rendererConfig);
	}

	public void configurateRenderer(){
		rendererConfig.title = "RoboMaster Simulator Platform - Java [Normal Mode]";
		rendererConfig.width = (int) (this.config.width * this.config.scaleFactor);
		rendererConfig.height = (int) (this.config.height * this.config.scaleFactor);
		rendererConfig.foregroundFPS = this.config.renderedFrameRate;
		rendererConfig.backgroundFPS = 120;
		rendererConfig.useGL30 = false;
	}

	/**
	 * Called when the game is first created.
	 */
	@Override
	public void create() {
		systems = new Systems();
		envRenderer = new EnvRenderer(this);
		setScreen(envRenderer);

		isLoaded = true;
		Systems.start();
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

	//	public void launch(){
//		LwjglApplicationConfiguration rendererConfig = new LwjglApplicationConfiguration();
//
//		rendererConfig.title = "RoboMaster Simulator - Java Platform";
//
//		rendererConfig.width = (int) (this.config.width * this.config.scaleFactor);
//		rendererConfig.height = (int) (this.config.height * this.config.scaleFactor);
//		rendererConfig.foregroundFPS = this.config.renderedFrameRate;
//		rendererConfig.backgroundFPS = 120;
//
//		rendererConfig.useGL30 = false;
//		new LwjglApplication(this, rendererConfig);
//	}

}
