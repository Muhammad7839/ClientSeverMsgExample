package org.example.clientsevermsgexample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.*;
import java.net.*;

public class ServerView {

    @FXML private TextField tf_message;
    @FXML private Button button_send;
    @FXML private VBox vbox_messages;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    @FXML
    public void initialize() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(5000);
                clientSocket = serverSocket.accept();
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                String msg;
                while ((msg = in.readLine()) != null) {
                    String finalMsg = "Client: " + msg;
                    Platform.runLater(() -> vbox_messages.getChildren().add(new Text(finalMsg)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        button_send.setOnAction(e -> {
            String msg = tf_message.getText();
            out.println(msg);
            vbox_messages.getChildren().add(new Text("Server: " + msg));
            tf_message.clear();
        });
    }
}
