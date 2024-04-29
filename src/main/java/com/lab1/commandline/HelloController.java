package com.lab1.commandline;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class HelloController implements Initializable {
    Server serv = new Server();

    public static ObservableList<String> Log = FXCollections.observableArrayList();




    @FXML
    public ListView LogBord;
    @FXML
    public TextField MainTextField;

    @FXML
    public FlowPane MainFlow;

    @FXML
    private void OnOKClicked() throws IOException {
        String resp;
        Log.add(MainTextField.getText());
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("sh", "-c", MainTextField.getText());
        builder.directory(new File("/home/me/IdeaProjects/demo/src/main/java/com/lab1/demo/"));

        Process process = builder.start();
        BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
        for(resp=output.readLine(); resp!=null;resp=output.readLine()) {

            System.out.println("Respons:" + resp);
            Log.add(resp);
        }

        output.close();
        process.destroyForcibly();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Server.start(8080);
           serv.start();
           Log.addListener((javafx.beans.Observable observable) ->{

           });
        LogBord.setItems(Log);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}