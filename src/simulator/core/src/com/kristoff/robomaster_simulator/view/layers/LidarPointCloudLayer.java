package com.kristoff.robomaster_simulator.view.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.RoboMasterPoint;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMasters;
import com.kristoff.robomaster_simulator.utils.Position;
import com.kristoff.robomaster_simulator.view.renderers.EnvRenderer;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;

import java.awt.*;

public class LidarPointCloudLayer extends VisualLayer {
    ShapeRenderer shapeRenderer;
    ShapeRenderer shapeRenderer2;

    Runnable renderLidarPoints;


    //ShapeRenderer circleRenderer;
    public Array<RoboMasterPoint> lidarPointCloudPointsArray;
    public LidarPointCloudLayer(EnvRenderer env) {
        super(env);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer2 = new ShapeRenderer();
        //circleRenderer = new ShapeRenderer();

        lidarPointCloudPointsArray = Systems.roboMasters.teamBlue.get(0).lidarObservation.other;

        renderLidarPoints = new Runnable() {
            @Override
            public void run() {
//                shapeRenderer.setProjectionMatrix(environment.view.getOrthographicCamera().combined);
//                shapeRenderer.setAutoShapeType(true);
//                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//                for(RoboMasterPoint point : getLidarPointCloudPointsArray()){
//                    int i = point.x;
//                    int j = point.y;
//                    Point a = new Point(i,j);
//                    Point b = new Point(
//                            (int)(environment.roboMasters.teamBlue.get(0).getLidarPosition().x * 1000),
//                            (int)(environment.roboMasters.teamBlue.get(0).getLidarPosition().y * 1000));
//                    float distance = (float) a.distance(b);
//                    float red;
//                    float green;
//                    float blue;
//                    if(distance <= 7000){
//                        red = - 1f/7000f * distance + 1.0f;
//                    }
//                    else {
//                        red = 0f;
//                    }
//                    if (distance <= 6000){
//                        green = 1f/6000f * distance;
//                    }
//                    else {
//                        green = 1.0f;
//                    }
//                    if (distance <= 5000 && distance >= 2000){
//                        blue = -1f/3000f * distance + 5f/3f;
//                    }
//                    else {
//                        blue = 0f;
//                    }
//                    shapeRenderer.setColor(red,green,blue,1.0f);
//                    shapeRenderer.circle(
//                            i / 1000f,
//                            j / 1000f,
//                            0.04f,10);
//                }
//                shapeRenderer.end();
            }
        };
    }

    @Override
    public void act (float delta) {
        float scale = 1f / 1000f;
        super.act(delta);
        if(timestate > 1/15f){
            update(delta);
            timestate = 0f;
        }
        else{
            timestate += Gdx.graphics.getDeltaTime();
        }
    }

    @Override
    public void draw(){
        super.draw();
        //renderLidarPoints.run();

        shapeRenderer.setProjectionMatrix(environment.view.getOrthographicCamera().combined);
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for(RoboMasterPoint point : getLidarPointCloudPointsArray()){
            int i = point.x;
            int j = point.y;
            Point a = new Point(i,j);
            Point b = new Point(
                    (int)(environment.roboMasters.teamBlue.get(0).getLidarPosition().x * 1000),
                    (int)(environment.roboMasters.teamBlue.get(0).getLidarPosition().y * 1000));
            float distance = (float) a.distance(b);
            float red;
            float green;
            float blue;
            if(distance <= 7000){
                red = - 1f/7000f * distance + 1.0f;
            }
            else {
                red = 0f;
            }
            if (distance <= 6000){
                green = 1f/6000f * distance;
            }
            else {
                green = 1.0f;
            }
            if (distance <= 5000 && distance >= 2000){
                blue = -1f/3000f * distance + 5f/3f;
            }
            else {
                blue = 0f;
            }
            shapeRenderer.setColor(red,green,blue,1.0f);
            shapeRenderer.circle(
                    i / 1000f,
                    j / 1000f,
                    0.04f,10);
        }
        shapeRenderer.end();


        shapeRenderer2.setProjectionMatrix(environment.view.getOrthographicCamera().combined);
        shapeRenderer2.setAutoShapeType(true);
        shapeRenderer2.begin(ShapeRenderer.ShapeType.Line);
        for(RoboMaster roboMaster : Systems.roboMasters.all){
            shapeRenderer2.setColor(1.0f,0,0,1.0f);
            shapeRenderer2.line(
                    roboMaster.getLidarPosition().x,
                    roboMaster.getLidarPosition().y,
                    roboMaster.getLidarPosition().x + (float) (2f * Math.sin(roboMaster.getFacingAngle())),
                    roboMaster.getLidarPosition().y + (float) (2f * Math.cos(roboMaster.getFacingAngle())));

            shapeRenderer2.setColor(0f,1.0f,0,1.0f);
            shapeRenderer2.line(
                    roboMaster.getLidarPosition().x,
                    roboMaster.getLidarPosition().y,
                    roboMaster.getLidarPosition().x + (float) (3f * Math.sin(roboMaster.getCannonAngle())),
                    roboMaster.getLidarPosition().y + (float) (3f * Math.cos(roboMaster.getCannonAngle())));
        }
        shapeRenderer2.end();



        shapeRenderer.setProjectionMatrix(environment.view.getOrthographicCamera().combined);
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for(Position position : RoboMasters.teamBlue.get(0).enemiesObservationSimulator.getSafeZone()){
            shapeRenderer.setColor(1.0f,0f,0f,1.0f);
            shapeRenderer.circle(
                    position.x / 1000f,
                    position.y / 1000f,
                    0.04f,10);
        }
        shapeRenderer.end();
    }

    float timestate = 0;
    public void update(float delta){

    }

    public Array<RoboMasterPoint> getLidarPointCloudPointsArray(){
        return Systems.roboMasters.teamBlue.get(0).lidarObservation.other;
    }
}
