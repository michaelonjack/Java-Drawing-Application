
package drawingapplication;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;


// DrawPanel handles all interactions with the drawing pad using the mouse
class DrawPanel extends JPanel {
    
    private final MyShape[] shapes; // stores all the shapes the user draws
    private int shapeCount; // number of shapes the user draws
    private int shapeType; // determines the type of shape to draw
    private Stroke currentStroke; // current type of stroke (dashed, line width, etc.)
    private MyShape currentShape; // represents the current shape the user is drawing
    private Paint currentColor; // current drawing color
    private Boolean filledShape; // determines whether the shape is filled or not
    private final JLabel statusBar; // displays the current location of the mouse on the draw panel
    
    public DrawPanel() {
        
// initialize instance variable
        shapes = new MyShape[100];
        shapeCount = 0; // number of shapes on the pad
        shapeType = 0; // 0 is the index for a line shape
        filledShape = false;
        currentShape = null;
        currentStroke = new BasicStroke();
        currentColor = Color.BLACK;
        statusBar = new JLabel("Mouse outside of drawing pad.");
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(580,470));
        
        // Create and register listener for mouse and mouse motion on drawing pad
        // All mouse events will take place on the drawing pad
        MouseHandler mouseHandler = new MouseHandler();
        addMouseMotionListener(mouseHandler);
        addMouseListener(mouseHandler);
    } // end of DrawPanel constructor
    
    
    @Override
    // Called each time repaint() is called
    public void paintComponent(Graphics g) {
        
        // Downcast Graphics reference to Graphics2D
        Graphics2D g2D = (Graphics2D) g;
        
        super.paintComponent(g);
        
        // currentShape.draw() is needed because it is not added to the shapes container
        // until mouse is released so this is needed to see the shape WHILE drawing it
        if(currentShape != null) 
            currentShape.draw(g2D);
        
        // continue to draw the shapes that have already been created
        for( int i = 0; i < shapeCount; i++ ) {
            shapes[i].draw(g2D);
        }
    } // end of paintComponent
    
    public void setShapeType(int newShapeType) {
        shapeType = newShapeType;
    }

    public void setCurrentColor(Paint newCurrentColor) {
        currentColor = newCurrentColor;
    }

    public void setFilledShape(Boolean FilledShape) {
        this.filledShape = FilledShape;
    }
    
    public void setCurrentStroke(Stroke currentStroke) {
        this.currentStroke = currentStroke;
    }
    
    public JLabel getStatusBar() {
        return statusBar;
    }
    
    // Removes the last shape that was drawn
    public void clearLastShape() {
        // decrement shape count if it is not already zero
        if(shapeCount > 0)
            --shapeCount;
    }
    
    // Removes all shapes from the drawing pad
    public void clearDrawing () {
        shapeCount = 0;
    }
    
    
    
    
    
    private class MouseHandler extends MouseAdapter implements MouseMotionListener {
        // MouseMotionListener event handlers
        // handle event when user drags mouse with button pressed
        
        @Override
        public void mousePressed( MouseEvent event ) {
            
            // Creates a new shape depending on the shape type chosen by the combo box
            if( shapeType == 0 ) {
                currentShape = new MyLine(currentColor, currentStroke, event.getX(), 
                        event.getY(),event.getX(), event.getY());
            }
            else if( shapeType == 1 ) {
                currentShape = new MyOval(currentColor, currentStroke, event.getX(), 
                        event.getY(),event.getX(), event.getY(), filledShape);
            }
            else if( shapeType == 2 ) {
                currentShape = new MyRectangle(currentColor, currentStroke, event.getX(), 
                        event.getY(),event.getX(), event.getY(), filledShape);
            }
        } // end of mousePressed
        
        @Override
        public void mouseDragged( MouseEvent event ) {
            
            // Gives the position of the mouse on the drawing pad when being dragged
            statusBar.setText(String.format("Mouse position: [%d,%d]", 
                    event.getX(), event.getY() ));
            
            // This will allow user to see the shape while dragging
            currentShape.point2 = new Point( event.getX(),event.getY() );
            repaint();
       
        } // end of MouseDragged
        
        @Override
        public void mouseMoved( MouseEvent event ) {
            
            // Gives the position of the mouse on the drawng pad when moved
            statusBar.setText(String.format("Mouse position: [%d,%d]", 
                    event.getX(), event.getY() ));
        } // end of MouseMoved 
        
        @Override
        public void mouseExited( MouseEvent event ) {
            // When mouse leaves area of the draw pad, user is informed
            statusBar.setText("Mouse is outside of drawing pad.");
        } // end of mouseExited
        
        @Override
        public void mouseReleased( MouseEvent event ) {
            // When the user releases the mouse, a new shape has been drawn
            currentShape.point2 = new Point(event.getX(), event.getY());
            // add the new shape to the shapes array
            shapes[shapeCount] = currentShape;
            // increase the number of shapes that have been drawn
            shapeCount++;
            // reset current shape to null
            currentShape = null;
            repaint();
        }
        
    } // end of inner class MouseHandler
    
} // end of DrawPanel class
