package edu.hitsz.application;

import edu.hitsz.dao.RecordDAO;
import edu.hitsz.record.Record;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;

public class EndingBoard {
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JLabel modeLabel;
    private JLabel headerLabel;
    private JScrollPane tableScrollPanel;
    private JButton deleteButton;
    private JTable scoreTable;
    private DefaultTableModel model;

    public EndingBoard(RecordDAO recordDao) {

        load(recordDao);
        switch (Game.level){
            case 1 -> modeLabel.setText("难度：简单");
            case 2 -> modeLabel.setText("难度：普通");
            case 3 -> modeLabel.setText("难度：困难");
            default -> modeLabel.setText("难度：未知");
        }

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = scoreTable.getSelectedRow();
                recordDao.delete(row);
                int result = JOptionPane.showConfirmDialog(deleteButton,
                        "确定删除？");
                if (JOptionPane.YES_OPTION == result && row != -1) {
                    model.removeRow(row);
                }
            }
        });
    }

    public void load(RecordDAO recordDao) {
        String[] columnName = {"名次", "玩家名", "得分", "时间"};
        List<Record> users =  recordDao.getAll();
        String[][] tableData = new String[users.size()][4];
        for(int i=0;i<users.size();i++){
            tableData[i][0] = Integer.toString(i+1);
            tableData[i][1] = users.get(i).getUsername();
            tableData[i][2] = Integer.toString(users.get(i).getScore());
            tableData[i][3] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(users.get(i).getTime());
        }

        //表格模型
        model = new DefaultTableModel(tableData, columnName){
            @Override
            public boolean isCellEditable(int row, int col){
                return false;
            }
        };

        //JTable并不存储自己的数据，而是从表格模型那里获取它的数据
        scoreTable.setModel(model);
        tableScrollPanel.setViewportView(scoreTable);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

}
