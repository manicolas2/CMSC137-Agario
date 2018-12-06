package agario;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public final class GameClient implements Runnable {
	int x, y;
	String name;
	String server;
	boolean connected;
	DatagramSocket socket;
	Thread thread;
	int port;
	Game game;
	
	// Constructor
	public GameClient(int x, int y, String name, Game game) {
		this.x = x;
		this.y = y;
		this.name = name;
		this.server = "localhost";
		this.connected = false;
		this.port = 4444;
		this.game = game;
		try {
			this.socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.start();
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized boolean getConnected() {
		return this.connected;
	}
	
	public void sendMessage(String message) {
		try {
			byte[] bytes = message.getBytes();
			InetAddress ip = InetAddress.getByName(this.server);
			DatagramPacket packet = new DatagramPacket(bytes, bytes.length, ip, this.port);
			this.socket.send(packet);
		} catch(Exception e) {}
	}
	
	@Override
	public void run() {
		String serverData;
		
		// Send CONNECT to server
		this.sendMessage("CONNECT " + name);
		
		while(true){
			try{
				Thread.sleep(1);
			}catch(Exception ioe){}
						
			//Get data from players
			byte[] bytes = new byte[256];
			DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
			try{
     			this.socket.receive(packet);
			}catch(Exception e){e.printStackTrace();}
			
			serverData = new String(bytes);
			serverData = serverData.trim();

			// If currently not connected, and server sent a message that starts with CONNECTED
			if (!connected && serverData.startsWith("CONNECTED")){
				// Update flag
				this.connected = true;
				
				// Get net_index
				String[] temp = serverData.split(" ");
				this.game.getPlayer().setNetIndex(Integer.parseInt(temp[1]));
				this.game.getHandler().netIndex = Integer.parseInt(temp[1]);
				System.out.println("Connected. with" + temp[1]);
			// If currently not connected to UDP server
			}else if (!connected){
				System.out.println("Connecting..");				
				this.sendMessage("CONNECT " + name);
				
			// If currently connected to UDP server
			}else if (connected){
				// INIT_FOOD
				// If initial foods are sent
				if(serverData.startsWith("INIT_FOOD")) {
					System.out.println("foodd" + serverData);
					String[] temp = serverData.split(" ");
					this.game.getHandler().clearFood();
					if(temp.length != 1) {
						String[] foods = temp[1].split(":");
						for (int i = 0; i < foods.length; i++) {
							String[] food = foods[i].split("=");
							x 		= Integer.parseInt(food[0]);
							y 		= Integer.parseInt(food[1]);
							this.game.getHandler().addFood(new Food(x, y, ID.Food));
						}
					}
				}
				
				// INIT_BLOB
				// If initial blobs are sent
				else if(serverData.startsWith("INIT_BLOB")) {
					
					Blob newBlob;
					int x, y, index;
					String[] temp = serverData.split(" ");
					String[] blobs = temp[1].split(":");
					for (int i = 0; i < blobs.length; i++) {
						String[] blob = blobs[i].split("=");
						// 0 - index, 1- name, 2- x, 3 - y
						index 	= Integer.parseInt(blob[0]);
						x 		= Integer.parseInt(blob[2]);
						y 		= Integer.parseInt(blob[3]);
						newBlob = new Blob(x, y, ID.Blob, this.game.getHandler());
						newBlob.setNetIndex(index);
						this.game.getHandler().addBlob(newBlob);
					}
				}
				
				// NEW
				// If a new player is added
				else if(serverData.startsWith("NEW")) {
					Blob newBlob;
					int x, y, index;
					String[] temp = serverData.split(" ");
					String[] blob = temp[1].split("=");
					// 0 - index, 1- name, 2- x, 3 - y
					index 	= Integer.parseInt(blob[0]);
					x 		= Integer.parseInt(blob[2]);
					y 		= Integer.parseInt(blob[3]);
					newBlob = new Blob(x, y, ID.Blob, this.game.getHandler());
					newBlob.setNetIndex(index);
					this.game.getHandler().addBlob(newBlob);
				}
					
				// MOVE
				
				else if(serverData.startsWith("MOVE")) {
					Blob newBlob;
					int x, y, index;
					String[] temp = serverData.split(" ");
					String[] blob = temp[1].split("=");
					// 0 - index, 1- name, 2- x, 3 - y
					index 	= Integer.parseInt(blob[0]);
					x 		= Integer.parseInt(blob[2]);
					y 		= Integer.parseInt(blob[3]);
					// If update on movement is not the same as player 
					if(index != this.game.getHandler().netIndex) {
						newBlob = this.game.getHandler().blobs.get(index);
						newBlob.setX(x);
						newBlob.setY(y);
					}
				}
				// KILL
				// ATE
						
			}
		}
	}
}
