package com.kristoff.robomaster_simulator.view.layers;

import com.badlogic.gdx.physics.box2d.*;
import com.kristoff.robomaster_simulator.environment.Environment;

public class PhysicsDebugLayer {
    Environment environment;

    World physicalWorld;
    Box2DDebugRenderer box2DDebugRenderer;

    public PhysicsDebugLayer(Environment environment) {
        this.environment = environment;
        physicalWorld = environment.physicalSimulator.physicalWorld;
        box2DDebugRenderer = new Box2DDebugRenderer();
    }

    float timeState;
    public void render(float delta){
        box2DDebugRenderer.render(physicalWorld, environment.renderer.view.getOrthographicCamera().combined);
    }
}
