package GameServer;

import gameserver.GamePane;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import gamesim.Simulation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class GameServer extends Application
{
/*    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Game Server");
        // Closing the main window does is not sufficient to completely kill
        // the application, since we are going to be running a server thread.
        // Calling System.exit() should do the trick.
        stage.setOnCloseRequest(event->System.exit(0));
        stage.show();
    }
*/    @Override
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
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}