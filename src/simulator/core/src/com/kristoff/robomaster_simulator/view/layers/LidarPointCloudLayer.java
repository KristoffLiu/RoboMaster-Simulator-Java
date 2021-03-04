package com.kristoff.robomaster_simulator.view.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.kristoff.robomaster_simulator.robomasters.robomaster.modules.enemyobservations.EnemiesObservationPoint;
import com.kristoff.robomaster_simulator.systems.pointsimulator.StatusPoint;
import com.kristoff.robomaster_simulator.robomasters.teams.RoboMasters;
import com.kristoff.robomaster_simulator.utils.Position;
import com.kristoff.robomaster_simulator.view.renderers.EnvRenderer;
import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class LidarPointCloudLayer extends VisualLayer {
    ShapeRenderer shapeRenderer;
    ShapeRenderer shapeRenderer2;
    ShapeRenderer shapeRenderer3;
    ShapeRenderer shapeRenderer4;
    ShapeRenderer shapeRenderer5;

    public CopyOnWriteArrayList<StatusPoint> lidarPointCloudPointsArray;
    public LidarPointCloudLayer(EnvRenderer env) {
        super(env);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer2 = new ShapeRenderer();
        shapeRenderer3 = new ShapeRenderer();
        shapeRenderer4 = new ShapeRenderer();
        shapeRenderer5 = new ShapeRenderer();
        //circleRenderer = new ShapeRenderer();

        lidarPointCloudPointsArray = RoboMasters.teamBlue.get(0).lidarObservation.other;
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

        shapeRenderer.setProjectionMatrix(environment.view.getOrthographicCamera().combined);
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for(StatusPoint point : getLidarPointCloudPointsArray()){
            int i = point.x;
            int j = point.y;
            Point a = new Point(i,j);
            Point b = new Point(
                    (int)(environment.roboMasters.teamBlue.get(0).getLidarPosition().x / 10),
                    (int)(environment.roboMasters.teamBlue.get(0).getLidarPosition().y / 10));
            float distance = (float) a.distance(b);
            float red;
            float green;
            float blue;
            if(distance <= 700){
                red = - 1f/700f * distance + 1.0f;
            }
            else {
                red = 0f;
            }
            if (distance <= 600){
                green = 1f/600f * distance;
            }
            else {
                green = 1.0f;
            }
            if (distance <= 500 && distance >= 200){
                blue = -1f/300f * distance + 5f/3f;
            }
            else {
                blue = 0f;
            }
            shapeRenderer.setColor(red,green,blue,1.0f);
            shapeRenderer.circle(
                    i / 100f,
                    j / 100f,
                    0.04f,10);
        }
        shapeRenderer.end();


        shapeRenderer2.setProjectionMatrix(environment.view.getOrthographicCamera().combined);
        shapeRenderer2.setAutoShapeType(true);
        shapeRenderer2.begin(ShapeRenderer.ShapeType.Line);
        for(RoboMaster roboMaster : RoboMasters.all){
            float x = roboMaster.getPosition().x / 1000f;
            float y = roboMaster.getPosition().y / 1000f;
            shapeRenderer2.setColor(1.0f,0,0,1.0f);
            shapeRenderer2.circle(
                    x,
                    y,
                    0.05f,
                    20);
            shapeRenderer2.setColor(1.0f,0,0,1.0f);
            shapeRenderer2.line(
                    x,
                    y,
                    x + (float) (2f * Math.sin(roboMaster.getFacingAngle())),
                    y + (float) (2f * Math.cos(roboMaster.getFacingAngle())));
        }
        shapeRenderer2.end();



        shapeRenderer3.setProjectionMatrix(environment.view.getOrthographicCamera().combined);
        shapeRenderer3.setAutoShapeType(true);
        shapeRenderer3.begin(ShapeRenderer.ShapeType.Filled);
        for(EnemiesObservationPoint position : RoboMasters.teamBlue.get(0).enemiesObservationSimulator.getDangerousZone()){
            int x = position.x;
            int y = position.y;
            switch (position.observationStatus){
                case 1 ->{
                    shapeRenderer3.setColor(0.3f,0f,0f,1.0f);
                }
                case 2 ->{
                    shapeRenderer3.setColor(0.6f,0f,0f,1.0f);
                }
                case 3 ->{
                    shapeRenderer3.setColor(1.0f,0f,0f,1.0f);
                }
            }
            shapeRenderer3.circle(
                    x / 100f,
                    y / 100f,
                    0.025f,10);
        }
        shapeRenderer3.end();

        shapeRenderer5.setProjectionMatrix(environment.view.getOrthographicCamera().combined);
        shapeRenderer5.setAutoShapeType(true);
        shapeRenderer5.begin(ShapeRenderer.ShapeType.Filled);
        for(int i = 0; i<RoboMasters.teamBlue.get(0).pathPlanning.nodeGrid.length; i+=10){
            for(int j = 0; j<RoboMasters.teamBlue.get(0).pathPlanning.nodeGrid[i].length; j+=10){
                if(RoboMasters.teamBlue.get(0).pathPlanning.nodeGrid[i][j]){
                    int x = i * 10;
                    int y = j * 10;
                    shapeRenderer5.setColor(1.0f,1.0f,1.0f,0.1f);
                    shapeRenderer5.circle(
                            x / 1000f,
                            y / 1000f,
                            0.025f,10);
                }
            }
        }
        shapeRenderer5.end();

        shapeRenderer4.setProjectionMatrix(environment.view.getOrthographicCamera().combined);
        shapeRenderer4.setAutoShapeType(true);
        shapeRenderer4.begin(ShapeRenderer.ShapeType.Filled);
        for(Position position : RoboMasters.teamBlue.get(0).pathPlanning.results){
            if(position!=null){
                int x = position.x * 10;
                int y = position.y * 10;
                shapeRenderer4.setColor(0.0f,1.0f,0f,1.0f);
                shapeRenderer4.circle(
                        x / 1000f,
                        y / 1000f,
                        0.025f,10);
            }
        }
        shapeRenderer4.end();

    }

    float timestate = 0;
    public void update(float delta){

    }

    public CopyOnWriteArrayList<StatusPoint> getLidarPointCloudPointsArray(){
        return RoboMasters.teamBlue.get(0).lidarObservation.other;
    }
}
