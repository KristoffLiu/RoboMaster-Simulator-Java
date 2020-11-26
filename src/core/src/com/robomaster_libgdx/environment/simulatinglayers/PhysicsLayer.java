package com.robomaster_libgdx.environment.simulatinglayers;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.robomaster_libgdx.environment.Environment;

public class PhysicsLayer {
    Environment environment;

    World physicalWorld;
    Box2DDebugRenderer box2DDebugRenderer;

    public PhysicsLayer(Environment environment) {
        this.environment = environment;
        box2DDebugRenderer = new Box2DDebugRenderer();
        physicalWorld = new World(new Vector2(), false);

        //create world boundary
        this.createBoundary(environment.width, environment.height);
        this.createBoundary(0.205f, 0.205f,8.08f, 4.48f);


        createStaticBlocks();
        deployTeamBlue();
        deployTeamRed();

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
        for(TextureMapObject textureMapObject : environment.map.getBlocks()){
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
        for(TextureMapObject textureMapObject : environment.map.getBirthZone()){
            if(textureMapObject.getProperties().containsKey("blue")){
                float scale = 1f / 1000f;
                float halfWidth = textureMapObject.getTextureRegion().getRegionWidth() / 2f * scale;
                float halfHeight = textureMapObject.getTextureRegion().getRegionHeight() / 2f * scale;
                float x = textureMapObject.getX() * scale + halfWidth;
                float y = textureMapObject.getY() * scale + halfHeight;
                Vector2 centre = new Vector2();
                float rotation = (float) Math.toRadians(textureMapObject.getRotation());
                environment.teamBlue.get(i).createRoboMasterBody(x,y,this.physicalWorld);
                i ++;
            }
        }
    }

    private void deployTeamRed(){
        int i = 0;
        for(TextureMapObject textureMapObject : environment.map.getBirthZone()){
                if(textureMapObject.getProperties().containsKey("red")){
                float scale = 1f / 1000f;
                float halfWidth = textureMapObject.getTextureRegion().getRegionWidth() / 2f * scale;
                float halfHeight = textureMapObject.getTextureRegion().getRegionHeight() / 2f * scale;
                float x = textureMapObject.getX() * scale + halfWidth;
                float y = textureMapObject.getY() * scale + halfHeight;
                Vector2 centre = new Vector2();
                float rotation = (float) Math.toRadians(textureMapObject.getRotation());
                environment.teamRed.get(i).createRoboMasterBody(x,y,this.physicalWorld);
                i ++;
            }
        }
    }

    public void step(){
        physicalWorld.step(1/60f,6,2);


    }

    public void render(){
        box2DDebugRenderer.render(physicalWorld, environment.view.getOrthographicCamera().combined);
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


}
