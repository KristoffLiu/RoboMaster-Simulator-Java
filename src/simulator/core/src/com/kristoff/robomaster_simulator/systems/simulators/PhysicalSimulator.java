package com.kristoff.robomaster_simulator.systems.simulators;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.robomasters.modules.Dynamics;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMasters;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;

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

        deployTeamBlue();
        deployTeamRed();
        //create world boundary
        this.createBoundary(0.205f, 0.205f,8.08f, 4.48f);
        this.createStaticBlocks();


        for(RoboMaster roboMaster : Systems.roboMasters.teamBlue){
            roboMaster.transformRotation((float) (Math.PI));
        }

        box2DDebugRenderer = new Box2DDebugRenderer();

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

    @Override
    public void step(){
        physicalWorld.step(1/60f,6,2);
        for(RoboMaster roboMaster : Systems.roboMasters.all){
            roboMaster.simulateFriction();
        }
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

    private void createStaticBlocks(){
        for(TextureMapObject textureMapObject : Systems.map.getBlocks()){
            float scale = 1f / 1000f;
            float halfWidth = textureMapObject.getTextureRegion().getRegionWidth() / 2f * scale;
            float halfHeight = textureMapObject.getTextureRegion().getRegionHeight() / 2f * scale;
            float x = textureMapObject.getX() * scale + halfWidth;
            float y = textureMapObject.getY() * scale;
            Vector2 centre = new Vector2(0,halfHeight);
            float rotation = (float) Math.toRadians(textureMapObject.getRotation());
            createStaticBlock(x,y,halfWidth,halfHeight,centre,rotation);
        }
    }

    private void deployTeamBlue(){
        int i = 0;
        for(TextureMapObject textureMapObject : Systems.map.getBirthZones()){
            if(textureMapObject.getProperties().containsKey("blue")){
                float scale = 1f / 1000f;
                float halfWidth = textureMapObject.getTextureRegion().getRegionWidth() / 2f * scale;
                float halfHeight = textureMapObject.getTextureRegion().getRegionHeight() / 2f * scale;
                float x = textureMapObject.getX() * scale + halfWidth;
                float y = textureMapObject.getY() * scale + halfHeight;
                Systems.roboMasters.teamBlue.get(i).deploy(x,y,this.physicalWorld);
                Systems.roboMasters.teamBlue.get(i).dynamics = new Dynamics(Systems.roboMasters.teamBlue.get(i));
                Systems.roboMasters.teamBlue.get(i).dynamics.start();
                Systems.roboMasters.teamBlue.get(i).observation.start();
                i ++;
            }
        }
    }

    private void deployTeamRed(){
        int i = 0;
        for(TextureMapObject textureMapObject : Systems.map.getBirthZones()){
                if(textureMapObject.getProperties().containsKey("red")){
                float scale = 1f / 1000f;
                float halfWidth = textureMapObject.getTextureRegion().getRegionWidth() / 2f * scale;
                float halfHeight = textureMapObject.getTextureRegion().getRegionHeight() / 2f * scale;
                float x = textureMapObject.getX() * scale + halfWidth;
                float y = textureMapObject.getY() * scale + halfHeight;
                Systems.roboMasters.teamRed.get(i).deploy(x,y,this.physicalWorld);
                Systems.roboMasters.teamRed.get(i).dynamics = new Dynamics(Systems.roboMasters.teamRed.get(i));
                Systems.roboMasters.teamRed.get(i).dynamics.start();
                Systems.roboMasters.teamRed.get(i).observation.start();
                i ++;
            }
        }
    }

}