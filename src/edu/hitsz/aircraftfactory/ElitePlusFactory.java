package edu.hitsz.aircraftfactory;

import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.ElitePlus;
import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class ElitePlusFactory extends EnemyAircraftFactory{
    public ElitePlusFactory(int aircraftSpeedX, int aircraftSpeedY, int aircraftHp, int aircraftPower, int aircraftShootNum){
        super(aircraftSpeedX, aircraftSpeedY, aircraftHp, aircraftPower, aircraftShootNum);

    }

    @Override
    public EnemyAircraft createEnemyAircraft() {
        int random_num = (int)(Math.random() * 2 - 1);
        int direct = random_num > 0 ? 1: -1;
        return new ElitePlus((int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05), direct * aircraftSpeedX, aircraftSpeedY, aircraftHp, aircraftPower, aircraftShootNum);
    }

    @Override
    public void update(){
        if(aircraftHp <= 300){
            aircraftHp += 10;
        }
        if(aircraftPower <= 90){
            aircraftPower += 1;
        }
        if(aircraftShootNum <= 12){
            aircraftShootNum += 1;
        }
    }
}
