package com.mp3player.presentation.scenes;

import com.mp3player.presentation.components.Controls;
import com.mp3player.presentation.components.SongInfo;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class PlayerView extends BorderPane {

  SongInfo songInfo;
  Controls controls;
  VBox mainPane;

  public PlayerView() {
    controls = new Controls();
    songInfo = new SongInfo();

    AnchorPane mainPane = new AnchorPane();
    VBox contentBox = new VBox(songInfo);
    contentBox.setAlignment(Pos.CENTER);

    AnchorPane.setBottomAnchor(controls, 0.0);
    AnchorPane.setLeftAnchor(controls, 0.0);
    AnchorPane.setRightAnchor(controls, 0.0);

    AnchorPane.setTopAnchor(contentBox, 0.0);
    AnchorPane.setLeftAnchor(contentBox, 0.0);
    AnchorPane.setRightAnchor(contentBox, 0.0);

    mainPane.getChildren().addAll(contentBox, controls);
    mainPane.setStyle("-fx-background-radius: 10;");

    this.setStyle("-fx-background-radius: 10;");
    this.setCenter(mainPane);
  }
}
