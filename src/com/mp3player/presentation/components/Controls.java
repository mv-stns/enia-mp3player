package com.mp3player.presentation.components;

import com.mp3player.utils.Utils;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;

public class Controls extends HBox implements Styleable {

  private HBox musicPlayerControls;
  public Button skipButton;
  public Button prevButton;
  public Button playButton;
  public Button playPauseButton;
  public Button volumeButton;
  public SVGPath loopIcon;
  public Group loopIconActive;
  public Button loopButton;
  public Slider timeSlider;
  public Slider volumeSlider;
  public TimerView timerView;
  public String playButtonPath =
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
  public String pauseButtonPath =
      "M3.10762 0.333314H10.8923C11.2438 0.333302 11.547 0.333291 11.7967 0.353694C12.0602 0.375228"
          + " 12.3224 0.422772 12.5746 0.551301C12.951 0.743048 13.2569 1.04901 13.4487"
          + " 1.42533C13.5772 1.67759 13.6247 1.93973 13.6463 2.2033C13.6667 2.45301 13.6667"
          + " 2.75618 13.6666 3.1076V10.8924C13.6667 11.2438 13.6667 11.547 13.6463 11.7967C13.6247"
          + " 12.0602 13.5772 12.3224 13.4487 12.5746C13.2569 12.951 12.951 13.2569 12.5746"
          + " 13.4487C12.3224 13.5772 12.0602 13.6247 11.7967 13.6463C11.547 13.6667 11.2438"
          + " 13.6667 10.8924 13.6666H3.1076C2.75618 13.6667 2.45301 13.6667 2.2033 13.6463C1.93973"
          + " 13.6247 1.67759 13.5772 1.42533 13.4487C1.04901 13.2569 0.743048 12.951 0.551301"
          + " 12.5746C0.422772 12.3224 0.375228 12.0602 0.353694 11.7967C0.333291 11.547 0.333302"
          + " 11.2438 0.333314 10.8923V3.10762C0.333302 2.75619 0.333291 2.45301 0.353694"
          + " 2.2033C0.375228 1.93973 0.422772 1.67759 0.551301 1.42533C0.743048 1.04901 1.04901"
          + " 0.743048 1.42533 0.551301C1.67759 0.422772 1.93973 0.375228 2.2033 0.353694C2.45301"
          + " 0.333291 2.75619 0.333302 3.10762 0.333314Z";

