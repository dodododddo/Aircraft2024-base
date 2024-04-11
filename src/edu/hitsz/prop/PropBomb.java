package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

public class PropBomb extends AbstractProp{
    public PropBomb(int locationX, int locationY, int speedX, int speedY){
        super(locationX, locationY, speedX, speedY);
    }

    public void forward() {
        super.forward();
    }

    @Override
    public void applyToAircraft(HeroAircraft heroAircraft) {
        System.out.println("BombSupply active");
    }
}
