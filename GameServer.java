package agario;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import agario.NetBlob;

import java.net.InetAddress;

/***************************************
	
***************************************/
public final class GameServer implements Runnable, Constants{
	/* ATTRIBUTES */
	int 			currPlayers 		= 0;
	int 			maxPlayers;					
	DatagramSocket 	server 				= null;
	GameState 		gameState;
	int 			gameStatus 			= WAITING;
	Thread 			t 					= new Thread(this);
	Random 			r;
	DatagramPacket  packet;
	String 		    lobby_id;
	String 			winner;

	/* CONSTRUCTOR */
	public GameServer(int maxPlayers, String lobby_id) {
		this.maxPlayers = maxPlayers;
		this.gameState 	= new GameState();
		this.r 			= new Random();
		this.lobby_id 	= lobby_id;

		this.startServer();

		this.t.start();
	}
	
	/* METHDDS */

	/***************************************
	
	***************************************/
	private void startServer() {
		try {
			this.server = new DatagramSocket(PORT);
			this.server.setSoTimeout(100);
		} catch(IOException e) {
			System.err.println("Could not listen on port: " + PORT);
			System.exit(-1);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/***************************************
	
	***************************************/
	public void broadcast(String msg){
		for(Map.Entry<String, NetBlob> entry : this.gameState.netBlobs.entrySet()) {
			NetBlob blob = NetBlob.class.cast(entry.getValue());
			this.send(blob, msg);
		}
	}

	/***************************************
	
	***************************************/
	public void send(NetBlob blob, String msg){
		DatagramPacket packet;	
		byte buf[] = msg.getBytes();		
		packet = new DatagramPacket(buf, buf.length, blob.getAddress(), blob.getPort());
		
		try{
			this.server.send(packet);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}

	/***************************************
	
	***************************************/
	public void send(InetAddress address, int port, String msg){
		DatagramPacket packet;	
		byte buf[] = msg.getBytes();		
		packet = new DatagramPacket(buf, buf.length, address, port);
		
		try{
			this.server.send(packet);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	/***************************************
	This function converts an array of bytes 
	to String
	***************************************/
	private String convertBytestoString(byte[] bytes) {
		String temp;

		temp = new String(bytes);
		temp = temp.trim();

		return temp;
	}

	/***************************************
	
	***************************************/
	private byte[] receiveData() {
		// Get data from players
		byte[] bytes = new byte[256];
		this.packet = new DatagramPacket(bytes, bytes.length);
		try {
			this.server.receive(packet);
		} catch(Exception e) {}

		return bytes;
	}

	/***************************************
	
	***************************************/
	private void generateFood() {
		if(this.r.nextInt(10) == 5){
			this.gameState.addFood(new NetFood(r.nextInt(950), r.nextInt(700)));
		}
	}

	/***************************************
	
	***************************************/
	@Override
	public void run() {
		String data;

		while(this.gameStatus != END) {			
			// Generate food
			this.generateFood();

			// Get data from connection
			byte[] bytes = this.receiveData();
			// Convert data to string
			data = this.convertBytestoString(bytes);
			//System.out.println("RECEIVED: " +data);
			switch(this.gameStatus) {
			
				// If have not reached maximum number of players
				case WAITING:
					// If data received starts with CONNECT
					if (data.startsWith("CONNECT")) {
						
						// Send confirmation
						this.send(packet.getAddress(), packet.getPort(), "CONNECTED " + this.lobby_id);
		
					} else if(data.startsWith("DATA")) {
						// Get player data
						String tokens[] = data.split(" ");
						NetBlob blob = new NetBlob(tokens[1], packet.getAddress(), packet.getPort());

						// Update server gameState
						this.gameState.addBlob(tokens[1], blob);

						// Update number of currently connected players
						this.currPlayers++;

						// Inform client that id has sent
						this.send(blob, "DATASENT");
						
					} else if(data.startsWith("INIT")) {
						//Tokenize
						String[] playerInfo = data.split(" ");					  
						String[] blob_info		= playerInfo[1].split("=");

						String id = blob_info[0].trim();
						String name = blob_info[1].trim();
						int x = Integer.parseInt(blob_info[2].trim());
						int y = Integer.parseInt(blob_info[3].trim());
						int size = Integer.parseInt(blob_info[4].trim());
						int score = Integer.parseInt(blob_info[5].trim());

						//Update blob in gameState
						NetBlob blob = this.gameState.getBlob(id);

						blob.setId(id);
						blob.setName(name);
						blob.setX(x);
						blob.setY(y);
						blob.setSize(size);
						blob.setScore(score);

						// If reached max players, change game status
						if (this.currPlayers == this.maxPlayers){
							this.gameStatus = READY;
						}

					} else if(data.startsWith("MOVE")) {
						//Tokenize
						String[] playerInfo = data.split(" ");					  
						String[] blob_info		= playerInfo[1].split("=");

						String id = blob_info[0].trim();
						String name = blob_info[1].trim();
						int x = Integer.parseInt(blob_info[2].trim());
						int y = Integer.parseInt(blob_info[3].trim());
						int size = Integer.parseInt(blob_info[4].trim());
						int score = Integer.parseInt(blob_info[5].trim());

						NetBlob blob = this.gameState.getBlob(id);

						blob.setId(id);
						blob.setName(name);
						blob.setX(x);
						blob.setY(y);
						blob.setSize(size);
						blob.setScore(score);
					}

					break;
				case READY:
					System.out.println("Game ready");
					
					this.gameStatus = START;
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
					this.generateFood();

					// Always send food
					try{
						int pause = r.nextInt(100);
						if(pause == 4){
							this.broadcast("INIT_FOOD " + this.gameState.convertFoodString());
						}	
					}catch(Exception e){}

					// If data received is about movement update
					if (data.startsWith("MOVE")){
						
						//Tokenize
						String[] playerInfo = data.split(" ");					  
						String[] blob_info		= playerInfo[1].split("=");

						String id = blob_info[0].trim();
						String name = blob_info[1].trim();
						int x = Integer.parseInt(blob_info[2].trim());
						int y = Integer.parseInt(blob_info[3].trim());
						int size = Integer.parseInt(blob_info[4].trim());
						int score = Integer.parseInt(blob_info[5].trim());

						// if(this.gameState.containsBlob(id)){
							//Update blob in gameState
							NetBlob blob = this.gameState.getBlob(id);

							blob.setId(id);
							blob.setName(name);
							blob.setX(x);
							blob.setY(y);
							blob.setSize(size);
							blob.setScore(score);
						  
							//Send updated blob
							this.broadcast("MOVE " + blob.convertToString());
						// }
						if(score >= 1000) {
							this.gameStatus = END;
							this.broadcast("END " + id);
							this.winner = id;
						}
					}
					

					else if (data.startsWith("KILL")){
						//Tokenize
						String[] playerInfo = data.split(" ");					  
						String[] blob_info		= playerInfo[1].split("=");

						String id = blob_info[0].trim();
						String name = blob_info[1].trim();
						int x = Integer.parseInt(blob_info[2].trim());
						int y = Integer.parseInt(blob_info[3].trim());
						int size = Integer.parseInt(blob_info[4].trim());
						int score = Integer.parseInt(blob_info[5].trim());

						NetBlob blob = this.gameState.getBlob(id);

						blob.setId(id);
						blob.setName(name);
						blob.setX(x);
						blob.setY(y);
						blob.setSize(size);
						blob.setScore(score);
						  

						this.gameState.getBlob(id).setDead();
						this.broadcast("KILL " + blob.convertToString());
					}
					
					break;

				case END:
					break;
			}
		}

		this.server.close();
	}
	
}
