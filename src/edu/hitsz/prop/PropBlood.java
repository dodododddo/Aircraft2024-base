package edu.hitsz.prop;


import edu.hitsz.aircraft.HeroAircraft;

public class PropBlood extends AbstractProp{

    private int healing;
    public PropBlood(int locationX, int locationY, int speedX, int speedY, int healing){
        super(locationX, locationY, speedX, speedY);
        this.healing = healing;
    }

    public void forward() {
        super.forward();
    }

    @Override
    public void applyToAircraft(HeroAircraft heroAircraft) {
        heroAircraft.increaseHp(this.healing);
    }
}
