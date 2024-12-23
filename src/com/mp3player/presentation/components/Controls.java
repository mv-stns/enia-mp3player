package com.mp3player.presentation.components;

import static com.mp3player.utils.Constants.*;

import com.mp3player.utils.AdditionalFuncs;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

public class Controls extends HBox implements Styleable {

  private HBox musicPlayerControls;
  public Button skipButton;
  public Button prevButton;
  public Button playButton;
  public Button pauseButton;
  public Button volumeButton;
  public Button loopButton;
  public Slider timeSlider;
  public Slider volumeSlider;
  public TimerView timerView;

  public Controls() {
    loadCSS("controls.css");
    prevButton =
        AdditionalFuncs.createSVGButton(
            "M6.75 7a.75.75 0 0 0-1.5 0v10a.75.75 0 0 0 1.5 0zm3.102 5.66a.834.834 0 0 1 0-1.32a25"
                + " 25 0 0 1 6.935-3.787l.466-.165a.944.944 0 0 1 1.243.772a29.8 29.8 0 0 1 0"
                + " 7.68a.944.944 0 0 1-1.243.772l-.466-.165a25 25 0 0 1-6.935-3.788");
    playButton =
        AdditionalFuncs.createSVGButton(
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
                + " 0.450386C2.86513 0.553231 3.08877 0.702349 3.33749 0.868183Z");
    pauseButton =
        AdditionalFuncs.createSVGButton(
            "M17.276 5.47c.435.16.724.575.724 1.039V17.49c0 .464-.29.879-.724 1.039a3.7 3.7 0 0"
                + " 1-2.552 0A1.11 1.11 0 0 1 14 17.491V6.51c0-.464.29-.879.724-1.04a3.7 3.7 0 0 1"
                + " 2.552 0m-8 0c.435.16.724.575.724 1.039V17.49c0 .464-.29.879-.724 1.039a3.7 3.7"
                + " 0 0 1-2.552 0A1.11 1.11 0 0 1 6 17.491V6.51c0-.464.29-.879.724-1.04a3.7 3.7 0 0"
                + " 1 2.552 0");
    skipButton =
        AdditionalFuncs.createSVGButton(
            "M18.75 7a.75.75 0 0 0-1.5 0v10a.75.75 0 0 0 1.5 0zm-4.296 3.945c.69.534.69 1.576 0"
                + " 2.11a25.5 25.5 0 0 1-7.073 3.863l-.466.166c-.87.308-1.79-.28-1.907-1.178a30.3"
                + " 30.3 0 0 1 0-7.812c.118-.898 1.037-1.486 1.907-1.177l.466.165a25.5 25.5 0 0 1"
                + " 7.073 3.863");
    volumeButton =
        AdditionalFuncs.createSVGButton(
            "M5.003 11.716c.038-1.843.057-2.764.678-3.552c.113-.144.28-.315.42-.431c.763-.636"
                + " 1.771-.636 3.788-.636c.72 0 1.081 0"
                + " 1.425-.092q.107-.03.211-.067c.336-.121.637-.33 1.238-.746c2.374-1.645"
                + " 3.56-2.467 4.557-2.11c.191.069.376.168.541.29c.861.635.927 2.115 1.058"
                + " 5.073C18.967 10.541 19 11.48 19 12s-.033 1.46-.081 2.555c-.131 2.958-.197"
                + " 4.438-1.058 5.073a2.2 2.2 0 0"
                + " 1-.54.29c-.997.357-2.184-.465-4.558-2.11c-.601-.416-.902-.625-1.238-.746a3 3 0"
                + " 0 0-.211-.067c-.344-.092-.704-.092-1.425-.092c-2.017 0-3.025 0-3.789-.636a3 3 0"
                + " 0 1-.419-.43c-.621-.79-.64-1.71-.678-3.552a14 14 0 0 1 0-.57");

    timeSlider = new Slider(0, 100, 0);
    timeSlider.setMaxHeight(50);
    timeSlider.setOrientation(Orientation.HORIZONTAL);
    timeSlider.setBlockIncrement(1);
    timeSlider.setSnapToTicks(true);
    timeSlider
        .valueProperty()
        .addListener(
            (ObservableValue<? extends Number> ov, Number oldVal, Number newVal) -> {
              timeSlider
                  .lookup(".track")
                  .setStyle(
                      String.format(
                          "-fx-background-color: linear-gradient(to right, rgb(255, 94, 0) %d%%, "
                              + "#1B1B1B %d%%);",
                          newVal.intValue(), newVal.intValue()));
            });

    SVGPath loopIcon = new SVGPath();
    loopIcon.setContent(
        "M15.0268 11H6.02679C4.35979 11 1.02679 10 1.02679 6C1.02679 2 4.35979 1 6.02679"
            + " 1H14.0268C15.6938 1 19.0268 2 19.0268 6C19.0268 7.494 18.5618 8.57 17.8918 9.331\n"
            + //
            "M12.5268 8.5L15.0268 11L12.5268 13.5");
    loopIcon.getStyleClass().add("loop-path");
    loopButton = new Button();
    loopButton.setGraphic(loopIcon);
    loopButton.setAlignment(Pos.CENTER);
    loopButton.setMinWidth(42);
    loopButton.setMinHeight(42);
    loopButton.getStyleClass().add("loop-button");

    HBox volLoopWrapper = new HBox(42);
    volLoopWrapper.getStyleClass().add("time-loop-wrapper");
    volLoopWrapper.setPadding(new Insets(4));
    volLoopWrapper.setPrefWidth(Region.USE_PREF_SIZE);
    timeSlider.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(timeSlider, Priority.ALWAYS);
    volLoopWrapper.getChildren().addAll(timeSlider, loopButton);

    VBox timingSliderWrapper = new VBox(10);
    timerView = new TimerView();
    timingSliderWrapper.getChildren().addAll(timerView, volLoopWrapper);

    volumeSlider = new Slider(0, 100, 0);
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
    HBox volumeWrapper = new HBox(12);
    HBox.setHgrow(volumeWrapper, Priority.SOMETIMES);
    volumeWrapper.getStyleClass().add("vol-wrapper");
    volumeButton.getStyleClass().add("volButton");
    volumeWrapper.getChildren().addAll(volumeSlider, volumeButton);

    musicPlayerControls = new HBox(8);
    musicPlayerControls.getStyleClass().add("music-player-controls");
    musicPlayerControls.setPadding(new Insets(12));
    musicPlayerControls.setAlignment(Pos.CENTER_LEFT);
    musicPlayerControls.setMinWidth(400);
    musicPlayerControls
        .getChildren()
        .addAll(prevButton, pauseButton, playButton, skipButton, volumeWrapper);
    VBox controlWrapper = new VBox();
    controlWrapper.getChildren().addAll(timingSliderWrapper, musicPlayerControls);

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
}
