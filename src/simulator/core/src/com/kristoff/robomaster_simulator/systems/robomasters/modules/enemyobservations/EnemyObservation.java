package com.kristoff.robomaster_simulator.systems.robomasters.modules.enemyobservations;

import com.badlogic.gdx.math.Vector2;
import com.kristoff.robomaster_simulator.utils.BackendThread;

public class EnemyObservation extends BackendThread {
    Vector2 position = new Vector2();

    public EnemyObservation(){

    }

    public void setPosition(float x, float y){
        setPosition(new Vector2(x,y));
    }

    public void setPosition(Vector2 vector2){
        this.position = vector2;
    }
}
