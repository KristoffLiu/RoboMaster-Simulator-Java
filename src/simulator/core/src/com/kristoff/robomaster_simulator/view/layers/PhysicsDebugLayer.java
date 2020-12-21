package com.kristoff.robomaster_simulator.view.layers;

import com.badlogic.gdx.physics.box2d.*;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.view.renderers.EnvRenderer;

public class PhysicsDebugLayer {
    EnvRenderer envRenderer;
    World physicalWorld;
    Box2DDebugRenderer box2DDebugRenderer;

    public PhysicsDebugLayer(EnvRenderer envRenderer) {
        this.envRenderer = envRenderer;
        physicalWorld = Systems.physicalSimulator.physicalWorld;
        box2DDebugRenderer = new Box2DDebugRenderer();
    }

    public void render(float delta){
        box2DDebugRenderer.render(physicalWorld, envRenderer.view.getOrthographicCamera().combined);
    }
}
