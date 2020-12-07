/*******************************************************************************
 * Copyright 2015 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.tomgrill.gdxtesting.examples;

import com.mygdx.game.Rogue99;
import com.mygdx.game.interactable.Hero;
import com.mygdx.game.item.StrengthScroll;
import com.mygdx.game.item.sword;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class AutomatedAcceptanceTests {

    private Rogue99 game;
    private sword s;
    private StrengthScroll strengthScroll;

    @Before
    public void init(){
        game = new Rogue99();
        s = new sword(0,"sword", 15);
        strengthScroll = new StrengthScroll(0,"scroll_strength", 15);
        game.hero = new Hero(game,"hero");
        game.hero.getInventory().add(s);
        game.hero.getInventory().add(strengthScroll);
    }

    @Test
    public void testAutomatedMovePlayer(){
        game.usedItem(s);
        assertEquals(25, game.hero.getStr());
    }

    @Test
    public void testAutomatedUpdateNewTile(){
        assertEquals(2,game.hero.getInventory().size());
    }

    @Test
    public void testAutomatedUpdateOldTile(){
        assertEquals(2,game.hero.getInventory().size());
    }

    @Test
    public void testAutomatedMapUpdate(){
        game.hero.getInventory().remove(strengthScroll);
        assertEquals(1,game.hero.getInventory().size());
    }

    @Test
    public void testAutomatedInventoryUpdate(){
        assertEquals(2,game.hero.getInventory().size());
    }

    @Test
    public void testAutomatedAttackEnemy(){
        assertEquals(2,game.hero.getInventory().size());
    }

}
