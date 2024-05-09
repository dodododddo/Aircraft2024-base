package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.strategy.RingShoot;

import java.util.LinkedList;
import java.util.List;

public class Boss extends EnemyAircraft{

    public static boolean exist;


    public Boss(int locationX, int locationY, int speedX, int hp) {
        super(locationX, locationY, speedX, 0, hp);
        Boss.exist = true;
        power = 30;
        score = 100;
        propNum = 3;
        setStrategy(new RingShoot());
    }


    public int getHp() {
        return super.getHp();
    }

    public void forward() {
        super.forward();
    }




    @Override
    public int getScore(){
        return score;
    }

    @Override
    public int getPropNum() { return propNum;};

    @Override
    public void vanish(){
        isValid = false;
        Boss.exist = false;
    }
}
