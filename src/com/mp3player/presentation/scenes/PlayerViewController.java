package com.mp3player.presentation.scenes;

import static com.mp3player.utils.Constants.*;

import com.mp3player.business.MP3Player;
import com.mp3player.business.Track;
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
  private boolean isSliderDragging = false;
  MP3Player player;
  Button skipButton, prevButton, playButton, playPauseButton, volumeButton, loopButton;
  Text title, artist, album;
  ImageView albumCover;
  Slider timeSlider, volumeSlider;
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
    debug("INITIALIZING").blue();
    timerView.timeProperty().bindBidirectional(timeSlider.valueProperty());
    volumeSlider.valueProperty().bindBidirectional(player.currentVolumeProperty());

    Platform.runLater(
        () -> {
          timerSongDurationLabel.setText(formatTime(player.getCurrentSongDuration()));
        });

    volumeButton.addEventHandler(
        ActionEvent.ACTION,
        event -> {
          if (player.isMuted()) {
            player.setVolume(lastVol);
            player.setMuted(false);
            volumeButton.opacityProperty().set(100);
          } else {
            player.setVolume(lastVol);
            player.mute();
            volumeButton.opacityProperty().set(0.2);
          }
        });

    playPauseButton.addEventHandler(
        ActionEvent.ACTION,
        event -> {
          try {
            Thread.sleep(200);
          } catch (InterruptedException e) {
          }
          if (player.isSongPlaying() && player.isPaused()) {
            debug("SOMETHING IS WRONG!").redBackground();
            debug(
                    String.format(
                        "PLAYING: %s\t PAUSED: %s\t", player.isSongPlaying(), player.isPaused()))
                .blueBackground();
            return;
          }
          if (player.isSongPlaying()) {
            // root.controls.togglePlayPauseButtonPath();
            executeTask(() -> player.pause());
            updateSongDuration();
            return;
          } else {
            // root.controls.togglePlayPauseButtonPath();
            executeTask(() -> player.resume());
            Platform.runLater(
                () -> {
                  updateSongDuration();
                });
          }
          debug(
                  String.format(
                      "PLAYING: %s\t PAUSED: %s\t", player.isSongPlaying(), player.isPaused()))
              .blueBackground();
        });
    timeSlider
        .valueProperty()
        .addListener(
            (ov, oldVal, newVal) -> {
              timeSlider
                  .lookup(".track")
                  .setStyle(
                      String.format(
                          "-fx-background-color: linear-gradient(to right, -bool-val %d%%, #1B1B1B"
                              + " %d%%);",
                          newVal.intValue(), newVal.intValue()));
            });

    skipButton.addEventHandler(
        ActionEvent.ACTION,
        event -> {
          executeTask(() -> player.skip());
          updateSongDuration();
        });
    prevButton.addEventHandler(
        ActionEvent.ACTION,
        event -> {
          new Thread() {
            public void run() {
              player.skipBack();
            }
          }.start();
          return;
        });
    loopButton.addEventHandler(
        ActionEvent.ACTION,
        event -> {
          player.setLooping(!player.isLooping());
          root.controls.toggleLoopButtonIcon(player.isLooping());
          updateLoopButtonStyle();
        });

    Platform.runLater(
        () -> {
          title.textProperty().set(player.currentTrackProperty().get().getTitle());
          album.textProperty().set(player.currentTrackProperty().get().getAlbum());
          artist.textProperty().set(player.currentTrackProperty().get().getArtist());
          albumCover.setImage(player.currentTrackProperty().get().getCover());
          volumeSlider.valueProperty().set(player.currentVolumeProperty().doubleValue());
          updateSongDuration();
        });

    player
        .currentTrackProperty()
        .addListener(
            (observable, oldTrack, newTrack) -> {
              debug("Track changed: " + newTrack.getTitle()).red();
              new Thread() {
                public void run() {
                  Platform.runLater(
                      () -> {
                        updateSongInfo(newTrack);
                        updateSongDuration();
                      });
                  try {
                    Thread.sleep(200);
                  } catch (Exception e) {
                    Thread.currentThread().interrupt();
                  }
                  root.controls.updateSliderMax(player.getCurrentSongDuration());
                }
              }.start();
            });

    player
        .getPausedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              debug("Paused has been switched to:" + newVal);
              if (newVal == true) {
                timeSlider.setStyle("-bool-val: #575757;");
                root.controls.togglePlayPauseButtonPath();
              } else {
                timeSlider.setStyle("-bool-val: rgb(255, 94, 0);");
                root.controls.togglePlayPauseButtonPath();
              }
            });

    if (player.getCurrentSong() != null) {
      updateSongInfo(player.getCurrentSong());
    }

    volumeSlider.setOnMouseDragged(
        event -> {
          player.setMuted(false);
          player.setVolume((float) (volumeSlider.getValue()));
          lastVol = (float) volumeSlider.getValue();
          volumeButton.opacityProperty().set(Math.max(volumeSlider.getValue() / 100, 0.2));
        });
    timeSlider.setOnMousePressed(
        event -> {
          isSliderDragging = true;
        });
    timeSlider.setOnMouseReleased(
        event -> {
          isSliderDragging = false;
          int totalDuration = player.getCurrentSongDuration();
          int newTime = (int) ((timeSlider.getValue() / 100) * totalDuration);
          player.setPaused(false);
          player.moveTo(newTime);
        });
    timeSlider.setOnMouseDragged(
        event -> {
          int totalDuration = player.getCurrentSongDuration();
          int newTime = (int) ((timeSlider.getValue() / 100) * totalDuration);
          currentLengthLabel.setText(formatTime(newTime / 1000));
        });
    player
        .currentTimeProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                new Thread() {
                  public void run() {
                    Platform.runLater(
                        () -> {
                          if (!isSliderDragging) {
                            int currentTimeInSeconds = newValue.intValue();
                            timerView.setTime(currentTimeInSeconds / 1000);
                            currentLengthLabel.setText(formatTime(currentTimeInSeconds / 1000));
                            try {
                              root.controls.updateSliderValue(
                                  currentTimeInSeconds / 1000,
                                  player.getCurrentSongDuration() / 1000);
                              debug(
                                      String.format(
                                          "C: %s\tL: %s",
                                          newValue.intValue(), player.getCurrentSongDuration()))
                                  .red();
                            } catch (NullPointerException e) {
                              System.out.println("Song not loaded yet.");
                            }
                          }
                        });
                  }
                }.start();
              }
            });
  }

  private synchronized void updateSongInfo(Track newTrack) {
    title.textProperty().set(newTrack.getTitle());
    artist.textProperty().set(newTrack.getArtist());
    album.textProperty().set(newTrack.getAlbum());
    albumCover.setImage(
        newTrack.getCover() != null
            ? newTrack.getCover()
            : new Image("file:src/resources/images/album_cover.png"));
    updateSongDuration();
  }

  private void updateSongDuration() {
    new Thread() {
      public void run() {

        try {
          Thread.sleep(100);
        } catch (Exception e) {
        }

        // if (player.isLooping() && player.getCurrentSongDuration() ==
        // player.currentTimeProperty().get()) {
        //   player.moveTo(0);
        // }

        int duration = player.getCurrentSongDuration() / 1000;
        debug(duration).magenta();
        Platform.runLater(
            () -> {
              timerSongDurationLabel.setText(formatTime(duration));
            });
        // if ()
      }
    }.start();
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

  private void executeTask(Runnable r) {
    Task<Void> Task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            r.run();
            return null;
          }
        };
    new Thread(Task).start();
  }
}
