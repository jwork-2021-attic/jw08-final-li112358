package world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

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
/**
 *
 * @author Aeranythe Echosong
 */
public class World {
	
	private final int maxNums = 2;
	private int[][] map;
    private Tile[][] tiles;
    private int width;
    private int height;
    private List<Creature> creatures;
    private List<CommonMonster> cms;
    private List<Bullet> bullets;
    
    private CreatureAI[] players;
    
    //private CreatureAI cm;
    
    private int state;
    
    private CommonMonster[] CMtarget;

    public static final int TILE_TYPES = 2;
    
    public static final int
    GAME = 0,
    WIN = 1,
    LOSE1 = 2,
    LOSE2 = 3;
    
    public World(Tile[][] tiles, int[][] map) {
    	this(tiles);
    	this.map = map;
    }
    
    public World(Tile[][] tiles) {
        this.tiles = tiles;
        this.height = tiles.length;
        this.width = tiles[0].length;
        this.players = new CreatureAI[maxNums];
        this.CMtarget = new CommonMonster[maxNums];
        for(int i = 0; i < maxNums; ++i) {
        	players[i] = null;
        	CMtarget[i] = null;
    	}
        this.creatures = new ArrayList<>();
        this.bullets = new ArrayList<>();
        this.cms = new ArrayList<>();
        this.state = GAME;
    }
    
    public int[][] getMap(){
    	return map;
    }

    public Tile tile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return Tile.BOUNDS;
        } else {
            return tiles[y][x];
        }
    }
    
    public int state() {
    	return state;
    }
    
    public short glyph(int x, int y) {
        return tiles[y][x].glyph();
    }

    public GameColor color(int x, int y) {
        return tiles[y][x].color();
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public void dig(int x, int y) {
        if (tile(x, y).isDiggable()) {
            tiles[y][x] = Tile.FLOOR;
        }
    }
    
    public void addAtThisLocation(Creature creature, int x, int y) {
    	creature.setX(x);
    	creature.setY(y);
    	this.creatures.add(creature);
    }
    
    public void setPlayerLocation(Creature creature) {
    	int x = 1, y = 1;
    	switch(creature.id()) {
    	case 1:x = 1; y = 38; break;
    	case 2:x = 68; y = 38; break;
    	case 3:x = 60; y = 16; break;
    	case 4:x = 13; y = 11; break;
    	}
    	addAtThisLocation(creature, x, y);
    }
    
    public void setCommonMonsterLocation(Creature creature) {
    	int x = 1, y = 1;
    	switch(creature.id()) {
    	case -10:x = 60; y = 37; break;
    	case -9:x = 15; y = 36; break;
    	case -3:x = 5; y = 33; break;
    	case -4:x = 15; y = 19; break;
    	case -5:x = 16; y = 11; break;
    	case -6:x = 4; y = 2; break;
    	case -7:x = 55; y = 5; break;
    	case -8:x = 53; y = 14; break;
    	case -2:x = 60; y = 23; break;
    	case -1:x = 58; y = 32; break;
    	default:System.out.println("uncorrect cm id");
    	}
    	addAtThisLocation(creature, x, y);
    }

    public void addAtEmptyLocation(Creature creature) {
        int x;
        int y;

        do {
            x = (int) (Math.random() * this.width);
            y = (int) (Math.random() * this.height);
        } while (!tile(x, y).isGround() || this.creature(x, y) != null);

        creature.setX(x);
        creature.setY(y);

        this.creatures.add(creature);
    }

    public Creature creature(int x, int y) {
        for (Creature c : this.creatures) {
            if (c.x() == x && c.y() == y) {
                return c;
            }
        }
        return null;
    }

    public List<Creature> getCreatures() {
        return this.creatures;
    }
    
    public List<CommonMonster> getCommonMonster(){
    	return this.cms;
    }
    
    public List<Bullet> getBullets(){
    	return this.bullets;
    }

    public void remove(Creature target) {
    	if(target.id() >= CreatureFactory.PLAYER) {
    		players[target.id()/CreatureFactory.PLAYER-1] = null;
    		state = target.id()/CreatureFactory.PLAYER-1 + LOSE1;
    		for(int i = 0; i < maxNums; ++i) {
    			if(players[i]!=null)break;
    		}
    	}
    	else if(target.id() <= CreatureFactory.MONSTER) {
    		this.creatures.remove(target);
    		for(CommonMonster cm: cms) {
    			if(cm.id() == target.id()) {
    				cm.shutDown();
    				cms.remove(cm);
    				break;
    			}
    		}
    		if(cms.size() == 0)state = WIN;
    	}
    	else if(target.id() == CreatureFactory.BULLET) {
    		this.creatures.remove(target);
    		((Bullet) target).shutDown();
    	}
        this.creatures.remove(target);
    }

    public void update() {
    }
    
    public CommonMonster[] getCMTarget() {
    	return CMtarget;
    }
    
    public void setTile(int x, int y,Tile tile) {
    	this.tiles[y][x] = tile;
    }
    
    public void addPlayer(CreatureAI outPlayer) {
    	for(int i = 0; i < maxNums; ++i) {
    		if(players[i]==null) {
    			players[i] = outPlayer;
    			return;
    		}
    	}
    	System.out.println("玩家数量达到上限");
    	return;
    }
    
    public void setCM(List<CommonMonster> cms) {
    	this.cms = cms;
    	for(int i = 0; i < cms.size(); ++i) {
    		creatures.add(cms.get(i).creature);
    	}
    }
    
    public boolean positionNoCorrupt(int x, int y) {
    	for(CommonMonster cm : cms) {
			if(cm.x() == x && cm.y() == y) {
				return false;
			}
		}
    	for(int i = 0; i < maxNums; ++i) {
    		if(players[i]!=null) {
    			if(players[i].x()==x&&players[i].y()==y) {
    				return false;
    			}
    		}
    	}
    	return true;
    }
    
    public void attackPlayer(int i, int attackValue) {
    	if(players[i] == null) {
    		return;
    	}
    	players[i].beAttacked(attackValue);
    }
    
    public int getPlayerX(int i) {
    	if(players[i] == null) {
    		return 0;
    	}
    	return players[i].x();
    }
    
    public int getPlayerY(int i) {
    	if(players[i] == null) {
    		return 0;
    	}
    	return players[i].y();
    }
    
    //返回看见玩家序号
    public int seePlayer(int x, int y, int visionRadius) {
    	double min = Math.pow(visionRadius, 2);
    	int tip = -1;
    	for(int i = 0; i < maxNums; ++i) {
    		if(players[i] != null) {
    			double tmp = Math.pow(players[i].x() - x, 2) + Math.pow(players[i].y() - y, 2);
    			if(tmp < min) {
    				tmp = min;
    				tip = i;
    			}
    		}
    	}
    	return tip;
    }
    
    public boolean attackCM(int x, int y, int i, int attackValue) {
    	for(CommonMonster cm : cms) {
    		if(cm.x() == x && cm.y() == y) {
    			cm.beAttacked(attackValue);
    			CMtarget[i/CreatureFactory.PLAYER-1] = cm;
    			return true;
    		}
    	}
    	return false;
    }
    
    public void addBullet(Bullet bullet) {
    	this.creatures.add(bullet);
    	this.bullets.add(bullet);
    }
    
}
