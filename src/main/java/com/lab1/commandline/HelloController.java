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
import java.time.LocalDate;
import java.util.*;

public class HelloController implements Initializable {
    Server serv = new Server();

    public static ObservableList<String> Log = FXCollections.observableArrayList();
    Calendar now = Calendar.getInstance();



    @FXML
    public ListView LogBord;
    @FXML
    public TextField MainTextField;

    @FXML
    public FlowPane MainFlow;

    @FXML
    private void OnOKClicked() throws IOException {
        String resp;
        Log.add("["+Server.now.get(Calendar.HOUR_OF_DAY)+":"+Server.now.get(Calendar.MINUTE)+"]"+MainTextField.getText());
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("sh", "-c", MainTextField.getText());
        builder.directory(new File(Server.fullpath));

        Process process = builder.start();
        BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
        for(resp=output.readLine(); resp!=null;resp=output.readLine()) {

            System.out.println("Respons:" + resp);
            Log.add("Response:"+resp);
        }

        output.close();
        process.destroyForcibly();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Server.start(8090);
           serv.start();
           Log.addListener((javafx.beans.Observable observable) ->{

           });
        LogBord.setItems(Log);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}