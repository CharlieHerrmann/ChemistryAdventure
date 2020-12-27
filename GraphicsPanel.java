
// Written by: Charlie Herrmann
// Date: April 19th 2015
// Description: This project displays the game of connect 4. Players will take turns dropping peices into 
// one of 7 columns and the first to get 4 in a row wins. 

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.net.URL;
import java.util.*;
import javax.swing.Timer;
import javax.sound.midi.Synthesizer;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Polygon;
import java.awt.Shape;

public class GraphicsPanel extends JPanel implements KeyListener,MouseListener,MouseMotionListener{
	boolean[] keys = new boolean[4];
	Player p = new Player(500,500);
	Timer t = new Timer(15, new ClockListener(this));
	int screenX = 4;
	int screenY = 0;
	ClassLoader cldr = this.getClass().getClassLoader();
	String imagePath;
	URL imageURL;
	ImageIcon lab;
	ImageIcon chemist;
	ImageIcon glassShop;
	ImageIcon floor;
	boolean possOut = true;
	boolean pour = false;
	int hold = -1;
	String[] steps = new String[8];
	int step = 0;
	boolean calc = true;
	double baseUnused = 100;
	int labNum = 0;
	boolean flameOn = false;
	ArrayList<Item> items = new ArrayList<Item>();
	ArrayList<Integer> answer = new ArrayList<Integer>();
	ArrayList<Character> characters = new ArrayList<Character>();
	boolean revealAnswer = false;
	boolean talk = false;
	boolean moved = false; 
	String[] dialogue = null;
	ArrayList<String> inventory= new ArrayList<String>();
	int action = -1;
	int line = 0;
	boolean zeroCom = false;
	boolean oneCom = false;
	boolean twoCom = false;
	boolean threeCom = false;
	boolean hint = false;
	
