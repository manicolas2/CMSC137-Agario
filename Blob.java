package agario;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;


/***************************************
The class for the player
***************************************/
public final class Blob extends GameObject {
	/* ATTRIBUTES */
	private int size = 0;
	public int score = 0;
	Handler handler;
	String name;
	String blob_id;
	GameClient gc;
	boolean dead;

	/* CONSTRUCTOR */
	public Blob(int x, int y, ID id, Handler handler, String name){
		super(x, y, id);
		this.handler = handler;
		this.name = name;
		this.dead = false;
	}
	
	/* METHODS */

	/***************************************
	
	***************************************/
	public void setSize(int size) {
		this.size = size;
	}
	/***************************************
	
	***************************************/
	public int getSize() {
		return this.size;
	}
	
	/***************************************
	
	***************************************/
	public void setScore(int score) {
		this.score = score;
	}

	public void setGameClient(GameClient gc){
		this.gc = gc;
	}
	/***************************************
	
	***************************************/
	public void setName(String name) {
		this.name =  name;
	}
	/***************************************
	
	***************************************/
	public String getName() {
		return this.name;
	}

	/***************************************
	
	***************************************/
	public String getBlobId() {
		return this.blob_id;
	}

	/***************************************
	
	***************************************/
	public void setBlobId(String id) {
		this.blob_id = id;
	}
	
	/***************************************
	
	***************************************/
	public Rectangle getBounds() {
		return new Rectangle(x, y, 50+size, 50+size);
		
	}
	
	/***************************************
	
	***************************************/
	public void setDead(){
		this.dead = true;
	}


	public void tick() {
		x += speedX;
		y += speedY;
		
		x = GameCanvas.clamp(x, 0, 750 - (50 + size));
		y = GameCanvas.clamp(y, 0, GameCanvas._HEIGHT -(80 + size));
		
		collision();
	}
	
	/***************************************
	 Method for checking collision of blob and food
	***************************************/
	private void collision() {
		for(int i = 0; i < handler.foods.size(); i++) {
			Food food = handler.foods.get(i);
			if(getBounds().intersects(food.getBounds())) {
				this.size++;
				this.score += 10;
				handler.removeFood(food);
			}
		}
		LinkedList<String> deadList = new LinkedList<String>();
		for(Map.Entry<String, Blob> entry : this.handler.blobs.entrySet()) {
		    Blob blob = Blob.class.cast(entry.getValue());
		    int diff = Math.abs(this.size - blob.getSize());
		    if(getBounds().intersects(blob.getBounds()) && diff > 10){
		    	// case player > than other
		    	if(this.size > blob.getSize()){
		    		int tempSize, tempScore;
		    		setSize(this.size + (blob.getSize())/4);
		    		setScore(this.score + (blob.getSize())/5);
		    		// this.handler.removeBlob(blob.getBlobId());
		    		deadList.add(blob.getBlobId());
		    		this.gc.sendMessage("KILL " + blob.convertToString());
		    	}
		    	else if(this.size < blob.getSize()){
		    		this.dead = true;
		    		// this.gc.sendMessage("KILL " + convertToString());
		    	}
		    }
		}
		for(int i = 0; i < deadList.size(); i++) {
			String id = deadList.get(i);
			this.handler.removeBlob(id);
		}
	}
	
	/***************************************
	
	***************************************/
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillOval(x, y, 50+size, 50+size);
	}

	/***************************************
	
	***************************************/
	public String convertToString(){
		String string = "";
		string += blob_id + "=";
		string += name + "=";
		string += x + "=";
		string += y + "=";
		string += size + "=";
		string += score;
		
		return string;
	}

	public String convertToStringScore(){
		String string = "";
		string += blob_id + "=";
		string += name + "=";
		string += size + "=";
		string += score;
		
		return string;
	}
}
