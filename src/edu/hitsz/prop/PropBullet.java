package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.SprayShoot;
import edu.hitsz.strategy.StraightShoot;

public class PropBullet extends AbstractProp{
    public PropBullet(int locationX, int locationY, int speedX, int speedY){
        super(locationX, locationY, speedX, speedY);
    }

    public void forward() {
        super.forward();
    }



    @Override
    public void applyToAircraft(HeroAircraft heroAircraft) {
        heroAircraft.setStrategy(new SprayShoot());
        System.out.println("FireSupply active");
        recover(heroAircraft);

    }
}
