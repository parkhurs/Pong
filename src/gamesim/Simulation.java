package gamesim;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javafx.scene.shape.Shape;
import gamephysics.*;
import javafx.scene.text.Text;

public class Simulation {
    private Box outermost;
    private Ball ball;
    private Box player1;
    private Box player2;
    private Lock lock;
    private Box goal1;
    private Box goal2;
    private int p1Score = 0;
    private int p2Score = 0;
    private Text p1Scores = new Text(20,15,"Player One: " + p1Score);
    private Text p2Scores = new Text(20,245,"Player Two: " + p2Score);
    ArrayList<Shape> newShapes = new ArrayList<Shape>();

    public Simulation(int width,int height,int dX,int dY)
    {
        outermost = new Box(0,0,width,height,false);
        ball = new Ball(width/2,height/2,dX,dY);
        player1 = new Box(width/2-20,20, 40, 20,true);
        player2 = new Box(width/2-20, height-40, 40, 20, true);
        goal1 = new Box(width/2-(width/4)/2,0,width/4,10,true);
        goal2 = new Box(width/2-(width/4)/2,height-10,width/4,10,true);
        lock = new ReentrantLock();
    }
    
    public void evolve(double time)
    {
        lock.lock();
        Ray newLoc = player1.bounceRay(ball.getRay(), time);
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
        if(goal1.contains(ball.getRay().origin))
        {
            p2Score++;
            p2Scores.setText("Player Two: " + p2Score);
            ball.resetPos();
            player1.setPos(130,20);
            player2.setPos(130,210);
        }
        else if (goal2.contains(ball.getRay().origin))
        {
            p1Score++;
            p1Scores.setText("Player One: " + p1Score);
            ball.resetPos();
            player1.setPos(130, 20);
            player2.setPos(130, 210);
        }
        if (!outermost.contains(ball.getRay().origin))
        {
           ball.resetPos();
        }
            
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
        newShapes.add(outermost.getShape());
        newShapes.add(player1.getShape());
        newShapes.add(player2.getShape());
        newShapes.add(ball.getShape());
        newShapes.add(goal1.getShapeColor());
        newShapes.add(goal2.getShapeColor());
        newShapes.add(p1Scores);
        newShapes.add(p2Scores);
        return newShapes;
    }
    
    public void updateShapes()
    {
        player1.updateShape();
        player2.updateShape();
        ball.updateShape();
    }
}
