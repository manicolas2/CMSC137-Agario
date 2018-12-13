package agario;

import java.net.*;

import javax.swing.JTextField;

import java.io.*;
import proto.TcpPacketProtos.TcpPacket;
import proto.PlayerProtos.Player;

/***************************************
Contains functions for chat
***************************************/
public final class ChatModule {
	/* ATTRIBUTES */
	private DataOutputStream out;
	private DataInputStream in;
	private String ip;
	private int port;
	private Socket server;
	private Player player;
	private String id = null;
	private boolean connected;
	private GamePanel gamePanel;
	
	
	/* CONSTRUCTOR */
	public ChatModule(String ip, int port, String name) {
		this.ip = ip;
		this.port = port;
		this.connected = true;
		this.createPlayer(name);
		
		try {
			// Connect to chat server
			this.server = new Socket(this.ip, this.port);

			// Create streams
			OutputStream outStream = this.server.getOutputStream();
			this.out = new DataOutputStream(outStream);
			InputStream inStream = this.server.getInputStream();
			this.in = new DataInputStream(inStream);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/* METHODS */

	/***************************************
     
    ***************************************/
    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
	}
	
	/***************************************
	Set connection status to false
	***************************************/
	public void setConnected() {
		this.connected = false;
	}
	
	/***************************************
	Get connection status
	***************************************/
	public boolean getConnected() {
		return this.connected;
	}

	/***************************************
	Get id of player
	***************************************/
	public String getId() {
		return this.id;
	}
	
	/***************************************
	Create a player
	***************************************/
	private void createPlayer(String name) {
		this.player = Player.newBuilder()
				.setName(name)
				.build();
	}

	/***************************************
	Create a chat lobby
	***************************************/
	public String createALobby(int maxPlayers) {
		TcpPacket.CreateLobbyPacket.Builder createLobby = TcpPacket.CreateLobbyPacket.newBuilder()
				.setType(TcpPacket.PacketType.CREATE_LOBBY)
				.setMaxPlayers(maxPlayers);
		
		try {
			this.out.write(createLobby.build().toByteArray());
			while(in.available() == 0){}
			byte[] bytes = new byte[in.available()];
			in.read(bytes);
			TcpPacket msg = TcpPacket.parseFrom(bytes);
			TcpPacket.PacketType msg_type = msg.getType();
			
			// If ConnectPacket, successful
			if(msg_type == TcpPacket.PacketType.CREATE_LOBBY) {
				TcpPacket.CreateLobbyPacket message = TcpPacket.CreateLobbyPacket.parseFrom(bytes);	
				System.out.println("You have created chat lobby. {" + message.getLobbyId() + "}");
				//this.host = true;
				return message.getLobbyId();
			}

		} catch(Exception e) {
			System.out.print(e);
		}
		return null;
	}	
	
	/***************************************
	Connect to a chat lobby
	***************************************/
	public boolean connectToLobby(String lobby_id, ChatPanel chatPanel) {
		// Create ConnectPacket
		TcpPacket.ConnectPacket.Builder connect = TcpPacket.ConnectPacket.newBuilder()
				.setType(TcpPacket.PacketType.CONNECT)
				.setPlayer(this.player)
				.setLobbyId(lobby_id)
				.setUpdate(TcpPacket.ConnectPacket.Update.NEW);

		try{
			// Send ConnectPacket
			this.out.write(connect.build().toByteArray());	

			// Check response 
			while(in.available() == 0){}
			byte[] bytes = new byte[in.available()];
			in.read(bytes);
			
			// Get type of response
			TcpPacket msg = TcpPacket.parseFrom(bytes);
			TcpPacket.PacketType msg_type = msg.getType();

			if(msg_type == TcpPacket.PacketType.CONNECT) {
				chatPanel.displayMessage("Connected to chat lobby.\n");
				TcpPacket.ConnectPacket message = TcpPacket.ConnectPacket.parseFrom(bytes);
				if(message.hasPlayer()) {
					Player player = message.getPlayer();
					if(player.hasId()) this.id = player.getId();
				}
				return true;
			} else if(msg_type == TcpPacket.PacketType.ERR_LDNE) {
				chatPanel.displayMessage("Error: Lobby does not exist.\n");
			} else if(msg_type == TcpPacket.PacketType.ERR_LFULL) {
				chatPanel.displayMessage("Error: Lobby is full.\n");
			} 
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/***************************************
	Disconnect from chatLobby
	***************************************/
	public void disconnectFromLobby() {
		TcpPacket.DisconnectPacket.Builder disconnect = TcpPacket.DisconnectPacket.newBuilder()
			.setType(TcpPacket.PacketType.DISCONNECT);

		try{
			// Send DisconnectPacket
			out.write(disconnect.build().toByteArray());
			this.server.close();
		} catch(Exception e){}
	}
	
	/***************************************
	Send chat packet to chat lobby
	***************************************/
	public void sendChat(String message) {
		TcpPacket.ChatPacket.Builder chat = TcpPacket.ChatPacket.newBuilder()
			.setType(TcpPacket.PacketType.CHAT)
			.setMessage(message)
			.setPlayer(this.player);
		try{
			out.write(chat.build().toByteArray());	
		} catch(Exception e){
			System.out.println(e);
		}
	}
	
	/***************************************
	Receive chat from chat lobby
	***************************************/
	public void receiveChat(ChatPanel chatPanel) {
		try {
			while(in.available() == 0){ if(this.connected == false) return;}
			byte[] bytes = new byte[in.available()];
			in.read(bytes);
			TcpPacket msg = TcpPacket.parseFrom(bytes);
			TcpPacket.PacketType msg_type = msg.getType();
			
			if(msg_type == TcpPacket.PacketType.CHAT) {
				TcpPacket.ChatPacket message = TcpPacket.ChatPacket.parseFrom(bytes);
				if(message.hasPlayer()) {
					Player player = message.getPlayer();
					String id = player.getId();
					if(!id.equals(this.id))chatPanel.displayMessage(player.getName() + "::" + message.getMessage() + "\n");
				}
			} else if(msg_type == TcpPacket.PacketType.DISCONNECT) {
				TcpPacket.DisconnectPacket message = TcpPacket.DisconnectPacket.parseFrom(bytes);
				chatPanel.displayMessage(message.getPlayer().getName() + " disconnected from chat lobby.\n");
				this.gamePanel.getHandler().removeBlob(message.getPlayer().getId());
			} else if(msg_type == TcpPacket.PacketType.ERR) {
				TcpPacket.ErrPacket message = TcpPacket.ErrPacket.parseFrom(bytes);
				chatPanel.displayMessage("Error: " + message.getErrMessage() + "\n");
			} else if(msg_type == TcpPacket.PacketType.CONNECT) {
				TcpPacket.ConnectPacket message = TcpPacket.ConnectPacket.parseFrom(bytes);
				chatPanel.displayMessage(message.getPlayer().getName() + " connected to chat lobby. \n");
			}
		} catch(Exception e) {e.printStackTrace();} 
	}
}
