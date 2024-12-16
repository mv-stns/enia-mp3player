package com.mp3player;

import com.mp3player.controller.KeyboardController;
import com.mp3player.model.MP3Player;
import de.hsrm.mi.prog.util.StaticScanner;
import de.vaitschulis.utils.Formatting;

public class App {

    public static void main(String[] args) throws Exception {
        boolean running = true;
        MP3Player player = new MP3Player();
        KeyboardController kyC = new KeyboardController(player);

        
        System.out.println("\nWelcome to the MP3 Player!");
        kyC.listCommands();

        while (running) {
            System.out.println("\nPlease enter a command:");
            String command = StaticScanner.nextString();
            try {
                // clear terminal after each command
                System.out.print("\033[H\033[2J");
                kyC.executeCommand(command);
                if (kyC.isSongPlaying()) {
                    System.out.println(Formatting.MAGENTA_BOLD_BRIGHT + "Now playing: "
                            + kyC.getCurrentSong().getTitle() + Formatting.RESET);
                }
            } catch (Exception e) {
                System.out.println(
                        Formatting.RED_BOLD + "Player Not Initialized. Please play a song." + Formatting.RESET);
                // System.out.println(e);
            }
        }
    }
}
