package com.mp3player.utils;

import de.vaitschulis.utils.Formatting;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class Constants {
  public static final boolean DEBUG = true;

  public static Debug debug(Object message) {
    if (!DEBUG) return new Debug(message);

    return new Debug(message);
  }

  public static class Debug {
    private Object message;

    public Debug(Object message) {
      this.message = message;
    }

    public void magenta() {
      if (message instanceof StackTraceElement[]) {
        Platform.runLater(
            () -> {
              System.out.println(
                  Formatting.MAGENTA_BOLD
                      + Arrays.toString((StackTraceElement[]) message)
                      + Formatting.RESET);
            });
      } else if (message instanceof String) {
        Platform.runLater(
            () -> {
              System.out.println(Formatting.MAGENTA_BOLD + (String) message + Formatting.RESET);
            });
      }
    }

    public void magentaBackground() {
      if (message instanceof StackTraceElement[]) {
        Platform.runLater(
            () -> {
              System.out.println(
                  Formatting.MAGENTA_BACKGROUND
                      + Arrays.toString((StackTraceElement[]) message)
                      + Formatting.RESET);
            });
      } else if (message instanceof String) {
        Platform.runLater(
            () -> {
              System.out.println(
                  Formatting.MAGENTA_BACKGROUND + (String) message + Formatting.RESET);
            });
      }
    }

    public void red() {
      if (message instanceof StackTraceElement[]) {
        Platform.runLater(
            () -> {
              System.out.println(
                  Formatting.RED_BOLD
                      + Arrays.toString((StackTraceElement[]) message)
                      + Formatting.RESET);
            });
      } else if (message instanceof String) {
        Platform.runLater(
            () -> {
              System.out.println(Formatting.RED_BOLD + (String) message + Formatting.RESET);
            });
      }
    }

    public void redBackground() {
      if (message instanceof StackTraceElement[]) {
        Platform.runLater(
            () -> {
              System.out.println(
                  Formatting.RED_BACKGROUND
                      + Arrays.toString((StackTraceElement[]) message)
                      + Formatting.RESET);
            });
      } else if (message instanceof String) {
        Platform.runLater(
            () -> {
              System.out.println(Formatting.RED_BACKGROUND + (String) message + Formatting.RESET);
            });
      }
    }

    public void blue() {
      if (message instanceof StackTraceElement[]) {
        Platform.runLater(
            () -> {
              System.out.println(
                  Formatting.BLUE_BOLD
                      + Arrays.toString((StackTraceElement[]) message)
                      + Formatting.RESET);
            });
      } else if (message instanceof String) {
        Platform.runLater(
            () -> {
              System.out.println(Formatting.BLUE_BOLD + (String) message + Formatting.RESET);
            });
      }
    }

    public void blueBackground() {
      if (message instanceof StackTraceElement[]) {
        Platform.runLater(
            () -> {
              System.out.println(
                  Formatting.BLUE_BACKGROUND
                      + Arrays.toString((StackTraceElement[]) message)
                      + Formatting.RESET);
            });
      } else if (message instanceof String) {
        Platform.runLater(
            () -> {
              System.out.println(Formatting.BLUE_BACKGROUND + (String) message + Formatting.RESET);
            });
      }
    }
  }

  public static void debugNode(Node... nodes) {
    if (!DEBUG) return;

    for (Node node : nodes) {
      if (node instanceof Region) {
        ((Region) node)
            .setBorder(
                new Border(
                    new BorderStroke(
                        Color.RED,
                        BorderStrokeStyle.SOLID,
                        CornerRadii.EMPTY,
                        BorderWidths.DEFAULT)));
      }
    }
  }
}
