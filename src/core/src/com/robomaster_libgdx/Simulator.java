package com.robomaster_libgdx;

import com.badlogic.gdx.*;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.robomaster_libgdx.environment.Environment;

public class Simulator extends Game {

	public static Simulator current;

	public final float VIEW_WIDTH = 1920;
	public final float VIEW_HEIGHT = 1080;

	Environment environment;

	/**
	 * Called when the game is first created.
	 */
	@Override
	public void create() {
		environment = new Environment(this);
		setScreen(environment);
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
		if (environment != null) {
			environment.dispose();
			environment = null;
		}
	}

	public StretchViewport getStretchView(){
		return new StretchViewport(this.VIEW_WIDTH, this.VIEW_HEIGHT);
	}
}
