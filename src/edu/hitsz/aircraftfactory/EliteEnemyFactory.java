package edu.hitsz.aircraftfactory;

import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class EliteEnemyFactory extends EnemyAircraftFactory {

    public EliteEnemyFactory(int aircraftSpeedX, int aircraftSpeedY, int aircraftHp, int aircraftPower, int aircraftShootNum){
        super(aircraftSpeedX, aircraftSpeedY, aircraftHp, aircraftPower, aircraftShootNum);
    }

    public EnemyAircraft createEnemyAircraft() {
        int random_num = (int)(Math.random() * 2 - 1);
        int direct = random_num > 0 ? 1: -1;
        return new EliteEnemy((int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05), direct * aircraftSpeedX, aircraftSpeedY, aircraftHp, aircraftPower, aircraftShootNum);
    }

    @Override
    public void update(){
        if(aircraftHp <= 200){
            aircraftHp += 10;
        }
        if(aircraftPower <= 50){
            aircraftPower += 1;
        }
    }
}
