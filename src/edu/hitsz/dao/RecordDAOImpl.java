package edu.hitsz.dao;

import edu.hitsz.record.Record;

import java.io.FileWriter;
import java.io.IOException;
import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


public class RecordDAOImpl implements RecordDAO{

    private String save_path = "./src/data/score_record.txt";
    public RecordDAOImpl(){
    }

    @Override
    public void insert(Record record) {
        List<Record> records = getAll();
        records.add(record);
        records.sort(new Comparator<Record>() {
            @Override
            public int compare(Record r1, Record r2) {
                // 按照分数降序排序
                return Integer.compare(r2.getScore(), r1.getScore());
            }
        });
        try {
            FileWriter writer = new FileWriter(save_path);
            for(Record r: records){
                writer.write(r.toString());
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @Override
    public List<Record> getAll(){
        List<Record> records = new LinkedList<>();
        try {
            FileReader baseReader = new FileReader(save_path);
            BufferedReader reader = new BufferedReader(baseReader);

            while(true){
                String line = reader.readLine();
                if(line != null && !line.isEmpty()){
                    records.add(new Record(line));
                }
                else{
                    break;
                }
            }
            reader.close();
            baseReader.close();
        }
        catch (IOException e){
            e.printStackTrace();

        }
        return records;
    }

    public void printAll(){
        List<Record> records = getAll();
        for(int i = 0; i < records.size(); i++){
            System.out.println("第" + (i + 1) + "名: " + records.get(i).toString());
        }
    }
}
