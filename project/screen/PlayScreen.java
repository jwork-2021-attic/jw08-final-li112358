package screen;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;
import web.Display;
import world.GameColor;

public class PlayScreen extends RestartScreen{
	
	private int[][][] data;
	
	public PlayScreen(AsciiPanel terminal, Display app) {
		super(terminal, app);
	}
	
	public void setData(int[][][] data) {
		this.data = data;
	}
	
    @Override
    public void displayOutput() {
    	terminal.clear();
    	if(data == null)return;
		for(int i = 0; i < data.length; ++i) {
			for(int j = 0; j < data[i].length; ++j) 
				terminal.write((char)data[i][j][1], j, i, GameColor.getByIndex(data[i][j][0]).getColor());
		}
		app.repaint();
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
	}
	
	@Override
	public void pressKey(KeyEvent e) {
		
	}

	@Override
	public void releaseKey(KeyEvent e) {
		
	}
	
}
