package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.PropBlood;
import edu.hitsz.prop.PropBomb;
import edu.hitsz.prop.PropBullet;
import edu.hitsz.strategy.SprayShoot;
import edu.hitsz.strategy.StraightShoot;


import java.util.LinkedList;
import java.util.List;

public class EliteEnemy extends EnemyAircraft {

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        power = 30;
        direction = 1;
        propNum = 1;
        score = 20;
        setStrategy(new StraightShoot());
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
    public int getPropNum() { return propNum;};

}