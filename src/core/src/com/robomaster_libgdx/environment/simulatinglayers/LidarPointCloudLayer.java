package com.robomaster_libgdx.environment.simulatinglayers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.robomaster_libgdx.environment.Environment;
import com.robomaster_libgdx.environment.simulatinglayers.baselayers.VisualLayer;

import java.awt.*;

public class LidarPointCloudLayer extends VisualLayer {
    ShapeRenderer shapeRenderer;
    public Array<Vector2> lidarPointCloudPointsArray;
    public LidarPointCloudLayer(Environment environment) {
        super(environment);
        shapeRenderer = new ShapeRenderer();
        lidarPointCloudPointsArray = new Array<>();
    }

    @Override
    public void act (float delta) {
        float scale = 1f / 1000f;
        super.act(delta);
        update(delta);

    }

    @Override
    public void draw(){
        super.draw();
        shapeRenderer.setProjectionMatrix(environment.view.getOrthographicCamera().combined);
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for(Vector2 vector2 : getLidarPointCloudPointsArray()){
            int i = (int)vector2.x;
            int j = (int)vector2.y;
            Point a = new Point(i,j);
            Point b = new Point(
                    (int)(environment.teamBlue.get(0).getLidarPosition().x * 1000),
                    (int)(environment.teamBlue.get(0).getLidarPosition().y * 1000));
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
    }

    float timestate = 0;
    public void update(float delta){
        //flushArea();
        if(timestate > 1/60f){
            lidarPointCloudPointsArray = lidarPointCloudSimulate2(
                    environment.teamBlue.get(0).getLidarPosition().x,
                    environment.teamBlue.get(0).getLidarPosition().y);
            timestate = 0f;
        }
        else{
            timestate += delta;
        }
    }

    public Array<Vector2> lidarPointCloudSimulate(float c_x, float c_y){
        Array<Vector2> pointsArray = new Array<>();
        int centre_x = (int) (c_x * 1000);
        int centre_y = (int) (c_y * 1000);
        float precisionOfDegree = 0.05f;
        int x = 0;
        int y = 0;
        for(float degree = 0;degree < 360; degree += precisionOfDegree){
            float radian = degree * MathUtils.degreesToRadians;
            if(degree == 0 || degree == 180){
                for(y = 0;y <4800; y++){
                    if(degree == 0){
                        if(isPointContained(centre_x, centre_y + y)){
                            pointsArray.add(new Vector2(centre_x, centre_y + y));
                            break;
                        }
                    }
                    else {
                        if(isPointContained(centre_x, centre_y - y)){
                            pointsArray.add(new Vector2(centre_x, centre_y - y));
                            break;
                        }
                    }
                }
            }
            else if(degree == 90 || degree == 270){
                for(x = 0;x <8400; x++){
                    if(degree == 90){
                        if(isPointContained(centre_x + x, centre_y)){
                            pointsArray.add(new Vector2(centre_x + x, centre_y));
                            break;
                        }
                    }
                    else {
                        if(isPointContained(centre_x - x, centre_y)){
                            pointsArray.add(new Vector2(centre_x - x, centre_y));
                            break;
                        }
                    }
                }
            }
            else if(degree > 315 || degree < 45 || (degree > 135 && degree < 225)){
                for(y = 0;y <4800; y++){
                    int offset_x = (int) (Math.tan(radian) * y);
                    int offset_y = y;
                    if(degree > 135 && degree < 180){
                        offset_x = - offset_x;
                        offset_y = - offset_y;
                    }
                    else if(degree > 180 && degree < 225){
                        offset_x = - offset_x;
                        offset_y = - offset_y;
                    }
                    else{
                        offset_x = - offset_x;
                    }
                    if(isPointContained(centre_x + offset_x, centre_y + offset_y)){
                        pointsArray.add(new Vector2(centre_x + offset_x, centre_y + offset_y));
                        break;
                    }
                }
            }
            else{
                for(x = 0;x <8400; x++){
                    int offset_x = x;
                    int offset_y = (int) (x / Math.tan(radian));
                    if(degree <= 135){
                        offset_y = - offset_y;
                    }
                    else if(degree >= 225){
                        offset_x = - offset_x;
                        offset_y = - offset_y;
                    }
                    else if(degree <= 315){
                        offset_x = - offset_x;
                    }
                    if(isPointContained(centre_x + offset_x, centre_y + offset_y)){
                        pointsArray.add(new Vector2(centre_x + offset_x, centre_y + offset_y));
                        break;
                    }
                }
            }
            x = 0;
            y = 0;
        }
        return pointsArray;
    }

    public Array<Vector2> lidarPointCloudSimulate2(float c_x, float c_y){
        Array<Vector2> pointsArray = new Array<>();
        int centre_x = (int) (c_x * 1000);
        int centre_y = (int) (c_y * 1000);
        float precisionOfDegree = 30f;
        for(float degree = 0;degree < 360; degree += precisionOfDegree){
            float radian = degree * MathUtils.degreesToRadians;
            float minDistance = 0;
            Vector2 closestVector = new Vector2();
            Vector2 direction = new Vector2((float) Math.sin(radian),(float)Math.cos(radian));
            for(int i = 0; i < getPointMatrixArray().size; i++){
                Vector2 directionToThePoint = new Vector2(getPointMatrixArray().get(i).x - centre_x, getPointMatrixArray().get(i).y - centre_y);
                if(direction.hasSameDirection(directionToThePoint)){
                    if(minDistance == 0 || minDistance > directionToThePoint.len()){
                        minDistance = directionToThePoint.len();
                        closestVector = directionToThePoint;
                    }
                }
            }
            pointsArray.add(closestVector);
        }
        return pointsArray;
    }


    public Array<Vector2> getLidarPointCloudPointsArray(){
        return lidarPointCloudPointsArray;
    }

    public boolean isPointContained(int x, int y){
        return environment.matrixLayer.pointMatrix[x][y];
    }

    public Array<Vector2> getPointMatrixArray(){
        return environment.matrixLayer.pointMatrixArray;
    }

}
