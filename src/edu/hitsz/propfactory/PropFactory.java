package edu.hitsz.propfactory;

import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.prop.AbstractProp;
import java.util.List;

public abstract class PropFactory {
    public PropFactory(){}

    public abstract AbstractProp createProp(EnemyAircraft enemyAircraft);
}
