package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class RingShoot implements AbstractStrategy{

    private int maxNumBullet = 20;
    public RingShoot(){

    }

    @Override
    public List<BaseBullet> execute(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int locationX = aircraft.getLocationX();
        int locationY = aircraft.getLocationY();
        int power = aircraft.getPower();
        for(int i = 0; i < maxNumBullet; ++i) {
            int x = locationX + (int)(Math.cos(2 * Math.PI / maxNumBullet * i) * 20);
            int y = locationY + (int)(Math.sin(2 * Math.PI / maxNumBullet * i) * 20);
            int vX = (int)(Math.cos(2 * Math.PI / maxNumBullet * i) * 20);
            int vY = (int)(Math.sin(2 * Math.PI / maxNumBullet * i) * 20);
            if(aircraft instanceof HeroAircraft) {
                BaseBullet bullet = new HeroBullet(x, y, vX, vY, power);
                res.add(bullet);
            }
            else {
                BaseBullet bullet = new EnemyBullet(x, y, vX, vY, power);
                res.add(bullet);
            }
        }

        return res;
    }
}
