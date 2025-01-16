package com.mp3player;

import static com.mp3player.utils.Constants.*;

import com.mp3player.business.MP3Player;
import com.mp3player.presentation.scenes.PlayerView;
import com.mp3player.presentation.scenes.PlayerViewController;
import com.mp3player.presentation.scenes.PlaylistView;
import com.mp3player.presentation.scenes.PlaylistViewController;
import com.mp3player.utils.Utils;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class App extends Application {
  private static Scene scene;
  private static PlayerView playerView;
  private static PlayerViewController playerViewController;
  private static PlaylistView playlistView;
  private static PlaylistViewController playlistViewController;
  private boolean isTrafficLightAnimating, isTrafficLightVisible = false;
  private SVGPath iconLines, iconAudio;
  private boolean isPlaylistOpen = false;
  private Stage mainStage;
  private DoubleProperty stageHeight;

  public void init() {
    // Implementation eventueller Business-Logiken
  }

  @Override
  public void start(Stage mainStage) throws Exception {
    this.mainStage = mainStage;
    MP3Player player = new MP3Player();
    playerViewController = new PlayerViewController(player);
    playerView = playerViewController.getRoot();
    playlistViewController = new PlaylistViewController(player);
    playlistView = playlistViewController.getRoot();

    scene = new Scene(playerView, 1440, 600, true, SceneAntialiasing.BALANCED);
    mainStage.initStyle(StageStyle.TRANSPARENT);
    mainStage.resizableProperty().setValue(Boolean.TRUE);
    mainStage.setMinWidth(434);
    mainStage.setMaxWidth(900);
    mainStage.setMaxHeight(900);
    scene.setFill(null);
    StackPane mainContainer = new StackPane();
    mainContainer.getStyleClass().add("mainContainer");

    // Rounded Corners for root
    mainContainer.setPadding(new Insets(24));
    mainContainer.minWidth(450);
    mainContainer.getChildren().addAll(playerView, playlistView);
    mainContainer.setAlignment(Pos.CENTER);
    mainContainer.widthProperty().addListener(p -> updateShape(mainContainer));
    mainContainer.heightProperty().addListener(p -> updateShape(mainContainer));
    mainContainer.setStyle(
        mainContainer.getStyle() + "-fx-effect: -effects-button-background-default");

    // PlaylistView Button
    iconLines =
        new Utils.PathBuilder()
            .setContent(
                "M0.5 0.75C0.5 0.551088 0.579018 0.360322 0.71967 0.21967C0.860322 0.0790175"
                    + " 1.05109 0 1.25 0H13.25C13.4489 0 13.6397 0.0790175 13.7803"
                    + " 0.21967C13.921 0.360322 14 0.551088 14 0.75C14 0.948912 13.921 1.13968"
                    + " 13.7803 1.28033C13.6397 1.42098 13.4489 1.5 13.25 1.5H1.25C1.05109 1.5"
                    + " 0.860322 1.42098 0.71967 1.28033C0.579018 1.13968 0.5 0.948912 0.5"
                    + " 0.75ZM0.5 4.75C0.5 4.55109 0.579018 4.36032 0.71967 4.21967C0.860322"
                    + " 4.07902 1.05109 4 1.25 4H11.25C11.4489 4 11.6397 4.07902 11.7803"
                    + " 4.21967C11.921 4.36032 12 4.55109 12 4.75C12 4.94891 11.921 5.13968"
                    + " 11.7803 5.28033C11.6397 5.42098 11.4489 5.5 11.25 5.5H1.25C1.05109 5.5"
                    + " 0.860322 5.42098 0.71967 5.28033C0.579018 5.13968 0.5 4.94891 0.5"
                    + " 4.75ZM0.5 8.75C0.5 8.55109 0.579018 8.36032 0.71967 8.21967C0.860322"
                    + " 8.07902 1.05109 8 1.25 8H7.25C7.44891 8 7.63968 8.07902 7.78033"
                    + " 8.21967C7.92098 8.36032 8 8.55109 8 8.75C8 8.94891 7.92098 9.13968"
                    + " 7.78033 9.28033C7.63968 9.42098 7.44891 9.5 7.25 9.5H1.25C1.05109 9.5"
                    + " 0.860322 9.42098 0.71967 9.28033C0.579018 9.13968 0.5 8.94891 0.5"
                    + " 8.75ZM0.5 12.75C0.5 12.5511 0.579018 12.3603 0.71967 12.2197C0.860322"
                    + " 12.079 1.05109 12 1.25 12H6.25C6.44891 12 6.63968 12.079 6.78033"
                    + " 12.2197C6.92098 12.3603 7 12.5511 7 12.75C7 12.9489 6.92098 13.1397"
                    + " 6.78033 13.2803C6.63968 13.421 6.44891 13.5 6.25 13.5H1.25C1.05109 13.5"
                    + " 0.860322 13.421 0.71967 13.2803C0.579018 13.1397 0.5 12.9489 0.5"
                    + " 12.75Z")
            .setFill("#6B6B6B")
            .get();
    iconAudio =
        new Utils.PathBuilder()
            .setContent(
                "M9.595 0.742996C9.873 0.639996 10.205 0.552996 10.565 0.617996C11.007 0.697996"
                    + " 11.401 0.946996 11.665 1.31C11.88 1.606 11.945 1.944 11.973 2.24C12"
                    + " 2.527 12 2.886 12 3.295V3.405C12 3.705 12.002 4.025 11.894 4.32C11.809"
                    + " 4.5531 11.6754 4.76547 11.502 4.943C11.282 5.168 10.993 5.306 10.722"
                    + " 5.435L10.654 5.468L8.869 6.325C8.5 6.502 8.177 6.657 7.905 6.757C7.644"
                    + " 6.854 7.335 6.937 7 6.893V11.179C7 12.993 5.565 14.5 3.75 14.5C1.935"
                    + " 14.5 0.5 12.993 0.5 11.179C0.5 9.365 1.935 7.857 3.75 7.857C4.37165"
                    + " 7.85732 4.97975 8.03871 5.5 8.379V4.75H5.503C5.50033 4.58066 5.49933"
                    + " 4.399 5.5 4.205V4.095C5.5 3.795 5.498 3.475 5.606 3.18C5.69102 2.94689"
                    + " 5.82464 2.73452 5.998 2.557C6.218 2.332 6.507 2.194 6.778 2.065L6.846"
                    + " 2.032L8.631 1.175C9 0.997996 9.323 0.842996 9.595 0.742996Z")
            .setFill("#6B6B6B")
            .get();
    iconAudio.setTranslateX(9);
    iconAudio.setTranslateY(1);
    Group playlistIcon = new Group(iconLines, iconAudio);
    HBox plWrapper = new HBox(playlistIcon);
    playlistIcon.setScaleY(1.2);
    playlistIcon.setScaleX(1.2);

    plWrapper.setOnMouseClicked(
        (event) -> {
          togglePlistButton();
        });
    plWrapper.setAlignment(Pos.TOP_CENTER);
    plWrapper.setCursor(Cursor.HAND);
    HBox.setHgrow(playlistIcon, Priority.NEVER);
    plWrapper.setMaxWidth(Region.USE_PREF_SIZE);
    plWrapper.setMinWidth(Region.USE_PREF_SIZE);
    plWrapper.setMaxHeight(Region.USE_PREF_SIZE);
    plWrapper.setMinHeight(Region.USE_PREF_SIZE);
    plWrapper.setPadding(new Insets(12));

    // Traffic Light
    StackPane root = new StackPane();
    TrafficLight trafficLight = new TrafficLight(mainStage);
    ResizeGrabber resizeGrabber = new ResizeGrabber(mainStage);
    root.getChildren().addAll(mainContainer, trafficLight, resizeGrabber, plWrapper);

    StackPane.setAlignment(trafficLight, Pos.TOP_LEFT);
    trafficLight.setTranslateX(40);
    trafficLight.setTranslateY(40);
    trafficLight.setVisible(false);

    StackPane.setAlignment(resizeGrabber, Pos.BOTTOM_RIGHT);
    resizeGrabber.setTranslateX(-25);
    resizeGrabber.setTranslateY(-25);

    StackPane.setAlignment(plWrapper, Pos.TOP_RIGHT);
    plWrapper.setTranslateX(-25);
    plWrapper.setTranslateY(25);

    root.setPadding(new Insets(0));
    root.setStyle("-fx-background-color: transparent;");

    root.setPrefWidth(400);
    root.setMinWidth(400);
    scene.setRoot(root);

    scene.getStylesheets().add("file:src/com/mp3player/style.css");

    // setActions
    root.setOnMouseMoved(
        (MouseEvent event) -> {
          // debug(String.format("X: %s, Y: %s", event.getSceneX(), event.getSceneY()));
          boolean visibleArea = event.getSceneX() <= 150 && event.getSceneY() <= 250;
          if (!isTrafficLightAnimating && visibleArea != isTrafficLightVisible) {
            isTrafficLightAnimating = true;
            FadeTransition trafficLightFade =
                new FadeTransition(Duration.millis(visibleArea ? 150 : 300), trafficLight);
            trafficLightFade.setInterpolator(Interpolator.EASE_BOTH);

            if (visibleArea) {
              trafficLight.setVisible(true);
              trafficLight.setOpacity(0);
              trafficLightFade.setFromValue(0.0);
              trafficLightFade.setToValue(1.0);
            } else {
              trafficLightFade.setFromValue(1.0);
              trafficLightFade.setToValue(0.0);
            }

            trafficLightFade.setOnFinished(
                e -> {
                  isTrafficLightAnimating = false;
                  isTrafficLightVisible = visibleArea;
                  if (!visibleArea) {
                    trafficLight.setVisible(false);
                  }
                });

            trafficLightFade.play();
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
                  if (newWidth
                      < 405
                          + mainContainer.getInsets().getLeft()
                          + mainContainer.getInsets().getRight()) {
                    newWidth =
                        405
                            + mainContainer.getInsets().getLeft()
                            + mainContainer.getInsets().getRight();
                  }
                  if (newHeight
                      < 440
                          + mainContainer.getInsets().getTop()
                          + mainContainer.getInsets().getBottom()) {
                    newHeight =
                        440
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
    mainStage.setMinHeight(440);
    mainStage.setScene(scene);
    mainStage.show();
    stageHeight = new SimpleDoubleProperty(mainStage.getHeight());
    stageHeight.addListener((obs, old, newValue) -> mainStage.setHeight(newValue.doubleValue()));
  }

  private void togglePlistButton() {
    if (isPlaylistOpen()) {
      iconLines.setStyle("-fx-fill: #FF7713");
      iconAudio.setStyle("-fx-fill: white");
    } else {
      iconLines.setStyle("-fx-fill: #6B6B6B");
      iconAudio.setStyle("-fx-fill: #6B6B6B");
    }
    togglePlaylist();
  }

  private boolean isPlaylistOpen() {
    return isPlaylistOpen;
  }

  private void togglePlaylist() {
    double duration = 1200;
    Timeline timeline = new Timeline();
    timeline.getKeyFrames().clear();
    Interpolator bouncy = Interpolator.SPLINE(0.25, 0.1, 0.25, 1.0);

    if (!isPlaylistOpen) {
      KeyFrame moveUpFrame =
          new KeyFrame(
              Duration.millis(duration),
              new KeyValue(playlistView.translateYProperty(), -900, bouncy));
      timeline.getKeyFrames().add(moveUpFrame);
    } else {
      KeyFrame moveDownFrame =
          new KeyFrame(
              Duration.millis(duration),
              new KeyValue(playlistView.translateYProperty(), 0, bouncy));
      timeline.getKeyFrames().add(moveDownFrame);
    }

    timeline.play();
    isPlaylistOpen = !isPlaylistOpen;
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
    // debug(s);
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

    public TrafficLight(Stage mainStage) {
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
      setupButtonActions(closeButton, xIcon);
      closeButton.setOnMouseClicked(
          event -> {
            System.out.println("Close button clicked");
            Platform.exit();
            System.exit(0);
          });
      setupButtonActions(minimizeButton, minusIcon);
      minimizeButton.setOnMouseClicked(
          event -> {
            mainStage.setIconified(true);
          });
      setupButtonActions(maximizeButton, resizeIcon);
      maximizeButton.setOnMouseClicked(
          event -> {
            mainStage.setMaximized(!mainStage.isMaximized());
          });

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

    private void setupButtonActions(Circle button, Group icon) {
      button.setOnMouseEntered(event -> icon.setVisible(true));
      button.setOnMouseExited(event -> icon.setVisible(false));
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
      SVGPath svgPath =
          new Utils.PathBuilder()
              .setContent(svgContent)
              .setFill("white")
              .setStrokeWidth(1.5)
              .setStrokeStyle(StrokeLineCap.ROUND)
              .get();
      resizeGrabber.getChildren().add(svgPath);
      getChildren().add(resizeGrabber);

      setOnMousePressed(
          (MouseEvent event) -> {
            if (event.getSceneX() >= getLayoutX() && event.getSceneY() >= getLayoutY()) {
              setOnMouseDragged(
                  (MouseEvent event1) -> {
                    mainStage.setWidth(event1.getScreenX() - mainStage.getX());
                    mainStage.setHeight(event1.getScreenY() - mainStage.getY());
                  });
            }
          });
      this.setCursor(javafx.scene.Cursor.SE_RESIZE);
    }
  }

  public static void main(String[] args) {
    Application.launch(App.class, args);
  }
}
