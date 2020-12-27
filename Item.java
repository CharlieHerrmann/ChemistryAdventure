import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Item {
	ImageIcon image;
	ClassLoader cldr = this.getClass().getClassLoader();
	int x; 
	int y;
	int width; 
	int height;
	double t;
	double ti;
	boolean held = false;
	boolean cap = false;
	int dropX = 0;
	int dropY = 0;
	int dropStartY = -1;
	int capX = 0;
	int capY = 0;
	boolean ground = true;
	boolean onStand = false;
	boolean pour = false;
	char liquid;
	String type;
	double base = 0;
	double acid = 0;
	double pSeconds = 0;
	long startT = 0;
	boolean fire = false;
	double temp = 23.0;
	double size = 0;
	ArrayList<Drop> drops = new ArrayList<Drop>();
	public void fire(Graphics g, int x, int y, double scaled){
		Graphics2D g2 = (Graphics2D) g;
		int xPoly[] = {40+x,30+x,40+x,33+x,40+x,37+x, 45+x,51+x,59+x,67+x,74+x, 83+x,76+x, 86+x,80+x};
		int yPoly[] = {94+y,75+y,81+y,47+y,60+y,31+y,42+y,14+y,40+y,28+y,66+y,61+y,83+y,76+y,91+y};
		AffineTransform tx = new AffineTransform();
		Polygon p = new Polygon(xPoly, yPoly, xPoly.length);
		tx.scale(scaled, scaled);
		Shape newShape = tx.createTransformedShape(p);
		g2.fill(newShape);
	}
	public Item(int x, int y,String type,char liquid){
		this(x,y,type);
		this.liquid = liquid;
		if(liquid == '2')
			temp = 55;
	}
	public Item(int x, int y,String type){
		this.x = x;
		this.y = y;
		this.type = type;
		if(type.equals("flask")){
			width = 165;
			height = 200;
			t = 0;
		}
		else if(type.equals("beaker")){
			width = 100;
			height = 135;
			t = 50;
		}
		else if(type.equals("indicator")){
			width = 55;
			height = 100;
		}
		else if(type.equals("burette")){
			width = 30;
			height = 265;
		}
		else if(type.equals("stand")){
			width = 30;
			height = 430;
		}
		else if(type.equals("copper")){
			width = 60;
			height = 18;
		}
		else if(type.equals("flame")){
			width = 150;
			height = 183;
		}
		else if(type.equals("scale")){
			width = 200;
			height = 70;
		}
		else if(type.equals("ice box")){
			width = 100;
			height = 100;
		}
		else if(type.equals("pipette")){
			width = 55;
			height = 10;
		}
	}
	
	public void draw(Component c,Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(3));
		if(type.equals("flask")){
			g2.setStroke(new BasicStroke(1));
			int shift = 50;
			int xPoly[] = {102+x-152+shift,268+x-152+shift,(int)(268+x-152-t*1.1)+shift,(int)(102+x-152+t)+shift};
	        int yPoly[] = {271+y-100,271+y-100,(int)(271+y-100-2.3589*t),(int)(271+y-100-2.3589*t)};
	        System.out.println(base + " " + acid);
	        if(base>acid*1.05){
	        	g2.setColor(new Color(199,21,133,100));
	        }
	        else if(pSeconds*1000 + startT> System.currentTimeMillis() && liquid == '0' && !pour){
	        	g2.setColor(Color.BLACK);
	        	g2.setFont(new Font("TimesRoman",Font.PLAIN,30));
	        	g2.drawString("Timer: " + (System.currentTimeMillis()-startT)/1000.0+"", 800,30);
	        	g2.setColor(new Color(255,100,203,100));
	        }
	        else if(liquid == '0'){
	        	g2.setColor(Color.BLACK);
	        	g2.setFont(new Font("TimesRoman",Font.PLAIN,30));
	        	g2.drawString("Timer: 0.00", 800,30);
	        	g2.setColor(new Color(255,192,203,20));
	        }
	        if(t>0){
		        g2.fillPolygon(new Polygon(xPoly, yPoly, xPoly.length));
		        g2.fillArc(127-177+x+shift,271-121+y,50,50, 180,90);
		        g2.fillArc(242-177+x+shift,271-121+y,50,50, 270,90);
		        g2.fillRect(152-177+x+shift,296-121+y,116,24);
		        g2.fillRect(128-177+x+shift,292-121+y,162,5);
	        }
	        
			g2.setColor(Color.black);
			
			g2.setStroke(new BasicStroke(3));
			g2.drawArc(0+x-50+shift,150+y,50,50, 145,130);
			g2.drawLine(27+x-50+shift,200+y,138+x-50+shift,200+y);
			g2.drawArc(115+x-50+shift,150+y,50,50, -105,140);
			g2.drawLine(4+x-50+shift,161+y,57+x-50+shift,39+y);
			g2.drawLine(161+x-50+shift,161+y,102+x-50+shift,39+y);
			g2.drawLine(102+x-50+shift,9+y,102+x-50+shift,39+y);
			g2.drawLine(57+x-50+shift,9+y,57+x-50+shift,39+y);
			g2.drawOval(57+x-50+shift,0+y,45,20);
		}
		else if(type.equals("beaker")){
			if(liquid == '2' && onStand){
				g2.setFont(new Font("TimesRoman",Font.PLAIN,30));
				g2.drawString(String.format("%.2f",(100 + t*10.0/3))+ "g",x+10,y+170);
			}
			
			if(liquid == 'T' || liquid == '2'){
				double mI = ti*100/30;
				double mW = t*100/30 - mI;
			}
			if(t>0){
				if(liquid == 'b' || liquid == '1' || liquid == 'T' || liquid == '2')
					g2.setColor(new Color(0,0,255,50));
				else if(liquid == 'a')
					g2.setColor(new Color(255,192,203,40));
				else if (liquid == '3')
					g2.setColor(Color.green);
		    	g2.fillRect(x, (int)(y+116-t*2), 100, (int)t*2);
		    }
			g2.setColor(Color.black);
			g2.drawOval(x,y+6,100,17);
			g2.drawLine(x,13+y,x,116+y);
			g2.drawLine(100+x,13+y,100+x,116+y);
		    g2.drawOval(x,107+y,100,17);
		    if(t>0){
		    	if(liquid == 'b' || liquid == '1' || liquid == 'T' || liquid == '2')
					g2.setColor(new Color(0,0,255,50));
		    	else if(liquid == 'a')
					g2.setColor(new Color(255,192,203,40));
				else if (liquid == '3')
					g2.setColor(Color.green);
		    	g2.fillOval(x,107+y,100,17);
		    }
		    if(liquid == 'T' || liquid == '2'){
		    	g2.setColor(Color.black);
		    	g2.drawLine(x+10,y+100,x+10,y-75);
		    	g2.drawRect(x-15,y-125,50,50);
		    	g2.setFont(new Font("TimesRoman",Font.PLAIN,25));
		    	if(liquid == 'T')
		    		g2.drawString(String.format("%.1f", Math.min(temp,34.6)), x-12,y-85);
		    	else if(liquid == '2' && t>0){
		    		g2.setFont(new Font("TimesRoman",Font.PLAIN,21));
		    		g2.drawString(String.format("%.2f", temp), x-15,y-85);
		    	}
		    	
		    }
		}
		else if(type.equals("indicator")){
			g2.setColor(Color.black);
			//g2.drawLine(251, 209, 197,209);
			g2.drawArc(198+x-197,200+y-178,51,20,180,180);
			g2.drawLine(197+x-197, 209+y-178, 197+x-197, 266+y-178);
			g2.drawLine(250+x-197, 209+y-178, 250+x-197, 266+y-178);
			if(!cap){
				g2.drawLine(x,209+y-178,222+x-197,190+y-178);
				g2.drawLine(222+x-197,190+y-178,251+x-197,209+y-178);
				g2.setColor(Color.white);
				g2.fillArc(212+x-197, 178+y-178, 22, 40, 0, 180);
				g2.setColor(Color.black);
				g2.drawArc(212+x-197, 178+y-178, 22, 40, 0, 180);
			}
			g2.drawOval(197+x-197, 260+y-178, 54, 17);
			if(cap){
				g2.drawArc(198+x-197,200+y-178,51,20,180,360);
				g2.drawLine(x-capX,209+y-178-capY,222+x-197-capX,190+y-178-capY);
				g2.drawLine(222+x-197-capX,190+y-178-capY,251+x-197-capX,209+y-178-capY);
				g2.drawArc(198+x-197-capX,200+y-178-capY,51,20,180,180);
				g2.setColor(Color.white);
				g2.fillArc(212+x-197-capX, 178+y-178-capY, 22, 40, 0, 180);
				g2.setColor(Color.black);
				g2.drawArc(212+x-197-capX, 178+y-178-capY, 22, 40, 0, 180);
				g2.drawArc(x-capX+21, 31+y-capY,10,50,150,245);
			}
			if(dropStartY!=-1){
				g2.drawOval(dropX,dropY,10,10);
				if(dropY-dropStartY<190){
					dropY+=2;
				}
				else{
					dropStartY = -1;
				}
			}
			
		}
		else if(type.equals("ice box")){
			g2.setFont(new Font("TimesRoman",Font.PLAIN,25));
			g2.setColor(Color.black);
			g2.drawRect(x, y, 100, 100);
			g2.drawLine(x, 30+y, width+x, 30+y);
			g2.drawString("Ice Box",x+12,y+70);
			g2.setFont(new Font("TimesRoman",Font.PLAIN,15));
			g2.drawString("Drag Ice Here",x+7,y+20);
			if(!cap){
				
			}
			else if(cap){
				g2.drawRect(x-capX+40, y-capY+40, 20,20);
			}
			if(dropStartY!=-1){
				g2.setColor(Color.black);
				g2.drawRect((int)(dropX-size/2.0),(int)(dropY-size/2.0),(int)size,(int)size);
				if(size>1){
					size-=0.1;
				}
				else{
					dropStartY = -1;
				}
			}
			
		}
		else if(type.equals("burette")){
			if(ground){
				width = 265;
				height = 40;
				g2.drawOval(80-80+x,72-57+y,6,18);
				g2.drawLine(83-80+x,90-57+y,305-80+x,90-57+y);
				g2.drawLine(83-80+x,71-57+y,305-80+x,71-57+y);
				g2.drawRect(302-80+x,62-57+y,18,4);
				g2.drawRect(307-80+x,57-57+y,7,40);
				g2.drawLine(345-80+x,82-57+y,315-80+x,89-57+y);
				g2.drawLine(345-80+x,77-57+y,315-80+x,71-57+y);
			}
			else{
				width = 30;
				height = 265;
				g2.setColor(new Color(0,0,255,50));
			    g2.fillRect(6+x,(int)(y+225-5*t),18,(int)t*5);
				g2.setColor(Color.black);
				g2.drawOval(6+x,0+y,18,6);
				g2.drawLine(6+x,4+y,6+x,224+y);
				g2.drawLine(183-159+x,4+y,183-159+x,224+y);
				if(!pour)
					g2.drawRect(190-159+x,219+y,4,18);
				g2.drawRect(0+x,225+y,40,7);
			    g2.drawLine(177-159+x,262+y,183-159+x,232+y);
			    g2.drawLine(173-159+x,262+y,166-159+x,232+y);
			}
			for(int i = drops.size()-1; i>=0; i--){
				drops.get(i).draw(g2);
				drops.get(i).moveDown();
				if(drops.get(i).y>drops.get(i).startY+230)
					drops.remove(i);
			}
		}
		else if(type.equals("pipette")){
			g2.setColor(Color.BLACK);
			if(ground){
				width = 55;
				height = 10;
				g2.drawLine(10+x, 0+y, 55+x,0+y);
				g2.drawLine(10+x,0+y,0+x,5+y);
				g2.drawLine(10+x,10+y,0+x,5+y);
				g2.drawLine(10+x,10+y, 55+x,10+y);
				g2.drawOval(50+x, 0+y, 10,10);
				if(t>0){
					g2.setColor(new Color(128,128,128));
					g2.fillRect(x+8, y+2, 42, 7);
					g2.fillPolygon(new int[]{x+8,x+3,x+8}, new int[]{y+2,y+6,y+9}, 3);
					g2.setColor(new Color(0,255,0,(int)(t*255/54)));
					g2.fillRect(x+8, y+2, 42, 7);
					g2.fillPolygon(new int[]{x+8,x+3,x+8}, new int[]{y+2,y+6,y+9}, 3);
				}
			}
			else{
				width = 10;
				height = 55;
				g2.drawLine(0+x,5+y,0+x,50+y);
				g2.drawLine(10+x,5+y,10+x,50+y);
				g2.drawLine(10+x,50+y,5+x,55+y);
				g2.drawLine(0+x,50+y,5+x,55+y);
				g2.drawOval(x, y, 10,10);
				if(t>0){
					g2.setColor(new Color(128,128,128));
					g2.fillRect(x+2, y+9, 7, 41);
					g2.fillPolygon(new int[]{x+2, x+6, x+9}, new int[]{y+50,y+55,y+50}, 3);
					g2.setColor(new Color(0,255,0,(int)(t*255/54)));
					g2.fillRect(x+2, y+9, 7, 41);
					g2.fillPolygon(new int[]{x+2, x+6, x+9}, new int[]{y+50,y+55,y+50}, 3);
				}
			}
			if(onStand && y == 518){
				double weight = 2.75;
				if(t>0){
					weight += 1.25 - (0.5-(t)*.01);
				}
				g2.setColor(Color.black);
				g2.setFont(new Font("TimesRoman",Font.PLAIN,30));
				g2.drawString(String.format("%.2f",weight)+"g", x+10,y+60);
			}
			
		}
		else if(type.equals("stand")){
			g2.setColor(Color.black);
			int height = 150;
			g2.drawLine(10+x,0+y,10+x,300+y+height);
			g2.drawLine(20+x,0+y,20+x,300+y+height);
			g2.drawOval(10+x,y-5,10,5);
			if(liquid == '0'){
				g2.drawLine(-80+x,150+y,x+10,150+y);
				g2.drawRect(-100+x,140+y,20,20);
				g2.drawRect(-100+x,60+y,20,20);
				g2.drawLine(-80+x,70+y,x+10,70+y);
			}
			else if(liquid == '1'){
				g2.drawLine(-40+x,150+y,x+10,150+y);
				g2.drawOval(-190+x,135+y, 150,30);
			}
			
			g2.drawRect(-200+x,300+y+height,300,20);	
		}
		else if(type.equals("copper")){
			g2.drawOval(x,y,6,18);
			g2.drawLine(3+x,y,60+x,y);
			g2.drawLine(3+x,18+y,60+x,18+y);
			g2.drawOval(58+x,y,6,18);
		}
		else if(type.equals("flame")){
			g2.drawLine(0+x,183+y,150+x,183+y);
			g2.drawLine(0+x,183+y,60+x,158+y);
			g2.drawLine(90+x,158+y,150+x,183+y);
			g2.drawRect(65+x,0+y,20,123);
			g2.drawRect(60+x,119+y,30,40);
			if(fire){
				g2.setColor(new Color(232, 161, 9));
				fire(g2,(int)(x*(1/1.4)-5),(int)((y*(1/1.4)-95)),1.4);
				g2.setColor(new Color(222, 196, 0));
				fire(g2,(int)(x*(1/0.9)+25),(int)(y*(1/0.9)-95),0.9);
				g2.setColor(new Color(250, 246, 118));
				fire(g2,(int)(x*2+95),(y*2-100),0.5);
			}
		}
		else if(type.equals("scale")){
			g2.setColor(Color.black);
			g2.drawRect(x,y,200,10);
			g2.drawRect(x+20,y+10,160,60);
		}

        
		//image.paintIcon(c, g, this.x, this.y);
	}
}
