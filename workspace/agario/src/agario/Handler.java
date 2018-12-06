package agario;

import java.awt.Graphics;
import java.util.LinkedList;

public class Handler {
	LinkedList<Blob> blobs = new LinkedList<Blob>();
	LinkedList<Food> foods = new LinkedList<Food>();
	Blob player;
	int netIndex;
	
	public void tick() {
		GameObject tempObject;
		
		for(int i = 0; i < blobs.size(); i++){
			if(i != netIndex) {
				tempObject = blobs.get(i);
				tempObject.tick();
			}
		}
		
		for(int i = 0; i < foods.size(); i++){
			tempObject = foods.get(i);
			tempObject.tick();
		}
		player.tick();
	}
	
	public void render(Graphics g) {
		GameObject tempObject;
		for(int i = 0; i < blobs.size(); i++) {
			if(i != netIndex) {
				tempObject = blobs.get(i);
				tempObject.render(g);
			}
		}
		for(int i = 0; i < foods.size(); i++) {
			tempObject = foods.get(i);
			tempObject.render(g);
		}
		player.render(g);
	}
	
	public void addBlob(Blob blob) {
		this.blobs.add(blob);
	}

	public void removeBlob(Blob blob) {
		this.blobs.remove(blob);
	}
	
	public void addFood(Food food) {
		this.foods.add(food);
	}

	public void removeFood(Food food) {
		this.foods.remove(food);
	}
	
	public void clearFood() {
		this.foods = foods = new LinkedList<Food>();
	}
}
