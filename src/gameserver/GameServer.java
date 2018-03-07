package GameServer;

import static game.GameConstants.PLAYER1;
import static game.GameConstants.PLAYER2;
import gameserver.GamePane;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import gamesim.Simulation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.scene.control.TextArea;

public class GameServer extends Application
{
    @Override
    public void start(Stage primaryStage) {
        GamePane root = new GamePane();
        Simulation sim = new Simulation(300, 250, 2, 2);
        root.setShapes(sim.setUpShapes());
        
        Scene scene = new Scene(root, 300, 250);
        root.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case DOWN:
                    sim.moveInner(0, 3);
                    break;
                case UP:
                    sim.moveInner(0, -3);
                    break;
                case LEFT:
                    sim.moveInner(-3, 0);
                    break;
                case RIGHT:
                    sim.moveInner(3, 0);
                    break;
                
                case Q:
                    sim.movep2(-3,-3);
                    break;
                case W:
                    sim.movep2(0,-3);
                    break;
                case E:
                    sim.movep2(3,-3);
                    break;
                case D:
                    sim.movep2(3,0);
                    break;
                case C:
                    sim.movep2(3,3);
                    break;
                case X:
                    sim.movep2(0,3);
                    break;
                case Z:
                    sim.movep2(-3,3);
                    break;
                case A:
                    sim.movep2(-3,0);
                    break;
            }
        });
        
        root.requestFocus(); 

        primaryStage.setTitle("Game Physics");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest((event)->System.exit(0));
        primaryStage.show();

        // This is the main animation thread
        new Thread(() -> {
            while (true) {
                sim.evolve(1.0);
                Platform.runLater(()->sim.updateShapes());
                try {
                    Thread.sleep(25);
                } catch (InterruptedException ex) {

                }
            }
        }).start(); 
        
        new Thread( () ->
        {
            try
            {
                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(8000);
        
                while (true)
                {
                    // Listen for a new connection request
                    Socket socket1 = serverSocket.accept();
                    PrintWriter outputToClient1 = new PrintWriter(socket1.getOutputStream());
                    outputToClient1.println(PLAYER1);
                    outputToClient1.flush();
                    
                    
                    Socket socket2 = serverSocket.accept();
                    
                    PrintWriter outputToClient = new PrintWriter(socket2.getOutputStream());
                    outputToClient.println(PLAYER2);
                    outputToClient.flush();
                    outputToClient1.println(PLAYER2);
                    outputToClient1.flush();
                    
                    
                    //Platform.runLater( () -> {
                    // Display the client number
                    //textArea.appendText("at " + new Date() + '\n');
                    //});
                    
                    new Thread(new HandleAClient(socket1, socket2)).start();
                    // Create and start a new thread for the connection
//                    new Thread(new runGame(socket,textArea)).start();
                }
            }
            catch(IOException ex)
            {
                System.err.println(ex);
            }
        }).start();
    }
    
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
class HandleAClient implements Runnable, game.GameConstants {
    private Socket socket; // A connected socket
    private TextArea textArea;
    private String handle;
    
    public HandleAClient(Socket socket1, Socket socket2) {
      this.socket = socket;
      
    }
    
    public void run() {
        try
        {
            // Create reading and writing streams
            BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter outputToClient = new PrintWriter(socket.getOutputStream());

            // Continuously serve the client
            while (true)
            {
                // Receive request code from the client
                int request = Integer.parseInt(inputFromClient.readLine());
                // Process request
                switch(request)
                {
//                  case PLAYER1: {
//                      outputToClient.println(PLAYER1);
//                  outputToClient.flush();
//                  //handle = inputFromClient.readLine();
//                  break;
//              }
//              case PLAYER2: {
//                  outputToClient.println(PLAYER2);
//                  outputToClient.flush();
//                  break;
//              }
         }
      }
      } catch(IOException ex) {
            ex.printStackTrace();
            Platform.runLater(()->textArea.appendText("Exception in client thread: "+ex.toString()+"\n"));   
        }
    }
}