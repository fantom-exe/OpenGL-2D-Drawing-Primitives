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

// remove
// debug
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
            
            if(tokens[0].charAt(0) == 'l') { // line
                primitive = new float[5];
                primitive[0] = (float)tokens[0].charAt(0);
                
                // store vertex 1
                primitive[1] = Float.parseFloat(tokens[1]);
                primitive[2] = Float.parseFloat(tokens[2]);
                
                // store vertex 2
                primitive[3] = Float.parseFloat(tokens[3]);
                primitive[4] = Float.parseFloat(tokens[4]);
                
                primitives.add(primitive);
            }
            else if(tokens[0].charAt(0) == 'c') { // circle
                primitive = new float[4];
                primitive[0] = (float)tokens[0].charAt(0);
                
                // store center
                primitive[1] = Float.parseFloat(tokens[1]);
                primitive[2] = Float.parseFloat(tokens[2]);
                
                // store radius
                primitive[3] = Float.parseFloat(tokens[3]);
                
                primitives.add(primitive);
            }
            else if(tokens[0].charAt(0) == 'e') { // ellipse
                primitive = new float[5];
                primitive[0] = (float)tokens[0].charAt(0);
                
                // store center
                primitive[1] = Float.parseFloat(tokens[1]);
                primitive[2] = Float.parseFloat(tokens[2]);
                
                // store rx
                primitive[3] = Float.parseFloat(tokens[3]);
                
                // store ry
                primitive[4] = Float.parseFloat(tokens[4]);
                
                primitives.add(primitive);
            }
            
        } // while
        
        scanner.close();
    }
    
    // create window
    private void createWindow() throws Exception{
        Display.setFullscreen(false);
        
        Display.setDisplayMode(new DisplayMode(640, 480));
        Display.setTitle("Isajanyan_Program1");
        Display.create( );
    }
    
    // init GL
    private void initGL() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 640, 0, 480, 1, -1);
        
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
//                primitives.forEach(this::drawPrimitive);

                drawPrimitive(primitives.get(2)); // line 1 debug
//                drawPrimitive(primitives.get(3)); // line 2 debug
                
                Display.update();
                Display.sync(60);
            } catch(Exception e) {
                e.printStackTrace();
            }
            
            break; // debug
        }
        
        Display.destroy( );
    }
    
    // draw given primitive
    private void drawPrimitive(float[] floats) {
        switch((char)floats[0]) {
            case 'l':
                drawLine(floats[1], floats[2], floats[3], floats[4]);
                break;
            case 'c':
                drawCircle(floats[1], floats[2], floats[3]);
                break;
            case 'e':
                drawEllipse(floats[1], floats[2], floats[3], floats[4]);
                break;
            default:
                System.out.println("wtf"); // debug
                break;
        }
    }
    
    // draw line
    private void drawLine(float x0, float y0, float x1, float y1) {
        glColor3f(1.0f, 0.0f, 0.0f);
		
		/*
		• dx - change in x
		• dy - change in y
		• d - the distance to the midpoint
		• incrementRight - how much to move right (E)
		• incrementUpRight - how much to move up and to the right (NE)
		• x - the current x value to plot
		• y - the current y value to plot
		 */
        float dx = x1 - x0,
              dy = y1 - y0,
              d  = 2*dy - dx,
//              incrementRight   = 2*dy,
//              incrementUpRight = 2*(dy - dx),
              x = x0,
              y = y0,
              i = 1,
              temp;
        // debug
        System.out.println("dx "+dx); // debug
        System.out.println("dy "+dy); // debug
        System.out.println("d "+d); // debug
//        System.out.println("incrementRight "+incrementRight); // debug
//        System.out.println("incrementUpRight "+incrementUpRight); // debug
        
        // tests
        if(Math.abs(dy) < Math.abs(dx)) { // plotLineLow (x1, y1, x0, y0)
            if(x0 < x1) { // reverse positions of x0 y0 with x1 y1
                temp = x0;
                x0 = x1;
                x1 = temp;
                
                temp = y0;
                y0 = y1;
                y1 = temp;
            }
            
            /*
                if dy < 0
                    i = -1
                    dy = -dy
                end if
                
                D = 2*dy - dx
                y = y0
                
                for x from x0 to x1 {
                    plot(x,y)
                    
                    if D > 0
                        y = y + i
                        D = D - 2*dx
                    end if
                    
                    D = D + 2*dy
                }
             */
            
            if(dy < 0 || dx < 0) {
                i = -1;
                dy = -dy;
            }
            
            d  = 2*dy - dx;
//            incrementRight   = 2*dy;
//            incrementUpRight = 2*(dy - dx);
            x = x0;
            y = y0;
            
        }
        else { // plotLineHigh (x1, y1, x0, y0)
            if(y0 < y1) { // reverse positions of x0 y0 with x1 y1
                temp = x0;
                x0 = x1;
                x1 = temp;
                
                temp = y0;
                y0 = y1;
                y1 = temp;
            }
            
            /*
                if dx < 0
                    i = -1
                    dx = -dx
                end if
                
                D = 2*dx - dy
                x = x0
                
                for y from y0 to y1 {
                    plot(x,y)
                    
                    if D > 0
                       x = x + i
                       D = D - 2*dy
                    end if
                    
                    D = D + 2*dx
                }
             */
            d  = 2*dy - dx;
//            incrementRight   = 2*dy;
//            incrementUpRight = 2*(dy - dx);
            x = x0;
            y = y0;
            
        }
        
        glBegin(GL_POINTS);
            System.out.println(x+" "+y); // debug
            glVertex2f(x, y);
            
            while(x < x1) {
                if(d > 0) {
                    d += incrementUpRight;
                    x += 1;
                    y += 1;
                }
                else {
                    d += incrementRight;
                    x += 1;
                }
                
                glVertex2f(x, y);
                System.out.println(x+" "+y); // debug
                System.out.println("d "+d); // debug
            }
            
            glVertex2f(x, y);
        glEnd( );
        
    }
    
    // draw circle
    private void drawCircle(float x, float y, float r) {
        glColor3f(0.0f, 0.0f, 1.0f);
        
        
    }
    
    // draw ellipses
    private void drawEllipse(float x, float y, float rx, float ry) {
        glColor3f(0.0f, 1.0f, 0.0f);
        
        
    }
    
    // main
    public static void main(String[] args) {
        Isajanyan_Program1 program1 = new Isajanyan_Program1();
        program1.start();
    }
}
