import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.net.URL;

import javax.swing.ImageIcon;

public class Player {
	double x;
	double y; 
	ImageIcon image;
	
	ClassLoader cldr = this.getClass().getClassLoader();
	String imagePath;
	URL imageURL;
	ImageIcon front1;
	ImageIcon front2;
	ImageIcon front3;
	ImageIcon front4;
	ImageIcon back1;
	ImageIcon back2;
	ImageIcon back3;
	ImageIcon back4;
	ImageIcon left1;
	ImageIcon left2;
	ImageIcon left3;
	ImageIcon left4;
	ImageIcon right1;
	ImageIcon right2;
	ImageIcon right3;
	ImageIcon right4;
	String direction = "back";
	int sprite = 1;
	int speed = 3;
	boolean moving = false;
	public Player(int x, int y){
		String color;
		color = "player";
		imagePath = color+"/front1.png";			
		imageURL = cldr.getResource(imagePath);				
		front1 = new ImageIcon(imageURL);
		imagePath = color+"/front2.png";			
		imageURL = cldr.getResource(imagePath);				
		front2 = new ImageIcon(imageURL);
		imagePath = color+"/front3.png";			
		imageURL = cldr.getResource(imagePath);				
		front3 = new ImageIcon(imageURL);
		imagePath = color+"/front4.png";			
		imageURL = cldr.getResource(imagePath);				
		front4 = new ImageIcon(imageURL);
		imagePath = color+"/back1.png";			
		imageURL = cldr.getResource(imagePath);				
		back1 = new ImageIcon(imageURL);
		imagePath = color+"/back2.png";			
		imageURL = cldr.getResource(imagePath);				
		back2 = new ImageIcon(imageURL);
		imagePath = color+"/back3.png";			
		imageURL = cldr.getResource(imagePath);				
		back3 = new ImageIcon(imageURL);
		imagePath = color+"/back4.png";			
		imageURL = cldr.getResource(imagePath);				
		back4 = new ImageIcon(imageURL);
		imagePath = color+"/left1.png";			
		imageURL = cldr.getResource(imagePath);				
		left1 = new ImageIcon(imageURL);
		imagePath = color+"/left2.png";			
		imageURL = cldr.getResource(imagePath);				
		left2 = new ImageIcon(imageURL);
		imagePath = color+"/left3.png";			
		imageURL = cldr.getResource(imagePath);				
		left3 = new ImageIcon(imageURL);
		imagePath = color+"/left4.png";			
		imageURL = cldr.getResource(imagePath);				
		left4 = new ImageIcon(imageURL);
		imagePath = color+"/right1.png";			
		imageURL = cldr.getResource(imagePath);				
		right1 = new ImageIcon(imageURL);
		imagePath = color+"/right2.png";			
		imageURL = cldr.getResource(imagePath);				
		right2 = new ImageIcon(imageURL);
		imagePath = color+"/right3.png";			
		imageURL = cldr.getResource(imagePath);				
		right3 = new ImageIcon(imageURL);
		imagePath = color+"/right4.png";			
		imageURL = cldr.getResource(imagePath);				
		right4 = new ImageIcon(imageURL);
		this.x = x;
		this.y = y;
		image = back1;
	}
	public void switchColor(String color){
		imagePath = color+"/front1.png";			
		imageURL = cldr.getResource(imagePath);				
		front1 = new ImageIcon(imageURL);
		imagePath = color+"/front2.png";			
		imageURL = cldr.getResource(imagePath);				
		front2 = new ImageIcon(imageURL);
		imagePath = color+"/front3.png";			
		imageURL = cldr.getResource(imagePath);				
		front3 = new ImageIcon(imageURL);
		imagePath = color+"/front4.png";			
		imageURL = cldr.getResource(imagePath);				
		front4 = new ImageIcon(imageURL);
		imagePath = color+"/back1.png";			
		imageURL = cldr.getResource(imagePath);				
		back1 = new ImageIcon(imageURL);
		imagePath = color+"/back2.png";			
		imageURL = cldr.getResource(imagePath);				
		back2 = new ImageIcon(imageURL);
		imagePath = color+"/back3.png";			
		imageURL = cldr.getResource(imagePath);				
		back3 = new ImageIcon(imageURL);
		imagePath = color+"/back4.png";			
		imageURL = cldr.getResource(imagePath);				
		back4 = new ImageIcon(imageURL);
		imagePath = color+"/left1.png";			
		imageURL = cldr.getResource(imagePath);				
		left1 = new ImageIcon(imageURL);
		imagePath = color+"/left2.png";			
		imageURL = cldr.getResource(imagePath);				
		left2 = new ImageIcon(imageURL);
		imagePath = color+"/left3.png";			
		imageURL = cldr.getResource(imagePath);				
		left3 = new ImageIcon(imageURL);
		imagePath = color+"/left4.png";			
		imageURL = cldr.getResource(imagePath);				
		left4 = new ImageIcon(imageURL);
		imagePath = color+"/right1.png";			
		imageURL = cldr.getResource(imagePath);				
		right1 = new ImageIcon(imageURL);
		imagePath = color+"/right2.png";			
		imageURL = cldr.getResource(imagePath);				
		right2 = new ImageIcon(imageURL);
		imagePath = color+"/right3.png";			
		imageURL = cldr.getResource(imagePath);				
		right3 = new ImageIcon(imageURL);
		imagePath = color+"/right4.png";			
		imageURL = cldr.getResource(imagePath);				
		right4 = new ImageIcon(imageURL);
	}
	public void draw(int fieldX, int fieldY, Component c,Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		if(moving)
			sprite = (int)((System.currentTimeMillis()/120)%4);
		else
			sprite = 0;
		if(direction.equals("stop")){
			image = front1;
		}
		else if(direction.equals("front")){
			switch(sprite){
				case 0:image = front1; break;
				case 1: image = front2; break;
				case 2: image = front3; break;
				case 3: image = front4; 
			}
		}
		else if(direction.equals("back")){
			switch(sprite){
				case 0:image = back1; break;
				case 1: image = back2; break;
				case 2: image = back3; break;
				case 3: image = back4; 
			}
		}
		else if(direction.equals("left")){
			switch(sprite){
				case 0:image = left1; break;
				case 1: image = left2; break;
				case 2: image = left3; break;
				case 3: image = left4; 
			}
		}
		else if(direction.equals("right")){
			switch(sprite){
				case 0:image = right1; break;
				case 1: image = right2; break;
				case 2: image = right3; break;
				case 3: image = right4; 
			}
		}
		g2.drawImage(image.getImage(),(int)x + fieldX ,(int)y+ fieldY,null);

	}
}
