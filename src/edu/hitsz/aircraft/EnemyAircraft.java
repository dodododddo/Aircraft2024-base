package edu.hitsz.aircraft;


import edu.hitsz.application.Main;

public abstract class EnemyAircraft extends AbstractAircraft{

    protected int propNum;
    protected int score;
    protected boolean killed = false;


    public EnemyAircraft(int locationX, int locationY, int speedX, int speedY, int hp, int power, int shootNum){
        super(locationX, locationY, speedX, speedY, hp, power, shootNum);
    }

    public boolean isKilled(){
        return killed;
    }


    @Override
    public void decreaseHp(int decrease){
        hp -= decrease;
        if(hp <= 0){
            hp=0;
            killed = true;
            vanish();
        }
    }

    public void kill(){
        decreaseHp(this.hp);
    }

    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    public int getScore(){
        return score;
    }


    public int getPropNum(){ return propNum;};

}
