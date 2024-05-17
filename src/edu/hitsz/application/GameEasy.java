package edu.hitsz.application;

import edu.hitsz.aircraft.Boss;
import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraftfactory.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.propfactory.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

public class GameEasy extends Game{
    private boolean logging = true;
    public GameEasy(){
        super();
    }

    @Override
    public void initFactory(){
        heroAircraft = HeroAircraft.getInstance();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();

        enemyFactories = new HashMap<String, EnemyAircraftFactory>();
        enemyFactories.put("EliteEnemy", new EliteEnemyFactory(2, 10, 30, 20, 1));
        enemyFactories.put("MobEnemy",new MobEnemyFactory(0, 10, 30));
        enemyFactories.put("ElitePlus", new ElitePlusFactory(2, 10, 30, 30, 6));
//        enemyFactories.put("Boss", new BossFactory(2, 200, 50, 20));


        propFactories = new HashMap<String, PropFactory>();
        propFactories.put("PropBomb",new PropBombFactory());
        propFactories.put("PropBullet",new PropBulletFactory());
        propFactories.put("PropBlood", new PropBloodFactory(50));
        propFactories.put("PropBulletPlus", new PropBulletPlusFactory());
    }

    @Override
    public void createAircraftAction(){
        if (enemyAircrafts.size() < enemyMaxNumber) {
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

    @Override
    public void updateDifficulty(){
        //简单模式不提高难度
        if(logging){
            System.out.println("note: 简单模式难度不随时间提升!");
        }
        logging = false;
    }

    @Override
    public void initParam(){
        enemyMaxNumber = 4;
        cycleDuration = 800;
        elitePossibility = 0.1;
        backGroundImage = ImageManager.BACKGROUND_EASY_IMAGE;
    }
}

