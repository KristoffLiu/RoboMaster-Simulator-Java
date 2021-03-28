package com.kristoff.robomaster_simulator.view.layers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.Strategy.SearchNode;
import com.kristoff.robomaster_simulator.robomasters.modules.RendererInputListener;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.teams.RoboMasters;
import com.kristoff.robomaster_simulator.utils.Position;
import com.kristoff.robomaster_simulator.view.renderers.EnvRenderer;

import java.util.ArrayList;

public class RoboMasterLayer extends VisualLayer {
    ArrayList<Label> roboMastersIDList = new ArrayList<>();
    SpriteBatch spriteBatch;
    ShapeRenderer pathShapeRenderer;
    ShapeRenderer costMapShapeRenderer;

    public RoboMasterLayer(EnvRenderer envRenderer) {
        super(envRenderer);
        spriteBatch = new SpriteBatch();
        renderRoboMasters();
        pathShapeRenderer = new ShapeRenderer();
        costMapShapeRenderer = new ShapeRenderer();
    }

    public void renderRoboMasters() {
        for (RoboMaster roboMaster : RoboMasters.all) {
            roboMaster.renderer.addListener(new RendererInputListener(roboMaster));
            this.addActor(roboMaster.renderer);
        }
    }

    public void addID(){

    }

    @Override
    public void act (float delta) {
        float scale = 1f / 1000f;
        super.act(delta);
    }

    @Override
    public void draw(){
        drawCostMap();
        drawPaths();
        super.draw();
    }

    public void drawPaths(){
        pathShapeRenderer.setProjectionMatrix(environment.view.getOrthographicCamera().combined);
        pathShapeRenderer.setAutoShapeType(true);
        pathShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        Position position1 = RoboMasters.teamBlue.get(0).strategyMaker.getDecisionNode().position;
//        if(position1!=null){
//            int x = position1.x * 10;
//            int y = position1.y * 10;
//            shapeRenderer4.setColor(0.0f,1.0f,0f,1.0f);
//            shapeRenderer4.circle(
//                    x / 1000f,
//                    y / 1000f,
//                    0.05f,10);
//        }
//        Position position2 = RoboMasters.teamBlue.get(1).strategyMaker.getDecisionNode().position;
//        if(position2!=null){
//            int x = position2.x * 10;
//            int y = position2.y * 10;
//            shapeRenderer4.setColor(0.0f,0f,1f,1.0f);
//            shapeRenderer4.circle(
//                    x / 1000f,
//                    y / 1000f,
//                    0.05f,10);
//        }

        for(SearchNode node : RoboMasters.teamBlue.get(0).strategyMaker.getPathNodes()){
            Position position = node.position;
            if(position!=null){
                int x = position.x * 10;
                int y = position.y * 10;
                pathShapeRenderer.setColor(0.0f,1.0f,0f,1.0f);
                pathShapeRenderer.circle(
                        x / 1000f,
                        y / 1000f,
                        0.05f,10);
            }
        }
        for(SearchNode node : RoboMasters.teamBlue.get(1).strategyMaker.getPathNodes()){
            Position position = node.position;
            if(position!=null){
                int x = position.x * 10;
                int y = position.y * 10;
                pathShapeRenderer.setColor(0.0f,0f,1f,1.0f);
                pathShapeRenderer.circle(
                        x / 1000f,
                        y / 1000f,
                        0.05f,10);
            }
        }
        pathShapeRenderer.end();
    }

    public void drawCostMap(){
        costMapShapeRenderer.setProjectionMatrix(environment.view.getOrthographicCamera().combined);
        costMapShapeRenderer.setAutoShapeType(true);
        costMapShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for(int i = 20; i < 829; i+=10){
            for(int j = 20; j < 469; j+=10){
                int x = i * 10;
                int y = j * 10;
                int cost = RoboMasters.getRoboMaster("Blue2").costMap.getCostMap()[i][j];
                float colorFloat = 0;
                if(cost <= 255f){
                    colorFloat = (255f - cost) / 255f;
                }

                costMapShapeRenderer.setColor(colorFloat,colorFloat,colorFloat,1.0f);
                costMapShapeRenderer.circle(
                        x / 1000f,
                        y / 1000f,
                        0.05f,10);
            }
        }
        costMapShapeRenderer.end();
    }

}
