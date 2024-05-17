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

    public Boss(int locationX, int locationY, int speedX, int hp, int power, int shootNum) {
        super(locationX, locationY, speedX, 0, hp, power, shootNum);
        Boss.exist = true;
        this.power = power;
        this.shootNum = shootNum;
        this.score = 100;
        this.propNum = 3;
        setStrategy(new RingShoot());
    }

    public Boss(int locationX, int locationY, int speedX, int hp) {
        this(locationX, locationY, speedX, hp, 30, 20);
    }


    @Override
    public void vanish(){
        isValid = false;
        Boss.exist = false;
    }
}
