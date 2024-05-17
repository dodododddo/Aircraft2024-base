package edu.hitsz.aircraftfactory;

import edu.hitsz.aircraft.EnemyAircraft;

import java.util.Random;

public abstract class EnemyAircraftFactory {

    protected int aircraftSpeedX;
    protected int aircraftSpeedY;
    protected int aircraftHp;
    protected int aircraftPower;
    protected int aircraftShootNum;

    public EnemyAircraftFactory(int aircraftSpeedX, int aircraftSpeedY, int aircraftHp, int aircraftPower, int aircraftShootNum){
        this.aircraftSpeedX = aircraftSpeedX;
        this.aircraftSpeedY = aircraftSpeedY;
        this.aircraftHp = aircraftHp;
        this.aircraftPower = aircraftPower;
        this.aircraftShootNum = aircraftShootNum;
    }

    public abstract EnemyAircraft createEnemyAircraft();

    public abstract void update();

}
