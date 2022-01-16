package web;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;
import screen.RestartScreen;
import screen.Screen;
import screen.StartScreen;
import screen.LoseScreen;
import screen.PlayScreen;
import screen.WinScreen;
import javax.swing.JFrame;
import javax.swing.JLabel;
import world.GameColor;
import world.World;

public class Display extends JFrame implements KeyListener{
	static private AsciiPanel terminal = null;
	static private RestartScreen screen = null;
	private String gameString = "";
	private String keyAction = "";
	public String type = null;
	public int state = World.GAME;
	public Display(int height, int weight, String type){
		super();
		this.type = type;
		if(terminal == null)
			terminal = new AsciiPanel(height, weight, AsciiFont.TALRYTH_15_15);
		add(terminal);
		pack();
		if(screen == null) {
			screen = new StartScreen(terminal, this);
			new Thread((StartScreen)screen).start();
		}
		addKeyListener(this);
		this.setTitle(type);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
        repaint();
	}
	
	public void reset(int i) {
		state = i;
		screen.shutDown();
		switch(i) {
		case World.GAME:
			screen = new StartScreen(terminal, this);
			break;
		case World.WIN:
			screen = new WinScreen(terminal, this);
			break;
		case World.LOSE1:
		case World.LOSE2:
			screen = new LoseScreen(terminal, this);
			break;
		}
		screen.displayOutput();
		new Thread((RestartScreen)screen).start();
	}
	
	public void play(int[][][] data) {
		screen = screen.Link();
		screen.setData(data);	
	}
	
	
	
	public String getGameString() {
		String newString = gameString;
		gameString = "";
		return newString;
	}

	@Override
	public void keyPressed(KeyEvent e){
		screen.pressKey(e);	
		int c = e.getKeyCode();
		gameString += "press:"+c+";";
		if(screen != null)gameString += screen.getScreenString();
	}

	@Override
	public void keyReleased(KeyEvent e){
		screen.releaseKey(e);	
		int c = e.getKeyCode();
		gameString += "release:" + c+";";
	}

	@Override
	public void keyTyped(KeyEvent e){

	}
}