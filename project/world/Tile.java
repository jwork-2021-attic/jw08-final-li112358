package world;

import java.awt.Color;

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
public enum Tile {
	
    leftWall((short) 0, GameColor.transparentWhite),
    
    rightWall((short) 1, GameColor.transparentWhite),
    
    Wall((short)2, GameColor.transparentWhite),
    
    Step((short)3, GameColor.transparentWhite),
    
	Barrier((short)4, GameColor.transparentWhite),
	
	COMMONMONSTER((short)5, GameColor.transparentWhite),
	
	FLOOR((short)255, GameColor.brightWhite),
	
	BULLET((short)11,GameColor.white),
	
	////////////////
	

    BOUNDS((short)255, GameColor.magenta);

    private short glyph;

    public short glyph() {
        return glyph;
    }

    private GameColor color;

    public GameColor color() {
        return color;
    }

    public boolean isDiggable() {//¿ÉÍÚ¾ò£¬±¦²Ø
        return this != Tile.Wall && this != Tile.BOUNDS && this != Tile.COMMONMONSTER && this != Tile.leftWall && this != Tile.rightWall && this != Tile.Step && this != Tile.Barrier;
    }
    
    public boolean isWall() {
		return this == Tile.Wall || this == Tile.leftWall || this == Tile.rightWall || this == Tile.Barrier || this == Tile.Step;
	}

    public boolean isGround() {
        return this == Tile.FLOOR;
    }

    Tile(short s, GameColor color) {
        this.glyph = s;
        this.color = color;
    }
}
