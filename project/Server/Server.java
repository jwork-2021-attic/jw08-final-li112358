package Server;

import screen.*;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import web.*;
import world.GameData;
import world.World;

public class Server extends Thread{
	private static final String mip = "127.0.0.1";
	private static final int servermdk = 7790;
	public static int panelWeight = 70;
	public static int panelHeight = 50;
	
	private static int gamestat0 = 0;
	private static int gamestat1 = 0;
	
	private static Server_listen listen;
	private static Server_send send;
	
	private static GameBody pys = null;
	static Display display = null;
	
	public static void main(String[] args) {
		display = new Display(panelHeight, panelWeight, "Server");
		while(true) {
			try {
				Thread.sleep(50);
				String stat = display.getGameString();
				if(stat.indexOf("release")!=-1) {
					int i = 3;
					i+=1;
				}
				dealMessages0(stat);
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void connect() {
		try {
			ServerSocket serverSocket = new ServerSocket(servermdk);
			Socket socket = null;
			socket = serverSocket.accept();
			pys = new GameBody(2);
			new Thread(pys).start();
			listen = new Server_listen(socket, pys);
			send = new Server_send(socket, pys);
			new Thread(listen).start();
			new Thread(send).start();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void dealMessages0(String message) {//´¦ÀíÓÎÏ·×´Ì¬
		if(message == "")return;
		if(gamestat0 == 0) {
			int statlength = message.lastIndexOf("gamestat:");
			if(statlength >= 0) {
				String stat = "";
				for(int i = statlength + 9; i < message.length(); ++i) {
					char c = message.charAt(i);
					if(c == ';') {
						break;
					}
					else {
						if(c >= '0' && c <= '9') {
							stat += c;
						}
					}
				}
				gamestat0 = gamestat0 > Integer.parseInt(stat) ? gamestat0 : Integer.parseInt(stat);	
			}
			switch(gamestat0) {
			case 0:break;
			case 1:pys = new GameBody(); new Thread(pys).start(); break;
			case 2:pys = new GameBody(1); new Thread(pys).start(); break;
			case 3:connect();break;
			}
		}
		else {
			if(gamestat0 == 3)gamestat1 = listen.gamestat1;
			if(gamestat0 == 1 || gamestat0 == 2 || gamestat1 != 0) {
				if(pys.getState()!=World.GAME) {
					display.reset(pys.getState());
				}
				display.play(pys.getData());
				int lastIndex = -1, thisIndex = 0;
				while(thisIndex < message.length()) {
					lastIndex = thisIndex;
					thisIndex = message.indexOf(':', lastIndex + 1);
					if(thisIndex == -1)break;
					int h, key;
					switch(message.substring(lastIndex, thisIndex)) {
					case "press":
						h = message.indexOf(';', thisIndex); 
						key = Integer.parseInt(message.substring(thisIndex + 1, h));
						if(key == 27) {
							pys.shutDown();
							pys = null;
							display.reset(World.GAME);
							gamestat0 = 0;
							return;
						}
						pys.pressKey(key, 0); 
						thisIndex = h + 1;
						break;
					case "release":
						h = message.indexOf(';', thisIndex);
						key = Integer.parseInt(message.substring(thisIndex + 1, h));
						pys.releaseKey(key, 0);
						thisIndex = h + 1;
						break;
					case "gamestat":
						h = message.indexOf(';', thisIndex);
						thisIndex = h + 1;
						break;
					}
					
				}
			}
		}
	}
	
}

class Server_listen implements Runnable{
	private boolean exit = false;
	private Socket socket;
	public int gamestat1 = 0;
	private GameBody pys;
	Server_listen(Socket socket, GameBody pys){
		this.socket = socket;
		this.pys = pys;
	}
	
	public void shutDown() {
		exit = true;
	}
	
	@Override
	public void run() {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			while(!exit) {
				String message = (String) ois.readObject();
				if(gamestat1 == 0) {
				int statlength = message.lastIndexOf("gamestat:");
					if(statlength >= 0) {
						String stat = "";
						for(int i = statlength + 9; i < message.length(); ++i) {
							char c = message.charAt(i);
							if(c == ';') {
								break;
							}
							else {
								if(c >= '0' && c <= '9') {
									stat += c;
								}
							}
						}
						gamestat1 = gamestat1 > Integer.parseInt(stat) ? gamestat1 : Integer.parseInt(stat);	
					}
				}
				else {
					int lastIndex = -1, thisIndex = 0;
					while(thisIndex < message.length()) {
						lastIndex = thisIndex;
						thisIndex = message.indexOf(':', lastIndex + 1);
						if(thisIndex == -1)break;
						int h, key;
						switch(message.substring(lastIndex, thisIndex)) {
						case "press":
							h = message.indexOf(';', thisIndex);
							key = Integer.parseInt(message.substring(thisIndex + 1, h)); 
							pys.pressKey(key, 1); 
							thisIndex = h + 1;
							break;
						case "release":
							h = message.indexOf(';', thisIndex); 
							key = Integer.parseInt(message.substring(thisIndex + 1, h)); 
							pys.releaseKey(key, 1); 
							thisIndex = h + 1;
							break;
						case "gamestat":break;
						}
						
					}
				}
			}if(ois !=null)ois.close();
		}catch (Exception e) {
			System.out.println("server¼àÌýÊ§°Ü");
			e.printStackTrace();
		}
	}
	
}

class Server_send implements Runnable{
	private boolean exit = false;
	private Socket socket;
	private GameBody pys;
	
	Server_send(Socket socket, GameBody pys){
		this.socket = socket;
		this.pys = pys; 
	}
	
	public void shutDown() {
		exit = true;
	}
	
	@Override
	public void run() {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(this.socket.getOutputStream());
			while(!exit) {
				try {
					Thread.sleep(50);
					String stats = String.valueOf(pys.getState());
					stats += "begin:";
					int[][][] data = pys.getData();
					for(int i = 0; i < data.length; ++i) {
						for(int j = 0; j < data[i].length; ++j) {
							stats += data[i][j][0];
							stats += ":";
							stats += data[i][j][1];
							stats += ":";

						}
					}
					stats += "end";
					oos.writeObject(stats);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			oos.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}