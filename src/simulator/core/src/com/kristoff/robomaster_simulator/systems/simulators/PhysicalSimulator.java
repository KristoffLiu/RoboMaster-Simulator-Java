package com.kristoff.robomaster_simulator.systems.simulators;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.teams.RoboMasters;

public class PhysicalSimulator extends Simulator{
    public static PhysicalSimulator current;
    RoboMasters roboMasters;

    public World physicalWorld;
    Box2DDebugRenderer box2DDebugRenderer;

    Runnable runnable;

    public PhysicalSimulator() {
        this.current = this;
        this.isStep = true;

        physicalWorld = new World(new Vector2(), false);

        //create world boundary


//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                physicalWorld.step(1/60f,6,2);
//                for(RoboMaster roboMaster : simulator.roboMasters.getAll()){
//                    roboMaster.simulateFriction();
//                }
//            }
//        };
    }

    public void start(){
        this.createBoundary(0.205f, 0.205f,8.08f, 4.48f);
        this.createStaticBlocks();

        box2DDebugRenderer = new Box2DDebugRenderer();

        super.start();
    }

    @Override
    public void step(){
        physicalWorld.step(1/60f,6,2);
        RoboMasters.all.forEach(x->{
            if(x.RMPhysicalSimulation.body != null){
                Body body = x.RMPhysicalSimulation.body;
                int positionX = (int) (body.getPosition().x * 1000);
                int positionY = (int) (body.getPosition().y * 1000);
                x.actor.update(positionX, positionY, body.getAngle());
                //x.simulateFriction();
            }
        });
    }

    float step_delta = 0;
    public void step(float delta){
        step_delta = delta;
        runnable.run();
    }

    private void createBoundary(float width, float height){
        createBoundary(0f, 0f, width, height);
    }

    private void createBoundary(float x, float y, float width,float height){
        BodyDef boundary = new BodyDef();
        boundary.type = BodyDef.BodyType.StaticBody;
        boundary.position.set(x,y);

        Body worldBounds = physicalWorld.createBody(boundary);

        EdgeShape edgeShape = new EdgeShape();
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = edgeShape;

        //create boundaries
        edgeShape.set(
                new Vector2(0f,0f),
                new Vector2(width,0f));
        worldBounds.createFixture(fixtureDef);
        edgeShape.set(
                new Vector2(0f,0f),
                new Vector2(0f,height));
        worldBounds.createFixture(fixtureDef);
        edgeShape.set(
                new Vector2(width,0f),
                new Vector2(width,height));
        worldBounds.createFixture(fixtureDef);
        edgeShape.set(
                new Vector2(0f,height),
                new Vector2(width,height));
        worldBounds.createFixture(fixtureDef);
        edgeShape.dispose();
    }

    private void createStaticBlock(float x, float y, float halfWidth, float halfHeight,Vector2 centre,float rotation){
        PolygonShape roboMasterShape = new PolygonShape();
        roboMasterShape.setAsBox(halfWidth,halfHeight,centre,rotation);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(new Vector2(x, y));

        Body roboMasterBody = this.physicalWorld.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1f;
        fixtureDef.density = 1f;
        fixtureDef.shape = roboMasterShape;

        roboMasterBody.createFixture(fixtureDef);
        roboMasterShape.dispose();
    }

    private void createStaticBlocks() {
        for (TextureMapObject textureMapObject : Systems.map.getBlocks()) {
            float scale = 1f / 1000f;
            float textureX = textureMapObject.getX();
            float textureY = textureMapObject.getY();
            float textureWidth = textureMapObject.getTextureRegion().getRegionWidth();
            float textureHeight = textureMapObject.getTextureRegion().getRegionHeight();
            float textureRotation = textureMapObject.getRotation();
            float textureRotationInRadian = (float)Math.toRadians(textureRotation);

            float halfWidth = textureWidth / 2f;
            float halfHeight = textureHeight / 2f;
            float x = (textureX) * scale;
            float y = (textureY) * scale;
            if(textureMapObject.getName().equals("B5")){
                x = (textureX + textureWidth * (float)Math.cos(textureRotationInRadian) - halfWidth) * scale;
                y = (textureY - halfHeight) * scale;
            }
            Vector2 centre = new Vector2(halfWidth * scale, halfHeight * scale);

            createStaticBlock(x, y, halfWidth * scale, halfHeight * scale, centre, textureRotationInRadian);
        }
    }
}
