package de.tomgrill.gdxtesting.examples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mygdx.game.Rogue99;
import com.mygdx.game.interactable.Hero;
import com.mygdx.game.item.StrengthScroll;
import com.mygdx.game.item.sword;
import com.mygdx.game.map.Level;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class LevelTests {

    private Rogue99 game;
    private Level level;

    @Before
    public void init(){
        game = new Rogue99();
        game.hero = new Hero(game, "hero");
        game.setEnemyMap();
        level = new Level(game, 0, game.hero);
        level.generateFloorPlan();
        //level.setSeed(String.valueOf(System.currentTimeMillis()));
        level.generate();
    }

    @Test
    public void testAliveNeighbors(){
        String alive = "wall";
        level.getMap()[0][0].setType("wall");
        level.getMap()[1][0].setType("floor");
        level.getMap()[2][0].setType("wall");
        level.getMap()[0][1].setType("floor");
        level.getMap()[1][1].setType("wall"); //center
        level.getMap()[2][1].setType("wall");
        level.getMap()[0][2].setType("wall");
        level.getMap()[1][2].setType("floor");
        level.getMap()[2][2].setType("wall");

        assertEquals(6, level.countAliveNeighbors(level.getMap()[1][1], alive));
    }

    @Test
    public void testEntrance(){
        assertNotNull(level.entrance);
    }

    @Test
    public void testExit(){
        assertNotNull(level.exit);
    }

    @Test
    public void testEnemyCount(){
        assertEquals(7, level.enemies.size());
    }
}
