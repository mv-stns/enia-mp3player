package com.mp3player.business;

public enum MP3Controls {
    // Track Related
    PLAY("PLAY", "Play <track number>"),
    PAUSE("PAUSE", "Pause the current track"),
    NEXT("NEXT", "Play the next track"),
    PREVIOUS("PREVIOUS", "Play the previous track"),
    RESUME("RESUME", "Resume the current track"),
    STOP("STOP", "Stop the current track"),
    LOOP("LOOP", "Loop the current track"),

    // Volume Related
    VOLUME("VOLUME", "Set volume to <0-100>"),
    INCVOL("INCVOL", "Increase volume 3%"),
    DECVOL("DECVOL", "Decrease volume 3%"),
    MUTE("MUTE", "Mutes the current track"),

    // MISC
    HELP("HELP", "List all commands"),
    LIST("LIST", "List all tracks"),
    EXIT("EXIT", "Exit the MP3 Player");

    private final String COMMAND;
    private String description;

    private MP3Controls(String command, String description) {
        this.COMMAND = command;
        this.description = description;
    }

    public String getCommand() {
        return COMMAND;
    }

    public String getDescription() {
        return description;
    }
}
