package gamesim;

import gamephysics.*;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Box {
    private ArrayList<LineSegment> walls;
    private Rectangle r;
    public double x;
    public double y;
    public int width;
    public int height;
    public int taken = 0;
    
    // Set outward to true if you want a box with outward pointed normals
    public Box(int x,int y,int width,int height,boolean outward)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        walls = new ArrayList<LineSegment>();
        if(outward) {
            walls.add(new LineSegment(new Point(x+width,y),new Point(x,y)));
            walls.add(new LineSegment(new Point(x+width,y+height),new Point(x+width,y)));
            walls.add(new LineSegment(new Point(x,y+height),new Point(x+width,y+height)));
            walls.add(new LineSegment(new Point(x,y),new Point(x,y+height)));
        } else {
            walls.add(new LineSegment(new Point(x,y),new Point(x+width,y)));
            walls.add(new LineSegment(new Point(x+width,y),new Point(x+width,y+height)));
            walls.add(new LineSegment(new Point(x+width,y+height),new Point(x,y+height)));
            walls.add(new LineSegment(new Point(x,y+height),new Point(x,y)));
        }
    }
    
    public void setPos(Point p)
    {
        setPos(p.x,p.y);
    }
    public void setPos(double x, double y)
    {
        this.x=x;
        this.y=y;
        walls.clear();
        walls.add(new LineSegment(new Point(x+width,y),new Point(x,y)));
        walls.add(new LineSegment(new Point(x+width,y+height),new Point(x+width,y)));
        walls.add(new LineSegment(new Point(x,y+height),new Point(x+width,y+height)));
        walls.add(new LineSegment(new Point(x,y),new Point(x,y+height)));
        
    }
    public Ray bounceRay(Ray in,double time)
    {
        // For each of the walls, check to see if the Ray intersects the wall
        Point intersection = null;
        for(int n = 0;n < walls.size();n++)
        {
            LineSegment seg = in.toSegment(time);
            intersection = walls.get(n).intersection(seg);
            if(intersection != null)
            {
                // If it intersects, find out when
                double t = in.getTime(intersection);
                // Reflect the Ray off the line segment
                Ray newRay = walls.get(n).reflect(seg,in.speed);
                // Figure out where we end up after the reflection.
                Point dest = newRay.endPoint(time-t);
                return new Ray(dest,newRay.v,in.speed);
            }
        }
        return null;
    }
    
    public void move(double deltaX,double deltaY)
    {
        for(int n = 0;n < walls.size();n++)
            walls.get(n).move(deltaX,deltaY);
        x += deltaX;
        y += deltaY;
    }
    
    public boolean contains(Point p)
    {
        if(p.x >= x && p.x <= x + width && p.y >= y && p.y <= y + height)
            return true;
        return false;
    }
    
    public Shape getShape()
    {
        r = new Rectangle(x, y, width, height);
        r.setFill(Color.WHITE);
        r.setStroke(Color.BLACK);
        return r;
    }
    public Shape getShapeGray()
    {
        r = new Rectangle(x, y, width, height);
        r.setFill(Color.GRAY);
        r.setStroke(Color.BLACK);
        return r;
    }
    public Shape getShapeBlue()
    {
        r = new Rectangle(x, y, width, height);
        r.setFill(Color.BLUE);
        r.setStroke(Color.BLACK);
        return r;
    }
    
    public Point getPos()
    {
        Point p = new Point(x,y);
        return p;
    }
    public int getTaken()
    {
        return taken;
    }
    public void assignPlayer()
    {
        taken = 1;
    }
    
    public void removePlayer()
    {
        taken = 0;
    }
    
    public void updateShape()
    {
        r.setX(x);
        r.setY(y);
    }
}
