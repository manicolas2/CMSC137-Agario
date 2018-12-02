package agario;

import java.net.*;

import javax.swing.JTextField;

import java.io.*;
import proto.TcpPacketProtos.TcpPacket;
import proto.PlayerProtos.Player;

/*
 * Contains functions for chat
 */
public final class ChatModule {
	private DataOutputStream out;
	private DataInputStream in;
	private String ip;
	private int port;
	private Socket server;
	private Player player;
	private String id;
	private boolean connected;
	
	
	//Constructor
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
	
	//Modules
	// Toggle connection
	public void setConnected() {
		this.connected = false;
	}
	
	public boolean getConnected() {
		return this.connected;
	}
	// Create player
	private void createPlayer(String name) {
		this.player = Player.newBuilder()
				.setName(name)
				.build();
	}
	
	// Connect to a lobby
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
	
	// Disconnect from lobby
	public void disconnectFromLobby() {
		TcpPacket.DisconnectPacket.Builder disconnect = TcpPacket.DisconnectPacket.newBuilder()
			.setType(TcpPacket.PacketType.DISCONNECT);

		try{
			// Send DisconnectPacket
			out.write(disconnect.build().toByteArray());
			this.server.close();
		} catch(Exception e){}
	}
	
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
	
	// Receive ChatPacket
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
