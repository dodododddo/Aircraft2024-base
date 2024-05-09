package edu.hitsz.aircraftfactory;

import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class MobEnemyFactory extends EnemyAircraftFactory {
    public MobEnemyFactory(int aircraftSpeedX, int aircraftSpeedY, int aircraftHp){
        super(aircraftSpeedX, aircraftSpeedY, aircraftHp);
    }

    public EnemyAircraft createEnemyAircraft() {
        return new MobEnemy((int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05), aircraftSpeedX, aircraftSpeedY, aircraftHp);
    }
}
