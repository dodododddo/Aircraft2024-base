package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.strategy.StraightShoot;

import java.util.List;
public abstract class AbstractProp extends AbstractFlyingObject{
    public AbstractProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    protected static Thread clock;

    @Override
    public void forward() {
        super.forward();

        // 判定 x 轴出界
        if (locationX <= 0 || locationX >= Main.WINDOW_WIDTH) {
            vanish();
        }

        // 判定 y 轴出界
        if (speedY > 0 && locationY >= Main.WINDOW_HEIGHT ) {
            // 向下飞行出界
            vanish();
        }else if (locationY <= 0){
            // 向上飞行出界
            vanish();
        }
    }

    public void recover(HeroAircraft heroAircraft){
        Runnable r = () -> {
            try {
                Thread.sleep(5000);
                heroAircraft.setStrategy(new StraightShoot());
            } catch (InterruptedException ignore){
            }
        };

        if(clock != null){
            clock.interrupt();
        }
        clock = new Thread(r);
        clock.start();

    }

    public abstract void applyToAircraft(HeroAircraft heroAircraft);

}
