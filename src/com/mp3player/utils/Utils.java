package com.mp3player.utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;

public class Utils {

  public static Button createControlIcon(String svgPath) {
    SVGPath path = new SVGPath();
    path.setContent(svgPath);
    path.getStyleClass().add("control-icons");
    path.maxWidth(10);

    return new ButtonConfigurator(new Button())
        .graphic(path)
        .alignment(Pos.CENTER)
        .padding(new Insets(0))
        .styleClass("control-buttons")
        .minSize(42, 42)
        .get();
  }

  public static class ButtonConfigurator {
    private final Button button;

    public ButtonConfigurator(Button button) {
      this.button = button;
    }

    public ButtonConfigurator graphic(SVGPath graphic) {
      button.setGraphic(graphic);
      return this;
    }

    public ButtonConfigurator alignment(Pos pos) {
      button.setAlignment(pos);
      return this;
    }

    public ButtonConfigurator padding(Insets insets) {
      button.setPadding(insets);
      return this;
    }

    public ButtonConfigurator minSize(double width, double height) {
      button.setMinWidth(width);
      button.setMinHeight(height);
      return this;
    }

    public ButtonConfigurator styleClass(String... styleClasses) {
      button.getStyleClass().addAll(styleClasses);
      return this;
    }

    public Button get() {
      return button;
    }
  }

  public static class PathBuilder {
    private SVGPath path;

    public PathBuilder() {
      this.path = new SVGPath();
    }

    public PathBuilder setContent(String pathContent) {
      path.setContent(pathContent);
      return this;
    }

    public PathBuilder setStyleClasses(String... styleClasses) {
      path.getStyleClass().addAll(styleClasses);
      return this;
    }

    public PathBuilder setFill(String fill) {
      path.setStyle(path.getStyle() + "-fx-fill: " + fill + ";");
      return this;
    }

    public PathBuilder setSize(double width, double height) {
      path.minWidth(width);
      path.minHeight(height);
      return this;
    }

    public PathBuilder setStroke(String stroke) {
      path.setStyle(path.getStyle() + "-fx-stroke: " + stroke + ";");
      return this;
    }

    public PathBuilder setStrokeWidth(double strokeWidth) {
      path.setStyle(path.getStyle() + "-fx-stroke-width: " + strokeWidth + ";");
      return this;
    }

    public PathBuilder setStrokeStyle(StrokeLineCap strokeLineCap) {
      path.setStyle(path.getStyle() + "-fx-stroke-line-cap: " + strokeLineCap + ";");
      return this;
    }

    public PathBuilder setStyle(String... styles) {
      for (String style : styles) {
        path.setStyle(path.getStyle() + style);
      }
      return this;
    }

    public SVGPath get() {
      return path;
    }
  }
}
