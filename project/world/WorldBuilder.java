package world;

import java.io.*;
import java.net.URL;
import java.util.Random;

import asciiPanel.AsciiPanel;

/*
 * Copyright (C) 2015 s-zhouj
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
 * @author s-zhouj
 */
public class WorldBuilder {

    private int width;
    private int height;
    int[][] map;
    private Tile[][] tiles;

    public WorldBuilder(int height, int width) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[height][width];
    }

    public World build() {
        return new World(tiles, map);
    }
    
    void initMap() throws IOException {
    	URL url = WorldBuilder.class.getClassLoader().getResource("resources/mapNew.txt");
        InputStreamReader reader = new InputStreamReader(url.openStream());
    	BufferedReader bufferReader = new BufferedReader(reader);
    	String strTmp = "";
    	map = new int[height][];
    	int i = 0;
    	while((strTmp = bufferReader.readLine())!=null) {
    		if(i == 40)break;
    		map[i] = new int[width];
    		int size = strTmp.length();
    		for(int j = 0; j < 70; ++j) {
    			map[i][j] = strTmp.charAt(j) - 48;
    		}
    		i++;
    	}
        bufferReader.close();
    }
    
    private WorldBuilder mapTiles() {
    	for(int i = 0; i < height; ++i) {
    		for(int j = 0; j < width; ++j) {
    			switch(map[i][j]) {
    			case 0:
                    tiles[i][j] = Tile.leftWall;
                    break;
                case 1:
                    tiles[i][j] = Tile.rightWall;
                    break;
                case 2:
                	tiles[i][j] = Tile.Wall;
                	break;
                case 3:
                	tiles[i][j] = Tile.Step;
                	break;
                case 4:
                	tiles[i][j] = Tile.Barrier;
                	break;
                case 5:
                	tiles[i][j] = Tile.FLOOR;
                	break;
    			}
    		}
    	}
    	return this;
    }
    
    public WorldBuilder loadMap(int[][] map) {
    	this.map = map;
    	return mapTiles();
    }
    
    public WorldBuilder makeMaps(){
    	try {
			initMap();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return mapTiles();
    }
}
