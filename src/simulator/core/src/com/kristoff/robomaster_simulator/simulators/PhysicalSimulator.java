package com.kristoff.robomaster_simulator.simulators;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.environment.Environment;
import com.kristoff.robomaster_simulator.robomasters.RoboMasters;
import com.kristoff.robomaster_simulator.robomasters.types.RoboMaster;

public class PhysicalSimulation {
    Environment environment;
    RoboMasters roboMasters;

    public World physicalWorld;
    Box2DDebugRenderer box2DDebugRenderer;

    Runnable runnable;

    public PhysicalSimulation(final Environment environment) {
        this.environment = environment;
        roboMasters = environment.roboMasters;
        box2DDebugRenderer = new Box2DDebugRenderer();
        physicalWorld = new World(new Vector2(), false);

        //create world boundary
        this.createBoundary(0.205f, 0.205f,8.08f, 4.48f);

        createStaticBlocks();
        deployTeamBlue();
        deployTeamRed();

        for(RoboMaster roboMaster : roboMasters.getTeamBlue()){
            roboMaster.transformRotation((float) (Math.PI));
        }

        runnable = new Runnable() {
            @Override
            public void run() {
                physicalWorld.step(step_delta,6,2);
//                for(RoboMaster roboMaster : simulator.roboMasters.getAll()){
//                    roboMaster.simulateFriction();
//                }
            }
        };
    }

    public void step(){
//        roboMasters.getTeamBlue().get(0).
    }

    float step_delta = 0;
    public void step(float delta){
        step_delta = delta;
        runnable.run();
    }

    float timeState;
    public void render(float delta){
        box2DDebugRenderer.render(physicalWorld, this.environment.renderer.view.getOrthographicCamera().combined);
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
        for(TextureMapObject textureMapObject : this.environment.map.getBlocks()){
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

    private void createRoboMaster(){

    }

    private void deployTeamBlue(){
        int i = 0;
        for(TextureMapObject textureMapObject : this.environment.map.getBirthZones()){
            if(textureMapObject.getProperties().containsKey("blue")){
                float scale = 1f / 1000f;
                float halfWidth = textureMapObject.getTextureRegion().getRegionWidth() / 2f * scale;
                float halfHeight = textureMapObject.getTextureRegion().getRegionHeight() / 2f * scale;
                float x = textureMapObject.getX() * scale + halfWidth;
                float y = textureMapObject.getY() * scale + halfHeight;
                Vector2 centre = new Vector2();
                float rotation = (float) Math.toRadians(textureMapObject.getRotation());
                this.environment.roboMasters.getTeamBlue().get(i).createRoboMasterBody(x,y,this.physicalWorld);
                i ++;
            }
        }
    }

    private void deployTeamRed(){
        int i = 0;
        for(TextureMapObject textureMapObject : this.environment.map.getBirthZones()){
                if(textureMapObject.getProperties().containsKey("red")){
                float scale = 1f / 1000f;
                float halfWidth = textureMapObject.getTextureRegion().getRegionWidth() / 2f * scale;
                float halfHeight = textureMapObject.getTextureRegion().getRegionHeight() / 2f * scale;
                float x = textureMapObject.getX() * scale + halfWidth;
                float y = textureMapObject.getY() * scale + halfHeight;
                Vector2 centre = new Vector2();
                float rotation = (float) Math.toRadians(textureMapObject.getRotation());
                this.environment.roboMasters.getTeamRed().get(i).createRoboMasterBody(x,y,this.physicalWorld);
                i ++;
            }
        }
    }

    public void updateMatrix(){
        boolean[][] PointMatrix = new boolean[8490][4890];
        Array<Body> bodies = new Array<>();
//        for(Body body : bodies){
//            for(Fixture fixture : body.getFixtureList()){
//                Shape shape = fixture.getShape();
//                if (fixture.getShape() instanceof PolygonShape) {
//                    ((PolygonShape)shape).
//                }
//            }
//        }
    }


    public void simulateFriction(){

    }

}
