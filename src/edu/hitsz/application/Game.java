package edu.hitsz.application;

import edu.hitsz.aircraftfactory.*;
import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.EnemyBullet;
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
public abstract class Game extends JPanel {

    protected int backGroundTop = 0;
    protected static int level;
    public static boolean musicOn;
    /**
     * Scheduled 线程池，用于任务调度
     */
    protected final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    protected int timeInterval = 40;

    protected HeroAircraft heroAircraft;
    protected List<EnemyAircraft> enemyAircrafts;
    protected List<BaseBullet> heroBullets;
    protected List<BaseBullet> enemyBullets;
    protected List<AbstractProp> props;

    protected HashMap<String, EnemyAircraftFactory> enemyFactories;
    protected HashMap<String, PropFactory> propFactories;


    /**
     * 屏幕中出现的敌机最大数量
     */
    protected int enemyMaxNumber;

    protected double elitePossibility;
    /**
     * 当前得分
     */
    protected int score = 0;
    /**
     * 当前时刻
     */
    protected int time = 0;

    protected int boss_id = 1;   // 若为0则开局出现

    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    protected int cycleDuration;
    protected int cycleTime = 0;

    /**
     * 游戏结束标志
     */
    protected boolean gameOverFlag = false;

    protected BufferedImage backGroundImage;

    public Game() {
        initParam();
        initFactory();


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


    public abstract void initParam();
    public abstract void createAircraftAction();
    public abstract void initFactory();
    public abstract void updateDifficulty();
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
                createAircraftAction();
                // 飞机射出子弹
                shootAction();
                updateDifficulty();
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

    private void createProp(EnemyAircraft enemyAircraft){
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
                    if (enemyAircraft.isKilled()) {
                        // TODO 产生道具补给，分数在后处理阶段结算
                        if(enemyAircraft instanceof Boss){
                            MusicManager.execute("boss_defeated");
                        }
                        createProp(enemyAircraft);
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
                if(prop instanceof PropBomb){
                    ((PropBomb) prop).register(enemyBullets);
                    ((PropBomb) prop).register(enemyAircrafts);
                }
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
        DeathCheck();
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
    }

    private void DeathCheck() {
        for(EnemyAircraft enemyAircraft : enemyAircrafts){
            if(enemyAircraft.isKilled()){
                score += enemyAircraft.getScore();
            }
        }
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
    public void paint(Graphics g){
        super.paint(g);
        // 绘制背景,图片滚动
        g.drawImage(backGroundImage, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(backGroundImage, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);
        paintImageWithPositionRevised(g, props);

        paintImageWithPositionRevised(g, enemyAircrafts);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);
        paintHeroHealthBar(g);

        //绘制得分和生命值
        paintScoreAndLife(g);
    }
    // 在子类实现paint方法
    protected void paintHeroHealthBar(Graphics g) {
        int barWidth = heroAircraft.getWidth();
        int barHeight = 5;
        int x = heroAircraft.getLocationX() - (heroAircraft.getWidth() / 2);
        int y = heroAircraft.getLocationY() + (heroAircraft.getHeight() / 2);

        // 绘制血条底色
        g.setColor(Color.GRAY);
        g.fillRect(x, y, barWidth, barHeight);

        int hp = heroAircraft.getHp();
        int maxHp = heroAircraft.getMaxHp();
        int fillWidth = hp * barWidth / maxHp; // 计算填充宽度
        Color fillColor = hp > maxHp / 4 ? Color.GREEN : Color.RED;

        // 绘制血条填充色
        g.setColor(fillColor);
        g.fillRect(x, y, fillWidth, barHeight);

        // 绘制血条边框
        g.setColor(Color.BLACK);
        g.drawRect(x, y, barWidth, barHeight);
    }

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
