package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.strategy.SprayShoot;

import java.util.LinkedList;
import java.util.List;

public class ElitePlus extends EnemyAircraft{

    public ElitePlus(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        direction = 1;
        power = 30;
        propNum = 1;
        score = 40;
        setStrategy(new SprayShoot());
    }


    public int getHp() {
        return super.getHp();
    }

    public void forward() {
        super.forward();
        if (this.locationY >= 768) {
            this.vanish();
        }
    }



    @Override
    public int getScore(){
        return score;
    }

    @Override
    public int getPropNum(){ return propNum;};
}
