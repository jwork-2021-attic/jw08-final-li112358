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
package web;

import world.*;
import asciiPanel.AsciiPanel;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
/**
 *
 * @author Aeranythe Echosong
 */
public class GameBody implements Runnable{ //extends RestartScreen {
	private int playerNums = 0;
	private int[][][] data;
	private boolean keyA[], keyD[], keyW[], keyS[], keyJ[], keyK[];
	private CommonMonster[] CMtarget;
	private int num;
    private World world;
    private CreatureAI[] players = null;
    private List<CommonMonster> cms;
    private int screenWidth;
    private int screenHeight;
    private List<String> messages;
    private List<String> oldMessages;
    private int state;
    private int continueTime = 2000;
    private int CDTime = 10000;
    private long beginTime = 0;
    private long curTime = 0;
    private boolean exit = false;
    private CreatureFactory creatureFactory;

    public GameBody() {
    	
        load();
        
    }
    
    private void createCreatures() {
    	for(int i = 0; i < playerNums; ++i) {
    		this.players[i] = creatureFactory.newPlayer();
    		this.world.addPlayer(this.players[i]);
    	}
        for(int i = 0; i < num; ++i) {
        	cms.add((CommonMonster) creatureFactory.newCommonMonster());
        }
        this.world.setCM(cms);
    }
    
    private void load() {
    	File file = new File("save.txt");
    	if(!file.exists()) {
    		reset();
    		return;
    	}
    	int[][] PYdata = null;
		int[][] CMdata = null;
		int[][] map = null;
    	try {
			BufferedReader br;
			br = new BufferedReader(new FileReader(file));
			String line;
	    	while ((line=br.readLine())!=null) {
	    	    switch(line) {
	    	    case "data:":
	    	    	while((line = br.readLine())!=null) {
	    	    		if("end".equals(line))break;
	    	    		int lastindex = 0;
	    	    		int index = line.indexOf(':', lastindex);
	    	    		switch(line.substring(0, index)) {
	    	    		case "cmNums":
	    	    			lastindex = index + 1;
	    	    			index = line.indexOf(';',index);
	    	    			this.num = Integer.parseInt(line.substring(lastindex, index));
	    	    			CMdata = new int[num][3];
	    	    			for(int i = 0; i < CMdata.length; ++i) {
    	    					index = line.indexOf("hp:", index);
    	    					lastindex = index + 3;
    	    					index = line.indexOf(';', index);
    	    					CMdata[i][0] = Integer.parseInt(line.substring(lastindex, index));
    	    					index = line.indexOf("location:", index);
    	    					lastindex = index + 9;
    	    					index = line.indexOf(',', index);
    	    					CMdata[i][1] = Integer.parseInt(line.substring(lastindex, index));
    	    					lastindex = index + 1;
    	    					index = line.indexOf(';', index);
    	    					CMdata[i][2] = Integer.parseInt(line.substring(lastindex, index));
    	    				}
	    	    			break;
	    	    		case "playerNums":
	    	    			lastindex = index + 1;
	    	    			index = line.indexOf(';',index);
	    	    			this.playerNums = Integer.parseInt(line.substring(lastindex, index));
	    	    			PYdata = new int[playerNums][3];
	    	    			for(int i = 0; i < PYdata.length; ++i) {
    	    					index = line.indexOf("hp:", index);
    	    					lastindex = index + 3;
    	    					index = line.indexOf(';', index);
    	    					PYdata[i][0] = Integer.parseInt(line.substring(lastindex, index));
    	    					index = line.indexOf("location:", index);
    	    					lastindex = index + 9;
    	    					index = line.indexOf(',', index);
    	    					PYdata[i][1] = Integer.parseInt(line.substring(lastindex, index));
    	    					lastindex = index + 1;
    	    					index = line.indexOf(';', index);
    	    					PYdata[i][2] = Integer.parseInt(line.substring(lastindex, index));
    	    				}
	    	    			break;
	    	    		}
	    	    	}
	    	    	break;
	    	    case "map:":
	    	    	if((line=br.readLine())!=null) {
	    	    		int lastindex = 0;
	    	    		int index = line.indexOf(',');
	    	    		int row = Integer.parseInt(line.substring(lastindex, index));
	    	    		lastindex = index + 1;
	    	    		index = line.indexOf(';', index);
	    	    		int col = Integer.parseInt(line.substring(lastindex, index));
	    	    		map = new int[row][col];
	    	    		int i = 0, j = 0;
	    	    		while((line=br.readLine())!=null) {
	    	    			if("end".equals(line)) {
	    	    				break;
	    	    			}
	    	    			for(int k = 0; k < line.length(); ++k) {
	    	    				if(line.charAt(k) != ' ') {
	    	    					map[i][j] = line.charAt(k) - 48;
	    	    					j++;
	    	    				}
	    	    			}
	    	    			j = 0;
	    	    			i++;
	    	    		}
	    	    	}
	    	    	break;
	    	    }
	    	}
	    	this.screenHeight = map.length;
            this.screenWidth = map[0].length;
    		this.world = new WorldBuilder(map.length, map[0].length).loadMap(map).build();
    		creatureFactory = new CreatureFactory(this.world);
	    	playerInit();
	    	for(int i = 0; i < playerNums; ++i) {
	    		this.players[i] = creatureFactory.setPlayer(PYdata[i][0], PYdata[i][1], PYdata[i][2]);
	    		this.world.addPlayer(players[i]);
	    	}
	    	for(int i = 0; i < num; ++i) {
	        	cms.add((CommonMonster) creatureFactory.setCommonMonster(CMdata[i][0], CMdata[i][1], CMdata[i][2]));
	        }
	    	this.world.setCM(cms);
	    	data = new int[this.screenHeight][this.screenWidth][2];
	        this.messages = new ArrayList<String>();
	        this.oldMessages = new ArrayList<String>();
	        CMtarget = null;
	        startGame();
	    	br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("load error");
		}
    }
    
