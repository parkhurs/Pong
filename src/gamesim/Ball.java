package gamesim;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import gamephysics.*;

public class Ball {
    private Ray r;
    private Circle c;
    private int startingX;
    private int startingY;
    private int startVelX;
    private int startVelY;
    
    public Ball(int startX,int startY,int dX,int dY)
    {
        Vector v = new Vector(dX,dY);
        startingX = startX;
        startingY = startY;
        startVelX = dX;
        startVelY = dY;
        double speed = v.length();
        r = new Ray(new Point(startX,startY),v,speed);
    }
    
    public void resetPos()
    {
        Vector v = new Vector(startVelX, startVelY);
        double speed = v.length();
        r = new Ray(new Point(startingX,startingY),v,speed);
    }
    
    public void endBall()
    {
        Vector v = new Vector(0,0);
        double speed = v.length();
        r = new Ray(new Point(startingX,startingY),v,speed);
    }
    
    public Ray getRay()
    {
        return r;
    }
    
    public void setRay(Ray r)
    {
        this.r = r;
    }
    
    public void move(double time)
    {
        r = new Ray(r.endPoint(time),r.v,r.speed);
    }
    
    public Shape getShape()
    {
        c = new Circle(r.origin.x,r.origin.y,4);
        c.setFill(Color.RED);
        return c;
    }
    
    public void setPos(Point p)
    {
        r.origin = p;
    }
    
    public Point getBall()
    {
        Point p = new Point(r.origin.x, r.origin.y);
        return p;
    }
    
    public void updateShape()
    {
        c.setCenterX(r.origin.x);
        c.setCenterY(r.origin.y);
    }
}
