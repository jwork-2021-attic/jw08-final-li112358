package world;

import asciiPanel.AsciiPanel;
import java.awt.Color;

public enum GameColor {
	red(AsciiPanel.red),
	white(AsciiPanel.white),
	green(AsciiPanel.green),
	brightBlack(AsciiPanel.brightBlack),
	darkGray(Color.DARK_GRAY),
	brightWhite(AsciiPanel.brightWhite),
	transparentWhite(AsciiPanel.transparentWhite),
	greenYellow(AsciiPanel.greenYellow),
	brownness(AsciiPanel.brownness),
	magenta(AsciiPanel.magenta);
	Color color;
	GameColor(Color c) {
		this.color = c;
	} 
	public Color getColor(){
		return this.color;
	}
	public static GameColor getByIndex(int i) {
		for(GameColor item : GameColor.values()) {
			if(i == item.ordinal()) {
				return item;
			}
		}
		return null;
	}
}
