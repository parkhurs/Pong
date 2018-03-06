/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameserver;

import gamesim.Simulation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

/**
 *
 * @author Sam
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private TextArea textArea;
    
    private int clientNo = 0;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new Thread( () ->
        {
            try
            {
                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(8000);
        
                while (true)
                {
                    // Listen for a new connection request
                    Socket socket = serverSocket.accept();
                    
                    clientNo++;
                    
                    Platform.runLater( () -> {
                    // Display the client number
                    textArea.appendText("Starting thread for client " + clientNo + " at " + new Date() + '\n');
                    });
          
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
}

class runGame implements Runnable, game.GameConstants
{
    int p1Score = 0;
    int p2Score = 0;
    private Socket socket;
    private TextArea textArea;
    private Simulation simGame;
    
    public runGame(Socket socket, TextArea textArea)
    {
        this.socket = socket;
        this.textArea = textArea;
    }
    public void run() 
    {
        try
        {
            BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter outputToClient = new PrintWriter(socket.getOutputStream());
            
            while (true)
            {
                // Receive request code from the client
                int request = Integer.parseInt(inputFromClient.readLine());
                switch(request)
                {
                    case PLAYER1:
                        
                        break;
                    case PLAYER2:
                        
                        break;
                    case PLAYER1_WON:
                        
                        break;
                    case PLAYER2_WON:
                        
                        break;
                    case SEND_MOVE:
                        
                        break;
                    case GET_MOVE:
                        
                        break;
                    case GET_SCORE:
                        
                }
                
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}