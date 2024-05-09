package edu.hitsz.aircraftfactory;

import edu.hitsz.aircraft.EnemyAircraft;

import java.util.Random;

public abstract class EnemyAircraftFactory {

    protected int aircraftSpeedX;
    protected int aircraftSpeedY;
    protected int aircraftHp;

    public EnemyAircraftFactory(int aircraftSpeedX, int aircraftSpeedY, int aircraftHp){
        this.aircraftSpeedX = aircraftSpeedX;
        this.aircraftSpeedY = aircraftSpeedY;
        this.aircraftHp = aircraftHp;
    }
    public abstract EnemyAircraft createEnemyAircraft();
}
