package edu.hitsz.aircraft;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import edu.hitsz.bullet.BaseBullet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class HeroAircraftTest {
    HeroAircraft instance = HeroAircraft.getInstance();
    @Test
    @DisplayName("Test method shoot")
    void shoot() {
        List<BaseBullet> bullets = instance.shoot();
        assertEquals(1, bullets.size());
        assertNotNull(bullets.get(0));
    }

    @Test
    @DisplayName("Test method getHp")
    void getHp() {
        assertEquals(100, instance.getHp());
    }


    @ParameterizedTest
    @CsvSource({"20, 80", "100, 0"})
    @DisplayName("Test method decreaseHp")
    void decreaseHp(int decrease, int hp) {
        //
        instance.decreaseHp(decrease);
        assertEquals(hp, instance.getHp());
    }

}