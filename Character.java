import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.net.URL;
import javax.swing.ImageIcon;

public class Character {
	int x;
	int y;
	int width;
	int height;
	ClassLoader cldr = this.getClass().getClassLoader();
	URL imageURL;
	ImageIcon image;
	String imagePath = "none";
	String[] dialogue;
	int action = -1;
	public Character(int x, int y, String imagePath, String[] dialogue,int action){
		this(x, y, imagePath, dialogue);
		this.action = action;
	}
	public Character(int x, int y, String imagePath, String[] dialogue,int width, int height, int action){
		this(x, y, imagePath, dialogue, width, height);
		this.action = action;
	}
	public Character(int x, int y, String imagePath, String[] dialogue){
		this.x = x;
		this.y = y;		
		this.imagePath = imagePath;
		imageURL = cldr.getResource(imagePath);				
		this.image = new ImageIcon(imageURL);
		this.dialogue = dialogue;
		this.height = image.getIconHeight();
		this.width = image.getIconWidth();
	}
	public Character(int x, int y, String imagePath, String[] dialogue, int width, int height){
		this.x = x;
		this.y = y;		
		this.height = height;
		this.width = width;
		this.dialogue = dialogue;
		this.imagePath = imagePath;
	}
	public void draw(Graphics g, Component c){
		Graphics2D g2 = (Graphics2D) g;
		if(imagePath.equals("ice")){
			g2.setColor(Color.white);
			g2.fillRect(x, y, width, height);
			g2.setColor(new Color(135,206,250,100));
			g2.fillRect(x, y, width, height);
		}
		else if(imagePath.equals("metal")){
			g2.setColor(new Color(128,128,128));
			g2.fillRect(x, y, width, height);
		}
		else if (imagePath.equals("table")){
			g2.setColor(new Color(139,69,19));
			g2.fillRect(x, y, width, height);
		}
		else if(!imagePath.equals("none"))
			image.paintIcon(c, g, x, y);
	}

}
