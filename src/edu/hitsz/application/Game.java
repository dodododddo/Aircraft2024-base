package edu.hitsz.application;

import edu.hitsz.aircraftfactory.*;
import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.dao.RecordDAO;
import edu.hitsz.dao.RecordDAOImpl;
import edu.hitsz.prop.*;
import edu.hitsz.propfactory.*;
import edu.hitsz.record.Record;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.HashMap;
import java.util.concurrent.*;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public class Game extends JPanel {

    protected int backGroundTop = 0;
    protected static int level;
    public static boolean musicOn;
    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;

    protected final HeroAircraft heroAircraft;
    protected final List<EnemyAircraft> enemyAircrafts;
    protected final List<BaseBullet> heroBullets;
    protected final List<BaseBullet> enemyBullets;
    protected final List<AbstractProp> props;

    protected final HashMap<String, EnemyAircraftFactory> enemyFactories;
    protected final HashMap<String, PropFactory> propFactories;


    /**
     * 屏幕中出现的敌机最大数量
     */
    private int enemyMaxNumber = 5;

    /**
     * 当前得分
     */
    private int score = 0;
    /**
     * 当前时刻
     */
    private int time = 0;

    private int boss_id = 1;   // 若为0则开局出现

    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int cycleDuration = 600;
    private int cycleTime = 0;

    /**
     * 游戏结束标志
     */
    private boolean gameOverFlag = false;



    public Game() {
        heroAircraft = HeroAircraft.getInstance();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();

        enemyFactories = new HashMap<String, EnemyAircraftFactory>();
        enemyFactories.put("EliteEnemy", new EliteEnemyFactory(2, 10, 30));
        enemyFactories.put("MobEnemy",new MobEnemyFactory(0, 10, 30));
        enemyFactories.put("ElitePlus", new ElitePlusFactory(2, 10, 30));
        enemyFactories.put("Boss", new BossFactory(2, 200));


        propFactories = new HashMap<String, PropFactory>();
        propFactories.put("PropBomb",new PropBombFactory());
        propFactories.put("PropBullet",new PropBulletFactory());
        propFactories.put("PropBlood", new PropBloodFactory(30));
        propFactories.put("PropBulletPlus", new PropBulletPlusFactory());




        /**
         * Scheduled 线程池，用于定时任务调度
         * 关于alibaba code guide：可命名的 ThreadFactory 一般需要第三方包
         * apache 第三方库： org.apache.commons.lang3.concurrent.BasicThreadFactory
         */
        this.executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("game-action-%d").daemon(true).build());

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

    }

    public void endingAction(){
        RecordDAO recordDAO = new RecordDAOImpl(Game.level);
        MusicManager.execute("game_over");
        executorService.shutdown();
        gameOverFlag = true;
        EndingBoard endingBoard = new EndingBoard(recordDAO);

        Main.cardPanel.add(endingBoard.getMainPanel());
        Main.cardLayout.last(Main.cardPanel);
        String userName = JOptionPane.showInputDialog("游戏结束，您的得分为: " + score + "\n" + "请输入名字记录得分: ");
        if(userName != null) {
            Record record = new Record(userName, score);
            recordDAO.insert(record);
            endingBoard.load(recordDAO);
        }

        System.out.println("Game Over!");
    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {
        MusicManager.activated = Game.musicOn;  // 总是使用Game类的musicOn设置
            MusicManager.execute("begin");

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            time += timeInterval;

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);
                // 新敌机产生

                if (enemyAircrafts.size() < enemyMaxNumber && !Boss.exist) {

                    if(score >= 400 * boss_id){
                        EnemyAircraft boss = enemyFactories.get("Boss").createEnemyAircraft();
                        enemyAircrafts.add(boss);
                        MusicManager.execute("boss");
                        boss_id++;
                    }
                    else {
                        int random_num = (int) (Math.random() * 6);
                        if (random_num == 0) {
                            enemyAircrafts.add(enemyFactories.get("ElitePlus").createEnemyAircraft());
                        } else if (random_num == 1 || random_num == 2) {
                            enemyAircrafts.add(enemyFactories.get("EliteEnemy").createEnemyAircraft());
                        } else if (random_num >= 3) {
                            enemyAircrafts.add(enemyFactories.get("MobEnemy").createEnemyAircraft());
                        }
                    }
                }
                // 飞机射出子弹
                shootAction();
            }

            // 子弹移动
            bulletsMoveAction();

            // 药品移动
            propsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            // 撞击检测
            crashCheckAction();

            // 后处理
            postProcessAction();

            //每个时刻重绘界面
            repaint();

            // 游戏结束检查英雄机是否存活
            if (heroAircraft.getHp() <= 0) {
                // 游戏结束
                endingAction();
            }

        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    //***********************
    //      Action 各部分
    //***********************

    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void shootAction() {
        // TODO 敌机射击
        for (AbstractAircraft enemy: enemyAircrafts){
            enemyBullets.addAll(enemy.shoot());
        }

        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void propsMoveAction(){
        for (AbstractProp prop : props){
            prop.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // TODO 敌机子弹攻击英雄
        for(BaseBullet bullet : enemyBullets){
            if (bullet.notValid()) {
                continue;
            }
            if(heroAircraft.crash(bullet)){
                MusicManager.execute("hit");
                bullet.vanish();
                heroAircraft.decreaseHp(bullet.getPower());
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (EnemyAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    MusicManager.execute("bullet");
                    if (enemyAircraft.notValid()) {
                        // TODO 获得分数，产生道具补给
                        if(enemyAircraft instanceof Boss){
                            MusicManager.execute("boss_defeated");
                        }
                        score += enemyAircraft.getScore();

                        for(int i = 0; i < (int)(Math.random() * (enemyAircraft.getPropNum() + 1));++i) {
                            // 创建0-propNum个道具
                            int random_num = (int) (Math.random() * 4);
                            if (random_num == 0) {
                                props.add(propFactories.get("PropBlood").createProp(enemyAircraft));
                            } else if (random_num == 1) {
                                props.add(propFactories.get("PropBomb").createProp(enemyAircraft));
                            } else if (random_num == 2) {
                                props.add(propFactories.get("PropBullet").createProp(enemyAircraft));
                            } else {
                                props.add(propFactories.get("PropBulletPlus").createProp(enemyAircraft));
                            }
                        }
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // Todo: 我方获得道具，道具生效
        for(AbstractProp prop : props){
            if(prop.notValid()){
                continue;
            }
            if(heroAircraft.crash(prop) || prop.crash(heroAircraft)){
                MusicManager.execute("supply");
                prop.vanish();
                prop.applyToAircraft(heroAircraft);
            }
        }



    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
    }


    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param  g
     */
    // 在子类实现paint方法

    protected void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    protected void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }

    public static void setLevel(int level){
        Game.level = level;
    }

    public static void setMusic(boolean music_on){
        Game.musicOn = music_on;
    }

}
