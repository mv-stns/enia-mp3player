package com.mp3player.presentation.components;

import static com.mp3player.utils.Constants.*;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TimerView extends HBox {
  public Label currentTimeLabel, songLength;
  private SimpleIntegerProperty time;
  private NumberFormat timeFormat;

  public TimerView() {
    time = new SimpleIntegerProperty();

    currentTimeLabel = new Label("00:00");
    currentTimeLabel.setFont(Font.loadFont("file:src/resources/fonts/SF-Pro-Rounded-Semibold.otf", 16));
    currentTimeLabel.setTextFill(Color.WHITE);

    songLength = new Label("00:00");
    songLength.setFont(Font.loadFont("file:src/resources/fonts/SF-Pro-Rounded-Semibold.otf", 16));
    songLength.setTextFill(Color.rgb(107, 107, 107));

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    this.setWidth(Region.USE_COMPUTED_SIZE);
    this.getChildren().addAll(currentTimeLabel, spacer, songLength);

    timeFormat =
        new NumberFormat() {
          private String timeString = "%02d:%02d";

          @Override
          public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
            return format((int) number, toAppendTo, pos);
          }

          @Override
          public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
            long min, sec;

            min = number / 60;
            sec = number % 60;
            toAppendTo.append(String.format(timeString, min, sec));
            return toAppendTo;
          }

          @Override
          public Number parse(String source, ParsePosition parsePosition) {
            return 0;
          }
        };
  }

  public SimpleIntegerProperty timeProperty() {
    return time;
  }

  public void setTime(int time) {
    this.time.set(time);
  }
}
