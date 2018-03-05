package gamesim;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javafx.scene.shape.Shape;
import gamephysics.*;

public class Simulation {
    private Box outermost;
    private Box outer;
    private Ball ball;
    private Box player1;
    private Box player2;
    private Lock lock;
    private Ray oldLoc;
    
    public Simulation(int width,int height,int dX,int dY)
    {
        outermost = new Box(0,0,width,height,false);
//        outer = new Box(1, 1, width-1, height-1, false);
        ball = new Ball(width/2,height/2,dX,dY);
        player1 = new Box(width - 60,height - 40, 40, 20,true);
        player2 = new Box(width-100, height-200, 40, 20, true);
        lock = new ReentrantLock();
    }
    
    public void evolve(double time)
    {
        lock.lock();
        Ray newLoc = player1.bounceRay(ball.getRay(), time);
        oldLoc = newLoc;
        Ray newLoc2 = player2.bounceRay(ball.getRay(), time);
        if(newLoc != null && newLoc2 == null)
            ball.setRay(newLoc);
        else if(newLoc == null && newLoc2 != null)
            ball.setRay(newLoc2);
        else {
            newLoc = outermost.bounceRay(ball.getRay(), time);
            if(newLoc != null)
                ball.setRay(newLoc);
            else
                ball.move(time);
        }
/*        if (!outermost.contains(ball.getRay().origin))
        {
           ball = new Ball(150, 150, 2, 2);
        }
  */          
        lock.unlock();
    }
    
    public void moveInner(int deltaX,int deltaY)
    {
        lock.lock();
        int dX = deltaX;
        int dY = deltaY;
        if(player1.x + deltaX < 0)
          dX = -player1.x;
        if(player1.x + player1.width + deltaX > outermost.width)
          dX = outermost.width - player1.width - player1.x;
       
        if(player1.y + deltaY < 0)
           dY = -player1.y;
        if(player1.y + player1.height + deltaY > outermost.height)
           dY = outermost.height - player1.height - player1.y;
        
        player1.move(dX,dY);
        if(player1.contains(ball.getRay().origin)) {
            // If we have discovered that the box has just jumped on top of
            // the ball, we nudge them apart until the box no longer
            // contains the ball.
            int bumpX = -1;
            if(dX < 0) bumpX = 1;
            int bumpY = -1;
            if(dY < 0) bumpY = 1;
            do {
            player1.move(bumpX, bumpY);
            ball.getRay().origin.x += -bumpX;
            ball.getRay().origin.y += -bumpY;
            } while(player1.contains(ball.getRay().origin));
        }
        lock.unlock();
    }
    
    public void movep2(int deltaX,int deltaY)
    {
        lock.lock();
        int dX = deltaX;
        int dY = deltaY;
        if(player2.x + deltaX < 0)
          dX = -player2.x;
        if(player2.x + player2.width + deltaX > outermost.width)
          dX = outermost.width - player2.width - player2.x;
       
        if(player2.y + deltaY < 0)
           dY = -player2.y;
        if(player2.y + player2.height + deltaY > outermost.height)
           dY = outermost.height - player2.height - player2.y;
        
        player2.move(dX,dY);
        if(player2.contains(ball.getRay().origin)) {
            // If we have discovered that the box has just jumped on top of
            // the ball, we nudge them apart until the box no longer
            // contains the ball.
            int bumpX = -1;
            if(dX < 0) bumpX = 1;
            int bumpY = -1;
            if(dY < 0) bumpY = 1;
            do {
            player2.move(bumpX, bumpY);
            ball.getRay().origin.x += -bumpX;
            ball.getRay().origin.y += -bumpY;
            } while(player2.contains(ball.getRay().origin));
        }
        lock.unlock();
    }
    
    public List<Shape> setUpShapes()
    {
        ArrayList<Shape> newShapes = new ArrayList<Shape>();
        newShapes.add(outermost.getShape());
//        newShapes.add(outer.getShape());
        newShapes.add(player1.getShape());
        newShapes.add(player2.getShape());
        newShapes.add(ball.getShape());
        return newShapes;
    }
    
    public void updateShapes()
    {
        player1.updateShape();
        player2.updateShape();
        ball.updateShape();
    }
}
