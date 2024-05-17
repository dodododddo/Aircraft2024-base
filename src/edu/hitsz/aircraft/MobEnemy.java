package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.PropBombObservable;
import edu.hitsz.strategy.DontShoot;

import java.util.LinkedList;
import java.util.List;

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends EnemyAircraft implements PropBombObservable {

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, 0, 0);
        score = 10;
        propNum = 0;
        setStrategy(new DontShoot());
    }

    @Override
    public void update(){
        this.kill();
    }





}
