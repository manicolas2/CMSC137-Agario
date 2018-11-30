package agario;

import java.util.HashMap;
import java.util.Iterator;

public final class GameState {
	// Attributes
	private HashMap players = new HashMap();
	
	// Constructor
	public GameState() {
		
	}
	
	// Methods
	
		// Update game state
	public void update(String name, NetPlayer player) {
		this.players.put(name, player);
	}
	
	public String toString() {
		String retval = "";
		for(Iterator ite = players.keySet().iterator(); ite.hasNext(); ){
			String name = (String)ite.next();
			NetPlayer player = (NetPlayer)players.get(name);
			retval += player.toString()+":";
		}
		return retval;
	}
	
	public HashMap getPlayers() {
		return this.players;
	}

}
