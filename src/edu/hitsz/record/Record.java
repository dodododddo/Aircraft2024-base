package edu.hitsz.record;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Record {
    private String username;
    private int score;
    private Date time;

    public Record(String username, int score){
        this.username = username;
        this.score = score;

        this.time = new Date();
    }

    public Record(String recordString){
        String[] fields = recordString.split(",");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        username = fields[0];
        try{
            score = Integer.parseInt(fields[1]);
            time = dateFormat.parse(fields[2]);
        }
        catch (ParseException e){
            e.printStackTrace();
        }

    }

    public Date getTime() {
        return time;
    }

    public int getScore() {
        return score;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return username + "," + score + "," + dateFormat.format(time) + "\n";
    }

}