    public GameBody(int playerNums) {
    	//super(terminal, app);
        this.screenHeight = 40;
        this.screenWidth = 70;
        //the number of CM
        this.num = 5;
        this.playerNums = playerNums;
        data = new int[this.screenHeight][this.screenWidth][2];
        playerInit();
        createWorld();
        if(playerNums > 2) {
        	System.out.println("玩家数最多为2人");
        	playerNums = 2;
        }
        this.messages = new ArrayList<String>();
        this.oldMessages = new ArrayList<String>();
        
        creatureFactory = new CreatureFactory(this.world);
        createCreatures();
        CMtarget = null;
        startGame();
    }
    
    private void reset() {
    	//super(terminal, app);
        this.screenHeight = 40;
        this.screenWidth = 70;
        //the number of CM
        this.num = 5;
        this.playerNums = 1;
        data = new int[this.screenHeight][this.screenWidth][2];
        playerInit();
        createWorld();
        if(playerNums > 2) {
        	System.out.println("玩家数最多为2人");
        	playerNums = 2;
        }
        this.messages = new ArrayList<String>();
        this.oldMessages = new ArrayList<String>();
        
        creatureFactory = new CreatureFactory(this.world);
        createCreatures();
        CMtarget = null;
        startGame();
    }
    
    public void startGame() {
    	for(int i  = 0; i < playerNums; ++i) {
	    	if(players[i] != null) {
	    		Thread threadPlayer = new Thread(this.players[i]);
	         	threadPlayer.start();
	    	}
    	}
    	for(int i = 0; i < cms.size(); ++i) {
        	Thread threadCM = new Thread(this.cms.get(i));
         	threadCM.start();
        }
    }
    
    private void playerInit() {
    	this.cms = new ArrayList<CommonMonster>(num);
        this.players = new CreatureAI[this.playerNums];
        this.keyA = new boolean[this.playerNums];
        this.keyD = new boolean[this.playerNums];
        this.keyW = new boolean[this.playerNums];
        this.keyS = new boolean[this.playerNums];
        this.keyJ = new boolean[this.playerNums];
        this.keyK = new boolean[this.playerNums];
        for(int i = 0; i < playerNums; ++i) {
        	players[i] = null;
        	keyA[i] = false;
        	keyD[i] = false;
        	keyW[i] = false;
        	keyS[i] = false;
        	keyJ[i] = false;
        	keyK[i] = false;
        }
    }

    private void createWorld() {
    	world = new WorldBuilder(40, 70).makeMaps().build();
    }

    private void displayTiles(int left, int top) {
        for (int y = 0; y < screenHeight && y < world.height(); y++) {
            for (int x = 0; x < screenWidth && x < world.width(); x++) {
                int wx = x + left;
                int wy = y + top;
                boolean seeeee = false;
                //terminal.write(world.glyph(wx, wy), x, y, world.color(wx, wy));
              //color
            	data[wy][wx][0] = world.color(wx, wy).ordinal();
            	//object
            	data[wy][wx][1] = world.glyph(wx,wy);  
            }
        }
        // Show creatures
        for (Creature creature : world.getCreatures()) {
            if (creature != null && creature.x() >= left && creature.x() < left + screenWidth && creature.y() >= top
                    && creature.y() < top + screenHeight) {
                //terminal.write(creature.glyph(), creature.x() - left, creature.y() - top, creature.color());
            	data[creature.y() - top][creature.x() - left][0] = creature.color().ordinal();
        		data[creature.y() - top][creature.x() - left][1] = creature.glyph();
            }
        }
        world.update();
    }

