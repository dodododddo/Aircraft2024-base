package edu.hitsz.application;

import edu.hitsz.aircraft.Boss;
import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraftfactory.*;
import edu.hitsz.propfactory.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

public class GameHard extends Game{
    private final int stone = 10;
    private int clk = 0;

    public GameHard(){
        super();
    }

    @Override
    public void initParam(){
        enemyMaxNumber = 8;
        cycleDuration = 400;
        elitePossibility = 0.3;
        backGroundImage = ImageManager.BACKGROUND_HARD_IMAGE;
    }

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
    public void initFactory(){
        heroAircraft = HeroAircraft.getInstance();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();

        enemyFactories = new HashMap<String, EnemyAircraftFactory>();
        enemyFactories.put("EliteEnemy", new EliteEnemyFactory(3, 10, 40, 30, 1));
        enemyFactories.put("MobEnemy",new MobEnemyFactory(0, 10, 30));
        enemyFactories.put("ElitePlus", new ElitePlusFactory(2, 10, 30, 50, 8));
        enemyFactories.put("Boss", new BossFactory(2, 400, 60, 25));


        propFactories = new HashMap<String, PropFactory>();
        propFactories.put("PropBomb",new PropBombFactory());
        propFactories.put("PropBullet",new PropBulletFactory());
        propFactories.put("PropBlood", new PropBloodFactory(50));
        propFactories.put("PropBulletPlus", new PropBulletPlusFactory());
    }


    public void updateFactory(){

        for(EnemyAircraftFactory factory: enemyFactories.values()){
            factory.update();
        }
        System.out.println("敌机属性提升");
    }

    public void updateParam(){

        if(enemyMaxNumber < 12){
            enemyMaxNumber += 1;
        }
        if(cycleDuration > 350){
            cycleDuration -= 5;
        }
        if(elitePossibility < 0.5){
            elitePossibility += 0.05;
        }

        System.out.println("敌机上限增加，双方射速提高，敌机刷新频率加快，精英机出现概率提高");
    }

    @Override
    public void updateDifficulty(){
        clk = (clk + 1) % stone;
        if(clk == 0) {
            updateParam();
            updateFactory();
        }
    }
}
