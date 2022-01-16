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
package screen;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import asciiPanel.AsciiPanel;
import web.Display;
import screen.PlayScreen;

/**
 *
 * @author Aeranythe Echosong
 */
public class StartScreen extends RestartScreen {
	
	private int choice = 0;
	
	private int choices = 0;
	
	public StartScreen(AsciiPanel terminal, Display app) {
		super(terminal, app);
	}
	
	private void Sure() {
		terminal.clear();
		switch(choice) {
		case 0:
			screenString = "gamestat:1;";
			break;
		case 1:
			screenString = "gamestat:2;";
			break;
		case 2:
			screenString = "gamestat:3;";
			break;
		}
		shutDown();
		app.repaint();
	}
	
    @Override
    public void displayOutput() {
    	if("Server".equals(app.type)|| "server".equals(app.type)) {
    		choices = 3;
	    	terminal.clear();
	    	terminal.write("load from save.txt", 0, 5);
	        terminal.write("Single game", 0, 10);
	        terminal.write("Online game", 0, 15);
	        switch(choice) {
	        case 0:terminal.write("    ",20,5);terminal.write("<---",20,5);break;
			case 1:terminal.write("    ",20,15);terminal.write("<---",20,10);break;
			case 2:terminal.write("    ",20,10);terminal.write("<---",20,15);break;
			}
	        app.repaint();
    	}
    	else if("Client".equals(app.type) || "client".equals(app.type)) {
    		choices = 1;
    		terminal.clear();
	        terminal.write("Join Online game", 0, 15);
	        terminal.write("    ",20,15);terminal.write("<---",20,15);
	        app.repaint();
    	}
    	else {
    		System.out.println("display了错误的类型：Server or Client");
    	}
    }

	@Override
	public void run() {
		while(!exit) {
			try {
				Thread.sleep(50);
				displayOutput();
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		link = new PlayScreen(terminal, app);
		new Thread(link).start();
	}
	
	@Override
	public void pressKey(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_ENTER:
			Sure();
			break;
		case KeyEvent.VK_W:
			choice = (choices + choice - 1) % choices;
			break;
		case KeyEvent.VK_UP:
			choice = (choices + choice - 1) % choices;
			break;
		case KeyEvent.VK_S:
			choice = (choice + 1) % choices;
			break;
		case KeyEvent.VK_DOWN:
			choice = (choice + 1) % choices;
			break;
		}
	}

	@Override
	public void releaseKey(KeyEvent e) {
		
	}

}