    //@Override
    public void displayOutput() {
        // Terrain and creatures
        displayTiles(0, 0);
        for(int i = 0; i < playerNums; ++i) {
        	data[players[i].y()][players[i].x()][0] = players[i].color().ordinal();
        	data[players[i].y()][players[i].x()][1] = players[i].glyph();
        }
    }
    
    public void shutDown() {
    	if(state == world.GAME)save();
    	for(int i = 0; i < playerNums; ++i) {
    		if(players[i]!=null)players[i].shutDown();
    	}
    	for(CommonMonster cm : cms) {
    		if(cm != null)cm.shutDown();
    	}
    	exit = true;
    }
    
    public void playerDo(int i) {
    	players[i].setAttack(keyJ[i]);
    	players[i].setJump(keyK[i]);
    	int direction = 0;
    	for(int dir = 0; dir < 4; ++dir) {
    		switch(dir) {
    		case PlayerAI.LEFT_ACTION:
    			if(keyA[i]) {
    			direction += 1;
    			}
    			break;
    		case PlayerAI.RIGHT_ACTION:
    			if(keyD[i]) {
    				direction += 2;
    			}
    			break;
    		case PlayerAI.UP_ACTION:
    			if(keyW[i]) {
    				direction += 4;
    			}
    			break;
    		case PlayerAI.DOWN_ACTION:
    			if(keyS[i]) {
    				direction += 8;
    			}
    			break;
    		}
    	}
    	players[i].setDirection(direction);
    }
    
    public void pressKey(int key, int i) {// 玩家序号0~N
    	switch (key) {
        case KeyEvent.VK_A:
        	//setA(true);
        	keyA[i] = true;
            break;
        case KeyEvent.VK_D:
        	//setD(true);
        	keyD[i] = true;
            break;
        case KeyEvent.VK_W:
        	//setW(true);
        	keyW[i] = true;
            break;
        case KeyEvent.VK_S:
        	//setS(true);
        	keyS[i] = true;
            break;
        case KeyEvent.VK_J:
        	//setJ(true);
        	keyJ[i] = true;
        	break;
        case KeyEvent.VK_K:
        	//setK(true);
        	keyK[i] = true;
        	break;
		default:
			break;
		}
    }
    
    public void releaseKey(int key, int i) {
    	switch (key) {
        case KeyEvent.VK_A:
        	//setA(false);
        	keyA[i] = false;
            break;
        case KeyEvent.VK_D:
        	//setD(false);
        	keyD[i] = false;
            break;
        case KeyEvent.VK_W:
        	//setW(false);
        	keyW[i] = false;
            break;
        case KeyEvent.VK_S:
        	//setS(false);
        	keyS[i] = false;
            break;
        case KeyEvent.VK_J:
        	//setJ(false);
        	keyJ[i] = false;
        	break;
        case KeyEvent.VK_K:
        	//setK(false);
        	keyK[i] = false;
        	break;
		default:
			break;
		}
    }
    
    public int[][][] getData() {
    	return data;
    }
    
    
    private void save() {
    	File file = new File("save.txt");
    	try {
        	if(!file.exists()) {
        		file.createNewFile();
        	}
    		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
    		bw.write("data:\n");
    		bw.write("cmNums:" + cms.size()+";");
    		for(CommonMonster cm : cms) {
    			if(cm!=null)bw.write("hp:"+cm.hp()+";"+"location:"+cm.x()+","+cm.y()+";");
    		}
    		bw.write("\n");
    		bw.write("playerNums:" + playerNums + ";");
    		for(int i = 0; i < playerNums; ++i) {
    			bw.write("hp:"+players[0].hp()+";"+"location:"+players[0].x() + "," +players[0].y()+";");
    		}
    		bw.write("\n");
    		bw.write("end\n");
    		bw.write("map:\n");
    		int[][] map = world.getMap();
    		bw.write(map.length+","+map[0].length+ ";\n");
			for(int i = 0; i < map.length; ++i) {
				for(int j = 0; j < map[i].length; ++j) {
					bw.write(map[i][j] + " ");
				}
				bw.write("\n");
			}
			bw.write("end\n");
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public int getState() {
    	return state;
    }
    
    @Override
	public void run() {
    	while(!exit) {
	    	try{
	    		Thread.sleep(50);
				curTime = System.currentTimeMillis();
				for(int i = 0;i < playerNums; ++i) {
					playerDo(i);
				}
				displayOutput();
				CMtarget = world.getCMTarget();
				state = world.state();
			    if(state != World.GAME) {
			    	shutDown();
			    }
			}catch (Exception e) {
				e.printStackTrace();
			}
    	}
	}

}
