package com.johnzaro_nikosece.medialab_battleship.customControls;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class IndexLabel extends Label
{
	public IndexLabel(double prefWidth, double prefHeight, String text)
	{
		setPrefSize(prefWidth, prefHeight);
		getStyleClass().addAll("font-size-15", "border-2px-black");
		setAlignment(Pos.CENTER);
		setText(text);
		GridPane.setHalignment(this, HPos.CENTER);
		GridPane.setValignment(this, VPos.CENTER);
	}
}
