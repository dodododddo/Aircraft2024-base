package edu.hitsz.application;

import edu.hitsz.aircraft.Boss;
import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraftfactory.EliteEnemyFactory;
import edu.hitsz.aircraftfactory.BossFactory;
import edu.hitsz.aircraftfactory.ElitePlusFactory;
import edu.hitsz.aircraftfactory.EnemyAircraftFactory;
import edu.hitsz.aircraftfactory.MobEnemyFactory;
import edu.hitsz.propfactory.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

public class GameMedium extends Game{
    public GameMedium(){
        super();
    }

    private int clk = 0;
    private final int stone = 20;

    @Override
    public void createAircraftAction(){
        if (enemyAircrafts.size() < enemyMaxNumber && !Boss.exist) {

            if(score >= 400 * boss_id){
                EnemyAircraft boss = enemyFactories.get("Boss").createEnemyAircraft();
                enemyAircrafts.add(boss);
                MusicManager.execute("boss");
                boss_id++;
            }
            else {
                int randomNum = (int) (Math.random() * 100);
                int r = (int)(100 * elitePossibility);
                if (randomNum < r / 4) {
                    enemyAircrafts.add(enemyFactories.get("ElitePlus").createEnemyAircraft());
                } else if (randomNum >= r / 4 && randomNum < r) {
                    enemyAircrafts.add(enemyFactories.get("EliteEnemy").createEnemyAircraft());
                } else if (randomNum >= r) {
                    enemyAircrafts.add(enemyFactories.get("MobEnemy").createEnemyAircraft());
                }
            }
        }
    }

    @Override
    public void initParam(){
        enemyMaxNumber = 6;
        cycleDuration = 600;
        elitePossibility = 0.2;
        backGroundImage = ImageManager.BACKGROUND_MEDIUM_IMAGE;
    }

    @Override
    public void initFactory(){
        heroAircraft = HeroAircraft.getInstance();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();

        enemyFactories = new HashMap<String, EnemyAircraftFactory>();
        enemyFactories.put("EliteEnemy", new EliteEnemyFactory(3, 10, 50, 30, 1));
        enemyFactories.put("MobEnemy",new MobEnemyFactory(0, 10, 30));
        enemyFactories.put("ElitePlus", new ElitePlusFactory(2, 10, 30, 50, 6));
        enemyFactories.put("Boss", new BossFactory(2, 200, 50, 20));


        propFactories = new HashMap<String, PropFactory>();
        propFactories.put("PropBomb",new PropBombFactory());
        propFactories.put("PropBullet",new PropBulletFactory());
        propFactories.put("PropBlood", new PropBloodFactory(50));
        propFactories.put("PropBulletPlus", new PropBulletPlusFactory());
    }

    @Override
    public void updateDifficulty(){
        clk = (clk + 1) % stone;
        if(clk == 0) {
            updateParam();
            updateFactory();
        }
    }

    public void updateFactory(){
        System.out.println("敌机属性提升");
        for(EnemyAircraftFactory factory: enemyFactories.values()){
            if(!(factory instanceof BossFactory))
                factory.update();
        }
    }

    public void updateParam(){

        if(elitePossibility < 0.4){
            elitePossibility += 0.05;
        }
        System.out.println("精英机出现概率提高");
    }
}
