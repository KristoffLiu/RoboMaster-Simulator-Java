package com.robomaster_libgdx.environment.libs;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Force {
    public Vector2 getForce(float magnitude, float degree){
        return getForce(magnitude, degree, true);
    }

    public Vector2 getForce(float magnitude, float degree, boolean isRadian){
        Vector2 vector2 = new Vector2();
        if(!isRadian){
            degree = (float) Math.toRadians(degree);
        }
        vector2.x = (float) (Math.cos(degree) * magnitude);
        vector2.y = (float) (Math.sin(degree) * magnitude);
        return vector2;
    }
}
