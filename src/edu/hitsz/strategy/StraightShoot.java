package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.*;


import java.util.LinkedList;
import java.util.List;

public class StraightShoot implements AbstractStrategy{
    private int maxNumBullet = 1;
    public StraightShoot(){
    }


    @Override
    public List<BaseBullet> execute(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();

        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + aircraft.getDirection() * 2;
        int vX = aircraft.getSpeedX();
        int vY = aircraft.getSpeedY() + aircraft.getDirection() * 3;
        if(aircraft instanceof HeroAircraft){
            vY = aircraft.getDirection() * 10;
        }


        for(int i = 0; i < maxNumBullet; ++i) {
            if(aircraft instanceof HeroAircraft) {
                BaseBullet bullet = new HeroBullet(x + (i * 2 - maxNumBullet + 1) * 10, y, vX, vY, aircraft.getPower());
                res.add(bullet);
            }
            else {
                BaseBullet bullet = new EnemyBullet(x + (i * 2 - maxNumBullet + 1) * 10, y, vX, vY, aircraft.getPower());
                res.add(bullet);
            }
        }

        return res;
    }
}
