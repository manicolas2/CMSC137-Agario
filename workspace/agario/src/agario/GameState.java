package agario;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public final class GameState {
	// Attributes
	LinkedList<NetBlob> netBlobs = new LinkedList<NetBlob>();
	LinkedList<NetFood> netFoods = new LinkedList<NetFood>();
	
	// Constructor
	
	// Methods
	public void addBlob(NetBlob blob) {
		this.netBlobs.add(blob);
	}

	public void removeBlob(NetBlob blob) {
		this.netBlobs.remove(blob);
	}
	
	public void addFood(NetFood food) {
		this.netFoods.add(food);
	}

	public void removeFood(NetFood food) {
		this.netFoods.remove(food);
	}
	
	public NetBlob getBlob(int index) {
		return this.netBlobs.get(index);
	}
	
	public NetFood getFood(int index) {
		return this.netFoods.get(index);
	}
	
	public String convertFoodString() {
		String string = "";
		for(int i = 0; i < this.netFoods.size(); i++) {
			NetFood food = this.netFoods.get(i);
			string += food.convertToString() + ":";
		}
		return string;
	}
	
	public String convertBlobString() {
		String string = "";
		for(int i = 0; i < this.netBlobs.size(); i++) {
			NetBlob blob = this.netBlobs.get(i);
			string += i + "=" + blob.convertToString() + ":";
		}
		return string;
	}
}
