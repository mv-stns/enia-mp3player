package com.mp3player.presentation.components;

import com.mp3player.utils.Utils;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import mp3player.scene.layout.ImageViewPane;

public class SongEntry extends HBox {
  public String songName, album;
  public ImageView albumCover;
  public ImageViewPane acWrapper;
  public Button playButton;

  public SongEntry(String s, String a, ImageView aC) {
    this.songName = s;
    this.album = a;
    this.albumCover = aC;

    init();
  }

  public SongEntry(String s, String a) {
    this.songName = s;
    this.album = a;
    this.albumCover = null;

    init();
  }

  public void init() {
    VBox songTextWrapper = new VBox(4);
    albumCover = new ImageView();
    final int imageWidth = 80;
    setImage();
    Text sN = new Text(songName);
    Text sA = new Text(album);
    sN.setStyle("-fx-fill: white");
    sN.setFont(Font.loadFont("file:src/resources/fonts/SF-Pro-Rounded-Semibold.otf", 24));
    sA.setStyle("-fx-fill: #6B6B6B");
    sA.setFont(Font.loadFont("file:src/resources/fonts/SF-Pro-Rounded-Semibold.otf", 12));
    songTextWrapper.getChildren().addAll(sN, sA);

    albumCover.setFitHeight(100);
    acWrapper = new ImageViewPane();
    acWrapper.setImageView(albumCover);
    acWrapper.getStyleClass().add("album-cover");
    acWrapper.setPrefSize(imageWidth, imageWidth);
    acWrapper.setMinSize(imageWidth, imageWidth);
    acWrapper.setMaxSize(imageWidth, imageWidth);
    Rectangle mask = new Rectangle(imageWidth, imageWidth);
    mask.setArcWidth(16);
    mask.setArcHeight(16);
    acWrapper.setClip(mask);

    String playSVG =
        "M3.33749 0.868183C3.34546 0.873497 3.35345 0.878828 3.36147 0.884175L10.394"
            + " 5.5725C10.5974 5.70813 10.7861 5.83388 10.931 5.95074C11.0822 6.0727 11.2605"
            + " 6.24182 11.3631 6.48922C11.4987 6.81622 11.4987 7.18373 11.3631 7.51074C11.2605"
            + " 7.75814 11.0822 7.92726 10.931 8.04921C10.7861 8.16607 10.5975 8.29181 10.394"
            + " 8.42743L3.33751 13.1318C3.08879 13.2976 2.86513 13.4467 2.67536 13.5496C2.48545"
            + " 13.6525 2.22477 13.7701 1.92052 13.7519C1.53135 13.7287 1.17185 13.5363"
            + " 0.936646 13.2254C0.752763 12.9823 0.706023 12.7002 0.686314 12.4851C0.66662"
            + " 12.2701 0.666638 12.0013 0.666658 11.7024L0.666659 2.3264C0.666659 2.31676"
            + " 0.666658 2.30715 0.666658 2.29757C0.666638 1.99864 0.66662 1.72984 0.686314"
            + " 1.51489C0.706023 1.29979 0.752763 1.01765 0.936646 0.774581C1.17185 0.463662"
            + " 1.53135 0.271262 1.92052 0.248025C2.22477 0.229859 2.48545 0.347467 2.67536"
            + " 0.450386C2.86513 0.553231 3.08877 0.702349 3.33749 0.868183Z";
    SVGPath playIcon = new Utils.PathBuilder().setContent(playSVG).setFill("white").get();
    playButton =
        new Utils.ButtonConfigurator(new Button())
            .graphic(playIcon)
            .alignment(Pos.CENTER)
            .padding(new Insets(14))
            .styleClass("play-button")
            .minSize(42, 42)
            .get();

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    HBox songCard = new HBox(acWrapper, songTextWrapper, spacer, playButton);
    songCard.setAlignment(Pos.CENTER);
    songCard.setSpacing(12);
    songCard.setPadding(new Insets(4, 12, 4, 4));
    songCard.getStyleClass().add("song-card");

    this.getChildren().add(new VBox(songCard));
  }

  private void setImage() {
    Image placeholder = new Image("file:src/resources/images/album_cover.png");
    albumCover.setImage(placeholder);
    albumCover.setPreserveRatio(true);
  }

  public void setImage(Image img) {
    Platform.runLater(
        () -> {
          albumCover.setImage(img);
          albumCover.setPreserveRatio(true);
          albumCover.setFitHeight(acWrapper.getHeight());
        });
  }
}
