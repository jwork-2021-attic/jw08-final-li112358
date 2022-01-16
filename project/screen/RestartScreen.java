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

import asciiPanel.AsciiPanel;
import web.Display;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;

/**
 *
 * @author Aeranythe Echosong
 */
public class RestartScreen implements Screen, Runnable {
	protected String screenString = "";
	
	protected Display app;
	
	protected AsciiPanel terminal;
	
	protected boolean exit = false;
	
	protected RestartScreen link;

	protected RestartScreen(AsciiPanel terminal, Display app){
		this.terminal = terminal;
		this.link = this;
		this.app = app;
	}
	
    @Override
    public void displayOutput() {
    	return;
	}

	@Override
	public void run() {
	}

	public RestartScreen Link() {
		return link;
	}

	public void shutDown() {
		exit = true;
	}

	@Override
	public void pressKey(KeyEvent e) {
		shutDown();
		
	}

	@Override
	public void releaseKey(KeyEvent e) {
		
	}
	
	public String getScreenString() {
		return screenString;
	}
	
	public void setData(int[][][] data) {
		
	}

}