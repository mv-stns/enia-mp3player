package com.mp3player.utils;

import javafx.event.EventTarget;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.shape.SVGPath;

public class AdditionalFuncs {

    public static Button createSVGButton(String svgPath) {
        SVGPath path = new SVGPath();
        path.setContent(svgPath);
        path.getStyleClass().add("control-icons");

        // path.setScaleX(1);
        // path.setScaleY(1);
        path.maxWidth(10);

        Button button = new Button();
        button.setGraphic(path);
        button.setAlignment(Pos.CENTER);
        button.setPadding(new Insets(0));
        button.getStyleClass().addAll("control-buttons");
        button.setMinWidth(42);
        button.setMinHeight(42);

        return button;
    }
    public static void loadCSS(Parent e, String s) {
        e.getStylesheets().add("file:" + e.getClass().getResource(s).getPath());
    }
}
