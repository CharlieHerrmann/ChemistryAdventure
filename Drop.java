import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Drop {
	int startY;
	int y;
	int x;
	public Drop(int x, int y){
		this.x = x;
		this.y = y;
		startY = y;
	}
	public void draw(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,255,50));
		g2.fillOval(x, y, 5,10);
	}
	public void moveDown(){
		y+=5;
	}
}
