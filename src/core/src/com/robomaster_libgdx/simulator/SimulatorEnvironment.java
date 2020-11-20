package com.robomaster_libgdx.simulator;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.Random;

public class SimulatorEnvironment extends Game {

	public static SimulatorEnvironment current;

	public static final float VIEW_WIDTH = 1920;
	public static final float VIEW_HEIGHT = 1080;

	SimulatorScreen simulatorScreen;

	/**
	 * Called when the game is first created.
	 */
	@Override
	public void create() {
		simulatorScreen = new SimulatorScreen(this);
		setScreen(simulatorScreen);
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
		if (simulatorScreen != null) {
			simulatorScreen.dispose();
			simulatorScreen = null;
		}
	}

	public StretchViewport getStretchView(){
		return new StretchViewport(this.VIEW_WIDTH, this.VIEW_HEIGHT);
	}
}
