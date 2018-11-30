package agario;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class GameServer implements Runnable{
	// Constants
	public static final int START		 = 0;
	public static final int IN_PROGRESS  = 1;
	public final int 		END          = 2;
	public final int 		WAITING		 = 3;

	// Attributes
	String playerData;
	int currPlayerCount = 0;
	int numPlayers;					//exact number of players that must be connected 
	DatagramSocket serverSocket = null;
	GameState gameState;
	int gameStatus = WAITING;
	Thread t = new Thread(this);
	
	
	// Constructor
	public GameServer(int port, int numPlayers) {
		this.numPlayers = numPlayers;
		try {
			serverSocket = new DatagramSocket(port);
			serverSocket.setSoTimeout(100);
		} catch(IOException e) {
			System.err.println("Could not listen on port: " + port);
			System.exit(-1);
		} catch(Exception e) {
			System.out.println(e);
		}
		
		this.gameState = new GameState();
		
		this.t.start();
	}
	
	
	//Methods
	
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
			// Start
			switch(this.gameStatus) {
				case WAITING:
					System.out.println("WAITING...");
					if (playerData.startsWith("CONNECT")){
						// Get player data
						String tokens[] = playerData.split(" ");
						NetPlayer player = new NetPlayer(tokens[1], packet.getAddress(), packet.getPort());
						System.out.println("Player connected: "+ tokens[1]);
						
						this.gameState.update(tokens[1].trim(), player);
						this.currPlayerCount++;
						if (this.currPlayerCount == this.numPlayers){
							this.gameStatus = START;
						}
					}
					break;
				case START:
					System.out.println("Game started");
					this.gameStatus = IN_PROGRESS;
					break;
				case IN_PROGRESS:
					System.out.println("Game in progress");
					
					if (this.playerData.startsWith("PLAYER")){
						//Tokenize:
						String[] playerInfo = playerData.split(" ");					  
					  
						String pname = playerInfo[1];
						int x = Integer.parseInt(playerInfo[2].trim());
						int y = Integer.parseInt(playerInfo[3].trim());
					  
						//Get the player from the game state
						NetPlayer player = (NetPlayer)this.gameState.getPlayers().get(pname);					  
						player.setX(x);
						player.setY(y);
					  
						//Update the game state
						this.gameState.update(pname, player);
					}
					break;
			}
		}
	}
	
	// Main
	public static void main(String[] args) {
		int port 			= 4444;
		int numOfPlayers 	= 3;
		
		new GameServer(port, numOfPlayers);
	}
}
