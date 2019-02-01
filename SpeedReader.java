import java.io.*;
import java.awt.*;

import java.awt.font.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import java.util.*;

/* Linh Nguyen, 2021
* COSC102   Lab 12
*
* Speed Reader 
*
* Credit: Nifty Assignments written by Peter-Michael Osera
* http://nifty.stanford.edu/2015/posera-speed-reader/
*
* Provided code
* @author Elodie Fourquet 
* @date Mar, 2015
* @updated Nov, 2018
* */

public class SpeedReader extends JComponent implements ActionListener {
	//default values
	public static final String DEFAULT_FILE_NAME = "feste.a"; 
	public static final int FRAME_WIDTH = 800;
	public static final int FRAME_HEIGHT = 200;
	public static final int FONT_SIZE = 32; 
	public static final int DEFAULT_WORDS_PER_MIN = 120; 
	
	//Seconds and ms per Minute
	//These will be useful in your WPM to delay calculation!
	public static final int SEC_PER_MIN = 60; 
	public static final int MILLIS_SEC_PER_MIN = SEC_PER_MIN * 1000;
	
	//instance variable 
	private String[] arr; 
	
	private int wordIndex = 0; 
	private Font font;  
	
	public SpeedReader(Scanner scan, int fontSize, int wpm) {
		
		super();
		
		int inBwWordDelay = delay(wpm);
		System.out.println("wpm " + wpm + " ms delay " + inBwWordDelay); 
		
		initListener();
		
		//*** You need to do something with scan here! ***
		this.arr = fileContent(scan); 
		
		//Instantiation of our timer
		//Look at the API
		//http://docs.oracle.com/javase/7/docs/api/javax/swing/Timer.html
		//'this' tells the timer to use the actionPerformed declared in SpeedReader
		Timer timer = new Timer(inBwWordDelay, this);  
		timer.setDelay(inBwWordDelay);
		timer.start();
		
		// Needs to be a Monospace font
		font = new Font("Courier", Font.BOLD, fontSize);
		
	}
	
	private String[] fileContent(Scanner scan) {
		ArrayList<String> list = new ArrayList<String>(); 
		while (scan.hasNext()) { 
			list.add(scan.next());
		} 
		
		String[] arr = new String[list.size()]; 
		for (int i = 0; i < arr.length; i++) {
			arr[i] = list.get(i); 
		}
		
		return arr; 
	}
	
	//*** You will need to implement this method ***
	//returns the millisecond value corresponding to words/minute
	//How do you conert WPM into a delay time between words?
	//ex: 120 WPM = a delay time of ???
	//Use your constants above!
	private static int delay(int wpm) {
		return MILLIS_SEC_PER_MIN/wpm; 
	}
	
	private void initListener() {
		setFocusable(true);
		requestFocus();
		
		addKeyListener(new KeyAdapter() {
				
				public void keyPressed(KeyEvent ae) { //Handles key presses
					
					if (ae.getKeyCode()==KeyEvent.VK_Q) //If user presses Q
						System.exit(1);
					else if (ae.getKeyCode()==KeyEvent.VK_SPACE) { //... or space
						wordIndex = (wordIndex+1) % arr.length;
						repaint();
					}
				}
		});
	}
	
	/**
	* Paints the elements on our window
	*/
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setColor(Color.YELLOW);    
		//repaint background to clear the window each time
		g2d.fillRect(0, 0, getWidth(), getHeight()); //Draws the background
		
		g2d.setFont(font);
		
		//The below code draws text to the string
		//Trace through and try to figure out what's going on here
		//The API and variable/method names should give you context
		//This will be important to coloring and positioning your Strings
		String word = arr[wordIndex];
		
		FontMetrics metrics = g2d.getFontMetrics();
		int charWidth = (metrics.stringWidth(word)) / word.length(); 
		
		int focusIdx = getFocusIdx(word.length()); 
		
		int oriX = (getWidth() / 2) - focusIdx*charWidth;
		int oriY = getHeight() / 2;
		
		g2d.setColor(Color.BLACK);                   
		g2d.drawString(word, oriX, oriY);
	
		//set the focus character 
		// always at the center of the screen 
		g2d.setColor(Color.RED);
		g2d.drawString(word.substring(focusIdx, focusIdx+1),
								getWidth() / 2, oriY);
		g2d.dispose();
	}
	
	private int getFocusIdx(int length) {
		//dependent on the length to choose the location of the focus character 
		int[] constantLocation = {1, 5, 9, 13}; 
		int idx = 0; 
		
		while (idx < constantLocation.length) { 
			if (length <= constantLocation[idx]) 
				return idx;
			idx++; 
		}
		return idx; 
	} 
	
	/**
	* Handles Timer events 
	* this method gets called automatically every time the timer "ticks"
	* See the instantiation of the timer / Java API to see the frequency at which
	* the timer "ticks"
	*/
	public void actionPerformed(ActionEvent ae) {
		wordIndex = (wordIndex+1) % arr.length;
		repaint();
	}
	
	
	private static void init(final File f, final int w, final int h, 
				final int font, final int wpm) throws FileNotFoundException {
		
		final Scanner scan = new Scanner(f);
		SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					createAndShowGUI(scan, w, h, font, wpm); 
				}
		});
	}
	
	
	private static void createAndShowGUI(Scanner scan, int width, int height, int fontSize, int wpm) {
		
		JFrame f = new JFrame("Speed Reader");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		System.out.println("width " + width + " heigth " + height);
		f.setSize(width, height);
        
		SpeedReader component = new SpeedReader(scan, fontSize, wpm);
		f.add(component);
		f.setVisible(true);
	}
    
    
	public static void main(String[] args) throws IOException {
		
		// **** Implement command line arguments! ****
		// The provided code only handles speed; the other values are hard-coded
		
		System.out.println("Usage: run SpeedReader <filename> <width> <height><font size> <wpm>");
		
		//declaration of necessary variables
		String file = DEFAULT_FILE_NAME; 
		int width = FRAME_WIDTH; int height = FRAME_HEIGHT; 
		int fontSize = FONT_SIZE; int wpm = DEFAULT_WORDS_PER_MIN;    
		
		// Your code should be able to take up to 5 command line arguments
		// (defaults should be use if fewer than 5 args are provided)
		// args[0] filename
		// args[1] width of the panel
		// args[2] height of the panel
		// args[3] font size
		// args[4] speed at which the text should be displayed (word per min)
		
		if (args.length > 0) 
			file = args[0];
		if (args.length > 1) 
			width = Integer.parseInt(args[1]); 
		if (args.length > 2) 
			height = Integer.parseInt(args[2]); 
		if (args.length > 3) 
			fontSize = Integer.parseInt(args[3]); 
		if (args.length > 4) 
			wpm = Integer.parseInt(args[4]); 
		
		init(new File(file), width, height, fontSize, wpm);
	}
	
}

