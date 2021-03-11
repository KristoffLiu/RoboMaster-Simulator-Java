package com.kristoff.robomaster_simulator.view.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.robomaster.types.Enemy;
import com.kristoff.robomaster_simulator.robomasters.teams.RoboMasters;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.view.renderers.EnvRenderer;
import com.kristoff.robomaster_simulator.view.ui.controls.Image;
import com.kristoff.robomaster_simulator.view.ui.controls.TextBlock;
import com.kristoff.robomaster_simulator.view.ui.controls.UIElement;
import com.kristoff.robomaster_simulator.view.ui.pages.UIPage;

import java.util.ArrayList;

public class HUD extends UIPage {
    EnvRenderer envRenderer;
    ArrayList<Image> roboMastersIDList = new ArrayList<>();
    ArrayList<Image> enemyInViewIndicators = new ArrayList<>();
    Image lockedIndicator = new Image();

    public HUD(EnvRenderer envRenderer) {
        super(envRenderer.view.getViewport());
        this.envRenderer = envRenderer;
        for(RoboMaster roboMaster : RoboMasters.all){
            Image roboImage = new Image();
            roboImage.setTextureRegion("RoboMasters/Indicators/" + roboMaster.name + ".png");
            roboImage.setTag(roboMaster);
            roboImage.setScale(0.02f);
            roboMastersIDList.add(roboImage);
            this.addUIElement(roboImage);

            if(RoboMasters.teamRed.contains(roboMaster)){
                Image enemyInViewIndicatorImage = new Image();
                enemyInViewIndicatorImage.setTextureRegion("RoboMasters/Indicators/InView.png");
                enemyInViewIndicatorImage.setTag(roboMaster);
                enemyInViewIndicatorImage.setScaleX(0.015f);
                enemyInViewIndicatorImage.setScaleY(0.018f);
                enemyInViewIndicators.add(enemyInViewIndicatorImage);
                this.addUIElement(enemyInViewIndicatorImage);
            }
        }
        lockedIndicator = new Image();
        lockedIndicator.setTextureRegion("RoboMasters/Indicators/IsLocked.png");
        lockedIndicator.setScaleX(0.015f);
        lockedIndicator.setScaleY(0.018f);
        this.addUIElement(lockedIndicator);
    }

    @Override
    public void act(float delta){
        super.act(delta);
        for(Image roboImage : this.roboMastersIDList){
            RoboMaster roboMaster = (RoboMaster) roboImage.getTag();
            roboImage.setPosition((roboMaster.getX() - 310f) / 1000f , (roboMaster.getY() + 210f) / 1000f );
        }
        for(Image inViewImage : this.enemyInViewIndicators){
            setInViewImage(inViewImage);
        }
    }

    public void setInViewImage(Image inViewImage){
        Enemy enemy = (Enemy) inViewImage.getTag();
        if(enemy.isInTheView()) inViewImage.setTextureRegion("RoboMasters/Indicators/InView.png");
        else inViewImage.setTextureRegion("RoboMasters/Indicators/Lost.png");
        inViewImage.setPosition((enemy.getX() + 320f) / 1000f , (enemy.getY() - 220f) / 1000f );
        if(enemy.isLocked()){
            lockedIndicator.setPosition((enemy.getX() + 320f) / 1000f , (enemy.getY() - 0f) / 1000f );
        }
    }
}


//    EnvRenderer envRenderer;
//    ArrayList<BitmapFont> roboMastersIDList = new ArrayList<>();
//
//    //绘图，封装openGL
//    SpriteBatch spriteBatch;
//    // BitmapFont是libgdx提供的文字显示用类，内部将图片转化为可供opengl调用的
//    // 文字贴图(默认不支持中文)。
//
//
//    public HUD(EnvRenderer envRenderer) {
//        super(envRenderer.view.getViewport());
//        this.envRenderer = envRenderer;
//        spriteBatch = new SpriteBatch();
//        for(RoboMaster roboMaster : RoboMasters.all){
//            BitmapFont bitmapFont = new BitmapFont(Gdx.files.internal("font/ImpactFont.fnt"),
//                    Gdx.files.internal("font/ImpactFont.png"), false);
//            roboMastersIDList.add(bitmapFont);
//        }
//    }
//
//    public void addID(){
//
//    }
//
//    @Override
//    public void act(float delta){
//        super.act(delta);
//        spriteBatch.begin();
//        for(int i = 0; i < RoboMasters.all.size(); i ++){
//            RoboMaster roboMaster = RoboMasters.all.get(i);
//            BitmapFont bitmapFont = roboMastersIDList.get(i);
//            bitmapFont.draw(spriteBatch, roboMaster.name, 100, 200);
//        }
//        spriteBatch.end();
//    }
