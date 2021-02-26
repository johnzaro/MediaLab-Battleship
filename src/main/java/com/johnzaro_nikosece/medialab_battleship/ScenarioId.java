package com.johnzaro_nikosece.medialab_battleship;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class ScenarioId
{
    static int answer;
    
    public static int display(String title, String message)
    {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);

        TextField nameInput = new TextField();
        nameInput.setOnAction(e ->
        {
            answer = Integer.parseInt(nameInput.getText());
            window.close();
        });
        
        Button enterButton = new Button("Enter");
        enterButton.setOnAction(e ->
        {
            answer = Integer.parseInt(nameInput.getText());
            window.close();
        });
       
        Button exitButton = new Button("Back");
        exitButton.setOnAction(e ->
        {
            answer = -1;
            window.close();
        });
        
        HBox layout = new HBox(10);
        layout.setPadding(new Insets(20, 20, 20 ,20));
        layout.getChildren().addAll(label, nameInput,  enterButton, exitButton);

        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        
        return answer;
    }
}

