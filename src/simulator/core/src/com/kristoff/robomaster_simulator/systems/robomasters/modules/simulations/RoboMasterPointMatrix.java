package com.kristoff.robomaster_simulator.systems.robomasters.modules.simulations;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMasters;
import com.kristoff.robomaster_simulator.systems.simulators.MatrixSimulator;

import static java.lang.Math.*;

public class RoboMasterPointMatrix {
    RoboMaster thisRoboMaster;

    public Array<RoboMasterPoint> current;
    public Array<RoboMasterPoint> previous;

    Runnable runnable;

    public RoboMasterPointMatrix(RoboMaster roboMaster){
        this.thisRoboMaster = roboMaster;
        current = new Array<>();
        previous = new Array<>();

        runnable = new Runnable() {
            @Override
            public void run() {
                if(thisRoboMaster != RoboMasters.teamBlue.get(0)){
                    previous = current;
                    current = new Array<>();
                    double angle = thisRoboMaster.mainBody.body.getTransform().getRotation();
                    if(angle > 2*PI){
                        angle = angle % (2*PI);
                    }
                    updateMatrix(
                            angle,
                            new Vector2(
                                    thisRoboMaster.getPosition().x * 1000,
                                    thisRoboMaster.getPosition().y * 1000),
                            (int) (thisRoboMaster.property.width * 1000),
                            (int) (thisRoboMaster.property.height * 1000),
                            current
                    );
                }
            }
        };
    }

    public void step(){
        runnable.run();
    }

    private void updateMatrix(double angle, Vector2 center, int width, int height, Array<RoboMasterPoint> matrix){
        Vector2 a = getVertex(angle, new Vector2(center.x - width/2,center.y + height/2), center);
        Vector2 b = getVertex(angle, new Vector2(center.x + width/2,center.y + height/2), center);
        Vector2 c = getVertex(angle, new Vector2(center.x - width/2,center.y - height/2), center);
        Vector2 d = getVertex(angle, new Vector2(center.x + width/2,center.y - height/2), center);
        addLineByTwoPoint(a, b, matrix);
        addLineByTwoPoint(b, d, matrix);
        addLineByTwoPoint(c, d, matrix);
        addLineByTwoPoint(c, a, matrix);
    }

    private Vector2 getVertex(double rotatedAngle, Vector2 orginalPosition, Vector2 centre_p){
        return new Vector2(
                (float) (cos(rotatedAngle) * (orginalPosition.x-centre_p.x) - sin(rotatedAngle) * (orginalPosition.y-centre_p.y) + centre_p.x),
                (float) (sin(rotatedAngle) * (orginalPosition.x-centre_p.x) + cos(rotatedAngle) * (orginalPosition.y-centre_p.y) + centre_p.y)
        );
    }

    private void addLineByTwoPoint(Vector2 a, Vector2 b, Array<RoboMasterPoint> matrix){
        float gradient = (a.y - b.y)/(a.x - b.x);
        MatrixSimulator.MatrixPointStatus pointStatus = MatrixSimulator.MatrixPointStatus.TeamBlue;
        if(thisRoboMaster.team == RoboMasters.teamRed){
            pointStatus = MatrixSimulator.MatrixPointStatus.TeamRed;
        }
        if( Math.abs(a.x - b.x) >= Math.abs(a.y - b.y)){
            if(a.x < b.x){
                for(int i = 0 ; i < b.x - a.x; i++ ){
                    matrix.add(new RoboMasterPoint((int)a.x + i, (int)(a.y + gradient * i),pointStatus));
                }
            }
            else{
                for(int i = 0 ; i < a.x - b.x; i++ ){
                    matrix.add(new RoboMasterPoint((int)b.x + i, (int)(b.y + gradient * i),pointStatus));
                }
            }
        }
        else {
            if(a.y < b.y){
                for(int i = 0 ; i < b.y - a.y; i++ ){
                    matrix.add(new RoboMasterPoint((int)(a.x + i / gradient), (int)(a.y+i),pointStatus));
                }
            }
            else{
                for(int i = 0 ; i < a.y - b.y; i++ ){
                    matrix.add(new RoboMasterPoint((int)(b.x + i / gradient), (int)(b.y+i),pointStatus));
                }
            }
        }
    }
}
