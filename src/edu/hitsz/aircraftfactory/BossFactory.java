package edu.hitsz.aircraftfactory;

import edu.hitsz.aircraft.Boss;
import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class BossFactory extends EnemyAircraftFactory{
    public BossFactory(int aircraftSpeedX, int aircraftHp){
        super(aircraftSpeedX, 0, aircraftHp);
    }

    @Override
    public EnemyAircraft createEnemyAircraft() {
        assert !Boss.exist;
        return new Boss(
                    Main.WINDOW_WIDTH / 2,
                    ImageManager.BOSS_IMAGE.getHeight() / 2,
                    aircraftSpeedX, aircraftHp);


    }
}
