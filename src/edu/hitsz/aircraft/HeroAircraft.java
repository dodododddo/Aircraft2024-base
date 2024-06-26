package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.strategy.*;

import java.util.LinkedList;
import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    /**攻击方式 */

    /**
     * 子弹一次发射数量
     */


    /**
     * 子弹伤害
     */

    private volatile static HeroAircraft instance;

    /**
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp    初始生命值
     */


    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp, int power, int shootNum) {
        super(locationX, locationY, speedX, speedY, hp, power, shootNum);
        direction = -1;
        setStrategy(new StraightShoot());
    }


    public static HeroAircraft getInstance(){
        if(instance == null){
            synchronized (HeroAircraft.class) {
                if (instance == null) {
                    instance = new HeroAircraft(
                            Main.WINDOW_WIDTH / 2,
                            Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                            0, 0, 200, 30, 1);
                }
            }
        }
        return instance;
    }



    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }


    @Override
    public void decreaseHp(int decrease){
        hp -= decrease;
        if(hp <= 0){
            hp=0;
            vanish();
        }
        System.out.println("hit! hp:" + hp);
    }

    @Override
    public void increaseHp(int increase){
        hp += increase;
        if(hp > maxHp){
            hp = maxHp;
        }
        System.out.println("healing! hp:" + hp);
    }

    public int getMaxHp(){
        return maxHp;
    }

}
