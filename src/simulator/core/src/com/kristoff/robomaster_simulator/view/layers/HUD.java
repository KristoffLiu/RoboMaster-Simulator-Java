package com.kristoff.robomaster_simulator.view.layers;

import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.types.Enemy;
import com.kristoff.robomaster_simulator.teams.RoboMasters;
import com.kristoff.robomaster_simulator.view.renderers.EnvRenderer;
import com.kristoff.robomaster_simulator.view.ui.controls.Image;
import com.kristoff.robomaster_simulator.view.ui.pages.UIPage;

import java.util.ArrayList;

public class HUD extends UIPage {
    EnvRenderer envRenderer;
    ArrayList<Image> hpBarIndicatorBackgroundList = new ArrayList<>();
    ArrayList<Image> hpBarIndicatorForegroundList = new ArrayList<>();
    ArrayList<Image> roboMastersIDList = new ArrayList<>();
    ArrayList<Image> enemyInViewIndicators = new ArrayList<>();
    ArrayList<Image> isDeadIndicatorList = new ArrayList<>();

    Image lockedIndicator;

    public HUD(EnvRenderer envRenderer) {
        super(envRenderer.view.getViewport());
        this.envRenderer = envRenderer;
        for(RoboMaster roboMaster : RoboMasters.all){
            Image roboImage = new Image();
            roboImage.setTextureRegion("RoboMasters/Indicators/" + roboMaster.name + ".png");
            roboImage.setTag(roboMaster);
            roboImage.setScale(0.02f);
            roboMastersIDList.add(roboImage);

            Image hpBarIndicatorBackground = new Image();
            hpBarIndicatorBackground.setTextureRegion("RoboMasters/Indicators/HPBarBackground.png");
            hpBarIndicatorBackground.setTag(roboMaster);
            hpBarIndicatorBackground.setScale(0.02f);
            hpBarIndicatorBackgroundList.add(hpBarIndicatorBackground);

            Image hpBarIndicatorForeground = new Image();
            hpBarIndicatorForeground.setTextureRegion("RoboMasters/Indicators/HPBarForeground.png");
            hpBarIndicatorForeground.setTag(roboMaster);
            hpBarIndicatorForeground.setScale(0.02f);
            hpBarIndicatorForegroundList.add(hpBarIndicatorForeground);

            Image isDeadIndicator = new Image();
            isDeadIndicator.setTextureRegion("RoboMasters/Indicators/dead.png");
            isDeadIndicator.setTag(roboMaster);
            isDeadIndicator.setScale(0.00f);
            isDeadIndicatorList.add(isDeadIndicator);

            this.addUIElement(roboImage);
            this.addUIElement(hpBarIndicatorBackground);
            this.addUIElement(hpBarIndicatorForeground);
            this.addUIElement(isDeadIndicator);

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
        for(int i = 0; i < this.hpBarIndicatorBackgroundList.size(); i++){
            Image hpBarIndicatorBackground = hpBarIndicatorBackgroundList.get(i);
            Image hpBarIndicatorForeground = hpBarIndicatorForegroundList.get(i);

            RoboMaster roboMaster = (RoboMaster) hpBarIndicatorBackground.getTag();
            hpBarIndicatorBackground.setPosition((roboMaster.getX() - 280f) / 1000f , (roboMaster.getY() - 330f) / 1000f );
            hpBarIndicatorForeground.setPosition((roboMaster.getX() - 280f) / 1000f , (roboMaster.getY() - 330f) / 1000f );
            float percent = roboMaster.getHealthPercent() > 0 ? roboMaster.getHealthPercent() : 0;
            hpBarIndicatorForeground.setWidth(hpBarIndicatorBackground.getWidth() * percent);
        }
        for(Image inViewImage : this.enemyInViewIndicators){
            setInViewImage(inViewImage);
        }
        for(Image isDeadImage : this.isDeadIndicatorList){
            RoboMaster roboMaster = (RoboMaster) isDeadImage.getTag();
            if(!roboMaster.isAlive){
                isDeadImage.setScale(0.007f);
                isDeadImage.setPosition((roboMaster.getX() - 220f) / 1000f , (roboMaster.getY() - 200f) / 1000f );
            }
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