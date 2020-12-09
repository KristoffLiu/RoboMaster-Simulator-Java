package com.kristoff.robomaster_simulator.view.layers;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.core.Simulator;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.view.Renderer;

public class PhysicsDebugLayer {
    Simulator simulator;

    World physicalWorld;
    Box2DDebugRenderer box2DDebugRenderer;

    public PhysicsDebugLayer(Simulator simulator) {
        this.simulator = simulator;
        physicalWorld = simulator.physicalSimulation.physicalWorld;
        box2DDebugRenderer = new Box2DDebugRenderer();
    }

    float timeState;
    public void render(float delta){
        box2DDebugRenderer.render(physicalWorld, simulator.renderer.view.getOrthographicCamera().combined);
    }
}
