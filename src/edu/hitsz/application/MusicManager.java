package edu.hitsz.application;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class MusicManager {
    public static boolean activated;
    private static final Map<String, String> EVENT_MUSIC_PATH_MAP = new HashMap<>();
    public static String BGM_PATH = "./src/videos/bgm.wav";
    public static String BGM_BOSS_PATH = "./src/videos/bgm_boss.wav";
    public static String BOMB_PATH = "./src/videos/bomb_explosion.wav";
    public static String BULLET_PATH = "./src/videos/bullet.wav";
    public static String BULLET_HIT_PATH = "./src/videos/bullet_hit.wav";
    public static String GAME_OVER_PATH = "./src/videos/game_over.wav";
    public static String SUPPLY_PATH = "./src/videos/get_supply.wav";

    private static Thread bgm_thread;
    private static Thread boss_thread;

    static {
        EVENT_MUSIC_PATH_MAP.put("begin", BGM_PATH);
        EVENT_MUSIC_PATH_MAP.put("boss", BGM_BOSS_PATH);
        EVENT_MUSIC_PATH_MAP.put("bomb", BOMB_PATH);
        EVENT_MUSIC_PATH_MAP.put("bullet", BULLET_PATH);
        EVENT_MUSIC_PATH_MAP.put("hit", BULLET_HIT_PATH);
        EVENT_MUSIC_PATH_MAP.put("game_over", GAME_OVER_PATH);
        EVENT_MUSIC_PATH_MAP.put("supply", SUPPLY_PATH);
    }


    public static void execute(String event) {
        // events: begin, boss, boss_defeated, bomb, bullet, hit, game_over, supply
        if(!activated) return;

        switch (event) {
            case "begin" -> {
                bgm_thread = new MusicThread(BGM_PATH);
                bgm_thread.start();
            }

            case "boss" -> {
                boss_thread = new MusicThread(BGM_BOSS_PATH);
                boss_thread.start();
            }

            case "boss_defeated" -> {
                if (boss_thread != null) {
                    boss_thread.interrupt();
                }
            }

            case "game_over" -> {
                if (bgm_thread != null) {
                    bgm_thread.interrupt();
                }
                if (boss_thread != null) {
                    boss_thread.interrupt();
                }
            }

            case "bomb", "bullet", "hit", "supply" -> new MusicThread(EVENT_MUSIC_PATH_MAP.get(event)).start();
        }
    }
}




