package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.strategy.AbstractStrategy;

import java.util.List;

/**
 * 所有种类飞机的抽象父类：
 * 敌机（BOSS, ELITE, MOB），英雄飞机
 *
 * @author hitsz
 */
public abstract class AbstractAircraft extends AbstractFlyingObject {
    /**
     * 生命值
     */
    protected int maxHp;
    protected int hp;
    protected AbstractStrategy strategy;
    protected int direction;
    protected int power;
    protected int shootNum;

    public AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp, int power, int shootNum) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
        this.power = power;
        this.shootNum = shootNum;
    }

    public void decreaseHp(int decrease){
        hp -= decrease;
        if(hp <= 0){
            hp=0;
            vanish();
        }
    }

    public void increaseHp(int increase){
        hp += increase;
        if(hp > maxHp){
            hp = maxHp;
        }
    }

    public int getHp() {
        return hp;
    }

    public int getDirection() {return direction;}

    public int getPower() {return power;}

    public int getShootNum(){
        return shootNum;
    }

    /**
     * 飞机射击方法，可射击对象必须实现
     * @return
     *  可射击对象需实现，返回子弹
     *  非可射击对象空实现，返回null
     */
    public List<BaseBullet> shoot(){
        return strategy.execute(this);
    }

    public void setStrategy(AbstractStrategy strategy){
        this.strategy = strategy;
    }

    public void setShootNum(int shootNum){
        this.shootNum = shootNum;
    }



}


