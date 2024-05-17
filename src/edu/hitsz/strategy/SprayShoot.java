package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.*;


import java.util.LinkedList;
import java.util.List;

public class SprayShoot implements AbstractStrategy{

    private double maxAngle = Math.PI / 4;
    public SprayShoot(){
    }

    @Override
    public List<BaseBullet> execute(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int locationX = aircraft.getLocationX();
        int locationY = aircraft.getLocationY();
        int power = aircraft.getPower();
        int shootNum = aircraft.getShootNum();
        for(int i = 0; i < shootNum; ++i) {
            int x = locationX + (int)(Math.sin(maxAngle / (shootNum - 1) * i - maxAngle / 2) * 20);
            int y = locationY + (int)(Math.cos(maxAngle / (shootNum - 1) * i - maxAngle / 2) * 20);
            int vX = aircraft.getSpeedX() + (int)(Math.sin(maxAngle / (shootNum - 1) * i - maxAngle / 2) * 10) * aircraft.getDirection();
            int vY = aircraft.getSpeedY() + (int)(Math.cos(maxAngle / (shootNum - 1) * i - maxAngle / 2) * 10) * aircraft.getDirection();

            if(aircraft instanceof HeroAircraft){
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
