package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.MusicManager;
import edu.hitsz.basic.AbstractFlyingObject;

import java.util.LinkedList;
import java.util.List;

public class PropBomb extends AbstractProp{
    private final List<PropBombObservable> observers;
    public PropBomb(int locationX, int locationY, int speedX, int speedY){
        super(locationX, locationY, speedX, speedY);
        observers = new LinkedList<>();
    }

    public void forward() {
        super.forward();
    }

    @Override
    public void applyToAircraft(HeroAircraft heroAircraft) {
        System.out.println("BombSupply active");
        MusicManager.execute("bomb");
        notifyAllObservers();
    }

    public void register(PropBombObservable observer){
        observers.add(observer);
    }

    public void register(List<? extends AbstractFlyingObject> flyingObjects){
        for (AbstractFlyingObject flyingObject: flyingObjects){
            if(flyingObject instanceof PropBombObservable){
                register((PropBombObservable) flyingObject);
            }
        }
    }

    public void notifyAllObservers(){
        for(PropBombObservable observer: observers){
            observer.update();
        }
    }
}