	// method: GraphicsPanel Constructor
	// description: This 'method' runs when a new instance of this class in instantiated.  It sets default values  
	// that are necessary to run this project.  You do not need to edit this method.
	public GraphicsPanel(){
		setPreferredSize(new Dimension(1000, 700));
        this.setFocusable(true);			// for keylistener
		this.addKeyListener(this);
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		t.start();
		imagePath = "lab.png";	
		imageURL = cldr.getResource(imagePath);			
		lab = new ImageIcon(imageURL);
		imagePath = "chemist.png";
		imageURL = cldr.getResource(imagePath);			
		chemist = new ImageIcon(imageURL);
		imagePath = "glassshop.png";
		imageURL = cldr.getResource(imagePath);			
		glassShop = new ImageIcon(imageURL);
		setLab();
		setScreen();
	} 
	public void setLab(){
		calc = false;
		revealAnswer = false;
		hint = false;
		answer.clear();
		items.clear();
		step = 0;
		if(labNum == 0){
			steps[0] = "Fill flask with red acid (Sulfuric Acid) from beaker.";
			steps[1] = "Place burrette on ring stand.";
			steps[2] = "Fill burrette with blue base (Sodium Hydroxide) from beaker.";
			steps[3] = "Place flask on stand.";
			steps[4] = "Place two drops of indicator in flask.";
			steps[5] = "Press f to open and close burrette. Titrate the acid until it is pink for 20 seconds. Press space to continue when done.";
			items.add(new Item(3,400,"flask",'0'));
			items.add(new Item(678,500,"indicator"));
			items.add(new Item(272,475,"beaker",'a'));
			items.add(new Item(166,475,"beaker",'b'));
			items.add(new Item(573,130,"stand",'0'));
			items.add(new Item(737,563,"burette"));	
		}
		else if(labNum == 1){
			steps[0] = "Place Bunsen burner on stand.";
			steps[1] = "Place beaker on the stand.";
			steps[2] = "Put the 50g metal sample in the beaker.";
			steps[3] = "Turn on Bunsen Burner by pressing the f key.";
			steps[4] = "The solution has boiled. Put the metal in the beaker with the temerature probe. The beaker contains 100 ml of water. (Assume density is 1g/ml)";
			steps[5] = "";
			steps[6] = "Press space to continue.";
			items.add(new Item(161,580,"copper"));
			items.add(new Item(18,475,"beaker",'1'));
			items.add(new Item(864,475,"beaker",'T'));
			items.add(new Item(736,130,"stand",'1'));
			items.add(new Item(303,416,"flame"));
		}
		else if(labNum == 2){
			steps[0] = "Place beaker with temperature probe on scale";
			steps[1] = "Fill beaker with water from other beaker";
			steps[2] = "Drag ice from the ice bucket into the beaker";
			steps[3] = "Press space to continue when done.";
			items.add(new Item(161,528,"scale"));
			items.add(new Item(18,475,"beaker",'2'));
			items.add(new Item(718,475,"beaker",'b'));
			items.add(new Item(500,500,"ice box"));
			items.get(1).t = 0;
			items.get(2).t = 30;
		}
		else if(labNum == 3){
			steps[0] = "Place pipette on scale.";
			steps[1] = "Fill pipette with hydrate.";
			steps[2] = "Place filled pipette on the scale.";
			steps[3] = "Place filled pipette on the stand.";
			steps[4] = "Place Bunsen Burner on the stand.";
			steps[5] = "Turn on Bunsen Burner by pressing the f key.";
			steps[6] = "Wait until mass and color stop changing. Press space to continue.";
			
			items.add(new Item(363,475,"beaker",'3'));
			items.add(new Item(8,528,"scale"));
			items.add(new Item(259,590,"pipette"));
			items.add(new Item(709,130,"stand",'1'));
			items.add(new Item(831,416,"flame"));
		}
		
	}
	public void setScreen(){
		characters.clear();
		if(screenX == 0 && screenY == 2){
			characters.add(new Character(100,100,"player/back1.png",new String[]{"Everyone around here looks the same."}));
			if(inventory.contains("axe") && !inventory.contains("ice") )
				characters.add(new Character(0,500,"ice",new String[]{"This axe will work to break the ice.", "Ice has been added to your inventory"},200,200, 6));
			else if (inventory.contains("axe") && inventory.contains("ice"))
				characters.add(new Character(0,500,"ice",new String[]{"Thank god this ice pond was here."},200,200));
			else
				characters.add(new Character(0,500,"ice",new String[]{"If only I had something to break the ice."},200,200));
		}
		else if(screenX == 1 && screenY == 2){
			
		}
		else if(screenX == 2 && screenY == 2){
			
		}
		else if(screenX == 0 && screenY == 1){
			
		}
		else if(screenX == 1 && screenY == 1){
			
		}
		else if(screenX == 2 && screenY == 1){
			
		}
		else if(screenX == 0 && screenY == 0){
			
		}
		else if(screenX == 1 && screenY == 0){
			if(!inventory.contains("axe"))
				characters.add(new Character(500,50,"player/back1.png",new String[]{"Hey kid come here.","I just finished chopping down every tree in town.", "I guess I won't be needing this anymore.", "An axe has been added to your inventory."},5));
			else 
				characters.add(new Character(500,50,"player/back1.png",new String[]{"I'm not even a lumberjack. I just really don't like trees."}));
		}
		else if(screenX == 2 && screenY == 0){
			characters.add(new Character(900,50,"player/back1.png",new String[]{"I have always wanted to check out that abandoned mineshaft but I'm too scared to go in alone. ","Come with me we can check it out."},1));
		}
		else if (screenX == 6){
			if(twoCom && !inventory.contains("indicator"))
				characters.add(new Character(500,400,"player/back1.png",new String[]{"Did you find the heat of fusion of ice?","Oh yes, 80 calories per gram, of course.","Well a deal's a deal. I'm done with this indicactor anyway.", "Indicator has been added to your inventory."},11));
			else if(twoCom)
				characters.add(new Character(500,400,"player/back1.png",new String[]{"Thanks for your calculation!"}));
			else 
				characters.add(new Character(500,400,"player/back1.png",new String[]{"I'm a young chemist just like you kid.", "I've been trying to do some calculations but I forget the heat of fusion of ice.", "I'll tell you what, if you figure it out, I'll give you a reward."}));
		}
		else if(screenX == 7){
			if(inventory.contains("pipette")){
				characters.add(new Character(500,400,"player/back1.png",new String[]{"Maybe this place needs more furnishing."}));
			}
			else{
				characters.add(new Character(500,400,"player/back1.png",new String[]{"I remember when this place was packed.", "Nobody comes in here anymore", "Heck, this place is going under anyways, here's a free pipette.", "A pipette has been added to your inventory."},4));
			}
		}
		else if(screenX == 8){
			if(inventory.contains("metal")){
				characters.add(new Character(50,400,"player/back1.png",new String[]{"This place gives me the creeps. Lets go."},3));
			}
			else{
				characters.add(new Character(50,400,"player/back1.png",new String[]{"There's probably metal bits and pieces all over this place."},1));
				characters.add(new Character(600,420,"metal",new String[]{"Metal has been added to your inventory"},30,30,2));
			}
		}
		else if (screenX == 4){
			characters.add(new Character(500,200,"player/back1.png",new String[]{"Find the materials to complete each of the 4 labs!"}));
			
			if(!inventory.contains("indicator"))
				characters.add(new Character(50,50,"table",new String[]{"You need indicator to complete this lab."}, 75,200));
			else 
				characters.add(new Character(50,50,"table",new String[]{"Find the molarity of an unknown base."}, 75,200,7));
			if(!inventory.contains("metal"))
				characters.add(new Character(870,50,"table",new String[]{"You need metal to complete this lab."}, 75,200));
			else 
				characters.add(new Character(870,50,"table",new String[]{"Find the specific heat of the metal"}, 75,200,8));
			if(!inventory.contains("ice"))
				characters.add(new Character(50,450,"table",new String[]{"You need ice to complete this lab."}, 75,200));
			else 
				characters.add(new Character(50,450,"table",new String[]{"Find the heat of fusion of ice."}, 75,200,9));
			if(!inventory.contains("pipette"))
				characters.add(new Character(870,450,"table",new String[]{"You need a pipette to complete this lab."}, 75,200));
			else 
				characters.add(new Character(870,450,"table",new String[]{"Find the formula for the hydrate."}, 75,200,10));
		}
		
	}
	public void action(int action){
		if(action == 1){
			screenX = 8;
			p.x = 100;
			setScreen();
		}
		if(action == 2){
			inventory.add("metal");
			setScreen();
		}
		if(action == 3){
			screenX = 2;
			screenY = 0;
			p.x = 900;
			p.y = 130;
			setScreen();
		}
		if(action == 4){
			inventory.add("pipette");
			setScreen();
		}
		if(action == 5){
			inventory.add("axe");
			setScreen();
		}
		if(action == 6){
			inventory.add("ice");
			setScreen();
		}
		if(action == 7){
			screenX = 5;
			labNum = 0;
			setLab();
			setScreen();
		}
		if(action == 8){
			screenX = 5;
			labNum = 1;
			setLab();
			setScreen();
		}
		if(action == 9){
			screenX = 5;
			labNum = 2;
			setLab();
			setScreen();
		}
		if(action == 10){
			screenX = 5;
			labNum = 3;
			setLab();
			setScreen();
		}
		if(action == 11){
			inventory.add("indicator");
			setScreen();
		}
	}
	public void controls(){
		double store = p.speed;
		if(keys[0]){
			p.direction = "left";
			p.x-=p.speed;
	    }
	    else if(keys[2]){
	    	p.direction = "right";
	    	p.x+=p.speed;
	    }
	    else if(keys[1]){
			p.direction = "front";
			p.y-=p.speed;
	    }
	    else if(keys[3]){
	    	p.direction = "back";
	    	p.y+=p.speed;
	    }
		p.moving = (keys[0] || keys[1] || keys[2] || keys[3]);
			
	}
		
	
	// method:clock
			// description: This method is called by the clocklistener every 5 milliseconds.  You should update the coordinates
			//				of one of your characters in this method so that it moves as time changes.  After you update the
			//				coordinates you should repaint the panel.
	public void clock(){
		this.repaint();
	}
	
