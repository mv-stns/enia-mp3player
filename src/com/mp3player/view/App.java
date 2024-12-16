package com.mp3player.view;

import com.mp3player.model.MP3Player;
import com.mp3player.view.App.ResizeGrabber;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {
  private static Scene scene;
  private static PlayerView playerView;

  // Stage
  // ├── Frame (Base Element)
  // │ └── Pane
  public void init() {
    // Implementation eventueller Business-Logiken
  }

  @Override
  public void start(Stage mainStage) throws Exception {
    MP3Player player = new MP3Player();
    playerView = new PlayerView(player);

    scene = new Scene(playerView, 1440, 800);
    mainStage.initStyle(StageStyle.DECORATED);
    mainStage.resizableProperty().setValue(Boolean.TRUE);
    scene.setFill(null);
    StackPane mainContainer = new StackPane();
    mainContainer.getStyleClass().add("mainContainer");

    // Rounded Corners for root
    mainContainer.setPadding(new Insets(20, 20, 20, 20));
    mainContainer.minWidth(450);
    mainContainer.getChildren().add(playerView);
    mainContainer.setAlignment(Pos.CENTER);
    mainContainer.widthProperty().addListener(p -> updateShape(mainContainer));
    mainContainer.heightProperty().addListener(p -> updateShape(mainContainer));

    // Traffic Light
    StackPane root = new StackPane();
    TrafficLight trafficLight = new TrafficLight(mainStage);
    ResizeGrabber resizeGrabber = new ResizeGrabber(mainStage);
    root.getChildren().addAll(mainContainer, trafficLight, resizeGrabber);
    StackPane.setAlignment(trafficLight, Pos.TOP_LEFT);
    trafficLight.setTranslateX(40);
    trafficLight.setTranslateY(40);
    StackPane.setAlignment(resizeGrabber, Pos.BOTTOM_RIGHT);
    resizeGrabber.setTranslateX(-25);
    resizeGrabber.setTranslateY(-25);
    root.setPadding(new Insets(0));
    root.setMinWidth(Region.USE_PREF_SIZE);
    root.setMinHeight(Region.USE_PREF_SIZE);
    root.setStyle("-fx-background-color: transparent;");
    // set subtle dropshadow inset for the mainContainer creating a subtle white 1px
    // no blur dropshadow border on the inside top

    scene.setRoot(root);

    scene.getStylesheets().add("file:src/com/mp3player/style.css");

    // setActions
    root.setOnMouseMoved(
        (MouseEvent event) -> {
          System.out.println(String.format("X: %s, Y: %s", event.getSceneX(), event.getSceneY()));
          if (event.getSceneX() <= 150 && event.getSceneY() <= 250) {
            trafficLight.setVisible(true);
          } else {
            trafficLight.setVisible(false);
          }
        });
    scene.setOnMousePressed(
        (MouseEvent event) -> {
          if (event.getSceneX() >= scene.getWidth() - 33
              && event.getSceneY() >= scene.getHeight() - 34) {
            scene.setOnMouseDragged(
                (MouseEvent event1) -> {
                  double newWidth = event1.getScreenX() - mainStage.getX() + 23;
                  double newHeight = event1.getScreenY() - mainStage.getY() + 24;

                  // Ensure the new width is at least 400 + root insets
                  if (newWidth
                      < 405
                          + mainContainer.getInsets().getLeft()
                          + mainContainer.getInsets().getRight()) {
                    newWidth =
                        405
                            + mainContainer.getInsets().getLeft()
                            + mainContainer.getInsets().getRight();
                  }
                  // Ensure the new height is at least 470 + root insets
                  if (newHeight
                      < 470
                          + mainContainer.getInsets().getTop()
                          + mainContainer.getInsets().getBottom()) {
                    newHeight =
                        470
                            + mainContainer.getInsets().getTop()
                            + mainContainer.getInsets().getBottom();
                  }

                  mainStage.setWidth(newWidth);
                  mainStage.setHeight(newHeight);
                });
          } else {
            scene.setOnMouseDragged(
                (MouseEvent event1) -> {
                  mainStage.setX(event1.getScreenX() - event.getSceneX());
                  mainStage.setY(event1.getScreenY() - event.getSceneY());
                });
          }
        });

    mainStage.setTitle("MP3 Player");
    mainStage.setMinWidth(400);
    mainStage.setMinHeight(470);
    mainStage.setScene(scene);
    mainStage.show();
  }

  private void updateShape(StackPane signUp) {
    // PART OF THE FIX
    // (https://stackoverflow.com/questions/67699182/why-is-fx-background-radius-10-not-working/67705149#67705149)
    double r = 30; // BACKGROUND IMAGE RADIUS
    double w = signUp.getWidth();
    double h = signUp.getHeight();
    String s =
        "-fx-shape:\"M "
            + r
            + " 0 "
            + // p1
            " L "
            + (w - r)
            + " "
            + "0"
            + // p2
            " Q "
            + w
            + " 0 "
            + w
            + " "
            + r
            + // p3,p4
            " L "
            + w
            + " "
            + (h - r)
            + // p5
            " Q "
            + w
            + " "
            + h
            + " "
            + (w - r)
            + " "
            + h
            + // p6,p7
            " L "
            + r
            + " "
            + h
            + // p8
            " Q 0 "
            + h
            + " 0 "
            + (h - r)
            + // p9,p10
            " L 0 "
            + r
            + // p11
            " Q 0 0 "
            + r
            + " 0 Z\";"; // p0,p1
    System.out.println(s);
    signUp.setStyle(s);
  }

  public void stop() {
    // Freigabe von Resourcen
  }

  class TrafficLight extends Group {
    private static final int BUTTON_RADIUS = 6;
    private static final int BUTTON_Y_POSITION = 20;
    private static final double ICON_SCALE = 0.8;
    private static final double ICON_TRANSLATE_Y = 15.5;
    private static final int BUTTON_SPACING = 18;

    private final Stage mainStage;

    public TrafficLight(Stage mainStage) {
      this.mainStage = mainStage;
      int currentPos = 20;

      Circle closeButton = createButton(currentPos, Color.web("#FF5F56"));
      Group xIcon =
          createIcon(
              currentPos,
              "M1.18642 0.789477C1.54765 0.428247 2.13332 0.428247 2.49455 0.789477L8.64472"
                  + " 6.93965C9.00595 7.30088 9.00595 7.88655 8.64472 8.24778C8.28349 8.60901"
                  + " 7.69783 8.60901 7.3366 8.24778L1.18642 2.0976C0.825189 1.73637 0.825189"
                  + " 1.15071 1.18642 0.789477Z",
              "M8.6447 0.789477C8.28347 0.428247 7.6978 0.428247 7.33657 0.789477L1.18639"
                  + " 6.93965C0.825163 7.30088 0.825163 7.88655 1.18639 8.24778C1.54762 8.60901"
                  + " 2.13329 8.60901 2.49452 8.24778L8.6447 2.0976C9.00593 1.73637 9.00593 1.15071"
                  + " 8.6447 0.789477Z");

      currentPos += BUTTON_SPACING;
      Circle minimizeButton = createButton(currentPos, Color.web("#FEBC2E"));
      Group minusIcon =
          createIcon(
              currentPos,
              "M0.915558 4.51872C0.915558 4.00787 1.32968 3.59375 1.84052 3.59375L7.99059"
                  + " 3.59375C8.50144 3.59375 8.91556 4.00787 8.91556 4.51872C8.91556 5.02956"
                  + " 8.50144 5.44368 7.99059 5.44368L1.84053 5.44368C1.32968 5.44368 0.915558"
                  + " 5.02956 0.915558 4.51872Z");

      currentPos += BUTTON_SPACING;
      Circle maximizeButton = createButton(currentPos, Color.web("#26C940"));
      Group resizeIcon =
          createIcon(
              currentPos,
              "M1.85846 5.01112V1.46143H5.40816L1.85846 5.01112Z",
              "M1.85846 0.518555H5.40816C5.78953 0.518555 6.13335 0.748286 6.27929 1.10063C6.42523"
                  + " 1.45296 6.34456 1.85852 6.07489 2.12819L2.5252 5.67789C2.25553 5.94756"
                  + " 1.84997 6.02823 1.49763 5.88229C1.14529 5.73634 0.915558 5.39253 0.915558"
                  + " 5.01116V1.46146C0.915558 0.940708 1.33771 0.518555 1.85846 0.518555ZM2.80137"
                  + " 2.40436V2.73478L3.13179 2.40436H2.80137Z",
              "M7.97293 4.02599L7.97293 7.57568L4.42323 7.57568L7.97293 4.02599Z",
              "M7.97296 8.51855L4.42326 8.51855C4.04189 8.51855 3.69807 8.28882 3.55213"
                  + " 7.93648C3.40619 7.58414 3.48686 7.17858 3.75653 6.90892L7.30622"
                  + " 3.35922C7.57589 3.08955 7.98145 3.00888 8.33379 3.15482C8.68613 3.30077"
                  + " 8.91586 3.64458 8.91586 4.02595L8.91586 7.57565C8.91586 8.0964 8.49371"
                  + " 8.51855 7.97296 8.51855ZM7.03005 6.63274L7.03005 6.30233L6.69963"
                  + " 6.63274L7.03005 6.63274Z");
      setupButtonActions(closeButton, xIcon, () -> Platform.exit());
      setupButtonActions(minimizeButton, minusIcon, () -> mainStage.setIconified(true));
      setupButtonActions(
          maximizeButton, resizeIcon, () -> mainStage.setMaximized(!mainStage.isMaximized()));

      getChildren()
          .addAll(closeButton, xIcon, minimizeButton, minusIcon, maximizeButton, resizeIcon);
    }

    private Circle createButton(int centerX, Color color) {
      Circle button = new Circle(BUTTON_RADIUS, color);
      button.setCenterX(centerX);
      button.setCenterY(BUTTON_Y_POSITION);
      return button;
    }

    private Group createIcon(int translateX, String... svgPaths) {
      Group icon = new Group();
      for (String path : svgPaths) {
        SVGPath svgPath = createSVGPath(path);
        positionIcon(svgPath, translateX);
        icon.getChildren().add(svgPath);
      }
      icon.setVisible(false);
      return icon;
    }

    private SVGPath createSVGPath(String content) {
      SVGPath svgPath = new SVGPath();
      svgPath.setContent(content);
      return svgPath;
    }

    private void positionIcon(SVGPath icon, int translateX) {
      icon.setTranslateX(translateX - 4.5);
      icon.setTranslateY(ICON_TRANSLATE_Y);
      icon.setScaleX(ICON_SCALE);
      icon.setScaleY(ICON_SCALE);
      icon.setMouseTransparent(true);
    }

    private void setupButtonActions(Circle button, Group icon, Runnable action) {
      button.setOnMouseEntered(event -> icon.setVisible(true));
      button.setOnMouseExited(event -> icon.setVisible(false));
      button.setOnMouseClicked(event -> action.run());
    }
  }

  class ResizeGrabber extends Group {
    public ResizeGrabber(Stage mainStage) {
      HBox resizeGrabber = new HBox();
      String svgContent =
          "M12.776 1.28424C12.9085 1.14207 12.9806 0.95402 12.9771 0.759718C12.9737 0.565417 12.895"
              + " 0.380032 12.7576 0.242619C12.6202 0.105206 12.4348 0.0264943 12.2405"
              + " 0.0230661C12.0462 0.0196378 11.8581 0.091761 11.716 0.224241L1.21997"
              + " 10.7202C1.14628 10.7889 1.08718 10.8717 1.04619 10.9637C1.0052 11.0557 0.983156"
              + " 11.155 0.981379 11.2557C0.979602 11.3564 0.998127 11.4565 1.03585 11.5498C1.07357"
              + " 11.6432 1.12971 11.7281 1.20093 11.7993C1.27215 11.8705 1.35698 11.9266 1.45037"
              + " 11.9644C1.54376 12.0021 1.64379 12.0206 1.74449 12.0188C1.8452 12.0171 1.94451"
              + " 11.995 2.03651 11.954C2.12851 11.913 2.21131 11.8539 2.27997 11.7802L12.776"
              + " 1.28424ZM12.776 6.28424C12.9085 6.14207 12.9806 5.95402 12.9771 5.75972C12.9737"
              + " 5.56542 12.895 5.38003 12.7576 5.24262C12.6202 5.10521 12.4348 5.02649 12.2405"
              + " 5.02307C12.0462 5.01964 11.8581 5.09176 11.716 5.22424L6.21997 10.7202C6.14628"
              + " 10.7889 6.08718 10.8717 6.04619 10.9637C6.0052 11.0557 5.98316 11.155 5.98138"
              + " 11.2557C5.9796 11.3564 5.99813 11.4565 6.03585 11.5498C6.07357 11.6432 6.12971"
              + " 11.7281 6.20093 11.7993C6.27215 11.8705 6.35698 11.9266 6.45037 11.9644C6.54376"
              + " 12.0021 6.64379 12.0206 6.74449 12.0188C6.8452 12.0171 6.94451 11.995 7.03651"
              + " 11.954C7.12851 11.913 7.21131 11.8539 7.27997 11.7802L12.776 6.28424Z";
      javafx.scene.shape.SVGPath svgPath = new javafx.scene.shape.SVGPath();
      svgPath.setContent(svgContent);
      svgPath.setFill(Color.WHITE); // Set the color of the SVG
      resizeGrabber.getChildren().add(svgPath);
      getChildren().add(resizeGrabber);

      setOnMousePressed(
          (MouseEvent event) -> {
            // Resize logic for bottom corners
            if (event.getSceneX() >= getLayoutX() && event.getSceneY() >= getLayoutY()) {
              setOnMouseDragged(
                  (MouseEvent event1) -> {
                    mainStage.setWidth(event1.getScreenX() - mainStage.getX());
                    mainStage.setHeight(event1.getScreenY() - mainStage.getY());
                  });
            }
          });
      // make border red for debugging
      this.setStyle(
          "-fx-border-color: red;"
              .concat("-fx-border-width: 1;".concat("-fx-border-style: solid;"))
              .concat("-fx-background-color: transparent;"));
      this.setStyle("-fx-cursor: se-resize;");
    }
  }

  public static void main(String[] args) {
    Application.launch(App.class, args);
  }
}
