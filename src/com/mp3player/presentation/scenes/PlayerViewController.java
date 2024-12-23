package com.mp3player.presentation.scenes;

import com.mp3player.business.MP3Player;
import com.mp3player.presentation.Controller;
import com.mp3player.presentation.components.TimerView;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class PlayerViewController extends Controller<PlayerView> {
  MP3Player player;
  Button skipButton;
  Button prevButton;
  Button playButton;
  Button pauseButton;
  Button volumeButton;
  Button loopButton;

  Text title;
  Text artist;
  Text album;
  ImageView albumCover;
  Slider timeSlider;
  Slider volumeSlider;
  TimerView timerView;
  Label timerSongDurationLabel, currentLengthLabel;

  public PlayerViewController(MP3Player player) {
    this.player = player;
    root = new PlayerView();

    playButton = root.controls.playButton;
    pauseButton = root.controls.pauseButton;
    skipButton = root.controls.skipButton;
    prevButton = root.controls.prevButton;
    volumeButton = root.controls.volumeButton;
    loopButton = root.controls.loopButton;

    title = root.songInfo.songTitle;
    artist = root.songInfo.songArtist;
    album = root.songInfo.songAlbum;
    albumCover = root.songInfo.albumCover;

    timeSlider = root.controls.timeSlider;
    timerView = root.controls.timerView;
    timerSongDurationLabel = root.controls.timerView.songLenght;
    currentLengthLabel = root.controls.timerView.currentTimeLabel;
    
    volumeSlider = root.controls.volumeSlider;

    initialize();
  }

  private void initialize() {
    root.controls.getTimerView().timeProperty().bindBidirectional(timeSlider.valueProperty());

    playButton.addEventHandler(
        ActionEvent.ACTION,
        event -> {
          player.play();
          updateSongDuration();
        });
    pauseButton.addEventHandler(
        ActionEvent.ACTION,
        event -> {
          player.pause();
        });
    skipButton.addEventHandler(
        ActionEvent.ACTION,
        event -> {
          player.next();
          updateSongDuration();
        });
    prevButton.addEventHandler(
        ActionEvent.ACTION,
        event -> {
          player.previous();
          updateSongDuration();
        });
    loopButton.addEventHandler(
        ActionEvent.ACTION,
        event -> {
          player.setLooping(!player.isLooping());
          updateLoopButtonStyle();
        });

    title.textProperty().set(player.currentTrackProperty().get().getTitle());
    album.textProperty().set(player.currentTrackProperty().get().getAlbum());
    artist.textProperty().set(player.currentTrackProperty().get().getArtist());
    albumCover.setImage(player.currentTrackProperty().get().getCover());

    player
        .currentTrackProperty()
        .addListener(
            (observable, oldTrack, newTrack) -> {
              Platform.runLater(
                  () -> {
                    if (newTrack != null) {
                      title.textProperty().set(newTrack.getTitle());
                      artist.textProperty().set(newTrack.getArtist());
                      album.textProperty().set(newTrack.getAlbum());
                      albumCover.setImage(
                          newTrack.getCover() != null
                              ? newTrack.getCover()
                              : new Image("file:src/resources/images/album_cover.png"));
                      updateSongDuration();
                      root.controls.updateSliderMax(player.getCurrentSongDuration());
                    } else {
                      title.textProperty().set("Title");
                      artist.textProperty().set("Artist");
                      album.textProperty().set("Album");
                      albumCover.setImage(new Image("file:src/resources/images/album_cover.png"));
                    }
                  });
            });

    player
        .currentTimeProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Platform.runLater(
                    () -> {
                      int currentTimeInSeconds = newValue.intValue();
                      timerView.setTime(currentTimeInSeconds);
                      currentLengthLabel.setText(formatTime(currentTimeInSeconds));
                      root.controls.updateSliderValue(
                          currentTimeInSeconds, player.getCurrentSongDuration());
                    });
              }
            });

    root.controls.timeSlider.setOnMouseReleased(
        event -> {
          int totalDuration = player.getCurrentSongDuration();
          int newTime = (int) ((root.controls.timeSlider.getValue() / 100) * totalDuration);
          Task<Void> t =
              new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                  player.moveTo(newTime);
                  return null;
                }
              };
          new Thread(t).start();
        });
  }

  private void updateSongDuration() {
    int duration = player.getCurrentSongDuration();
    Platform.runLater(
        () -> {
          timerSongDurationLabel.setText(formatTime(duration));
        });
  }

  private String formatTime(int seconds) {
    int minutes = seconds / 60;
    int secs = seconds % 60;
    return String.format("%02d:%02d", minutes, secs);
  }

  private void updateLoopButtonStyle() {
    if (player.isLooping()) {
      loopButton.getStyleClass().add("active-loop");
    } else {
      loopButton.getStyleClass().remove("active-loop");
    }
  }
}
