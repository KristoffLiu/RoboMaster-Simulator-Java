package com.kristoff.robomaster_simulator.robomasters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ProtoMaster extends RoboMaster{
    public ProtoMaster(){
        super(
                new TextureRegion(
                        new Texture("RoboMasters/ProtoMaster.png")));
    }


}
