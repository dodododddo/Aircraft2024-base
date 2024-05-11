package edu.hitsz.application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenu {
    private JPanel mainPanel;
    private JPanel patternPanel;
    private JPanel musicPanel;
    private JButton easyButton;
    private JButton mediumButton;
    private JButton hardButton;
    private JLabel musicLabel;
    private JComboBox musicBox;

    public StartMenu() {

        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.setLevel(1);
                Game game = new GameEasy();
                Main.cardPanel.add(game);
                Main.cardLayout.last(Main.cardPanel);

                game.action();
            }
        });
        mediumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.setLevel(2);
                Game game = new GameMedium();
                Main.cardPanel.add(game);
                Main.cardLayout.last(Main.cardPanel);

                game.action();
            }
        });
        hardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.setLevel(3);

                Game game = new GameHard();
                Main.cardPanel.add(game);
                Main.cardLayout.last(Main.cardPanel);
                game.action();
            }
        });
        musicBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Todo
                String judge = (String) musicBox.getSelectedItem();
                Game.setMusic("开".equals(judge));
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
