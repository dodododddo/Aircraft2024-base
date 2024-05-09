package edu.hitsz.aircraft;



public abstract class EnemyAircraft extends AbstractAircraft{

    protected int propNum;
    protected int score;

    public EnemyAircraft(int locationX, int locationY, int speedX, int speedY, int hp){
        super(locationX, locationY, speedX, speedY, hp);
    }

    public abstract int getScore();

    public abstract int getPropNum();
}
