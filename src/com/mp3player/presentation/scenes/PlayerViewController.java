package com.mp3player.presentation.scenes;

import static com.mp3player.utils.Constants.*;

import com.mp3player.business.MP3Player;
import com.mp3player.presentation.Controller;
import com.mp3player.presentation.components.TimerView;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

public class PlayerViewController extends Controller<PlayerView> {
  MP3Player player;
  Button skipButton;
  Button prevButton;
  Button playButton;
  Button playPauseButton;
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
  float lastVol;

  public PlayerViewController(MP3Player player) {
    this.player = player;
    root = new PlayerView();

    playPauseButton = root.controls.playPauseButton;
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
    timerSongDurationLabel = root.controls.timerView.songLength;
    currentLengthLabel = root.controls.timerView.currentTimeLabel;

    volumeSlider = root.controls.volumeSlider;

    initialize();
  }

  private void initialize() {
    timerView.timeProperty().bindBidirectional(timeSlider.valueProperty());
    volumeSlider.valueProperty().bindBidirectional(player.currentVolumeProperty());

    volumeButton.addEventHandler(
        ActionEvent.ACTION,
        event -> {
          if (player.isMuted()) {
            player.setVolume(lastVol);
            player.setMuted(false);
          } else {
            player.setVolume(lastVol);
            player.mute();
          }
        });

    playPauseButton.addEventHandler(
        ActionEvent.ACTION,
        event -> {
          if (!player.isSongPlaying() && !player.isAudioPlayerInitialized()) {
            player.play();
            return;
          }
          if (player.isSongPlaying() && !player.isPaused()) {
            Task<Void> pauseTask =
                new Task<Void>() {
                  @Override
                  protected Void call() throws Exception {
                    replaceSVG(playPauseButton, root.controls.pauseButtonPath);
                    player.pause();
                    return null;
                  }
                };
            new Thread(pauseTask).start();
          } else {
            replaceSVG(playPauseButton, root.controls.playButtonPath);
            Task<Void> resumeTask =
                new Task<Void>() {
                  @Override
                  protected Void call() throws Exception {
                    player.resume();
                    updateSongDuration();
                    return null;
                  }
                };
            new Thread(resumeTask).start();
          }
          System.out.println(
              String.format(
                  "PLAYING: %s\t PAUSED: %s\t", player.isSongPlaying(), player.isPaused()));
        });
    skipButton.addEventHandler(
        ActionEvent.ACTION,
        event -> {
          Task<Void> nextTask =
              new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                  player.next();
                  updateSongDuration();
                  return null;
                }
              };
          new Thread(nextTask).start();
        });
    prevButton.addEventHandler(
        ActionEvent.ACTION,
        event -> {
          Task<Void> prevTask =
              new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                  player.previous();
                  updateSongDuration();
                  return null;
                }
              };
          new Thread(prevTask).start();
        });
    loopButton.addEventHandler(
        ActionEvent.ACTION,
        event -> {
          player.setLooping(!player.isLooping());
          updateLoopButtonStyle();
        });

    Platform.runLater(
        () -> {
          title.textProperty().set(player.currentTrackProperty().get().getTitle());
          album.textProperty().set(player.currentTrackProperty().get().getAlbum());
          artist.textProperty().set(player.currentTrackProperty().get().getArtist());
          albumCover.setImage(player.currentTrackProperty().get().getCover());
          volumeSlider.valueProperty().set(player.currentVolumeProperty().doubleValue());
        });

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
    volumeSlider.setOnMouseDragged(
        event -> {
          player.setMuted(false);
          player.setVolume((float) (volumeSlider.getValue()));
          lastVol = (float) volumeSlider.getValue();
        });
    timeSlider.setOnMouseDragEntered(
        event -> {
          player.pause();
        });
    timeSlider.setOnMouseDragged(
        event -> {
          int totalDuration = player.getCurrentSongDuration();
          int newTime = (int) ((timeSlider.getValue() / 100) * totalDuration);
          player.moveTo(newTime);
        });
    timeSlider.setOnMouseDragExited(
        event -> {
          player.play();
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

  private Button replaceSVG(Button b, String p) {
    SVGPath newPath = new SVGPath();
    Button button = new Button();
    Platform.runLater(
        () -> {
          newPath.setContent(p);
          newPath.maxWidth(10);

          button.setGraphic(newPath);
          button.setAlignment(Pos.CENTER);
          button.setPadding(new Insets(0));
          button.getStyleClass().addAll("control-buttons");
          button.setMinWidth(42);
          button.setMinHeight(42);
        });
    return button;
  }
}
