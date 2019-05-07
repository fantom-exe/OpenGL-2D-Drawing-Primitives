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
                primitives.forEach(this::drawPrimitive);
//                drawPrimitive(primitives.get(0)); //  debug
                
                Display.update();
                Display.sync(60);
            } catch(Exception e) {
                e.printStackTrace();
            }
            
//            break; // debug
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
                break;
        }
    }
    
    // begin draw line
    private void drawLine(float x0, float y0, float x1, float y1) {
        glColor3f(1.0f, 0.0f, 0.0f);
		
        float dx = x1 - x0,
              dy = y1 - y0;
        
        // tests
        if(Math.abs(dy) < Math.abs(dx)) {
            if(x0 > x1) {
                plotLineLow(x1, y1, x0, y0);
            }
            else {
                plotLineLow(x0, y0, x1, y1);
            }
        }
        else {
            if(y0 > y1) {
                plotLineHigh(x1, y1, x0, y0);
            }
            else {
                plotLineHigh(x0, y0, x1, y1);
            }
        }
        
    }
    
    private void plotLineLow(float x0, float y0, float x1, float y1) {
        float dx = x1 - x0,
              dy = y1 - y0,
              yi = 1;
        
        if(dy < 0) {
            yi = -1;
            dy = -dy;
        }
        
        float d = 2*dy - dx,
              x = x0,
              y = y0;
    
        // plot
        glBegin(GL_POINTS);
            while(x <= x1) {
                glVertex2f(x, y);
                
                if (d > 0) {
                    y = y + yi;
                    d -= 2 * dx;
                }
                
                d += 2 * dy;
                x += 1;
            }
        glEnd();
    }
    
    private void plotLineHigh(float x0, float y0, float x1, float y1) {
        float dx = x1 - x0,
              dy = y1 - y0,
              xi = 1;
        
        if(dx < 0) {
          xi = -1;
          dx = -dx;
        }
        
        float d = 2*dx - dy,
              x = x0,
              y = y0;
        
        // plot
        glBegin(GL_POINTS);
            while(y <= y1) {
                glVertex2f(x, y);
                
                if(d > 0) {
                    x = x + xi;
                    d -= 2 * dy;
                }
                
                d += 2 * dx;
                y += 1;
            }
        glEnd();
    }
    // end draw line
    
    
    // begin draw circle
    private void drawCircle(float x, float y, float r) {
        glColor3f(0.0f, 0.0f, 1.0f);
        
        // init values
        float plotX  = 0,
              plotY  = r,
              d      = 3 - (2 * r); // decision parameter
        
        // plot
        plotCircle(x, y, plotX, plotY);
    }
    
    private void plotCircle(float centerX, float centerY, float plotX, float plotY) {
        glBegin(GL_POINTS);
            glVertex2f(centerX+plotX, centerY+plotY);
            glVertex2f(centerX-plotX, centerY+plotY);
            glVertex2f(centerX+plotX, centerY-plotY);
            glVertex2f(centerX-plotX, centerY-plotY);
            glVertex2f(centerX+plotY, centerY+plotX);
            glVertex2f(centerX-plotY, centerY+plotX);
            glVertex2f(centerX+plotY, centerY-plotX);
            glVertex2f(centerX-plotY, centerY-plotX);
        glEnd();
    }
    // end draw circle
    
    
    // draw ellipses
    private void drawEllipse(float x, float y, float rx, float ry) {
        glColor3f(0.0f, 1.0f, 0.0f);
        
        
    }
    // end draw ellipse
    
    
    // main
    public static void main(String[] args) {
        Isajanyan_Program1 program1 = new Isajanyan_Program1();
        program1.start();
    }
    
}
