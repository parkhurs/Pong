package GameServer;

import gameserver.GamePane;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import gamesim.Simulation;

public class GameServer extends Application {

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
                case S:
                    sim.movep2(0,3);
                    break;
                case W:
                    sim.movep2(0,-3);
                    break;
                case A:
                    sim.movep2(-3,0);
                    break;
                case D:
                    sim.movep2(3,0);
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
    public static void main(String[] args) {
        launch(args);
    }

}
