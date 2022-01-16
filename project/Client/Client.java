package Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JFrame;

import web.*;
import world.GameData;
import world.World;

public class Client {
	private static String mip = "127.0.0.1";
	private static final int servermdk = 7790;
	public static int panelWeight = 70;
	public static int panelHeight = 50;
	
	private static Display display = null;
	
	private static Client_listen listen;
	private static Client_send send;
	
    public static void main(String[] args){
    	if(args.length > 0) {
    		mip = args[0];
    	}
    	Display display = new Display(panelHeight, panelWeight, "Client");
		try {
            Socket socket;
            int times = 0;
            while(true) {
            	try {
            		times++;
            		if(times == 11)throw new Exception("outTime");
            		Thread.sleep(1000);
            		socket = new Socket(mip, servermdk);
            		break;
            	}catch(Exception e) {
            		if("outTime".equals(e.getMessage()))throw new Exception();
            		System.out.println("搜索房间"+times+"/"+10);
            	}
            }
            InputStreamReader is = new InputStreamReader(socket.getInputStream());
            OutputStreamWriter os = new OutputStreamWriter(socket.getOutputStream());
            listen = new Client_listen(socket, display);
            send = new Client_send(socket, display);
            new Thread(listen).start();
            new Thread(send).start();
        }catch (Exception e){
            System.out.println("房主未开启房间");
        }
    }
    
}
class Client_listen implements Runnable {
    private Socket socket;
    private Display display;
    Client_listen(Socket socket, Display display) {
        this.socket = socket;
        this.display = display;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            while(true) {
            	Thread.sleep(50);
            	String stats = (String) ois.readObject();
            	int state = stats.charAt(0) - 48;
            	switch(state) {
            	case World.GAME:
            		int[][][]data = new int[Client.panelHeight - 10][Client.panelWeight][2];
                	int data_i = 0;
                	int lastIndex = -1, thisIndex = stats.indexOf("begin:") + 6, tip = stats.indexOf("end");
                	while(thisIndex < stats.length() && thisIndex < tip) {
                		lastIndex = thisIndex;
                		if((thisIndex = stats.indexOf(':', lastIndex + 1)) == -1)break;
                		data[data_i/(Client.panelWeight*2)][(data_i/2)%Client.panelWeight][data_i%2] = Integer.parseInt(stats.substring(lastIndex, thisIndex));
                		data_i++;
                		thisIndex += 1;
                	}
                	display.play(data);
            		break;
            	default:
            		display.reset(state);
            		break;
            	}
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
        	try {
				if(socket != null)socket.close();
				System.out.println("连接结束");
			} catch (IOException ee) {
				ee.printStackTrace();
			}
        }
    }
}
class Client_send implements Runnable{
	private Socket socket;
	private Display display;
    Client_send(Socket socket, Display display){
        this.socket = socket;
        this.display = display;
    }

    @Override
    public void run() {
        try {
        	ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            
            while (true) {
            	try {
            		Thread.sleep(50);
            		String stat = display.getGameString();
            		oos.writeObject(stat);
            		oos.flush();
            	}catch (Exception e) {
            		e.printStackTrace();
            		System.out.println("无法向socket输入");
            		break;
            	}
            }
        }catch (Exception ee){
            ee.printStackTrace();
        }
    }
}