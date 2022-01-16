/*
 * Copyright (C) 2015 Aeranythe Echosong
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package world;

import java.util.List;


import asciiPanel.AsciiPanel;

/**
 *
 * @author Aeranythe Echosong
 */
public class CreatureFactory {

    private World world;
    
    private int cmNum = 1;
    
    private int playerNum = 1;
    
    static final int
    BULLET = 0,
    PLAYER = 1,
    MONSTER = -1;

    public CreatureFactory(World world) {
        this.world = world;
    }

    public CreatureAI newPlayer() {
        Creature player = new Creature(this.world, (short)(13 + (playerNum - 1) * 6), GameColor.brightWhite, PLAYER * playerNum++, 100, 30, 1000, 500, 100, 100);
        world.setPlayerLocation(player);
        return new PlayerAI(player, world);
    }
    
    public CreatureAI newCommonMonster() {
        Creature cm = new Creature(this.world, (short)5, GameColor.green, MONSTER * cmNum++, 100, 20, 2000, 0, 300, 8);
        world.setCommonMonsterLocation(cm);
        return new CommonMonster(cm, world);
    }
    
    public CreatureAI setPlayer(int hp, int x, int y) {
    	Creature player = new Creature(this.world, (short)(13 + (playerNum - 1) * 6), GameColor.brightWhite, PLAYER * playerNum++, 100, 30, 1000, 500, 100, 100);
    	player.setHP(hp);
    	world.addAtThisLocation(player, x, y);
    	return new PlayerAI(player, world);
    }
    
    public CreatureAI setCommonMonster(int hp, int x, int y) {
    	Creature cm = new Creature(this.world, (short)5, GameColor.green, MONSTER * cmNum++, 100, 20, 2000, 0, 300, 8);
    	cm.setHP(hp);
    	world.addAtThisLocation(cm, x, y);
    	return new CommonMonster(cm, world);
    }
}
