import proto.TcpPacketProtos.TcpPacket;
import proto.PlayerProtos.Player;
import java.net.*;
import java.io.*;
import java.util.Scanner;

public final class Client extends Thread {
	DataInputStream in;
	DataOutputStream out;
	Scanner sc;
	private Player player;
	Main main;
	private boolean connected;

	// Constructor
	// Client instantiates player
	public Client(DataInputStream in, DataOutputStream out, Scanner sc, String name, Main main) {
		this.in = in;
		this.out = out;
		this.sc = sc;
		this.player = Player.newBuilder()
							.setName(name)
							.build();
		this.connected = true;
	}

	// Modules

	// Create a lobby then automatically connect to created lobby then chat
	// return true if successful
	public boolean createALobby(String lobby_id) {
		return false;
	}

	// Connect to a lobby
	public boolean connectToLobby(String lobby_id) {
		// Create ConnectPacket
		TcpPacket.ConnectPacket.Builder connect = TcpPacket.ConnectPacket.newBuilder()
				.setType(TcpPacket.PacketType.CONNECT)
				.setPlayer(this.player)
				.setLobbyId(lobby_id)
				.setUpdate(TcpPacket.ConnectPacket.Update.NEW);

		try{
			// Send ConnectPacket
			this.out.write(connect.build().toByteArray());	
			while(in.available() == 0){}
			byte[] bytes = new byte[in.available()];
			in.read(bytes);
			TcpPacket msg = TcpPacket.parseFrom(bytes);
			TcpPacket.PacketType msg_type = msg.getType();

			// If ConnectPacket, successful
			if(msg_type == TcpPacket.PacketType.CONNECT) {
				System.out.println("You are connected to chat lobby.");
				return true;
			}
			// Handle other error
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Send DisconnectPacket and handle packets received
	// return true when successful <- di ako sure if needed
	private boolean disconnectFromLobby() {
		return false;
	}

	public void chat() {
		System.out.println("type :q to disconnect from lobby");

		String msg;
		do {
			msg = this.sc.nextLine();
			
			TcpPacket.ChatPacket.Builder chat = TcpPacket.ChatPacket.newBuilder()
				.setType(TcpPacket.PacketType.CHAT)
				.setMessage(msg)
				.setPlayer(this.player);
			try{
				out.write(chat.build().toByteArray());	
			} catch(Exception e){}
		} while(!msg.equals(":q"));

		// Disconnect
		this.setDisconnect(); //Update flag to end thread
		// Call disconnectFromLobby()
		// ->
	}

	// Update flag
	public void setDisconnect() {
		this.connected = false;
	}

	// 
	private void receiveChat() {
		try {
			while(in.available() == 0){}
			byte[] bytes = new byte[in.available()];
			in.read(bytes);
			TcpPacket msg = TcpPacket.parseFrom(bytes);
			TcpPacket.PacketType msg_type = msg.getType();

			if(msg_type == TcpPacket.PacketType.CHAT) {
				TcpPacket.ChatPacket message = TcpPacket.ChatPacket.parseFrom(bytes);
				if(message.hasPlayer()) System.out.println(message.getPlayer() + "::" + message.getMessage());
				else System.out.println("::" + message.getMessage());
			}
			// Should try to handle other types??
		} catch(Exception e) {} 
	}

	@Override
	public void run() {
		while(this.connected) {
			this.receiveChat();
		}
	}
}