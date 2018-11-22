import java.net.*;
import java.io.*;
import java.util.Scanner;

public final class Main {
	// Attributes
	private DataOutputStream out;
	private DataInputStream in;
	private Scanner sc;
	private String ip;
	private int port;
	private Client client;
	private boolean connected;

	// Modules
	private void start() {
		try {
			// Connect to Server
			Socket server = new Socket(this.ip, this.port);
			System.out.println("Connected to server");

			// 
			OutputStream outStream = server.getOutputStream();
			this.out = new DataOutputStream(outStream);
			InputStream inStream = server.getInputStream();
			this.in = new DataInputStream(inStream);

			// Enter player name
			System.out.print("Enter name: ");
			String name = sc.nextLine();
			// Init client
			this.client = new Client(this.in, this.out, this.sc, name, this);

			int input;
			printMenu();
			input = sc.nextInt();
			sc.nextLine();
			// Connect to a lobby
			if(input == 1) {
				System.out.print("Enter lobby id: ");
				String lobby_id = sc.nextLine();

				// If connected to lobby
				if(this.client.connectToLobby(lobby_id)) {
					this.client.start(); 	//start chat thread
					this.client.chat();		//start loop of sending message
				} 

			// Create a lobby
			} else if(input == 2) {

			}

			// Disconnect from server
			client.setDisconnect(); //To end thread
			System.out.println("Disconnected from server");
			server.close();
		} catch(IOException e) {
			e.printStackTrace();
            System.out.println("Cannot find (or disconnected from) Server");
		} 
	}

	private void printMenu() {
		System.out.println("---- MENU ----");
		System.out.println("[1] Connect to a Lobby");
		System.out.println("[2] Create a lobby");
		System.out.println("[0] Exit");
	}

	// Constructor
	public Main(String ip, int port) {
		this.sc = new Scanner(System.in);
		this.ip = ip;
		this.port = port;
		this.connected = true;

		this.start();
	}

	// Main()
	public static void main(String[] args) {
		Main main = new Main("202.92.144.45", 80);
	}
}