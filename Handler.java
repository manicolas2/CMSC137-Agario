package agario;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/***************************************
Class that handles all blobs and foods	
***************************************/
public class Handler {
	/* ATTRIBUTES */
	HashMap<String, Blob> blobs = new HashMap<String, Blob>();
	LinkedList<Food> foods = new LinkedList<Food>();
	Blob player;
	
	/* CONSTRUCTOR */
	public void tick() {
		GameObject tempObject;

		for(Map.Entry<String, Blob> entry : blobs.entrySet()) {
			Blob blob = Blob.class.cast(entry.getValue());
			if(blob.getBlobId() != player.getBlobId()){
				blob.tick();
			}
		}

		if(player.dead == false){
			player.tick();
		}
	}
	
	/* METHODS */

	public boolean containsBlob(String blobID){
		return this.blobs.containsKey(blobID);
	}
	
	/***************************************
	
	***************************************/
	public void render(Graphics g) {
		GameObject tempObject;
		
		for(Map.Entry<String, Blob> entry : blobs.entrySet()) {
			Blob blob = Blob.class.cast(entry.getValue());
			if(blob.getBlobId() != player.getBlobId()){
				blob.render(g);
			}
		}
		
		for(int i = 0; i < foods.size(); i++) {
			tempObject = foods.get(i);
			tempObject.render(g);
		}

		// if(player.dead == false){
			player.render(g);
		// }
	}

	/***************************************
	
	***************************************/
	public void addBlob(String blob_id, Blob blob) {
		this.blobs.put(blob_id, blob);
	}

	/***************************************
	
	***************************************/
	public Blob getBlob(String blob_id) {
		return this.blobs.get(blob_id);
	}

	/***************************************
	
	***************************************/
	public HashMap<String, Blob> getBlobs() {
		return this.blobs;
	}

	/***************************************
	
	***************************************/
	public void removeBlob(String blob_id) {
		this.blobs.remove(blob_id);
	}
	
	/***************************************
	
	***************************************/
	public void addFood(Food food) {
		this.foods.add(food);
	}

	/***************************************
	
	***************************************/
	public void removeFood(Food food) {
		this.foods.remove(food);
	}
	
	/***************************************
	
	***************************************/
	public void clearFood() {
		this.foods = foods = new LinkedList<Food>();
	}
	
	/***************************************
	
	***************************************/
	public String printScores() {
		Blob tempObject;
		String string = "";
		for(Map.Entry<String, Blob> entry : blobs.entrySet()) {
			tempObject = Blob.class.cast(entry.getValue());
			string += tempObject.convertToStringScore() + "\n";
		}

		return string;
	}
}
