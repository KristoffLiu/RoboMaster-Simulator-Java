package com.kristoff.robomaster_simulator.teams.enemyobservations;

import com.kristoff.robomaster_simulator.utils.Position;

public class EnemiesObservationPoint extends Position {
    public int observationStatus = 0;

    public EnemiesObservationPoint(int x, int y, int observationStatus){
        this.x = x;
        this.y = y;
        this.observationStatus = observationStatus;
    }
}
