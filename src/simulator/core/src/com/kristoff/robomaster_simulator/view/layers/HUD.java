package com.kristoff.robomaster_simulator.view.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.teams.RoboMasters;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.view.renderers.EnvRenderer;
import com.kristoff.robomaster_simulator.view.ui.controls.TextBlock;
import com.kristoff.robomaster_simulator.view.ui.controls.UIElement;
import com.kristoff.robomaster_simulator.view.ui.pages.UIPage;

import java.util.ArrayList;

public class HUD extends UIPage {
    EnvRenderer envRenderer;
    ArrayList<TextBlock> roboMastersIDList = new ArrayList<>();

    public HUD(EnvRenderer envRenderer) {
        super(envRenderer.view.getViewport());
        this.envRenderer = envRenderer;
        for(RoboMaster roboMaster : RoboMasters.all){
            TextBlock textBlock = new TextBlock();
            textBlock.setTag(roboMaster);
            textBlock.setText(roboMaster.name);
            textBlock.setFontSize(1.4f);
            textBlock.setFontColor(1,1,1,1);
            textBlock.setRelativePosition(100,100, UIElement.HorizontalAlignment.LEFT_ALIGNMENT, UIElement.VerticalAlignment.TOP_ALIGNMENT);
            this.addUIElement(textBlock);
            roboMastersIDList.add(textBlock);
        }
    }

    public void addID(){

    }

    @Override
    public void act(float delta){
        super.act(delta);
        for(TextBlock textBlock : this.roboMastersIDList){
            RoboMaster roboMaster = (RoboMaster) textBlock.getTag();
            textBlock.setRelativePosition(100,100, UIElement.HorizontalAlignment.LEFT_ALIGNMENT, UIElement.VerticalAlignment.TOP_ALIGNMENT);
            System.out.println(textBlock.label.getX());
            System.out.println(textBlock.label.getY());
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