	// method: paintComponent
	// description: This method is called when the Panel is painted.  It contains code that draws shapes onto the panel.
	// parameters: Graphics g - this object is used to draw shapes onto the JPanel.
	// return: void
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		if(!moved)
			g2.drawString("hi",0,0);
		
		g2.setColor(Color.GREEN);
		g2.fillRect(0, 0, 1000,700);
		
		
		if(screenX == 0 && screenY == 2){
			g2.setColor(new Color(205,133,63));
			g2.fillRect(460, 330, 1000,70);
			g2.fillRect(460, 0, 70,400);
		}
		else if(screenX == 1 && screenY == 2){
			g2.setColor(new Color(205,133,63));
			g2.fillRect(0, 330, 1000,70);
			g2.fillRect(460, 0, 70,400);
			lab.paintIcon(this, g2, 400,450);	
			if(p.x>372 && p.x<598 && p.y>400 && p.y<582){
				int min = (int)Math.min(Math.min(Math.abs(372-p.x), Math.abs(598-p.x)), Math.min(Math.abs(p.y-400), Math.abs(582-p.y)));
				if(min == Math.abs(372-p.x)){
					p.x = 372;
				}else if(min == Math.abs(598-p.x)){
					p.x = 598;
				}else if(min == Math.abs(p.y-400)){
					p.y = 400;
				}else{
					if(p.x>470 && p.x<500){
						screenX = 4;
						
						setScreen();
					}
					p.y = 582;
				}	
			}
			
		}
		else if(screenX == 2 && screenY == 2){
			g2.setColor(new Color(205,133,63));
			g2.fillRect(0, 330, 530,70);
			g2.fillRect(460, 0, 70,400);
		}
		else if(screenX == 0 && screenY == 1){
			g2.setColor(new Color(205,133,63));
			g2.fillRect(460, 330, 1000,70);
			g2.fillRect(460, 0, 70,700);
			lab.paintIcon(this, g2, 100,100);	
			if(p.x>72 && p.x<298 && p.y>50 && p.y<232){
				int min = (int)Math.min(Math.min(Math.abs(72-p.x), Math.abs(298-p.x)), Math.min(Math.abs(p.y-50), Math.abs(232-p.y)));
				if(min == Math.abs(72-p.x)){
					p.x = 72;
				}else if(min == Math.abs(298-p.x)){
					p.x = 298;
				}else if(min == Math.abs(p.y-50)){
					p.y = 50;
				}else{
					if(p.x>170 && p.x<200){
						screenX = 6;
						setScreen();
						p.x = 500;
						p.y = 582;
					}
					else{
						p.y = 232;
					}
				}	
			}
		}
		else if(screenX == 1 && screenY == 1){
			g2.setColor(new Color(205,133,63));
			g2.fillRect(0, 330, 1000,70);
			g2.fillRect(460, 0, 70,700);
		}
		else if(screenX == 2 && screenY == 1){
			g2.setColor(new Color(205,133,63));
			g2.fillRect(0, 330, 530,70);
			g2.fillRect(460, 0, 70,700);
			glassShop.paintIcon(this, g2, 700,100);
			if(p.x>672 && p.x<898 && p.y>50 && p.y<232){
				int min = (int)Math.min(Math.min(Math.abs(672-p.x), Math.abs(898-p.x)), Math.min(Math.abs(p.y-50), Math.abs(232-p.y)));
				if(min == Math.abs(672-p.x)){
					p.x = 672;
				}else if(min == Math.abs(898-p.x)){
					p.x = 998;
				}else if(min == Math.abs(p.y-50)){
					p.y = 50;
				}else{
					if(p.x>770 && p.x<800){
						screenX = 7;
						setScreen();
						p.x = 500;
						p.y = 582;
					}
					else{
						p.y = 232;
					}
				}
			}
		}
		else if(screenX == 0 && screenY == 0){
			g2.setColor(new Color(205,133,63));
			g2.fillRect(460, 330, 540,70);
			g2.fillRect(460, 330, 70,400);
		}
		else if(screenX == 1 && screenY == 0){
			g2.setColor(new Color(205,133,63));
			g2.fillRect(0, 330, 1000,70);
			g2.fillRect(460, 330, 70,400);
		}
		else if(screenX == 2 && screenY == 0){
			g2.setColor(new Color(205,133,63));
			g2.fillRect(0, 330, 460,70);
			g2.fillRect(460, 330, 70,400);
			g2.setStroke(new BasicStroke(10));
			for(int i = 0; i<8; i++){
				if(i %3 == 0)
					g2.setColor(new Color(139,69,19));
				else if(i%3 == 1)
					g2.setColor(new Color(205,133,63));
				else 
					g2.setColor(new Color(160,82,45));
				g2.drawLine(900+15*i, 0,1000,100-15*i);
			}
			
			g2.setStroke(new BasicStroke(1));
		}
		else if(screenX ==4){
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, 1000,700);
			g2.setColor(Color.PINK);
			int tileSize = 10;
			for(int i = 0; i<1000; i+=tileSize){
				for(int j= 0; j<700; j+=tileSize){
					if((i/tileSize)%2!=(j/tileSize)%2)
						g2.fillRect(i, j,tileSize,tileSize);
				}
			}
			g2.setColor(Color.BLACK);
			g2.fillRect(461, 695, 70, 10);
			if(p.x>446 && p.x<516 && p.y>650){
				screenX = 1;
				screenY = 2;
				setScreen();
				p.y = 582;
			}
			g2.setColor(new Color(139,69,19));
			for(int i = 0; i<2; i++){
				for(int j = 0; j<2; j++){
					g2.fillRect(50+820*i, 50+j*400, 75, 200);	
					
				}
			}
			
		}
		else if (screenX == 6){
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, 1000,700);
			g2.setColor(Color.PINK);
			int tileSize = 10;
			for(int i = 0; i<1000; i+=tileSize){
				for(int j= 0; j<700; j+=tileSize){
					if((i/tileSize)%2!=(j/tileSize)%2)
						g2.fillRect(i, j,tileSize,tileSize);
				}
			}
			g2.setColor(Color.BLACK);
			g2.fillRect(461, 695, 70, 10);
			if(p.x>446 && p.x<516 && p.y>650){
				screenX = 0;
				screenY = 1;
				setScreen();
				p.y = 235;
				p.x = 180;
			}
		}
		else if (screenX == 7){
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, 1000,700);
			g2.setColor(Color.PINK);
			int tileSize = 10;
			for(int i = 0; i<1000; i+=tileSize){
				for(int j= 0; j<700; j+=tileSize){
					if((i/tileSize)%2!=(j/tileSize)%2)
						g2.fillRect(i, j,tileSize,tileSize);
				}
			}
			g2.setColor(Color.BLACK);
			g2.fillRect(461, 695, 70, 10);
			if(p.x>446 && p.x<516 && p.y>650){
				screenX = 2;
				screenY = 1;
				setScreen();
				p.y = 235;
				p.x = 780;
			}
		}
		else if (screenX == 8){
			p.y = 400;
			g2.setColor(new Color(160,82,45));
			g2.fillRect(0,0,1000,250);
			g2.fillRect(0,450,1000,250);
			g2.setColor(new Color(222,184,135));
			g2.fillRect(0,250,1000,200);
			
		}
		else if(screenX == 5 && !calc && labNum == 0){
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, 1000,700);
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("TimesRoman",Font.PLAIN,60));
			g2.drawString("Titration Lab", 300,50);
			g2.setFont(new Font("TimesRoman",Font.PLAIN,18));
			g2.drawString("", 700,100);
			for(int i = 0; i<steps[step].split(" ").length/10.0; i++){
				String a = "";
				for(int j = 0; j<10; j++){
					if(steps[step].split(" ").length>j+i*10)
						a += steps[step].split(" ")[j+i*10]+ " ";
				}
				g2.drawString(a, 20,100+50*i);
			}
			g2.setColor(new Color(139,69,19));
			g2.fillRect(0, 600, 1000,40);
			for(int i = 0; i<items.size(); i++)
				items.get(i).draw(this, g2);
			for(int i = 0; i<items.size(); i++){
				if(items.get(i).dropStartY!=-1 && step == 4)
					step = 5;
				for(int j = 0; j<items.size(); j++){
					if(items.get(i).type.equals("flask") && items.get(j).pour && items.get(i).onStand && items.get(j).type.equals("burette") && items.get(j).t>0 && step == 5){
						items.get(j).t-=0.1;
						items.get(i).t+=0.1;
						items.get(j).drops.add(new Drop(13+items.get(j).x,items.get(j).y+232));
						items.get(i).base+=(100/358.0);
						baseUnused = 100-items.get(i).base;
						items.get(i).pSeconds = 100;
					}
				}
			}
		}
		else if(screenX == 5 && labNum == 0){
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, 1000,700);
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
			g2.drawString("initial volume of base (Sodium Hydroxide): 0.0 ml", 20,20);
			g2.drawString("final volume of base: "+ String.format("%.2f",100-baseUnused) + " ml", 20,120);
			g2.drawString("volume of acid (Sulfuric Acid): 50.0 ml", 20,220);
			g2.drawString("molarity of acid: 0.500 M ", 20,320);
			g2.drawString("Molarity of base (M):",80,450);
			g2.drawRect(80,500,50,50);
			g2.fillOval(135,535,10,10);
			for(int i = 0; i<2; i++){
				g2.setColor(Color.black);
				g2.drawRect(50*i+150,500,50,50);
			}
			for(int i = 0; i<answer.size(); i++){
				int c = 0;
				if(i == 0)
					c = -20;
				g2.setFont(new Font("TimesRoman",Font.PLAIN,40));
				g2.drawString(answer.get(i)+"", 50*i+115+c,540);
			}
			if(!revealAnswer){
				g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
				g2.drawRect(500,500,200,100);
				g2.drawString("Answer",500,530);
			}
			else{
				g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
				g2.drawString("The answer is...",500,530);
				g2.setFont(new Font("TimesRoman",Font.PLAIN,30));
				g2.drawString("1.00M",500,580);
			}
			if(!hint){
				g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
				g2.drawRect(500,400,200,100);
				g2.drawString("Hint",500,430);
			}
			else{
				g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
				g2.drawString("(#H)MaVa = MbVb(#OH)",500,430);
				
			}
				
		}
		else if(screenX == 5 && labNum == 1 && !calc){
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, 1000,700);
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("TimesRoman",Font.PLAIN,60));
			g2.drawString("Specific Heat of Metal Lab", 200,50);
			g2.setFont(new Font("TimesRoman",Font.PLAIN,18));
			for(int i = 0; i<steps[step].split(" ").length/10.0; i++){
				String a = "";
				for(int j = 0; j<10; j++){
					if(steps[step].split(" ").length>j+i*10)
						a += steps[step].split(" ")[j+i*10]+ " ";
				}
				g2.drawString(a, 20,100+50*i);
			}
			g2.setColor(new Color(139,69,19));
			g2.fillRect(0, 600, 1000,40);
			for(int i = 0; i<items.size(); i++){
				items.get(i).draw(this, g2);
				if(step == 5){
					items.get(i).temp += 0.01;
					if(items.get(i).temp >= 34.6)
						step = 6;
				}
			}
		}
		else if(screenX == 5 && labNum == 1){
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, 1000,700);
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
			g2.drawString("Initial temperature of the water: 23.0 C", 20,20);
			g2.drawString("Final temperature of the water and copper: 34.6 C", 20,120);
			g2.drawString("Volume of the water: 100 ml (assume density is 1g/ml)", 20,220);
			g2.drawString("Mass of the copper: 50g ", 20,320);
			g2.drawString("Specific Heat of Metal (cal/gC):",80,450);
			g2.drawRect(80,500,50,50);
			g2.fillOval(135,535,10,10);
			for(int i = 0; i<2; i++){
				g2.setColor(Color.black);
				g2.drawRect(50*i+150,500,50,50);
			}
			for(int i = 0; i<answer.size(); i++){
				int c = 0;
				if(i == 0)
					c = -20;
				g2.setFont(new Font("TimesRoman",Font.PLAIN,40));
				g2.drawString(answer.get(i)+"", 50*i+115+c,540);
			}
			if(!revealAnswer){
				g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
				g2.drawRect(500,500,200,100);
				g2.drawString("Answer",500,530);
			}
			else{
				g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
				g2.drawString("The answer is...",500,530);
				g2.setFont(new Font("TimesRoman",Font.PLAIN,30));
				g2.drawString("0.35 cal/gC",500,580);
			}
			if(!hint){
				g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
				g2.drawRect(500,400,200,100);
				g2.drawString("Hint",500,430);
			}
			else{
				g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
				g2.drawString("MCΔT = MCΔT",500,430);
				
			}
		}
		else if(screenX == 5 && labNum == 2 && !calc){
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, 1000,700);
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("TimesRoman",Font.PLAIN,60));
			g2.drawString("Heat of Fusion of Ice", 300,50);
			g2.setFont(new Font("TimesRoman",Font.PLAIN,18));
			for(int i = 0; i<steps[step].split(" ").length/10.0; i++){
				String a = "";
				for(int j = 0; j<10; j++){
					if(steps[step].split(" ").length>j+i*10)
						a += steps[step].split(" ")[j+i*10]+ " ";
				}
				g2.drawString(a, 20,100+50*i);
			}
			g2.setColor(new Color(139,69,19));
			g2.fillRect(0, 600, 1000,40);
			for(int i = 0; i<items.size(); i++){
				items.get(i).draw(this, g2);
			}
			
		}
		else if(screenX == 5 && labNum == 2){
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, 1000,700);
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
			g2.drawString("Initial water temperature: 55.00", 20,20);
			for(int i = 0; i<items.size(); i++){
				if(items.get(i).type.equals("beaker") && items.get(i).liquid == '2'){
					g2.drawString("Final water temperature: " + String.format("%.2f",items.get(i).temp), 20,120);
					g2.drawString("Mass of beaker and the water: " + String.format("%.2f",(100 + (items.get(i).t-items.get(i).ti)*10.0/3)) + "g", 20,320);
					g2.drawString("Mass of beaker and the water and ice: " + String.format("%.2f",(100 + (items.get(i).t)*10.0/3)) + "g", 20,420);
				}
			}
			g2.drawString("Mass of beaker: 100g", 20,220);

			g2.drawRect(200,500,50,50);
			g2.fillOval(185,535,10,10);
			g2.drawString("Heat of fusion of ice (cal/g):",80,450);
			for(int i = 0; i<2; i++){
				g2.setColor(Color.black);
				g2.drawRect(50*i+80,500,50,50);
			}
			for(int i = 0; i<answer.size(); i++){
				int c = 0;
				if(i == 0 || i== 1)
					c = -20;
				g2.setFont(new Font("TimesRoman",Font.PLAIN,40));
				g2.drawString(answer.get(i)+"", 50*i+115+c,540);
			}
			
			if(!revealAnswer){
				g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
				g2.drawRect(500,500,200,100);
				g2.drawString("Answer",500,530);
			}
			else{
				g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
				g2.drawString("The answer is...",500,530);
				g2.setFont(new Font("TimesRoman",Font.PLAIN,30));
				g2.drawString("80.0 cal/g",500,580);
			}
			if(!hint){
				g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
				g2.drawRect(500,400,200,100);
				g2.drawString("Hint",500,430);
			}
			else{
				g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
				g2.drawString("MCΔT = MCΔT + MHfus",500,430);
			}
		}
		else if(screenX == 5 && labNum == 3 && !calc){
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, 1000,700);
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("TimesRoman",Font.PLAIN,60));
			g2.drawString("Hydrate Lab", 300,50);
			g2.setFont(new Font("TimesRoman",Font.PLAIN,18));
			for(int i = 0; i<steps[step].split(" ").length/10.0; i++){
				String a = "";
				for(int j = 0; j<10; j++){
					if(steps[step].split(" ").length>j+i*10)
						a += steps[step].split(" ")[j+i*10]+ " ";
				}
				g2.drawString(a, 20,100+50*i);
			}
			g2.setColor(new Color(139,69,19));
			g2.fillRect(0, 600, 1000,40);
			for(int i = 0; i<items.size(); i++){
				items.get(i).draw(this, g2);
				if(items.get(i).type.equals("pipette") && items.get(i).onStand && items.get(i).y == 270 && flameOn && items.get(i).t>=1){
					items.get(i).t-=0.1;
					System.out.println(items.get(i).t);
				}
			}
		}
		else if(screenX == 5 && labNum == 3){
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, 1000,700);
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
			g2.drawString("Mass of the pipette: 2.75g", 20,20);
			g2.drawString("Mass of the pipette and the hydrate (NiSo4 * ?H20) : 4.04g", 20,120);
			for(int i = 0; i<items.size(); i++){
				if(items.get(i).type.equals("pipette"))
					g2.drawString("Mass after heating: " + String.format("%.2f",(3.5 + (items.get(i).t)*.01)) + "g" , 20,220);
			}
			g2.drawString("Number of water molecules in formula:",80,450);
			g2.drawRect(80,500,50,50);
			for(int i = 0; i<answer.size(); i++){
				int c = 0;
				if(i == 0)
					c = -20;
				g2.setFont(new Font("TimesRoman",Font.PLAIN,40));
				g2.drawString(answer.get(i)+"", 50*i+115+c,540);
			}
			
			if(!revealAnswer){
				g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
				g2.drawRect(500,500,200,100);
				g2.drawString("Answer",500,530);
			}
			else{
				g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
				g2.drawString("The answer is...",500,530);
				g2.setFont(new Font("TimesRoman",Font.PLAIN,30));
				g2.drawString("6",500,580);
			}
			if(!hint){
				g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
				g2.drawRect(500,400,200,100);
				g2.drawString("Hint",500,430);
			}
			else{
				g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
				g2.drawString("Convert to moles.",500,430);
			}
			
		}

		if(screenX!=5)
			p.draw(0,0, this, g);
		for(int i = 0; i<characters.size(); i++){
			characters.get(i).draw(g2, this);
			if(p.x<characters.get(i).x + characters.get(i).width && p.x + p.image.getIconWidth()>characters.get(i).x && p.y<characters.get(i).y + characters.get(i).height && p.y + p.image.getIconHeight()>characters.get(i).y){
				double min = Math.min(Math.min(Math.abs(characters.get(i).x + characters.get(i).width-p.x), Math.abs(p.x + p.image.getIconWidth()-characters.get(i).x)), Math.min(Math.abs(characters.get(i).y + characters.get(i).height-p.y), Math.abs(p.y + p.image.getIconHeight()-characters.get(i).y)));
				if(min == Math.abs(characters.get(i).x + characters.get(i).width-p.x)){
					p.x = characters.get(i).x + characters.get(i).width;
				}
				else if(min == Math.abs(p.x + p.image.getIconWidth()-characters.get(i).x)){
					p.x =characters.get(i).x-p.image.getIconWidth();
				}
				else if(min == Math.abs(characters.get(i).y + characters.get(i).height-p.y)){
					p.y = characters.get(i).y + characters.get(i).height;
				}
				else if (min == Math.abs(p.y + p.image.getIconHeight()-characters.get(i).y)){
					p.y = characters.get(i).y-p.image.getIconHeight();
				}
				talk = true;
				dialogue = characters.get(i).dialogue;
				action = characters.get(i).action;
				line = 0;
			}
		}
		if(screenX == 4){
			if(zeroCom){
				g2.setColor(Color.green);
				g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
				g2.drawString("Finished",52,80);
			}
			if(oneCom){
				g2.setColor(Color.green);
				g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
				g2.drawString("Finished",52+820,80);
			}
			if(twoCom){
				g2.setColor(Color.green);
				g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
				g2.drawString("Finished",52,80+400);
			}
			if(threeCom){
				g2.setColor(Color.green);
				g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
				g2.drawString("Finished",52+820,80+400);
			}
		}
		if(talk){
			g2.setColor(Color.white);
			g2.fillRect(0, 550, 1000,150);
			g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
			g2.setColor(Color.BLACK);
			g2.drawString(dialogue[line], 30,600);
			g2.setFont(new Font("TimesRoman",Font.PLAIN,20));
			g2.drawString("Press Space to Continue", 30,660);
		}
		possOut = false;
		if(p.y>280 && p.y<380 && p.x<0 && screenX>0 && screenX<3){
			screenX--;
			setScreen();
			p.x = 960;
		}
		else if(p.y>280 && p.y<380 && p.x>960 && screenX<2 && screenX<3){
			screenX++;
			setScreen();
			p.x = 0;
		}
		else if(p.x>446 && p.x<516 && p.y<0 && screenY>0 && screenX<3){
			screenY--;
			setScreen();
			p.y = 650;
		}
		else if(p.x>446 && p.x<516 && p.y>650 && screenY<2 && screenX<3){
			screenY++;
			setScreen();
			p.y = 0;
		}
		else if(p.x<0){
			p.x = 0;
		}
		else if(p.x>960){
			p.x = 960;
		}
		else if(p.y>650){
			p.y = 650;
		}
		else if(p.y<0){
			p.y = 0;
		}
		controls();
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		moved = true;
		if(e.getKeyCode() == KeyEvent.VK_H){
			screenX = 4;
			setScreen();
			p.x = 500;
			p.y = 500;
		}
		
		if(screenX == 5){
			if(calc){
				if(e.getKeyCode() >= KeyEvent.VK_0 && e.getKeyCode() <=  KeyEvent.VK_9 && answer.size()<3 && !(labNum == 3 && answer.size()>0)){
					answer.add(e.getKeyCode()-48);
				}
				if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
					answer.clear();
				}
			}
			if(e.getKeyCode() == KeyEvent.VK_SPACE && ((step == 5 && labNum == 0) || (step == 6 && (labNum == 1 || labNum==3)) || (step == 3 && labNum == 2))){
				calc = true;
			}
			if(e.getKeyCode() == KeyEvent.VK_SPACE && calc && revealAnswer){
				screenX = 4;
				if(labNum == 0)
					zeroCom = true;
				else if (labNum == 1)
					oneCom = true;
				else if (labNum == 2)
					twoCom = true;
				else if (labNum == 3)
					threeCom = true;
				setScreen();
			}
			
			if(e.getKeyCode() == KeyEvent.VK_R){
				setLab();
			}
			if(e.getKeyCode() == KeyEvent.VK_F){
				if(labNum == 0){
					for(int i = 0; i<items.size(); i++){
						if(items.get(i).type.equals("burette")){
							items.get(i).pour = !items.get(i).pour;
							for(int j = 0; j<items.size(); j++){
								if(items.get(j).type.equals("flask") && !items.get(i).pour){
									items.get(j).pSeconds = (int)(20.5*(items.get(j).base/items.get(j).acid));
									items.get(j).startT = System.currentTimeMillis();
								}
							}
						}
					}
				}
				else if (labNum == 1){
					for(int i = 0; i<items.size(); i++){
						if(items.get(i).type.equals("flame") && step>2){
							items.get(i).fire = !items.get(i).fire;
							if(step == 3)
								step = 4;
						}
					}
				}
				else if (labNum == 3){
					for(int i = 0; i<items.size(); i++){
						if(items.get(i).type.equals("flame") && step>4){
							items.get(i).fire = !items.get(i).fire;
							flameOn = items.get(i).fire; 
							if(step == 5)
								step = 6;
						}
					}
				}
			}
		}
		else{
			if(e.getKeyCode() == KeyEvent.VK_SPACE && talk){
				System.out.println("yoyoyo");
				if(dialogue.length-1>line){
					line++;
				}
				else{
					action(action);
					talk = false;
					action = -1;
					line = 0;
				}
			}
			if(e.getKeyCode() == KeyEvent.VK_I && !talk){
				talk = true;
				if(inventory.size()>0)
					dialogue = new String[]{"Inventory: " + inventory};
				else 
					dialogue = new String[]{"There is nothing in your inventory."};
			}
		}
		if(e.getKeyCode()>36 && e.getKeyCode()<=40)
			keys[e.getKeyCode()-37] = true;
		this.repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()>36 && e.getKeyCode()<=40)
			keys[e.getKeyCode()-37] = false;
		this.repaint();
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		for(int i = 0; i<items.size(); i++){
			if((items.get(i).type.equals("indicator")||items.get(i).type.equals("ice box")) && e.getX()>items.get(i).x && e.getX()<items.get(i).x+items.get(i).width && e.getY()>items.get(i).y && e.getY()<items.get(i).y+40){
				if(hold == -1){
					hold = i;
					items.get(i).cap = true;
				}
			}
			
			else if(e.getX()>items.get(i).x && e.getX()<items.get(i).x+items.get(i).width && e.getY()>items.get(i).y && e.getY()<items.get(i).y+items.get(i).height){
				if(hold == -1){
					items.get(i).ground = false;
					hold = i;
				}
				else if(items.get(hold).type == "beaker" && items.get(i).type == "flask" && items.get(hold).liquid == 'a'){
					if(items.get(hold).t>0){
						items.get(hold).t-=0.3;
						items.get(i).t+=0.1;
						items.get(i).acid+=(50/167.0);
					}
					else if(step == 0){
						step = 1;
					}
				}
				else if(items.get(hold).type == "pipette" && step>0 && items.get(i).type == "beaker"){
					items.get(hold).t = 54;
					if(step == 1){
						step = 2;
					}
				}
				else if(labNum == 2 && items.get(i).onStand && items.get(i).ti == 0 && items.get(hold).type == "beaker" && items.get(i).type == "beaker" && items.get(hold).liquid == 'b' && items.get(i).liquid == '2'){
					if(items.get(hold).t>0){
						items.get(hold).t-=0.1;
						items.get(i).t+=0.1;
						if(step==1)
							step = 2;
					}
				}
				else if(items.get(hold).type == "indicator" && items.get(hold).cap && items.get(i).type == "flask" && items.get(hold).dropStartY == -1){
					if(e.getX()>items.get(i).x+30 && e.getX()<items.get(i).x+items.get(i).width-30 && e.getY()>items.get(i).y && e.getY()<items.get(i).y+20){
						items.get(hold).dropStartY = items.get(i).y;
						items.get(hold).dropY = items.get(i).y + 30;
						items.get(hold).dropX = items.get(i).x + items.get(i).width/2-5 ;
					}
				}
				
				
			}
			else if(e.getX()>items.get(i).x-80 && e.getX()<items.get(i).x+items.get(i).width && e.getY()>items.get(i).y && e.getY()<items.get(i).y+items.get(i).height && step == 2){
				if(hold!=-1 && items.get(hold).type == "beaker" && items.get(i).type == "burette" && items.get(hold).liquid == 'b'){
					if(items.get(hold).t>0 && items.get(i).t<40){
						items.get(hold).t-=0.14;
						items.get(i).t+=0.1;
					}
					else if(step == 2){
						step = 3;
					}
				}
			}
			if(hold == i){
				if(items.get(i).cap){
					items.get(i).capX = items.get(i).x- (e.getX()-items.get(i).width/2);
					items.get(i).capY = items.get(i).y - (e.getY()-items.get(i).height/2);
				}else{
					items.get(i).x = e.getX()-items.get(i).width/2;
					items.get(i).y = e.getY()-items.get(i).height/2;
					items.get(i).onStand = false;
				}
				
			}
			
		}
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		for(int i = 0; i<items.size(); i++){

		}
		this.repaint();
	
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getX()>500 && e.getX()<700 && e.getY()>400 && e.getY()< 500){
			hint = true;
		}
		if(e.getX()>500 && e.getX()<700 && e.getY()>500 && e.getY()< 600 && (answer.size() == 3 || (answer.size()==1 && labNum==3))){
			revealAnswer = true;
		}
		this.repaint();	
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		
		
	}
	@Override
	
	public void mouseReleased(MouseEvent e) {
		for(int i = 0; i<items.size(); i++){
			if(hold!=-1 && labNum == 1 && e.getX()>items.get(i).x-200 && e.getX()+50<items.get(i).x+items.get(i).width && e.getY()+200>items.get(i).y && e.getY()<items.get(i).y+items.get(i).height-200 && items.get(i).type.equals("stand") && items.get(hold).type.equals("beaker") && items.get(hold).liquid == '1'){
				items.get(hold).y = 160;
				items.get(hold).x = items.get(i).x-170;
				items.get(hold).onStand = true;
				hold = -1;
				if(step == 1)
					step = 2;
			}
			if(hold!=-1 && labNum == 3 && e.getX()>items.get(i).x-200 && e.getX()+50<items.get(i).x+items.get(i).width && e.getY()+200>items.get(i).y && e.getY()<items.get(i).y+items.get(i).height-200 && items.get(i).type.equals("stand") && items.get(hold).type.equals("pipette")){
				items.get(hold).y = 270;
				items.get(hold).x = items.get(i).x-150;
				items.get(hold).onStand = true;
				items.get(hold).ground = true;
				hold = -1;
				if(step == 3)
					step = 4;
			}
			else if(hold!=-1 && labNum == 2 && e.getX()>items.get(i).x && e.getX()<items.get(i).x+items.get(i).width && e.getY()>items.get(i).y-200 && e.getY()<items.get(i).y+items.get(i).height+100 && items.get(i).type.equals("scale") && items.get(hold).type.equals("beaker")){
				items.get(hold).y = 400;
				items.get(hold).x = items.get(i).x+50;
				items.get(hold).onStand = true;
				hold = -1;
				if(step == 0)
					step = 1;
			}
			else if(hold!=-1 && labNum == 3 && e.getX()>items.get(i).x && e.getX()<items.get(i).x+items.get(i).width && e.getY()>items.get(i).y-200 && e.getY()<items.get(i).y+items.get(i).height+100 && items.get(i).type.equals("scale") && items.get(hold).type.equals("pipette")){
				items.get(hold).y = 518;
				items.get(hold).x = items.get(i).x+70;
				items.get(hold).onStand = true;
				items.get(hold).ground = true;
				hold = -1;
				if(step == 0)
					step = 1;
				if(step == 2)
					step = 3;
			}
			else if(hold!=-1 && labNum == 1 && items.get(i).onStand && e.getX()>items.get(i).x && e.getX()<items.get(i).x+items.get(i).width && e.getY()>items.get(i).y && e.getY()<items.get(i).y+items.get(i).height && items.get(i).type.equals("beaker") && items.get(hold).type.equals("copper")){
				items.get(hold).y = 260;
				items.get(hold).x = items.get(i).x+20;
				hold = -1;
				if(step == 2)
					step = 3;
			}
			else if(hold!=-1 && labNum == 1 && e.getX()>items.get(i).x && e.getX()<items.get(i).x+items.get(i).width && e.getY()>items.get(i).y && e.getY()<items.get(i).y+items.get(i).height && items.get(i).type.equals("beaker") && items.get(i).liquid == 'T' && items.get(hold).type.equals("copper")){
				items.get(hold).y = 570;
				items.get(hold).x = items.get(i).x+20;
				hold = -1;
				if(step == 4)
					step = 5;
			}
			else if(hold!=-1 && e.getX()>items.get(i).x-100 && e.getX()<items.get(i).x+items.get(i).width && e.getY()>items.get(i).y && e.getY()<items.get(i).y+items.get(i).height && items.get(i).type.equals("stand") && items.get(hold).type.equals("burette")){
				items.get(hold).y = 100;
				items.get(hold).x = items.get(i).x-105;
				hold = -1;
				if(step==1){
					step = 2;
				}
			}
		}
		for(int i = 0; i<items.size(); i++){
			if(hold!=-1 && (items.get(i).type.equals("burette")||items.get(i).type.equals("pipette")) && e.getX()>items.get(i).x && e.getX()<items.get(i).x+items.get(i).width && e.getY()>items.get(i).y && e.getY()<items.get(i).y+items.get(i).height && !items.get(i).onStand){
				if(items.get(i).type.equals("burette"))
					items.get(i).y = 563;
				else
					items.get(i).y = 590;
				items.get(i).ground = true;
				hold = -1;
			}
		}
		for(int i = 0; i<items.size(); i++){
			items.get(i).cap = false;
			if(e.getX()>items.get(i).x && e.getX()<items.get(i).x+items.get(i).width && e.getY()>items.get(i).y && e.getY()<items.get(i).y+items.get(i).height && !(items.get(i).y == 90 && labNum == 1)){
				if(items.get(i).type.equals("flask")){
					items.get(i).y = 400;
					for(int j = 0; j<items.size(); j++){
						if(items.get(j).type.equals("stand") && items.get(j).x-items.get(i).x > 100 && items.get(j).x-items.get(i).x<300){
							items.get(i).onStand = items.get(j).x-items.get(i).x>153 && items.get(j).x-items.get(i).x<183;
							items.get(i).y = 380;
							if(items.get(i).onStand && step == 3)
								step = 4;
						}
					}
				}
				else if(hold!=-1 && items.get(i).t>0 &&  items.get(hold).type == "ice box" && items.get(i).t<45 && items.get(hold).cap && items.get(i).type == "beaker" && items.get(hold).dropStartY == -1){
					items.get(hold).dropStartY = 1;
					items.get(hold).size = 20;
					items.get(hold).dropY = items.get(i).y + 90;
					items.get(hold).dropX = items.get(i).x + items.get(i).width/2+5 ;
					items.get(i).t+=0.6;
					items.get(i).ti+=0.6;
					double mI = items.get(i).ti*100/30;
					double mW = items.get(i).t*100/30 - mI;
					items.get(i).temp = (55*mW - mI*80)/(mI + mW);
					if(step == 2)
						step = 3;
				}
				else if(items.get(i).type.equals("flame")){
					items.get(i).y = 416;
					items.get(i).onStand = false;
					for(int j = 0; j<items.size(); j++){
						if(items.get(j).type.equals("stand") && items.get(j).x-items.get(i).x > 100 && items.get(j).x-items.get(i).x<300){
							items.get(i).onStand = true;
							items.get(i).y = 396;
							if(step == 0 && labNum!=3)
								step = 1;
							else if(labNum == 3 && step == 4)
								step = 5;
						}
					}
				}
				else if(items.get(i).type.equals("beaker") && items.get(i).y!=160 && items.get(i).y!=400){
					items.get(i).y = 475;
					items.get(i).onStand = false;
				}
				else if(items.get(i).type.equals("indicator")){
					items.get(i).y = 500;
				}	
				else if(items.get(i).type.equals("stand")){
					items.get(i).y = 130;
				}
				else if(items.get(i).type.equals("copper")){
					items.get(i).y = 580;
				}
				else if(items.get(i).type.equals("scale")){
					items.get(i).y = 528;
				}
				else if(items.get(i).type.equals("ice box")){
					items.get(i).y = 500;
				}
				else if(items.get(i).type.equals("pipette") && !items.get(hold).onStand){
					//items.get(i).y = 590;
				}
				hold = -1;
			}
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}