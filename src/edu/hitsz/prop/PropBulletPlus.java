package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.RingShoot;
import edu.hitsz.strategy.SprayShoot;

public class PropBulletPlus extends AbstractProp{
    public PropBulletPlus(int locationX, int locationY, int speedX, int speedY){
        super(locationX, locationY, speedX, speedY);
    }

    public void forward() {
        super.forward();
    }

    @Override
    public void applyToAircraft(HeroAircraft heroAircraft) {
        heroAircraft.setStrategy(new RingShoot());
        System.out.println("FireSupply active");
        recover(heroAircraft);
    }
}
