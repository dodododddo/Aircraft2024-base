package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.prop.*;
import edu.hitsz.strategy.SprayShoot;
import edu.hitsz.strategy.StraightShoot;


import java.util.LinkedList;
import java.util.List;

public class EliteEnemy extends EnemyAircraft implements PropBombObservable {

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int power, int shootNum) {
        super(locationX, locationY, speedX, speedY, hp, power, shootNum);
        this.propNum = 1;
        this.score = 20;
        this.direction = 1;
        setStrategy(new StraightShoot());
    }

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        this(locationX, locationY, speedX, speedY, hp, 30, 1);
    }

    @Override
    public void update(){
        this.kill();
    }



}