package agario;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import agario.NetBlob;

/***************************************

***************************************/
public final class GameState {
	/* ATTRIBUTES */
	HashMap<String, NetBlob> netBlobs = new HashMap<String, NetBlob>();
	LinkedList<NetFood> netFoods = new LinkedList<NetFood>();
	
	/* CONSTRUCTOR */
	
	/* METHODS */

	/***************************************

	***************************************/
	public void addBlob(String blob_id, NetBlob blob) {
		this.netBlobs.put(blob_id, blob);
		System.out.println("ADDED: " + blob_id);
	}

	/***************************************

	***************************************/
	public void removeBlob(String blob_id) {
		this.netBlobs.remove(blob_id);
	}
	
	/***************************************

	***************************************/
	public void addFood(NetFood food) {
		this.netFoods.add(food);
	}
	
	/***************************************

	***************************************/
	public NetBlob getBlob(String blob_id) {
		return this.netBlobs.get(blob_id);
	}
	
	/***************************************

	***************************************/
	public NetFood getFood(int index) {
		return this.netFoods.get(index);
	}

	public boolean containsBlob(String blobID){
		return this.netBlobs.containsKey(blobID);
	}
	
	/***************************************

	***************************************/
	public String convertFoodString() {
		String string = "";
		for(int i = 0; i < this.netFoods.size(); i++) {
			NetFood food = this.netFoods.get(i);
			string += food.convertToString() + ":";
		}
		return string;
	}
	
	/***************************************

	***************************************/
	public String convertBlobString() {
		String string = "";
		
		for(Map.Entry<String, NetBlob> entry : netBlobs.entrySet()) {
		    NetBlob blob = entry.getValue();
		    
		    string += blob.convertToString() + ":";
		}
		
		System.out.println("String: " + string);
		
		return string;
	}
}
