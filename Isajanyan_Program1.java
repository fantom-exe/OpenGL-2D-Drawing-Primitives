/***************************************************************
 * file: Isajanyan_Program1.java
 * author: Edward Isajanyan
 * class: CS 4450
 *
 * assignment: program 1
 * date: 2/11/2019
 *
 * purpose: Reads coordinates from a text file 'coordinates.txt'
 * and draws the primitives in a window.
 *
 ****************************************************************/

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.lwjgl.opengl.GL11.*;


public class Isajanyan_Program1 {
	// stores all primitives
	private ArrayList<float[]> primitives = new ArrayList<>();
	
	// start
	private void start() {
		try {
			// init primitives
			readCoordinates();
			
			// begin render
			createWindow();
			initGL();
			render();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// read coordinates from file
	private void readCoordinates() throws FileNotFoundException {
		Scanner  scanner = new Scanner(new File("coordinates.txt"));
		String   readLine;
		String[] tokens;
		float[]  primitive;
		
		while(scanner.hasNextLine()) {
			readLine = scanner.nextLine();
			tokens   = readLine.split("[ ,]");
			
			if(tokens[0].equals("l")) { // line
				// store vertices
				primitive = new float[4];
				
				primitive[0] = Float.parseFloat(tokens[0]);
				primitive[1] = Float.parseFloat(tokens[1]);
				primitive[2] = Float.parseFloat(tokens[2]);
				primitive[3] = Float.parseFloat(tokens[3]);
				
				primitives.add(primitive);
			}
			else if(tokens[0].equals("c")) { // circle
				do { // store transitions
					switch(tokens[0].charAt(0)) {
						case 'r':
							polygon.addRotation(Integer.parseInt(tokens[1]),
							                    Integer.parseInt(tokens[2]),
							                    Integer.parseInt(tokens[3]));
							break;
						case 's':
							polygon.addScaling(Float.parseFloat(tokens[1]),
							                   Float.parseFloat(tokens[2]),
							                   Float.parseFloat(tokens[3]),
							                   Float.parseFloat(tokens[4]));
							break;
						case 't':
							polygon.addTranslation(Integer.parseInt(tokens[1]),
							                       Integer.parseInt(tokens[2]));
							break;
					}
				} while(!scanner.hasNext("[P]") && scanner.hasNextLine());
				
			}
			else if(tokens[0].equals("e")) { // ellipse
				
			}
			
		} // while
		
		scanner.close();
	}
	
	// init GL
	private void initGL() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		glOrtho(-320, 320, -240, 240, 1, -1);
		
		glMatrixMode(GL_MODELVIEW);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
	}
	
	// render
	private void render() {
		// render loop
		while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			try {
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				glLoadIdentity( );
				
				// draw primitives
				
				
				Display.update();
				Display.sync(60);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		Display.destroy( );
	}
	
	// draw given polygon obj
	private void draw() {
		glColor3f(polygon.getColorAt(0), polygon.getColorAt(1), polygon.getColorAt(2));
		glPointSize(10);
		
		glBegin(GL_POLYGON);
		polygon.vertices.forEach(ints -> glVertex2f(ints[0], ints[1]));
		glEnd( );
	}
	
	// create window
	private void createWindow() throws Exception{
		Display.setFullscreen(false);
		
		Display.setDisplayMode(new DisplayMode(640, 480));
		Display.setTitle("Isajanyan_Program1");
		Display.create( );
	}
	
	private void storePrimitive(char type, int[] vertex,) {
	
	}
	
	// adds attributes to array and stores it into PrimitivesList
	private void storeLine(int x1, int y1, int x2, int y2) {
		glColor3f(1.0f, 0.0f, 0.0f);
		glPointSize(10);
		
		glBegin(GL_LINE_LOOP);
		glVertex2f(150, 200);
		glVertex2f(80, 145);
		glEnd( );
	}
	
	// circle
	private void drawCircle(int x, int y, int r) {
		glColor3f(0.0f, 0.0f, 1.0f);
		glPointSize(5);
		
		glBegin(GL_LINE_LOOP);
		glVertex2f(150, 200);
		glVertex2f(80, 145);
		glEnd( );
	}
	
	// ellipse
	private void drawEllipse(int x, int y, int rx, int ry) {
		glColor3f(0.0f, 1.0f, 0.0f);
		glPointSize(5);
		
		glBegin(GL_LINE_LOOP);
		glVertex2f(150, 200);
		glVertex2f(80, 145);
		glEnd( );
	}
	
	// main
	public static void main(String[] args) {
		Isajanyan_Program1 program1 = new Isajanyan_Program1();
		program1.start();
	}
}
