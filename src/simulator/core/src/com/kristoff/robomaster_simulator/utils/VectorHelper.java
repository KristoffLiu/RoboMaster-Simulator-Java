package com.kristoff.robomaster_simulator.utils;

import com.badlogic.gdx.math.Vector2;

public class VectorHelper {
    public static Vector2 getVector(float magnitude, float degree){
        return getVector(magnitude, degree, true);
    }

    public static Vector2 getVector(float magnitude, float degree, boolean isRadian){
        Vector2 vector2 = new Vector2();
        if(!isRadian){
            degree = (float) Math.toRadians(degree);
        }
        vector2.x = (float) (Math.sin(degree) * magnitude);
        vector2.y = (float) (Math.cos(degree) * magnitude);
        return vector2;
    }
}
