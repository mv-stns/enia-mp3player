package com.mp3player.controller;

import com.mp3player.model.MP3Player;
import com.mp3player.model.Track;
import de.vaitschulis.utils.Formatting;

public class KeyboardController {

  private MP3Player player;

  public KeyboardController(MP3Player p) {
    this.player = p;
  }

  public void executeCommand(String c) {

    String[] command = c.split(" ");
    try {
      MP3Controls control = MP3Controls.valueOf(command[0].toUpperCase());
      switch (control) {
        case PLAY:
          if (command.length > 1) {
            player.play(Integer.parseInt(command[1]));
          } else {
            this.player.play();
          }
          break;
        case PAUSE:
          player.pause();
          break;
        case RESUME:
          player.resume();
          break;
        case STOP:
          player.stop();
          break;
        case NEXT:
          player.next();
          break;
        case PREVIOUS:
          player.previous();
          break;
        case LOOP:
          player.loop();
          break;
        case VOLUME:
          setVolume(Float.parseFloat(command[1]));
          break;
        case INCVOL:
          setVolume(player.getVolume() + Float.parseFloat(command[1]));
          break;
        case DECVOL:
          System.out.println("Decreasing volume by " + command[1]);
          setVolume(player.getVolume() - Float.parseFloat(command[1]));
          break;
        case MUTE:
          player.mute();
          break;
        case LIST:
          player.listSongs();
          break;
        case HELP:
          listCommands();
          break;
        case EXIT:
          System.out.println("Exiting MP3 Player...");
          System.exit(0);
          break;
        default:
          System.out.println("Invalid command. Please try again.");
          break;
      }
    } catch (Exception e) {
      System.out.println(Formatting.RED_BOLD + "Invalid command. Please try again." + Formatting.RESET);
      e.printStackTrace();
    }
  }

  private void setVolume(float vol) {
    player.volume(vol);
  }

  public boolean isSongPlaying() {
    return player.isSongPlaying();
  }

  public Track getCurrentSong() {
    return player.getCurrentSong();
  }

  public void listCommands() {
    System.out.println(Formatting.BLUE_BACKGROUND_BRIGHT + "\nAvailable Commands:" + Formatting.RESET);
    for (MP3Controls control : MP3Controls.values()) {
      System.out.format("%-10s : %s\n", control.getCommand(), control.getDescription());
    }
  }

  public MP3Player getPlayer() {
    return player;
  }
}
