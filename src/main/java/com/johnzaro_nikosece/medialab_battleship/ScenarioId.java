package com.johnzaro_nikosece.medialab_battleship;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.layout.VBox;


public class ScenarioId {

    static boolean answer;





    public static boolean display(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);

        TextField nameInput = new TextField();
        Button enterButton = new Button("Enter");
        enterButton.setOnAction(e -> System.out.println(nameInput.getText()));
        Button exitButton = new Button("Back");

        exitButton.setOnAction(e -> {
            answer = false;
            window.close();
        });
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20 ,20));
        layout.getChildren().addAll(label, nameInput,  enterButton, exitButton);

        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();            //when you open a new window it blocks other click


        return  answer;

    }
}

