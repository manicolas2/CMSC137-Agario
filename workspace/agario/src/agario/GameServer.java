package agario;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public final class GameServer implements Runnable{
	// Constants
	public static final int START		 = 0;
	public static final int IN_PROGRESS  = 1;
	public final int 		END          = 2;
	public final int 		WAITING		 = 3;

	// Attributes
	String 			playerData;
	int 			currPlayers 		= 0;
	int 			maxPlayers;					
	DatagramSocket 	serverSocket 		= null;
	GameState 		gameState;
	int 			gameStatus 			= WAITING;
	Thread 			t 					= new Thread(this);
	Random 			r;
	
	// Constructor
	public GameServer(int port, int maxPlayers) {
		this.maxPlayers = maxPlayers;
		this.gameState 	= new GameState();
				
		try {
			serverSocket = new DatagramSocket(port);
			serverSocket.setSoTimeout(100);
		} catch(IOException e) {
			System.err.println("Could not listen on port: " + port);
			System.exit(-1);
		} catch(Exception e) {
			System.out.println(e);
		}
		
		// Start thread
		this.t.start();
		
		// Start generating food
		while(true) {
//			if(this.gameStatus == IN_PROGRESS || this.gameStatus == START) {
				r = new Random();
				try {
					Thread.sleep(r.nextInt(200));
				}catch(Exception e){
					System.out.println(e);
	            }
				
				if(r.nextInt(10) == 5){
	                this.gameState.addFood(new NetFood(r.nextInt(1050), r.nextInt(750)));
	                this.broadcast("INIT_FOOD " + this.gameState.convertFoodString());
	            }
//			}
		}
	}
	
	// Methods

	public void broadcast(String msg){
		for(int i = 0; i < this.gameState.netBlobs.size(); i++) {
			NetBlob blob = this.gameState.netBlobs.get(i);
			this.send(blob, msg);
		}
	}

	public void send(NetBlob blob, String msg){
		DatagramPacket packet;	
		byte buf[] = msg.getBytes();		
		packet = new DatagramPacket(buf, buf.length, blob.getAddress(), blob.getPort());
		
		try{
			serverSocket.send(packet);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(true) {
			// Get data from players
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try {
				serverSocket.receive(packet);
			} catch(Exception e) {}
			
			// Convert byte array to string
			this.playerData = new String(buf);
			this.playerData = this.playerData.trim();
			
			
			switch(this.gameStatus) {
			
				// If have not reached maximum number of players
				case WAITING:
					
					// If data received starts with CONNECT
					if (playerData.startsWith("CONNECT")){
						
						// Get player data
						String tokens[] = playerData.split(" ");
						NetBlob blob = new NetBlob(tokens[1], packet.getAddress(), packet.getPort());
						
						// Inform
						System.out.println("Player connected: "+ tokens[1]);
						
						// Update server gameState
						this.gameState.addBlob(blob);
						
						// Send confirmation
						this.send(blob, "CONNECTED " + this.currPlayers);
						
						// Update number of currently connected players
						this.currPlayers++;
						
						// If reached max players, change game status
						if (this.currPlayers == this.maxPlayers){
							this.gameStatus = START;
						}
					}
					break;
					
				// If have reached maximum number of players, start game
				case START:
					System.out.println("Game started");
					this.broadcast("INIT_FOOD " + this.gameState.convertFoodString());
					this.broadcast("INIT_BLOB " + this.gameState.convertBlobString());
					this.gameStatus = IN_PROGRESS;
					break;
					
				// If game is currently in progress
				case IN_PROGRESS:
					
					// If data received is about movement update
					if (this.playerData.startsWith("MOVE")){
						
						//Tokenize
						String[] playerInfo = playerData.split(" ");					  
						String[] blob_info		= playerInfo[1].split("=");
						int index = Integer.parseInt(blob_info[0]);
						int x = Integer.parseInt(blob_info[1].trim());
						int y = Integer.parseInt(blob_info[2].trim());
						
//						System.out.println("server" +playerData);
						//Update blob in gameState
						NetBlob blob = this.gameState.getBlob(index);					  
						blob.setX(x);
						blob.setY(y);
					  
						//Send updated blob
//						System.out.print(blob.convertToString());
						this.broadcast("MOVE " + index + "=" + blob.convertToString());
					}
					
					break;
			}
		}
	}
	
	// Main
	public static void main(String[] args) {
		int port 			= 4444;
		int numOfPlayers 	= 2;
		
		new GameServer(port, numOfPlayers);
	}
}
