
package drawingapplication;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.Paint;
import java.awt.Point;

public abstract class MyShape {
    
    protected Paint shapeColor; // color of the shape being drawn
    protected Stroke myStroke;
    protected Point point1; // first point that makes up the shape
    protected Point point2; // second point that makes up the shape
    
    
    public MyShape() {
        shapeColor = Color.BLACK;
        point1 = new Point(0,0);
        point2 = new Point(0,0);
        myStroke = new BasicStroke();
    }
    
    public MyShape( Paint color, Stroke newStroke, int x1, int y1, int x2, int y2 ) {
        shapeColor = color;
        myStroke = newStroke;
        point1 = new Point(x1,y1);
        point2 = new Point(x2,y2);
    }

    public Paint getShapeColor() {
        return shapeColor;
    } 

    public void setShapeColor(Color newShapeColor) {
        shapeColor = newShapeColor;
    }

    public Point getPoint1() {
        return point1;
    }

    public void setPoint1(Point point1) {
        this.point1 = point1;
    }


    public Point getPoint2() {
        return point2;
    }

    public void setPoint2(Point point2) {
        this.point2 = point2;
    }
    
    public abstract void draw( Graphics2D g );
    
}


abstract class MyBoundedShape extends MyShape {
    
    protected Boolean filled;
    protected int width; 
    protected int height; 
    protected int smallX; // the smaller x-value between point1 and point2
    protected int smallY; // the smaller y-value between point1 and point2
    
    public MyBoundedShape() {
        super(); // call to MyShape's (superclass) constructor
        filled = false; 
    }
    
    public MyBoundedShape(Paint color, Stroke newStroke, int x1, int y1, int x2, int y2, Boolean isFilled) {
        
        super(color, newStroke, x1, y1, x2, y2); // call to MyShape's (superclass) constructor
        filled = isFilled;
    }
}



class MyLine extends MyShape {
    
    public MyLine() {
        super();
    }
    
    public MyLine(Paint color, Stroke newStroke, int x1, int y1, int x2, int y2) {
        super(color, newStroke, x1, y1, x2, y2);
    }
    
    @Override
    public void draw(Graphics2D g) {
        g.setStroke(myStroke); // first set the stroke type
        g.setPaint(shapeColor); // and set the color
        // then draw the line connectng points 1 and 2
        g.drawLine(point1.x, point1.y, point2.x, point2.y);
    }
}





class MyRectangle extends MyBoundedShape {
    
    public MyRectangle() {
        super(); // call to MyBoundedShape's (super class) constructor
    }
    
    public MyRectangle(Paint color, Stroke newStroke, int x1, int y1, int x2, int y2, Boolean isFilled) {
        super(color, newStroke, x1, y1, x2, y2, isFilled);
    }
    
    @Override
    public void draw(Graphics2D g) {
        
        width = Math.abs(point1.x-point2.x);
        height = Math.abs(point1.y-point2.y);
        
        if( point1.x < point2.x )
            smallX = point1.x;
        else
            smallX = point2.x;
        
        if( point1.y < point2.y )
            smallY = point1.y;
        else
            smallY = point2.y;
        
        g.setStroke(myStroke); // set the stroke type
        g.setPaint(shapeColor); // and the color of the shape
        
        // then draw either a filled shape or empty shape depending on filled boolean
        if( !filled )
            g.drawRect(smallX, smallY, width, height);
        else
            g.fillRect(smallX, smallY, width, height);
    }
}





class MyOval extends MyBoundedShape {
    
    public MyOval() {
        super();
    }
    
    public MyOval(Paint color, Stroke newStroke, int x1, int y1, int x2, int y2, Boolean isFilled) {
        super(color, newStroke, x1, y1, x2, y2, isFilled);
    }
    
    @Override
    public void draw(Graphics2D g) {
        
        width = Math.abs(point1.x-point2.x);
        height = Math.abs(point1.y-point2.y);
        
        if( point1.x < point2.x )
            smallX = point1.x;
        else
            smallX = point2.x;
        
        if( point1.y < point2.y )
            smallY = point1.y;
        else
            smallY = point2.y;
        
        g.setStroke(myStroke);
        g.setPaint(shapeColor);
        
        if( !filled )
            g.drawOval(smallX, smallY, width, height);
        else
            g.fillOval(smallX, smallY, width, height);
        
    }
}
