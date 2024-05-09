package edu.hitsz.propfactory;

import edu.hitsz.aircraft.Boss;
import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.PropBlood;
import edu.hitsz.prop.PropBulletPlus;

import java.util.LinkedList;
import java.util.List;

public class PropBulletPlusFactory extends PropFactory {
    public PropBulletPlusFactory() {
    }

    @Override
    public AbstractProp createProp(EnemyAircraft enemyAircraft) {
        assert (enemyAircraft.notValid());

        int x = enemyAircraft.getLocationX();
        int y = enemyAircraft.getLocationY();
        int speedX = 0;
        int speedY;
        if (enemyAircraft instanceof Boss){
            speedY = 10;
        }
        else{
            speedY = enemyAircraft.getSpeedY();
        }

        int random_x = (int)(Math.random() * 3) - 1;
        int random_y = (int)(Math.random() * 3) - 1;

        return  new PropBulletPlus(x + random_x * 20, y + random_y * 20,
                speedX + random_x * 2, speedY + random_y * 2);
    }
}
