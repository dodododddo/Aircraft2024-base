package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.prop.PropBombObservable;
import edu.hitsz.strategy.SprayShoot;

import java.util.LinkedList;
import java.util.List;

public class ElitePlus extends EnemyAircraft implements PropBombObservable {

    public ElitePlus(int locationX, int locationY, int speedX, int speedY, int hp, int power, int shootNum) {
        super(locationX, locationY, speedX, speedY, hp, power, shootNum);
        direction = 1;
        propNum = 1;
        score = 40;
        setStrategy(new SprayShoot());
    }

    public ElitePlus(int locationX, int locationY, int speedX, int speedY, int hp){
        this(locationX, locationY, speedX, speedY, hp, 40, 6);
    }


    @Override
    public void update(){
        decreaseHp(this.maxHp / 2);
    }




}
