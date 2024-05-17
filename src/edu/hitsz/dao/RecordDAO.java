package edu.hitsz.dao;
import edu.hitsz.record.Record;
import java.util.List;
import java.util.Optional;

public interface RecordDAO {
    void insert(Record record);
    void delete(int idx);
    List<Record> getAll();
}
