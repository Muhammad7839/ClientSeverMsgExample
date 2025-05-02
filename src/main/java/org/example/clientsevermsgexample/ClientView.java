package org.example.clientsevermsgexample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.*;
import java.net.*;

public class ClientView {

    @FXML private TextField tf_message;
    @FXML private Button button_send;
    @FXML private VBox vbox_messages;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    @FXML
    public void initialize() {
        try {
            socket = new Socket("localhost", 5000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        String finalMsg = "Server: " + msg;
                        Platform.runLater(() -> vbox_messages.getChildren().add(new Text(finalMsg)));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            button_send.setOnAction(e -> {
                String msg = tf_message.getText();
                out.println(msg);
                vbox_messages.getChildren().add(new Text("Client: " + msg));
                tf_message.clear();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