  public Controls() {
    loadCSS("controls.css");
    prevButton =
        Utils.createControlIcon(
            "M6.75 7a.75.75 0 0 0-1.5 0v10a.75.75 0 0 0 1.5 0zm3.102 5.66a.834.834 0 0 1 0-1.32a25"
                + " 25 0 0 1 6.935-3.787l.466-.165a.944.944 0 0 1 1.243.772a29.8 29.8 0 0 1 0"
                + " 7.68a.944.944 0 0 1-1.243.772l-.466-.165a25 25 0 0 1-6.935-3.788");
    playPauseButton = Utils.createControlIcon(playButtonPath);
    skipButton =
        Utils.createControlIcon(
            "M18.75 7a.75.75 0 0 0-1.5 0v10a.75.75 0 0 0 1.5 0zm-4.296 3.945c.69.534.69 1.576 0"
                + " 2.11a25.5 25.5 0 0 1-7.073 3.863l-.466.166c-.87.308-1.79-.28-1.907-1.178a30.3"
                + " 30.3 0 0 1 0-7.812c.118-.898 1.037-1.486 1.907-1.177l.466.165a25.5 25.5 0 0 1"
                + " 7.073 3.863");
    SVGPath volumeIcon = new SVGPath();
    volumeIcon.setContent(
        "M5.003 11.716c.038-1.843.057-2.764.678-3.552c.113-.144.28-.315.42-.431c.763-.636"
            + " 1.771-.636 3.788-.636c.72 0 1.081 0"
            + " 1.425-.092q.107-.03.211-.067c.336-.121.637-.33 1.238-.746c2.374-1.645"
            + " 3.56-2.467 4.557-2.11c.191.069.376.168.541.29c.861.635.927 2.115 1.058"
            + " 5.073C18.967 10.541 19 11.48 19 12s-.033 1.46-.081 2.555c-.131 2.958-.197"
            + " 4.438-1.058 5.073a2.2 2.2 0 0"
            + " 1-.54.29c-.997.357-2.184-.465-4.558-2.11c-.601-.416-.902-.625-1.238-.746a3 3 0"
            + " 0 0-.211-.067c-.344-.092-.704-.092-1.425-.092c-2.017 0-3.025 0-3.789-.636a3 3 0"
            + " 0 1-.419-.43c-.621-.79-.64-1.71-.678-3.552a14 14 0 0 1 0-.57");
    volumeIcon.getStyleClass().add("volume-icon");
    volumeIcon.maxWidth(10);
    volumeButton =
        new Utils.ButtonConfigurator(new Button())
            .graphic(volumeIcon)
            .alignment(Pos.CENTER)
            .padding(new Insets(0))
            .minSize(42, 42)
            .styleClass("volume-button", "button")
            .get();
    volumeButton.opacityProperty().set(50);

    timeSlider = new Slider(0, 100, 0);
    timeSlider.setMaxHeight(50);
    timeSlider.setOrientation(Orientation.HORIZONTAL);
    timeSlider.setBlockIncrement(1);

    loopIcon = new SVGPath();
    loopIcon.setContent(
        "M15.0268 11H6.02679C4.35979 11 1.02679 10 1.02679 6C1.02679 2 4.35979 1 6.02679"
            + " 1H14.0268C15.6938 1 19.0268 2 19.0268 6C19.0268 7.494 18.5618 8.57 17.8918 9.331\n"
            + "M12.5268 8.5L15.0268 11L12.5268 13.5");

    loopIconActive =
        new Group(
            new Utils.PathBuilder() // path
                .setContent(
                    "M15.0811 11H6.08105C4.41405 11 1.08105 10 1.08105 6C1.08105 2 4.41405 1"
                        + " 6.08105 1H14.0811C15.7481 1 19.0811 2 19.0811 6C19.0811 7.494 18.6161"
                        + " 8.57 17.9461 9.331")
                .setFill("transparent")
                .setStroke("white")
                .setStrokeWidth(1.5)
                .setStrokeStyle(StrokeLineCap.ROUND)
                .get(),
            new Utils.PathBuilder()
                .setContent("M12.5811 8.5L15.0811 11L12.5811 13.5")
                .setStroke("white")
                .setFill("transparent")
                .setStrokeStyle(StrokeLineCap.ROUND)
                .setStrokeWidth(1.5)
                .get(), // arrow
            new Utils.PathBuilder() // circle
                .setContent(
                    "M6.26184 11C6.26184 12.7153 4.87131 14.1058 3.15601 14.1058C1.4407 14.1058"
                        + " 0.0501709 12.7153 0.0501709 11C0.0501709 9.28469 1.4407 7.89417 3.15601"
                        + " 7.89417C4.87131 7.89417 6.26184 9.28469 6.26184 11Z")
                .setFill("#03FF75")
                .get());

    loopIcon.getStyleClass().add("loop-path");
    loopButton =
        new Utils.ButtonConfigurator(new Button())
            .graphic(loopIcon)
            .alignment(Pos.CENTER)
            .minSize(42, 42)
            .styleClass("loop-button")
            .get();
    loopButton.setGraphic(loopIcon);

    HBox volLoopWrapper = new HBox(42);
    volLoopWrapper.getStyleClass().add("time-loop-wrapper");
    volLoopWrapper.setPadding(new Insets(4));
    volLoopWrapper.setPrefWidth(Region.USE_PREF_SIZE);
    timeSlider.setMaxWidth(Double.MAX_VALUE);
    timeSlider.setPadding(new Insets(0, 8, 0, 8));
    HBox.setHgrow(timeSlider, Priority.ALWAYS);
    volLoopWrapper.getChildren().addAll(timeSlider, loopButton);

    VBox timingSliderWrapper = new VBox(10);
    timerView = new TimerView();
    timingSliderWrapper.getChildren().addAll(timerView, volLoopWrapper);

    volumeSlider = new Slider(0, 100, 50);
    volumeSlider.setMaxHeight(50);
    volumeSlider.setOrientation(Orientation.HORIZONTAL);
    volumeSlider.setBlockIncrement(1);
    volumeSlider.setSnapToTicks(true);
    volumeSlider
        .valueProperty()
        .addListener(
            (ObservableValue<? extends Number> ov, Number oldVal, Number newVal) -> {
              volumeSlider
                  .lookup(".track")
                  .setStyle(
                      String.format(
                          "-fx-background-color: linear-gradient(to right, rgb(255, 94, 0) %d%%, "
                              + "#1B1B1B %d%%);",
                          newVal.intValue(), newVal.intValue()));
            });
    Platform.runLater(
        () -> {
          volumeSlider
              .lookup(".track")
              .setStyle(
                  "-fx-background-color: linear-gradient(to right, rgb(255, 94, 0) 50%, #1B1B1B"
                      + " 50%);");
        });
    HBox volumeWrapper = new HBox(12);
    HBox.setHgrow(volumeWrapper, Priority.SOMETIMES);
    volumeWrapper.getStyleClass().add("vol-wrapper");
    volumeWrapper.setPadding(new Insets(0, 12, 0, 12));
    volumeButton.getStyleClass().add("volButton");
    volumeWrapper.getChildren().addAll(volumeSlider, volumeButton);

    musicPlayerControls = new HBox(8);
    musicPlayerControls.getStyleClass().add("music-player-controls");
    musicPlayerControls.setAlignment(Pos.CENTER_LEFT);
    musicPlayerControls.setMinWidth(400);
    musicPlayerControls
        .getChildren()
        .addAll(prevButton, playPauseButton, skipButton, volumeWrapper);
    VBox controlWrapper = new VBox();
    controlWrapper.setSpacing(24);
    controlWrapper.getChildren().addAll(timingSliderWrapper, musicPlayerControls);

    this.setPrefWidth(Region.USE_PREF_SIZE);

    this.setId("controls");
    this.setAlignment(Pos.CENTER);
    this.getChildren().add(controlWrapper);
  }

  @Override
  public void loadCSS(String stylesheet) {
    this.getStylesheets().add("file:" + this.getClass().getResource(stylesheet).getPath());
  }

  public TimerView getTimerView() {
    return timerView;
  }

  public void updateSliderMax(int duration) {
    timeSlider.setMax(100);
  }

  public void updateSliderValue(int currentTime, int totalDuration) {
    if (totalDuration > 0) {
      double percentage = (double) currentTime / totalDuration * 100;
      timeSlider.setValue(percentage);
    }
  }

  public void togglePlayPauseButtonPath() {
    if (playPauseButton.getGraphic() instanceof SVGPath) {
      SVGPath currentPath = (SVGPath) playPauseButton.getGraphic();
      if (currentPath.getContent().equals(playButtonPath)) {
        currentPath.setContent(pauseButtonPath);
      } else {
        currentPath.setContent(playButtonPath);
      }
    }
  }

  public void toggleLoopButtonIcon(boolean isLooping) {
    Platform.runLater(
        () -> {
          if (isLooping) {
            loopButton.setGraphic(loopIconActive);
          } else {
            loopButton.setGraphic(loopIcon);
          }
        });
  }
}
