package agario;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Map;
import javax.swing.JDialog;
import java.awt.Window;
import java.awt.Frame;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JLabel;

/***************************************
Has functions for receiving and sending
from and to UDP server
***************************************/
public final class GameClient implements Runnable {
	/* ATTRIBUTES */
	int x, y;
	String name;
	String server;
	boolean connected;
	boolean hasId;
	DatagramSocket socket;
	Thread thread;
	int port;
	GameCanvas game;
	ChatPanel chatPanel;
	ChatModule chatModule;
	String blob_id;
	String winner;
	boolean end;
	ScoreBoard scoreBoard;
	// String id;

	/* CONSTRUCTOR */
	public GameClient(int x, int y, String name, GameCanvas game, ChatPanel chatPanel, String ip) {
		this.x = x;
		this.y = y;
		this.name = name;
		this.server = ip;
		this.connected = false;
		this.port = 3010;
		this.game = game;
		this.chatPanel = chatPanel;
		this.chatModule = chatModule;
		this.hasId = false;
		this.end = false;

		try {
			this.socket = new DatagramSocket();
			System.out.println("socket sent");
		} catch (SocketException e) {
			e.printStackTrace();
		}

		this.start();
	}
	
	/* METHODS */
	/***************************************

	***************************************/
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
	}
	/***************************************

	***************************************/
	public void setScoreBoard(ScoreBoard board) {
		this.scoreBoard = board;
	}

	/***************************************

	***************************************/
	public synchronized boolean getConnected() {
		return this.connected;
	}

	public boolean getHasId(){
		return this.hasId;
	}
	
	/***************************************

	***************************************/
	public void sendMessage(String message) {
		try {
			byte[] bytes = message.getBytes();
			InetAddress ip = InetAddress.getByName(this.server);
			DatagramPacket packet = new DatagramPacket(bytes, bytes.length, ip, this.port);
			this.socket.send(packet);
		} catch(Exception e) {}
	}
	
	/***************************************

	***************************************/
	@Override
	public void run() {
		String serverData;
		
		// Send CONNECT to server
		this.sendMessage("CONNECT ");
		System.out.println("connected");
		while(!this.end){
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
				String[] info = serverData.split(" ");
				String lobby_id = info[1];

				System.out.println(serverData);

				
				String id = this.chatPanel.start(lobby_id);

				System.out.println("connected to server");

				System.out.println("Sending id" + id);

				this.sendMessage("DATA " + id);
				this.connected = true;
				this.game.setId(id);
				this.blob_id = id;

			// If currently not connected to UDP server
			} else if (!connected) {			
				this.sendMessage("CONNECT ");
			
			// If server received id
			} else if(serverData.startsWith("DATASENT")) {
				System.out.println("ID confirmed");
				this.hasId = true;

				this.sendMessage("INIT " + this.game.getHandler().player.convertToString());

			// If currently connected to UDP server
			} else if (connected && hasId) {
				this.scoreBoard.scores.setText(this.game.getHandler().printScores());
				// INIT_FOOD - If initial foods are sent
				if(serverData.startsWith("INIT_FOOD")) {
					String[] temp = serverData.split(" ");
					this.game.getHandler().clearFood();
					if(temp.length != 1) {
						String[] foods = temp[1].split(":");
						for (int i = 0; i < (foods.length) - 1; i++) {
							String[] food = foods[i].split("=");
							x 		= Integer.parseInt(food[0]);
							y 		= Integer.parseInt(food[1]);
							this.game.getHandler().addFood(new Food(x, y, ID.Food));
						}
					}
				}
				
				// INIT_BLOB - If initial blobs are sent
				else if(serverData.startsWith("INIT_BLOB")) {
					System.out.println("init");
					Blob newBlob;
					int x, y, index, size, score;

					String name, id;
					String[] temp = serverData.split(" ");
					String[] blobs = temp[1].split(":");

					for (int i = 0; i < blobs.length; i++) {
						String[] blob = blobs[i].split("=");

						id 		= (String) blob[0];
						name	= (String) blob[1];
						x 		= Integer.parseInt(blob[2]);
						y 		= Integer.parseInt(blob[3]);
						size	= Integer.parseInt(blob[4]);
						score 	= Integer.parseInt(blob[5]);
						
						newBlob = new Blob(x, y, ID.Blob, this.game.getHandler(), name);
						newBlob.setSize(size);
						newBlob.setScore(score);
						newBlob.setBlobId(id);

						if(id != this.game.getPlayer().getBlobId()) {
							this.game.getHandler().addBlob(id, newBlob);
						}
					}

					
				}
				
				// MOVE - Update movement of player
				else if(serverData.startsWith("MOVE ")) {
					Blob newBlob;
					int x, y, size, score;
					String name, id;
					String[] temp = serverData.split(" ");
					String[] blob = temp[1].split("=");

					id		= blob[0];
					name	= blob[1];
					x 		= Integer.parseInt(blob[2]);
					y 		= Integer.parseInt(blob[3]);
					size	= Integer.parseInt(blob[4]);
					score 	= Integer.parseInt(blob[5]);
					
					// If update on movement is not the same as player 
					if(id != this.game.getPlayer().getBlobId()) {

						newBlob = this.game.getHandler().getBlob(id);

						newBlob.setBlobId(id);
						newBlob.setName(name);
						newBlob.setX(x);
						newBlob.setY(y);
						newBlob.setSize(size);
						newBlob.setScore(score);
					}
				}
				// KILL
				else if(serverData.startsWith("KILL")){
					Blob newBlob;
					int x, y, size, score;
					String name, id;
					String[] temp = serverData.split(" ");
					String[] blob = temp[1].split("=");

					id		= blob[0];

					for(Map.Entry<String, Blob> entry : this.game.getHandler().blobs.entrySet()) {
						Blob tempblob = Blob.class.cast(entry.getValue());
						if(id == tempblob.getBlobId()){
							this.game.getHandler().getBlob(id).setDead();
						}
					}
				}
				// ATE
				// END
				else if(serverData.startsWith("END")){
					String[] temp = serverData.split(" ");
					this.winner = temp[1];
					this.connected = false;
					this.end = true;
				}
			}
		}

		System.out.println("WINNER" + this.winner);

		GameWindow gw = this.game.getWindow();
		gw.dispose();

		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		String win_name = this.game.getHandler().getBlob(this.winner).getName();
		JLabel label = new JLabel("WINNER " + win_name);
		panel.add(label);
		frame.setContentPane(panel);
		frame.setPreferredSize(new Dimension(300, 300));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMaximumSize(new Dimension(300, 300));
		frame.setMinimumSize(new Dimension(300, 300));
		frame.setFocusable(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);

	}
}
